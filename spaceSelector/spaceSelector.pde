
PVector selector;
int angle;
PVector v1, v2;
int p;
boolean anim;
int progress;

void setup() {
	size(640,480);
	//noSmooth();
	angle = 0;
	p=0;
	anim=false;
	v1 = new PVector(mouseX,mouseY);
}

void draw() {
	background(0);
	selector = new PVector(mouseX, mouseY);
	fill(48,128,219);
	rect(width-50,height-50, 50,50);
	rect(0,0, 50,50);
	rect(0,height-50, 50,50);
	rect(width-50,0, 50,50);
	color target = get((int) selector.x, (int) selector.y);
	
	//println(target);

	v2 = new PVector(mouseX,mouseY);
	if(target == -16777216) {
		v1=v2;
		p=0;
		anim=false;
		progress=0;
	}
	else {
		p++;
		if(p>=20) {
			selectionner();
		}
	}
	noStroke();
	fill(255);
	ellipse(selector.x,selector.y,10,10);
}

void selectionner() {
	anim=true;
	animation();
}

void animation() {
	if(anim==true) {
		progress++;
		strokeCap(SQUARE);
		noFill();
		stroke(255);
		strokeWeight(30);
		arc(selector.x,selector.y,40,40,radians(0), radians(progress));
		if(progress >= 360) {
			action();
			progress=0;
		}
	}

	
}

void action() {
	println("ACTION");
}