Node[] myNodes = new Node[35];
Attractor myAttractor;
int radius = 125;
void setup() {
	size(1024, 768);
	background(255);
	noCursor();
	for(int i = 0; i<myNodes.length; i++) {
		myNodes[i] = new Node(random(width), random(height));
	}
	myAttractor = new Attractor(width/2, height/2);
}

void draw() {
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
void mouseWheel(MouseEvent event) {
	float e = event.getCount();
	println(e);
	if(e < 0)radius -= 5;
	else radius += 5;
	myAttractor.r = radius;
}


class Node extends PVector {
	PVector velocity = new PVector();
	float minX=5, minY=5, maxX=width-5, maxY=height-5;
	float damping = 0.1;

	Node(float xpos, float ypos) {
		x=xpos;
		y=ypos;
		velocity.x = random(-3,3);
		// velocity.x = 0.1;
		velocity.y = random(-3,3);
		// velocity.y = 0.1;
	}

	void update() {
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
	void attract(Node theNode) {
		float dx = x - theNode.x;
		float dy = y - theNode.y;
		float d = mag(dx,dy);
		if(d>0 && d<r) {
			float s = d/r;
			float force = 1/pow(s,0.9)-1;
			force = force/r;
			theNode.velocity.x += dx * force;
			theNode.velocity.y += dy * force;
		}
	}
}