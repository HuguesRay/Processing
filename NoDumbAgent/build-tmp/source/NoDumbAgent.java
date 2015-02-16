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

public class NoDumbAgent extends PApplet {

int n = 0;
int e = 1;
int s = 2;
int w = 3;


int stepsize = 3;
int radius = 1;
int direction;
int minlength = 10;
int angleCount = 7;
float angle;

float posX;
float posY;
float posXcross;
float posYcross;

public void setup() {
	size(1280,720);
	colorMode(HSB, 360, 100, 100, 100);
	smooth();
	background(360);
	
	posX=random(0,width);
	posY=height/2;
	posXcross = posX;
	posYcross = posY;
	angle = 90;
	direction=s;
}

public void draw() {
	for (int i = 0; i<mouseX/10; i++) {
		//direction = (int) random(0,8);
		
		//println(direction);
		strokeWeight(1);
		stroke(180);
		//point(posX, posY);

		
		posX += cos(radians(angle)) * stepsize;
		posY += sin(radians(angle)) * stepsize;
		

		boolean reachedBorder = false;
		//println(posY);
		if(posY<= 5) {
			direction = s;
			reachedBorder = true;
		}
		else if(posY>=height-5) {
			direction = n;
			reachedBorder = true;
		}
		else if(posX<= 5) {
			direction = e;
			reachedBorder = true;
		}
		else if(posX>=width-5) {
			direction = w;
			reachedBorder = true;
		}

		int px = (int) posX;
		int py = (int) posY;
		//println(reachedBorder);
		if (get(px, py) != color(360) || reachedBorder == true) {
			//println(direction);
			angle = getRandomAngle(direction);
			float distance = dist(posX,posY,posXcross,posYcross);
			//println(distance);
			if(distance >= minlength) {
				strokeWeight(distance/20);
				stroke(0, mouseY/10);
				line(px,py,posXcross,posYcross);
			}
			posXcross = posX;
			posYcross = posY;
		}

		//fill(0, 40);
		//ellipse(posX, posY, radius*2, radius*2);
	}
		
}

public float getRandomAngle(int theDirection) {
	float a = (floor(random(-angleCount, angleCount)) + 0.5f) * 90.0f/angleCount;

  if (theDirection == n) return (a - 90);
  if (theDirection == e) return (a);
  if (theDirection == s) return (a + 90);
  if (theDirection == w) return (a + 180);
  return 0;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "NoDumbAgent" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
