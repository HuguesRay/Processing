import ddf.minim.*;
import ddf.minim.ugens.*;
 
Minim       minim;
AudioOutput out;
Oscil       wave;

Attractor myAttractor;
Attractor myHelper;
int radius = 350;
int sensX = 1;
int sensY = 1;
int xCount = 44;
int yCount = 40;
int randomColorIndex = 3;
int index = 0;
float r = 150;
float g = 150;
float b = 150;
float diffR = 0;
float diffG = 0;
float diffB = 0;
color[] colorSet = new color[6];
float gridSize = 768;
Node[] myNodes = new Node[xCount*yCount];

void setup() {
	size(1024, 768);
	noCursor();

	minim = new Minim(this);
	out = minim.getLineOut();
	wave = new Oscil( 440, 0.5f, Waves.SINE );
	wave.patch( out );

	myAttractor = new Attractor(width/2, height/2, radius);
	myHelper = new Attractor(0, 0);
	colorSet[0] = color(222,4,4);
	colorSet[1] = color(222,4,215);
	colorSet[2] = color(12,4,222);
	colorSet[3] = color(37,222,4);
	colorSet[4] = color(237,230,9);
	colorSet[5] = color(237,230,9);
	initGrid();
}

void draw() {
	background(0);
	noFill();
	stroke(255,30);
	strokeWeight(2);
	ellipse(mouseX, mouseY, myAttractor.r*2, myAttractor.r*2);
	ellipse(myHelper.x, myHelper.y, myHelper.r*2, myHelper.r*2);
	myAttractor.x = mouseX;
	myAttractor.y = mouseY;

	if(myHelper.x>width-5) {
		sensX = -1;
	}
	if(myHelper.x<5) {
		sensX = 1;
	}
	if(myHelper.y>height-5) {
		sensY = -1;
	}
	if(myHelper.y<5) {
		sensY = 1;
	}
	// myHelper.x = myHelper.x+sensX;
	// myHelper.y = myHelper.y+sensY;
	if(index >= 10) {
		index = 0;
		fill(randomColor());
	}
	else fill(getColor());
	int numbInRange = 0;
	float averageVelocity = 0;
	for(int i=0; i<myNodes.length; i++) {
		noStroke();
		myAttractor.attract(myNodes[i]);
		myHelper.attract(myNodes[i]);
		myNodes[i].update();
		numbInRange += myAttractor.inRange(myNodes[i]);
		// fill(0,0,0,(myNodes[i].velocity.x)*100);
		if(myAttractor.getRange(myNodes[i]) > radius-5 && myAttractor.getRange(myNodes[i]) < radius + 5) {
			ellipse(myNodes[i].x, myNodes[i].y,10,10);
		}
		else {
			ellipse(myNodes[i].x, myNodes[i].y,5,5);
		}
		
		averageVelocity += (Math.abs(myNodes[i].velocity.x) + Math.abs(myNodes[i].velocity.y))/2;
	}
	index++;
	averageVelocity = averageVelocity/numbInRange;
	float freq = map( averageVelocity, -4, 10, 50, 520 );
	float amp = map( numbInRange+300, 0, xCount*yCount, 0f, 1f );
	wave.setAmplitude( amp );
  	if(freq < 9999) wave.setFrequency( freq );
  	// println();
}

void initGrid() {
	int i=0;
	for(int x=0;x<xCount;x++) {
		for(int y=0;y<yCount;y++) {
			float posX = (gridSize/(xCount-1))*x+(width-gridSize)/2;
			float posY = (gridSize/(yCount-1))*y+(height-gridSize)/2;
			myNodes[i] = new Node(posX, posY);
			i++;
		}
	}
}

void mouseWheel(MouseEvent event) {
	float e = event.getCount();
	if(e < 0)radius += 5;
	else radius -= 5;
	myAttractor.r = radius;
	// myHelper.r = radius;
}
int signum(float f) {
  if (f > 0) return 1;
  if (f < 0) return -1;
  return 0;
} 

color randomColor() {
	//caclule de différence en temps réel
	float liveDiffR = r - red(colorSet[randomColorIndex]);
	float liveDiffG = g - green(colorSet[randomColorIndex]);
	float liveDiffB = b - blue(colorSet[randomColorIndex]);

	//si la différence en temps réel est a zero, nouvelle couleur et calcule de fraction de différence
	if(liveDiffR + liveDiffG + liveDiffB <= 0) {
		
		randomColorIndex = throwColor();
		// println("new random color"+randomColorIndex);
		diffR = r - red(colorSet[randomColorIndex]);
		diffG = g - green(colorSet[randomColorIndex]);
		diffB = b - blue(colorSet[randomColorIndex]);
	}
	//si les fractions sont a zero, refait le calcule de fraction
	//** utiliser à la première utilisation **
	if(diffR + diffG + diffB <= 0) {
		// println("assignation de différences");
		diffR = r - red(colorSet[randomColorIndex]);
		diffG = g - green(colorSet[randomColorIndex]);
		diffB = b - blue(colorSet[randomColorIndex]);
	}
	// calcule et modification du rouge
	// println(diffR);
	if(diffR != 0){
		if(signum(diffR) == -1) {
			r += (diffR*-1)/100;
			r = (r > red(colorSet[randomColorIndex])) ? red(colorSet[randomColorIndex]) : r;
		}
		else {
			r -= diffR/100;
			r = (r < red(colorSet[randomColorIndex])) ? red(colorSet[randomColorIndex]) : r;
		}
	}
	// calcule et modification du vert
	if(diffG != 0){
		if(signum(diffG) == -1) {
			g += (diffG*-1)/100;
			g = (g > green(colorSet[randomColorIndex])) ? green(colorSet[randomColorIndex]) : g;
		}
		else {
			g -= diffG/100;
			g = (g < green(colorSet[randomColorIndex])) ? green(colorSet[randomColorIndex]) : g;
		}
	}
	// calcule et modification du bleu
	if(diffB != 0){
		if(signum(diffB) == -1) {
			b += (diffB*-1)/100;
			b = (b > blue(colorSet[randomColorIndex])) ? blue(colorSet[randomColorIndex]) : b;
		}
		else {
			b -= diffB/100;
			b = (b < blue(colorSet[randomColorIndex])) ? blue(colorSet[randomColorIndex]) : b;
		}
	}
	//assignation de la couleur pour le fill()
	color c = color(r,g,b);
	// println(red(c));
	return c;
}

int throwColor() {
	return (int) random(0,5);
}

color getColor() {
	// println("retourne couleur");
	return color(r,g,b,255);
}


