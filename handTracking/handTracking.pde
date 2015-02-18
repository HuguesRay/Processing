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


void setup() {
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

void draw() {

 	background(255);
 	tracker.track();
  	tracker.display();
  	selector = tracker.getLerpedPos();

	fill(48,128,219);
	rect(width-50,height-50, 50,50);
	rect(0,0, 50,50);
	rect(0,height-50, 50,50);
	rect(width-50,0, 50,50);
	color target = get((int) selector.x, (int) selector.y);
	
	//println(target);

	v2 = new PVector(mouseX,mouseY);
	if(target == -13598501) {
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
		strokeWeight(30);
		arc(selector.x,selector.y,40,40,radians(0), radians(progress));
		if(progress >= 360) {
			action();
			progress=0;
		}
	}

	
}

void action() {
	println("ACTION");
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
}
