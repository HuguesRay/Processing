import org.openkinect.*;
import org.openkinect.processing.*;

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
float a = 1.78125, b = -0.78125, c = -0.78125, d = -1.95625, e = 0.7, f = -1.1;  //Coefficients
float x = 0.6, y = 0.9, z = 0.3;     //Variables
float xn,yn,zn,la,lb,lc,ld;          //Temporary Copies of Variables, Coefficients

int N = 800, u, v;                   //Image Size, Pixel Coordinates
PImage img = createImage(N,N,RGB);   //Image Itself
float K = N*0.2;                     //Image Scaling Constant

float[] r = {0.0,0.0,0.0};           //Color (float)
int[] clr = {0,0,0};                 //Color (int)
float[][][] fimg;                    //Point Accumulation Array

int nrSamples = 1000000;
int nrPasses = 16;
int nrIterations = nrSamples/nrPasses;
int imageProgress=0, total=0; //Bookkeeping
boolean noise = true, init = true, hide = false;    


int   toMouse(float a) {return (int)((float)N * (a + 4.0)/8.0);}
float fromMouse(int   X) {return -4.0 + 8.0 * (float)X/(float)N;}
int ma,mb,mc,md,active = 0;  //Mouse Coordinates of Coefficients


void setup() {
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

void draw() {
	background(0);

	//scale(1.2);
	kinect.tilt(deg);
	pushMatrix();
	scale(1.5);
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
          fimg[i][j][k] = 1.0;     //Set All to 1.0 (Log-Valued Zero)
    }}}
    imageProgress = 0; total = 0; init = false;
  }

  accumulatePoints();
  
  //-- Render Accumulated Points -------------------------------------------------------
  if (imageProgress == nrPasses && total == nrSamples){

    //Find Max Value - //
    float t1 = millis();
    float max = -1.0;
    for (int i=0; i < N; i++) {
      for (int j=0; j < N; j++) {
          if (fimg[i][j][3] > max){
            max = fimg[i][j][3];}}}

    //Adjust Values and Fill Image
    float logval, logmax = log(max);
    float M = (logmax * logmax) / 255.0;  //Precomputation for ratio (log(val)/log(max))^2
    
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
	color target = get((int) selector.x, (int) selector.y);
	
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



void selectionner() {
	anim=true;
	animation();
}

void animation() {
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

void update(){
  la = a; lb = b; lc = c; ld = d; //Add Noise to Coefficients for Smoothness
  if (noise) { la += random(-0.001, 0.001);  lb += random(-0.001, 0.001);
               lc += random(-0.001, 0.001);  ld += random(-0.001, 0.001);}
  
  xn = sin (la*y) - cos(lb*x); //*************************************************
  yn = sin (lc*x) - cos(ld*y); //** Update Temp Variables -- Magic Lies Here  ****
  zn = sin ( e*x) - cos( f*z); //*************************************************
  
  x = xn;  y = yn;  z = zn;       //Set Original to Temp
  u = (int)((x+2.5) * K);         //Convert to 2D Image Space for Plotting
  v = (int)((y+2.5) * K);
  
  r[0] = z * 0.9 + (1.0-z) * 0.6; //Map Z-Coordinate to Color
  r[1] = z * 0.2 + (1.0-z) * 0.4;
  r[2] = z * 0.5 + (1.0-z) * 0.9;
}

void accumulatePoints(){
  if (imageProgress < nrPasses){
    colorMode(RGB, 1.0);
    for (int i = 0; i < nrIterations; i++){
      update();                                           //Compute Next Point
      fimg[u][v][0] += r[0];  fimg[u][v][1] += r[1];      //Add New Point to Total
      fimg[u][v][2] += r[2];  fimg[u][v][3] += 1.0;
      if (i < 2000) {stroke(r[0],r[1],r[2]); point(u,v);} //Draw Points
    }
    colorMode(RGB, 255);
    imageProgress++;  if (imageProgress <= nrPasses) total += nrIterations;
  }
}


void action() {
	println("action");
	if (active != 0) active = 0;
  	else if (sq(selector.x-mc) + sq(selector.y-md) < sq(10.0)) {active = 2;}
  	else if (sq(selector.x-ma) + sq(selector.y-mb) < sq(10.0)) {active = 1;}
}

void mousePressed(){
  if (active != 0) active = 0;
  else if (sq(mouseX-mc) + sq(mouseY-md) < sq(10.0)) {active = 2;}
  else if (sq(mouseX-ma) + sq(mouseY-mb) < sq(10.0)) {active = 1;}
}

void stop() {
  tracker.quit();
  super.stop();
}

void keyPressed() {
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
