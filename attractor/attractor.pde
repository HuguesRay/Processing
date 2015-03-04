Node[] myNodes = new Node[35];

void setup() {
	size(1024, 768);
	background(255);
	for(int i = 0; i<myNodes.length; i++) {
		myNodes[i] = new Node(random(width), random(height));
	}
}

void draw() {
	// println(mouseX);
	background(255);
	noFill();
	stroke(0);
	strokeWeight(2);
	ellipse(mouseX, mouseY, 250, 250);
	for(int i=0; i<myNodes.length; i++) {
		noStroke();
		myNodes[i].update();
		fill(0,0,0,255);
		// fill(0,0,0,(myNodes[i].velocity.x)*100);
		ellipse(myNodes[i].x, myNodes[i].y,5,5);
		if(dist(myNodes[i].x, myNodes[i].y, mouseX, mouseY)<125) {
			stroke(2);
			line(myNodes[i].x, myNodes[i].y, mouseX, mouseY);
		}
	}
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
		//println(dist(this, new PVector(mouseX, mouseY)));
		if(dist(this, new PVector(mouseX, mouseY))<125) {
			velocity.x *= (1-damping);
			velocity.y *= (1-damping);
		}
		else {
			if(velocity.x<3 && velocity.x>-3) {
				velocity.x *= (1+damping);
			}
			if(velocity.y<3 && velocity.y>-3) {
				velocity.y *= (1+damping);
			}
		}
	}


}