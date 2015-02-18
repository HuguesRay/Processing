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
float oPosX;
float oPosY;
int oRadius;
int set=30;
ArrayList<Cercle> tunnel = new ArrayList<Cercle>();

public void setup() {
	size(720,720);	
	background(255,255,255);
	posx=(int)random(0,width);
	posy=(int)random(0,height);
}

public void draw() {
	noFill();
	oRadius = radius;
	radius = (int) random(10,30);
	set++;
	if(set>=40) {
		set=0;
		newPosition(posx,posy);
		float x3 = oPosX-posx;
		float y3 = oPosY-posy;
		if(abs(x3+y3) >= radius) {
			tunnel.add(new Cercle(posx,posy,radius));
		}	
	}
	

	//dispatch display() de chaque cercles
	for(int i = 0; i<tunnel.size(); i++) {
		Cercle monCercle = tunnel.get(i);
		smooth();
		monCercle.display();
	}
	println(tunnel.size());
}

public void mousePressed() {
	//background(255,255,255);
}

public void keyReleased() {
	if(key == 'r'){
		background(255,255,255);
	}
	
}
public void newPosition(float x,float y) {
	int NextAngle = (int) random(0,359);
	oPosX = posx;
	oPosY = posy;
	posx =  posx + (radius/2+oRadius/2) * cos(NextAngle);
	posy =  posy + (radius/2+oRadius/2) * sin(NextAngle);
	if(posx>=width) {
		posx = posx-width;
	}
	if(posx<=0) {
		posx=width;
	}
	if(posy>=height) {
		posy = posy-height;
	}
	if(posy<=0) {
		posy = height;
	}
}

class Cercle {
	float x;
	float y;
	int radius;

	Cercle(float px, float py, int rad) {
		smooth();
		x = px;
		y = py;
		radius = rad;

	}
	public void display() {
		smooth();
		ellipse(x,y,radius,radius);
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
