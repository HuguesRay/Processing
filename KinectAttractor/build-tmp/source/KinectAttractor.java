import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import org.openkinect.*; 
import org.openkinect.processing.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class KinectAttractor extends PApplet {




Kinect kinect;
KinectTracker tracker;

PVector selector;
int angle;
PVector v1, v2;
int p;
boolean anim;
int progress;
float deg;

//Variables and Coefficients (Initial Values) ------------------------------------
float a = 1.78125f, b = -0.78125f, c = -0.78125f, d = -1.95625f, e = 0.7f, f = -1.1f;  //Coefficients
float x = 0.6f, y = 0.9f, z = 0.3f;     //Variables
float xn,yn,zn,la,lb,lc,ld;          //Temporary Copies of Variables, Coefficients

int N = 800, u, v;                   //Image Size, Pixel Coordinates
PImage img = createImage(N,N,RGB);   //Image Itself
float K = N*0.2f;                     //Image Scaling Constant

float[] r = {0.0f,0.0f,0.0f};           //Color (float)
int[] clr = {0,0,0};                 //Color (int)
float[][][] fimg;                    //Point Accumulation Array

int nrSamples = 1000000;
int nrPasses = 16;
int nrIterations = nrSamples/nrPasses;
int imageProgress=0, total=0; //Bookkeeping
boolean noise = true, init = true, hide = false;    


public int   toMouse(float a) {return (int)((float)N * (a + 4.0f)/8.0f);}
public float fromMouse(int   X) {return -4.0f + 8.0f * (float)X/(float)N;}
int ma,mb,mc,md,active = 0;  //Mouse Coordinates of Coefficients


public void setup() {
	size(N,N);
	//scale(2);
	frameRate(30);
	deg = 29;
	fimg = new float[N][N][4];
	ma = toMouse(a); mb = toMouse(b); mc = toMouse(c); md = toMouse(d);
	kinect = new Kinect(this);
	tracker = new KinectTracker();
    angle = 0;
	p=0;
	anim=false;
	v1 = new PVector(0,0);
	//kinect.start();
	kinect.enableRGB(true);
 	// kinect.enableIR(true);
    kinect.enableDepth(true);
}

public void draw() {
	background(0);

	//scale(1.2);
	kinect.tilt(deg);
	pushMatrix();
	scale(1.5f);
	tracker.track();
  	tracker.display();
  	selector = tracker.getLerpedPos();
  	popMatrix();
  	// PImage img = kinect.getDepthImage();
  	// pushMatrix(); 
	// scale(-1,1); 
	// image(img,0,0); 
	// image(img,-width,0); 
	// popMatrix(); 
  	
  	// selector = tracker.getLerpedPos();


  float a0 = a, b0 = b, c0 = c, d0 = d;                //Adjust Coefficients Based On Mouse Position
  if (active == 1) { ma = (int) selector.x; mb = (int) selector.y; a = fromMouse(ma); b = fromMouse(mb);}
  if (active == 2) { mc = (int) selector.x; md = (int) selector.y; c = fromMouse(mc); d = fromMouse(md);}
  if (!(a == a0 && b == b0 && c == c0 && d == d0)) {init = true;} //Mouse Moved - Parameters Changed
  
  //Initialize Collection Bins
  if (init) {
    for (int i=0; i < N; i++) {
      for (int j=0; j < N; j++) {
        for (int k=0; k < 4; k++) {
          fimg[i][j][k] = 1.0f;     //Set All to 1.0 (Log-Valued Zero)
    }}}
    imageProgress = 0; total = 0; init = false;
  }

  accumulatePoints();
  
  //-- Render Accumulated Points -------------------------------------------------------
  if (imageProgress == nrPasses && total == nrSamples){

    //Find Max Value - //
    float t1 = millis();
    float max = -1.0f;
    for (int i=0; i < N; i++) {
      for (int j=0; j < N; j++) {
          if (fimg[i][j][3] > max){
            max = fimg[i][j][3];}}}

    //Adjust Values and Fill Image
    float logval, logmax = log(max);
    float M = (logmax * logmax) / 255.0f;  //Precomputation for ratio (log(val)/log(max))^2
    
    img.loadPixels();
    for (int i=0; i < N; i++) {
      for (int j=0; j < N; j++) {
        for (int k=0; k < 3; k++) {
          logval = log(fimg[i][j][k]);
          clr[k] = (int) (logval * logval / M);
        }
        img.pixels[j*N + i] = color(clr[0],clr[1],clr[2]);
    }}
    img.updatePixels(); //
    float t2 = millis(); println("Render: " + (t2-t1));
    total++;
  }

  if (total >= nrSamples) {image(img,0,0,N,N);} //Draw Rendered Image
  
  //Draw Indicators for Coefficients
  if (!hide) {  stroke(200);
    fill(100,0,0); ellipse(ma, mb, (active == 1) ? 40 : 20, (active == 1) ? 40: 20);
    fill(0,0,100); ellipse(mc, md, (active == 2) ? 40 : 20, (active == 2) ? 40: 20);}
 	
 	

	fill(48,128,219);
	// rect(width-50,height-50, 50,50);
	// rect(0,0, 50,50);
	// rect(0,height-50, 50,50);
	//rect(width-50,0, 50,50);
	int target = get((int) selector.x, (int) selector.y);
	
	// println(target);

	v2 = new PVector(mouseX,mouseY);
	if(target == -10223616 || target == -16777116) {
		p++;
		if(p>=20) {
			selectionner();
		}
	}
	else {
		v1=v2;
		p=0;
		anim=false;
		progress=0;
	}
	noStroke();
	fill(255);
	ellipse(selector.x,selector.y,10,10);
}



public void selectionner() {
	anim=true;
	animation();
}

public void animation() {
	if(anim==true) {
		progress=progress+5;
		strokeCap(SQUARE);
		noFill();
		stroke(255);
		//strokeWeight(30);
		arc(selector.x,selector.y,40,40,radians(0), radians(progress));
		if(progress >= 360) {
			action();
			progress=0;
		}
	}	
}

public void update(){
  la = a; lb = b; lc = c; ld = d; //Add Noise to Coefficients for Smoothness
  if (noise) { la += random(-0.001f, 0.001f);  lb += random(-0.001f, 0.001f);
               lc += random(-0.001f, 0.001f);  ld += random(-0.001f, 0.001f);}
  
  xn = sin (la*y) - cos(lb*x); //*************************************************
  yn = sin (lc*x) - cos(ld*y); //** Update Temp Variables -- Magic Lies Here  ****
  zn = sin ( e*x) - cos( f*z); //*************************************************
  
  x = xn;  y = yn;  z = zn;       //Set Original to Temp
  u = (int)((x+2.5f) * K);         //Convert to 2D Image Space for Plotting
  v = (int)((y+2.5f) * K);
  
  r[0] = z * 0.9f + (1.0f-z) * 0.6f; //Map Z-Coordinate to Color
  r[1] = z * 0.2f + (1.0f-z) * 0.4f;
  r[2] = z * 0.5f + (1.0f-z) * 0.9f;
}

public void accumulatePoints(){
  if (imageProgress < nrPasses){
    colorMode(RGB, 1.0f);
    for (int i = 0; i < nrIterations; i++){
      update();                                           //Compute Next Point
      fimg[u][v][0] += r[0];  fimg[u][v][1] += r[1];      //Add New Point to Total
      fimg[u][v][2] += r[2];  fimg[u][v][3] += 1.0f;
      if (i < 2000) {stroke(r[0],r[1],r[2]); point(u,v);} //Draw Points
    }
    colorMode(RGB, 255);
    imageProgress++;  if (imageProgress <= nrPasses) total += nrIterations;
  }
}


public void action() {
	println("action");
	if (active != 0) active = 0;
  	else if (sq(selector.x-mc) + sq(selector.y-md) < sq(10.0f)) {active = 2;}
  	else if (sq(selector.x-ma) + sq(selector.y-mb) < sq(10.0f)) {active = 1;}
}

public void mousePressed(){
  if (active != 0) active = 0;
  else if (sq(mouseX-mc) + sq(mouseY-md) < sq(10.0f)) {active = 2;}
  else if (sq(mouseX-ma) + sq(mouseY-mb) < sq(10.0f)) {active = 1;}
}

public void stop() {
  tracker.quit();
  super.stop();
}

public void keyPressed() {
  if(key=='-') {
    println("down");
    int t = tracker.getThreshold();
    tracker.setThreshold(t-10);
  }
  if(key=='=') {
    println("up");
    int t = tracker.getThreshold();
    tracker.setThreshold(t+10);
  }
  if(key=='p') {
    println(tracker.getThreshold());
  }
  if(key=='q') {
    stop();
  }
  if(keyCode== UP) {
    deg++;
    println(deg);
  }
  if(keyCode== DOWN) {
    deg--;
  }
}
class KinectTracker {



  // Size of kinect image
  int kw = 640;
  int kh = 480;
  int threshold = 640;

  // Raw location
  PVector loc;

  // Interpolated location
  PVector lerpedLoc;

  // Depth data
  int[] depth;


  PImage display;

  KinectTracker() {
    kinect.start();
    kinect.enableDepth(true);

    // We could skip processing the grayscale image for efficiency
    // but this example is just demonstrating everything
    kinect.processDepthImage(true);

    display = createImage(kw,kh,PConstants.RGB);

    loc = new PVector(0,0);
    lerpedLoc = new PVector(0,0);
  }

  public void track() {

    // Get the raw depth as array of integers
    depth = kinect.getRawDepth();

    // Being overly cautious here
    if (depth == null) return;

    float sumX = 0;
    float sumY = 0;
    float count = 0;

    for(int x = 0; x < kw; x++) {
      for(int y = 0; y < kh; y++) {
        // Mirroring the image
        int offset = kw-x-1+y*kw;
        // Grabbing the raw depth
        int rawDepth = depth[offset];

        // Testing against threshold
        if (rawDepth < threshold) {
          sumX += x;
          sumY += y;
          count++;
        }
      }
    }
    // As long as we found something
    if (count != 0) {
      loc = new PVector(sumX/count,sumY/count);
    }

    // Interpolating the location, doing it arbitrarily for now
    lerpedLoc.x = PApplet.lerp(lerpedLoc.x, loc.x, 0.3f);
    lerpedLoc.y = PApplet.lerp(lerpedLoc.y, loc.y, 0.3f);
  }

  public PVector getLerpedPos() {
    return lerpedLoc;
  }

  public PVector getPos() {
    return loc;
  }

  public void display() {
    PImage img = kinect.getDepthImage();

    // Being overly cautious here
    if (depth == null || img == null) return;

    // Going to rewrite the depth image to show which pixels are in threshold
    // A lot of this is redundant, but this is just for demonstration purposes
    display.loadPixels();
    for(int x = 0; x < kw; x++) {
      for(int y = 0; y < kh; y++) {
        // mirroring image
        int offset = kw-x-1+y*kw;
        // Raw depth
        int rawDepth = depth[offset];

        int pix = x+y*display.width;
        if (rawDepth < threshold) {
          // A red color instead
          display.pixels[pix] = color(150,50,50);
        } 
        else {
          display.pixels[pix] = img.pixels[offset];
        }
      }
    }
    display.updatePixels();

    // Draw the image
    //image(display,0,0);
  }

  public void quit() {
    kinect.quit();
  }

  public int getThreshold() {
    return threshold;
  }

  public void setThreshold(int t) {
    threshold =  t;
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "KinectAttractor" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
