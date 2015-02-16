float angle;
int radius;
float posx;
float posy;

void setup() {
	size(720,720);	
	background(255,255,255);
	posx=(int)random(0,width);
	posy=(int)random(0,height);
}

void draw() {
	noFill();
	radius = (int) random(10,30);
	point(posx,posy);
	ellipse(posx,posy, radius,radius);

	int NextAngle = (int) random(0,359);
	posx =  posx + radius/2 * cos(NextAngle);
	posy =  posy + radius/2 * sin(NextAngle);
}

void mousePressed() {
	//background(255,255,255);
}

void keyReleased() {
	if(key == 'r'){
		background(255,255,255);
		posx=(int)random(0,width);
		posy=(int)random(0,height);
	}
	
}