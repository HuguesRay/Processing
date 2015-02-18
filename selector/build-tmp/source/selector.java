import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class selector extends PApplet {


PVector selector;
int angle;
PVector v1, v2;
int p;
boolean anim;
int progress;

public void setup() {
	size(640,480);
	angle = 0;
	p=0;
	anim=false;
	v1 = new PVector(mouseX,mouseY);
}

public void draw() {
	background(0);
	selector = new PVector(mouseX, mouseY);

	v2 = new PVector(mouseX,mouseY);
	if(v2.dist(v1)>10) {
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "selector" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
