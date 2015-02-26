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
float theta1 = 0;

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
	 background(255);
  stroke(0);
  fill(175);
  rectMode(CENTER);
  
  translate(50,50);
  rotateZ(theta1);
  rect(0,0,60,60);
  
  theta1 += 0.02f;


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
