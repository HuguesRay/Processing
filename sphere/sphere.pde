//import processing.opengl.*;
import peasy.*;

PeasyCam cam;
 

int counter = 0;

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
	rotateY(millis()*0.001 * TWO_PI/10);
	// popMatrix();

	// fill(0,70,191);
	// translate(width/4, height/4);
	// sphere(30);
}