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

public class filterInteractif extends PApplet {

PShape currentShape;

int tileCount = 30;
float tileWidth, tileHeight;
float shapeSize = 10;
float newShapeSize = shapeSize;
float shapeAngle = 90;
float maxDist;

int shapeColor = color(255,211,67);

public void setup() {
	size(1336, 720);
	background(155, 155, 155, 0.1f);
	smooth();

	tileWidth = width/PApplet.parseFloat(tileCount);
	tileHeight = height/PApplet.parseFloat(tileCount);
	maxDist = sqrt(sq(width)+sq(height));

	currentShape = loadShape("module_1.svg");
}

public void draw() {
	background(155, 155, 155, 0.1f);
	smooth();

	for(int gridY=0; gridY<tileCount; gridY++) {
		//println(gridY);
		for (int gridX=0; gridX<tileCount; gridX++) {
			
			float posX = tileWidth*gridX + tileWidth/2;
			float posY = tileHeight+gridY + tileWidth/2;

			float angle = atan2(mouseY-posY, mouseX-posX) + radians(shapeAngle);

			newShapeSize = shapeSize;
			currentShape.disableStyle();
			fill(shapeColor);

			pushMatrix();
			translate(posX, posY);
			rotate(angle);
			shapeMode(CENTER);

			noStroke();
			shape(currentShape, 0,0, newShapeSize, newShapeSize);
			popMatrix();
		}
	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "filterInteractif" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
