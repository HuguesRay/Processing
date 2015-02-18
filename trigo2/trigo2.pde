float angle;
int radius;
float posx;
float posy;
float oPosX;
float oPosY;
int oRadius;
int set=30;
ArrayList<Cercle> tunnel = new ArrayList<Cercle>();

void setup() {
	size(720,720);	
	background(255,255,255);
	posx=(int)random(0,width);
	posy=(int)random(0,height);
}

void draw() {
	noFill();
	oRadius = radius;
	radius = (int) random(10,30);
	set++;
	if(set>=40) {
		set=0;
		newPosition(posx,posy);
		float x3 = oPosX-posx;
		float y3 = oPosY-posy;
		if(abs(x3+y3) >= radius) {
			tunnel.add(new Cercle(posx,posy,radius));
		}	
	}
	

	//dispatch display() de chaque cercles
	for(int i = 0; i<tunnel.size(); i++) {
		Cercle monCercle = tunnel.get(i);
		smooth();
		monCercle.display();
	}
	println(tunnel.size());
}

void mousePressed() {
	//background(255,255,255);
}

void keyReleased() {
	if(key == 'r'){
		background(255,255,255);
	}
	
}
void newPosition(float x,float y) {
	int NextAngle = (int) random(0,359);
	oPosX = posx;
	oPosY = posy;
	posx =  posx + (radius/2+oRadius/2) * cos(NextAngle);
	posy =  posy + (radius/2+oRadius/2) * sin(NextAngle);
	if(posx>=width) {
		posx = posx-width;
	}
	if(posx<=0) {
		posx=width;
	}
	if(posy>=height) {
		posy = posy-height;
	}
	if(posy<=0) {
		posy = height;
	}
}

class Cercle {
	float x;
	float y;
	int radius;

	Cercle(float px, float py, int rad) {
		smooth();
		x = px;
		y = py;
		radius = rad;

	}
	void display() {
		smooth();
		ellipse(x,y,radius,radius);
	}
}