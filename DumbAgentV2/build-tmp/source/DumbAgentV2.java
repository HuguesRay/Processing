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

public class DumbAgentV2 extends PApplet {

float angle;
int radius;

public void setup() {
	size(720,720);
	radius = width/3;
	
	background(255,255,255);
}

public void draw() {
	noFill();
	ellipse(width/2,height/2,radius,radius);
	fill(255,255,255);
	if(mousePressed == true) {
		angle= random(0,359);
		float posx = width/2 + radius/2 * cos(angle);
		float posy = width/2 + radius/2 * sin(angle);
		//ellipse(posx,posy, 10, 10);
		stroke(0);
		line(posx,posy,mouseX,mouseY);
	}
}

public void mousePressed() {
	//background(255,255,255);
}

public void keyReleased() {
	if(key == 'r'){
		background(255,255,255);
	}
	
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DumbAgentV2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
