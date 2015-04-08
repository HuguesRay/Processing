class Attractor {
	float x=0, y=0, z=0;
	float r = 150;


	Attractor(float theX, float theY, float theZ) {
		println("new attractor!");
		x = theX;
		y = theY;
		r = theZ/4;
	}

	Attractor(float theX, float theY, int radius) {
		println("new attractor!");
		x = theX;
		y = theY;
		r = radius;
	}
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
		else {
			// theNode.velocity.x -= theNode.velocity.x/50;
			// theNode.velocity.y -= theNode.velocity.y/50;
			theNode.reduce();
		}
	}
	int inRange(Node theNode) {
		float dx = x - theNode.x;
		float dy = y - theNode.y;
		float d = mag(dx,dy);
		if(d>0 && d<r) return 1;
		else return 0;
	}
	float getRange(Node theNode) {
		float dx = x - theNode.x;
		float dy = y - theNode.y;
		float d = mag(dx,dy);
		return d;
	}
}