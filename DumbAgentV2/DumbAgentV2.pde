float angle;
int radius;

void setup() {
	size(720,720);
	radius = width/3;
	
	background(255,255,255);
}

void draw() {
	noFill();
	ellipse(width/2,height/2,radius,radius);
	fill(255,255,255);
	if(mousePressed == true) {
		angle= random(0,359);
		float posx = width/2 + radius/2 * cos(angle);
		float posy = width/2 + radius/2 * sin(angle);
		//ellipse(posx,posy, 10, 10);
		stroke(0);
		line(posx,posy,mouseX,mouseY);
	}
}

void mousePressed() {
	//background(255,255,255);
}

void keyReleased() {
	if(key == 'r'){
		background(255,255,255);
	}
	
}