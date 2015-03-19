class Node extends PVector {
	PVector velocity = new PVector();
	float minX=5, minY=5, maxX=width-5, maxY=height-5;
	float damping = 0.1;

	Node(float xpos, float ypos) {
		x=xpos;
		y=ypos;
		// velocity.x = random(-3,3);
		velocity.x = 0;
		// velocity.y = random(-3,3);
		velocity.y = 0;
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