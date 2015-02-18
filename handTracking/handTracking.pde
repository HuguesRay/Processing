import org.openkinect.*;
import org.openkinect.processing.*;

Kinect kinect;
KinectTracker tracker;
int angle;
int angle2;
int stroking = 1;
void setup() {
	size(640,480);
	kinect = new Kinect(this);
	tracker = new KinectTracker();
        angle = 0;
        angle2 = 0;
	//kinect.start();
	//kinect.enableRGB(true);
       // kinect.enableIR(true);
        kinect.enableDepth(true);
}

void draw() {
  angle=angle+5;
  //angle2++;
  if(angle == 360) {
    stroking++;
    if(stroking==10) {
      stroking = 1;
    }
    angle=0;
  }
  background(255);
  tracker.track();
  tracker.display();
  PVector v1 = tracker.getLerpedPos();
  fill(50,100,250,200); 
  noStroke(); 
  ellipse(v1.x,v1.y,20,20);
  
  PVector v2 = tracker.getPos();
  noFill();
  stroke(50,100,250);
  strokeWeight(stroking);
  arc(v2.x,v2.y,50,50, radians(angle2), radians(angle));
  
  //println(v1.x, v1.y);
  if(v1.x>600 && v1.y <50) {
    println("bazinga");
  }
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
