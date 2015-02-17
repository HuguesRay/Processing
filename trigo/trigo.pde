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
	if(mousePressed == false) {
		for(int i = 0; i<=8;i++) {
			angle = random(0,359);
			float posx = width/2 + radius/2 * cos(angle);
			float posy = width/2 + radius/2 * sin(angle);

			// println(Math.abs(height/2-radius/2));
			float[] choix = {0,90,180,270};


			float length = 50;
			// println(height/2+radius/2);

			float angle2=choix[(int) random(choix.length)];
			if(angle2==0 || angle2==180) length = 50/((Math.abs(height/2-posy)/10));
			if(angle2==90 || angle2==270) length = 50/((Math.abs(width/2-posx)/10));

			
			// if(posy<height/2) angle2=270;
			// if(posy>height/2) angle2=90;

			//float length = 50/((Math.abs(width/2-posx)/10));
			stroke(0,(int) length+20);
			float px = posx + length*cos(radians(angle2));
			float py = posy + length*sin(radians(angle2));
			line(posx,posy,px,py);
		}
		
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