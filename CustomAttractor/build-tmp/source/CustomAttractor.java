import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.ugens.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CustomAttractor extends PApplet {



 
Minim       minim;
AudioOutput out;
Oscil       wave;

Attractor myAttractor;
Attractor myHelper;
int radius = 350;
int sensX = 1;
int sensY = 1;
int xCount = 44;
int yCount = 40;
int randomColorIndex = 3;
int index = 0;
float r = 150;
float g = 150;
float b = 150;
float diffR = 0;
float diffG = 0;
float diffB = 0;
int[] colorSet = new int[6];
float gridSize = 768;
Node[] myNodes = new Node[xCount*yCount];

public void setup() {
	size(1024, 768);
	noCursor();

	minim = new Minim(this);
	out = minim.getLineOut();
	wave = new Oscil( 440, 0.5f, Waves.SINE );
	wave.patch( out );

	myAttractor = new Attractor(width/2, height/2, radius);
	myHelper = new Attractor(0, 0);
	colorSet[0] = color(222,4,4);
	colorSet[1] = color(222,4,215);
	colorSet[2] = color(12,4,222);
	colorSet[3] = color(37,222,4);
	colorSet[4] = color(237,230,9);
	colorSet[5] = color(237,230,9);
	initGrid();
}

public void draw() {
	background(0);
	noFill();
	stroke(255,30);
	strokeWeight(2);
	ellipse(mouseX, mouseY, myAttractor.r*2, myAttractor.r*2);
	ellipse(myHelper.x, myHelper.y, myHelper.r*2, myHelper.r*2);
	myAttractor.x = mouseX;
	myAttractor.y = mouseY;

	if(myHelper.x>width-5) {
		sensX = -1;
	}
	if(myHelper.x<5) {
		sensX = 1;
	}
	if(myHelper.y>height-5) {
		sensY = -1;
	}
	if(myHelper.y<5) {
		sensY = 1;
	}
	// myHelper.x = myHelper.x+sensX;
	// myHelper.y = myHelper.y+sensY;
	if(index >= 10) {
		index = 0;
		fill(randomColor());
	}
	else fill(getColor());
	int numbInRange = 0;
	float averageVelocity = 0;
	for(int i=0; i<myNodes.length; i++) {
		noStroke();
		myAttractor.attract(myNodes[i]);
		myHelper.attract(myNodes[i]);
		myNodes[i].update();
		numbInRange += myAttractor.inRange(myNodes[i]);
		// fill(0,0,0,(myNodes[i].velocity.x)*100);
		if(myAttractor.getRange(myNodes[i]) > radius-5 && myAttractor.getRange(myNodes[i]) < radius + 5) {
			ellipse(myNodes[i].x, myNodes[i].y,10,10);
		}
		else {
			ellipse(myNodes[i].x, myNodes[i].y,5,5);
		}
		
		averageVelocity += (Math.abs(myNodes[i].velocity.x) + Math.abs(myNodes[i].velocity.y))/2;
	}
	index++;
	averageVelocity = averageVelocity/numbInRange;
	float freq = map( averageVelocity, -4, 10, 50, 520 );
	float amp = map( numbInRange+300, 0, xCount*yCount, 0f, 1f );
	wave.setAmplitude( amp );
  	if(freq < 9999) wave.setFrequency( freq );
  	// println();
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

public void mouseWheel(MouseEvent event) {
	float e = event.getCount();
	if(e < 0)radius += 5;
	else radius -= 5;
	myAttractor.r = radius;
	// myHelper.r = radius;
}
public int signum(float f) {
  if (f > 0) return 1;
  if (f < 0) return -1;
  return 0;
} 

public int randomColor() {
	//caclule de diff\u00e9rence en temps r\u00e9el
	float liveDiffR = r - red(colorSet[randomColorIndex]);
	float liveDiffG = g - green(colorSet[randomColorIndex]);
	float liveDiffB = b - blue(colorSet[randomColorIndex]);

	//si la diff\u00e9rence en temps r\u00e9el est a zero, nouvelle couleur et calcule de fraction de diff\u00e9rence
	if(liveDiffR + liveDiffG + liveDiffB <= 0) {
		
		randomColorIndex = throwColor();
		// println("new random color"+randomColorIndex);
		diffR = r - red(colorSet[randomColorIndex]);
		diffG = g - green(colorSet[randomColorIndex]);
		diffB = b - blue(colorSet[randomColorIndex]);
	}
	//si les fractions sont a zero, refait le calcule de fraction
	//** utiliser \u00e0 la premi\u00e8re utilisation **
	if(diffR + diffG + diffB <= 0) {
		// println("assignation de diff\u00e9rences");
		diffR = r - red(colorSet[randomColorIndex]);
		diffG = g - green(colorSet[randomColorIndex]);
		diffB = b - blue(colorSet[randomColorIndex]);
	}
	// calcule et modification du rouge
	// println(diffR);
	if(diffR != 0){
		if(signum(diffR) == -1) {
			r += (diffR*-1)/100;
			r = (r > red(colorSet[randomColorIndex])) ? red(colorSet[randomColorIndex]) : r;
		}
		else {
			r -= diffR/100;
			r = (r < red(colorSet[randomColorIndex])) ? red(colorSet[randomColorIndex]) : r;
		}
	}
	// calcule et modification du vert
	if(diffG != 0){
		if(signum(diffG) == -1) {
			g += (diffG*-1)/100;
			g = (g > green(colorSet[randomColorIndex])) ? green(colorSet[randomColorIndex]) : g;
		}
		else {
			g -= diffG/100;
			g = (g < green(colorSet[randomColorIndex])) ? green(colorSet[randomColorIndex]) : g;
		}
	}
	// calcule et modification du bleu
	if(diffB != 0){
		if(signum(diffB) == -1) {
			b += (diffB*-1)/100;
			b = (b > blue(colorSet[randomColorIndex])) ? blue(colorSet[randomColorIndex]) : b;
		}
		else {
			b -= diffB/100;
			b = (b < blue(colorSet[randomColorIndex])) ? blue(colorSet[randomColorIndex]) : b;
		}
	}
	//assignation de la couleur pour le fill()
	int c = color(r,g,b);
	// println(red(c));
	return c;
}

public int throwColor() {
	return (int) random(0,5);
}

public int getColor() {
	// println("retourne couleur");
	return color(r,g,b,255);
}


class Attractor {
	float x=0, y=0;
	float r = 150;

	Attractor(float theX, float theY, int radius) {
		x = theX;
		y = theY;
		r = radius;
	}
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
			float force = 1/pow(s,0.9f)-1;
			force = force/r;
			theNode.velocity.x += dx * force;
			theNode.velocity.y += dy * force;
		}
		else {
			theNode.velocity.x -= theNode.velocity.x/50;
			theNode.velocity.y -= theNode.velocity.y/50;
		}
	}
	public int inRange(Node theNode) {
		float dx = x - theNode.x;
		float dy = y - theNode.y;
		float d = mag(dx,dy);
		if(d>0 && d<r) return 1;
		else return 0;
	}
	public float getRange(Node theNode) {
		float dx = x - theNode.x;
		float dy = y - theNode.y;
		float d = mag(dx,dy);
		return d;
	}
}
class Node extends PVector {
	PVector velocity = new PVector();
	float minX=5, minY=5, maxX=width-5, maxY=height-5;
	float damping = 0.1f;

	Node(float xpos, float ypos) {
		x=xpos;
		y=ypos;
		// velocity.x = random(-3,3);
		velocity.x = 0;
		// velocity.y = random(-3,3);
		velocity.y = 0;
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CustomAttractor" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
