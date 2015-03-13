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

public class swarm extends PApplet {

int xCount = 10;
int yCount = 10;
float gridSize = 768;

Node[] myNodes = new Node[xCount*yCount];
Attractor myAttractor;
int radius = 125;



public void setup() {
	size(1024, 768);
	background(255);
	noCursor();
	// for(int i = 0; i<myNodes.length; i++) {
	// 	myNodes[i] = new Node(random(width), random(height));
	// }
	myAttractor = new Attractor(width/2, height/2);

	initGrid();
}

public void draw() {
	// println(mouseX);
	background(255);
	noFill();
	stroke(0,150);
	strokeWeight(2);
	ellipse(mouseX, mouseY, radius*2, radius*2);
	myAttractor.x = mouseX;
	myAttractor.y = mouseY;
	for(int i=0; i<myNodes.length; i++) {
		noStroke();
		myAttractor.attract(myNodes[i]);
		myNodes[i].update();
		fill(0,0,0,255);
		// fill(0,0,0,(myNodes[i].velocity.x)*100);
		ellipse(myNodes[i].x, myNodes[i].y,5,5);
	}
}

public void initGrid() {
	int i=0;
	for(int x=0;x<xCount;x++) {
		for(int y=0;y<yCount;y++) {
			float posX = (gridSize/(xCount-1))*x+(width-gridSize)/2;
			float posY = (gridSize/(yCount-1))*y+(height-gridSize)/2;
			myNodes[i] = new Node(posX, posY);
			i++;
		}
	}
}

public void keyPressed() {
	if(key == 'r') {
		initGrid();
	}
}

class Node extends PVector {
	PVector velocity = new PVector();
	float minX=5, minY=5, maxX=width-5, maxY=height-5;
	float damping = 0.02f;

	Node(float xpos, float ypos) {
		x=xpos;
		y=ypos;
		velocity.x = 0;
		// velocity.x = 0.1;
		velocity.y = 0;
		// velocity.y = 0.1;
	}

	public void update() {
		x += velocity.x;
		y += velocity.y;
		if(x<minX) velocity.x *= -1;
		if(x>maxX) velocity.x *= -1;

		if(y>maxY) velocity.y *= -1;
		if(y<minY) velocity.y *= -1;
	}
}

class Attractor {
	float x=0, y=0;
	float r = radius;

	Attractor(float theX, float theY) {
		x = theX;
		y = theY;
	}
	public void attract(Node theNode) {
		float dx = x - theNode.x;
		float dy = y - theNode.y;
		float d = mag(dx,dy);
		if(d>0 && d<r) {
			float s = d/r;
			float force = 1/pow(s,0.5f)-1;
			force = force/r;
			theNode.velocity.x += dx * force;
			theNode.velocity.y += dy * force;
		}
		else {
			float s = d/r;
			float force = 1/pow(s,0.5f)-1;
			force = force/r;
			// println(theNode.velocity.x);
			// theNode.velocity.x += dx / force;
			// theNode.velocity.y += dy / force;
		}
	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "swarm" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
