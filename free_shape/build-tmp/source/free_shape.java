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

public class free_shape extends PApplet {

float centerX, centerY;
int formResolution = 15;

float initRadius = 150;
int stepSize = 2;

float[] x = new float[formResolution];
float[] y = new float[formResolution];

public void setup() {
	//size(displayWidth, displayHeight);
	size(1336, 720);
	smooth();
	centerX = width/2;
	centerY = height/2;
	float angle = radians(360/PApplet.parseFloat(formResolution));
	for(int i=0;i<formResolution;i++) {
		x[i] = cos(angle*i) * initRadius;
		y[i] = sin(angle*i) * initRadius;
	}
	// println(x);
	stroke(0, 50);
	background(255);
}

public void draw() {
	strokeWeight(0.75f);
	noFill();
	//latence vers la direction de la souris
	if (mouseX !=0 || mouseY != 0) {
		centerX += (mouseX-centerX) * 0.005f;
		centerY += (mouseY-centerY) * 0.005f;
	}

	for(int i=0;i<formResolution;i++){
		x[i] += random(-stepSize, stepSize);
		y[i] += random(-stepSize, stepSize);
		// ellipse(x[i], y[i], initRadius, initRadius);
	}

	beginShape();

	curveVertex(x[formResolution-1]+centerX, y[formResolution-1]+centerY);

	for(int v=0;v<formResolution;v++) {
		curveVertex(x[v]+centerX, y[v]+centerY);

	}
	curveVertex(x[0]+centerX, y[0]+centerY);
	curveVertex(x[1]+centerX, y[1]+centerY);
	endShape();
	//println(centerX);
}

public void mousePressed() {
	centerX = mouseX;
	centerY = mouseY;
	float angle = radians(360/PApplet.parseFloat(formResolution));
	for(int i=0;i<formResolution;i++) {
		x[i] = cos(angle*i) * initRadius;
		y[i] = sin(angle*i) * initRadius;
	}
}

public void keyReleased() {
	if(key == BACKSPACE) background(255);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "free_shape" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
