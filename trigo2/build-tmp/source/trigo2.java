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

public class trigo2 extends PApplet {

float angle;
int radius;
float posx;
float posy;

public void setup() {
	size(720,720);	
	background(255,255,255);
	posx=(int)random(0,width);
	posy=(int)random(0,height);
}

public void draw() {
	noFill();
	radius = (int) random(10,30);
	point(posx,posy);
	ellipse(posx,posy, radius,radius);

	int NextAngle = (int) random(0,359);
	posx =  posx + radius/2 * cos(NextAngle);
	posy =  posy + radius/2 * sin(NextAngle);
}

public void mousePressed() {
	//background(255,255,255);
}

public void keyReleased() {
	if(key == 'r'){
		background(255,255,255);
		posx=(int)random(0,width);
		posy=(int)random(0,height);
	}
	
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "trigo2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
