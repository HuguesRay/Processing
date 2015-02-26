//import processing.opengl.*;
import peasy.*;

PeasyCam cam;
 

int counter = 0;
float theta1 = 0;

void setup() 
{
	size( 1336, 720, P3D);

	cam = new PeasyCam(this, 0,0,0, 100);
	cam.setMinimumDistance(200);
	cam.setMaximumDistance(2000);
	cam.lookAt(width/2, height/2, 0);

	lights();

}
 
void draw()
{
	 background(255);
  stroke(0);
  fill(175);
  rectMode(CENTER);
  
  translate(50,50);
  rotateZ(theta1);
  rect(0,0,60,60);
  
  theta1 += 0.02;


}