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

public class handTracking extends PApplet {




Kinect kinect;
KinectTracker tracker;

PVector selector;
int angle;
PVector v1, v2;
int p;
boolean anim;
int progress;


public void setup() {
	size(640,480);
	kinect = new Kinect(this);
	tracker = new KinectTracker();
    angle = 0;
	p=0;
	anim=false;
	v1 = new PVector(0,0);
	//kinect.start();
	//kinect.enableRGB(true);
 	// kinect.enableIR(true);
    kinect.enableDepth(true);
}

public void draw() {

  background(255);
  tracker.track();
  tracker.display();
  selector = tracker.getLerpedPos();

	v2 = tracker.getLerpedPos();
	println(v1);
	println(v2);
	if(v2.dist(v1)>10) {
		println("mouvement");
		v1=v2;
		p=0;
		anim=false;
		progress=0;
	}
	else {
		p++;
		if(p>=20) {
			selectionner();
		}
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
		progress++;
		strokeCap(SQUARE);
		noFill();
		stroke(255);
		strokeWeight(30);
		arc(selector.x,selector.y,40,40,radians(0), radians(progress));
		if(progress >= 360) {
			action();
			progress=0;
		}
	}

	
}

public void action() {
	println("ACTION");
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
}
class KinectTracker {



  // Size of kinect image
  int kw = 640;
  int kh = 480;
  int threshold = 500;

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
    image(display,0,0);
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
    String[] appletArgs = new String[] { "handTracking" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
