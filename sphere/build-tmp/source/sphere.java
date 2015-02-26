import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sphere extends PApplet {

//import processing.opengl.*;


PeasyCam cam;
 

int counter = 0;

public void setup() 
{
	size( 1336, 720, P3D);

	cam = new PeasyCam(this, 0,0,0, 100);
	cam.setMinimumDistance(200);
	cam.setMaximumDistance(2000);
	cam.lookAt(width/2, height/2, 0);

	lights();

}
 
public void draw()
{
	background(100, 100, 150);
	
	// noStroke();
	
	fill(227,227,5);
	translate(width/2, height/2);
	sphere(100);

	// pushMatrix();
	fill(181,110,11);
	translate(width/3, height/3);
	sphere(20);
	translate(width/2, height/2);
	counter++;
	rotateY(millis()*0.001f * TWO_PI/10);
	// popMatrix();

	// fill(0,70,191);
	// translate(width/4, height/4);
	// sphere(30);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sphere" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
