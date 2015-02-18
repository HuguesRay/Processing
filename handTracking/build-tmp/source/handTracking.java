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

public void setup() {
	size(640,480);
	kinect = new Kinect(this);
	
	kinect.start();
	kinect.enableRGB(true);
}

public void draw() {
	PImage img = kinect.getVideoImage();
	image(img,0,0);
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
