 int xCount = 100;
int yCount = 100;
float gridSize = 768;

import generativedesign.*;


Node[] myNodes = new Node[xCount*yCount];
Attractor myAttractor;
int radius = 125;



void setup() {
	size(1024, 768);
	background(255);
	noCursor();
	// for(int i = 0; i<myNodes.length; i++) {
	// 	myNodes[i] = new Node(random(width), random(height));
	// }
	myAttractor = new Attractor(width/2, height/2);
	myAttractor.setMode(0);
	myAttractor.setStrength(5);
	myAttractor.radius = 125;
	initGrid();
}

void draw() {
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
	// println(myNodes[2].getVelocity());
}

void initGrid() {
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

void keyPressed() {
	if(key == 'r') {
		initGrid();
	}
}
