
import peasy.*;

//PeasyCam cam;


int maxCount = 5000;
int currentCount = 1;

float[] x = new float[maxCount];
float[] y = new float[maxCount];
float[] r = new float[maxCount];

void setup() {
	size(1336,720);
	background(0);
	smooth();

//	cam = new PeasyCam(this, 100);
  //cam.setMinimumDistance(50);
 // cam.setMaximumDistance(500);

	x[0] = width/2;
	y[0] = height/2;
	r[0] = 10;

}

void draw() {
	background(0);

	strokeWeight(0.5);

	float newR = random(1,7);
	float newX = random(0+newR, width-newR);
	float newY = random(0+newR, height-newR);

	float closestDistance = 100000000;
	int closestIndex = 0;

	for(int i=0; i< currentCount; i++){
		float newDist = dist(newX, newY, x[i], y[i]);
		if(newDist<closestDistance) {
			closestDistance = newDist;
			closestIndex = i;
		}
	}

	float angle = atan2(newY-y[closestIndex], newX - y[closestIndex]);

	x[currentCount] = x[closestIndex] + cos(angle) * (r[closestIndex] + newR);
	y[currentCount] = y[closestIndex] + sin(angle) * (r[closestIndex] + newR);
	r[currentCount] = newR;
	currentCount++;

	for(int i = 0; i<currentCount; i++) {
		fill(227,227,5);
		ellipse(x[i], y[i], r[i], r[i]);
	}
}