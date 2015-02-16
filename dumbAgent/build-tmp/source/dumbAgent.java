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

public class dumbAgent extends PApplet {

int n = 0;
int ne = 1;
int e = 2;
int se = 3;
int s = 4;
int sw = 5;
int w = 6;
int nw = 7;

int stepsize = 1;
int radius = 1;
int direction;

int posX;
int posY;

public void setup() {
	size(1280,720);
	background(215,232,213);
	posX=width/2;
	posY=height/2;
        noStroke();
}

public void draw() {
	for (int i = 0; i<mouseX/4; i++) {
		direction = (int) random(0,8);
		//println(direction);

		if(direction == 0) {//NORTH
			posY -= stepsize;
		}
		else if(direction == 1) {//NORTHEAST
			posY -= stepsize;
			posX += stepsize;
		}
		else if(direction == 2) {//EAST
			posX += stepsize;
		}
		else if (direction == 3) {//SOUTHEAST
			posX += stepsize;
			posY += stepsize;
		}
		else if (direction == 4) {//SOUTH
			posY += stepsize;
		}
		else if (direction == 5) {//SOUTHWEST
			posY += stepsize;
			posX -= stepsize;
		}
		else if (direction == 6) {//WEST
			posX -= stepsize;
		}
		else if  (direction == 7) {//NORTHWEST
			posX -= stepsize;
			posY -= stepsize;
		}

                if(posX<=0) {
                  posX=width;
                }
                else if (posX>=width){
                  posX=0;
                }
                else if(posY<=0) {
                  posY=height;
                }
                else if(posY>=height) {
                  posY=0;
                }
                //println(mouseY/4);
		fill(74,58,71, mouseY/5);
		ellipse(posX, posY, radius*2, radius*2);
	}
		
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "dumbAgent" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
