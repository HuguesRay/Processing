//-------importation---------
import java.util.Map;
import java.util.Iterator;
import SimpleOpenNI.*;
import ddf.minim.*;
import ddf.minim.ugens.*;
import ddf.minim.signals.*;

//--------declaration de variables basic--------
ArrayList<Attractor> myAttractors = new ArrayList<Attractor>();
int radius = 150;
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

/*-------declaration de variable liées a minim---*/
Minim       minim;
AudioOutput out;
Oscil       wave;

//-------declaration variables pour tracking kinect------
SimpleOpenNI context;
int handVecListSize = 20;
Map<Integer,ArrayList<PVector>>  handPathList = new HashMap<Integer,ArrayList<PVector>>();
color[]       userClr = new color[]{ color(255,0,0),
                                     color(0,255,0),
                                     color(0,0,255),
                                     color(255,255,0),
                                     color(255,0,255),
                                     color(0,255,255)
                                   };

void setup() {
	// size(1024, 768);
	size(640,480);

	colorSet[0] = color(222,4,4);
	colorSet[1] = color(222,4,215);
	colorSet[2] = color(12,4,222);
	colorSet[3] = color(37,222,4);
	colorSet[4] = color(237,230,9);
	colorSet[5] = color(237,230,9);
	initGrid();

	//-------setup minim-------//
	minim = new Minim(this);
	out = minim.getLineOut();
	wave = new Oscil( 440, 0f, Waves.SINE );
	wave.patch( out );
	//------setup contect simpleNI-----
	context = new SimpleOpenNI(this);
  	if(context.isInit() == false)
  	{
    	println("Can't init SimpleOpenNI, maybe the camera is not connected!"); 
   		exit();
    	return;  
	}
	// enable depthMap generation 
	context.enableDepth();
  
	// disable mirror
	context.setMirror(true);

	// enable hands + gesture generation
	//context.enableGesture();
	context.enableHand();
	context.startGesture(SimpleOpenNI.GESTURE_WAVE);
}

void draw() {
	context.update();
	// overwritten by background***
	// scale(1.6);
	image(context.depthImage(),0,0);
	//--------draw visual----------
	// background(0);
	noFill();
	stroke(255,30);
	strokeWeight(2);

	if(index >= 10) {
		index = 0;
		fill(randomColor());
	}
	else fill(getColor());
	
	index++;
	// draw the tracked hands
  if(handPathList.size() > 0)  
  {    
    Iterator itr = handPathList.entrySet().iterator();  
    while(itr.hasNext())
    {
      Map.Entry mapEntry = (Map.Entry)itr.next(); 
      int handId =  (Integer)mapEntry.getKey();
      ArrayList<PVector> vecList = (ArrayList<PVector>)mapEntry.getValue();
      PVector p;
      PVector p2d = new PVector();
      
        stroke(userClr[ (handId - 1) % userClr.length ]);
        noFill(); 
        strokeWeight(1);        
        Iterator itrVec = vecList.iterator(); 
        beginShape();
          while( itrVec.hasNext() ) 
          { 
            p = (PVector) itrVec.next(); 
            
            context.convertRealWorldToProjective(p,p2d);
            vertex(p2d.x,p2d.y);
          }
        endShape();   
  
        stroke(userClr[ (handId - 1) % userClr.length ]);
        strokeWeight(4);
        p = vecList.get(0);
        context.convertRealWorldToProjective(p,p2d);
        point(p2d.x,p2d.y);


 //------- tracking des mains------------//
 		Attractor curAtt = myAttractors.get(handId-1);
        curAtt.x = p2d.x;
		curAtt.y = p2d.y;
		curAtt.r = p2d.z/4;
		strokeWeight(1);
		ellipse(curAtt.x, curAtt.y, curAtt.r*2, curAtt.r*2);
		if(index >= 10) {
			index = 0;
			fill(randomColor());
		}
		else fill(getColor());
		
		index++;
		int numbInRange = 0;
		float averageVelocity = 0;
		for(int i=0; i<myNodes.length; i++) {
			noStroke();
			curAtt.attract(myNodes[i]);
			myNodes[i].update();
			numbInRange += curAtt.inRange(myNodes[i]);
			if(curAtt.getRange(myNodes[i]) > curAtt.r-5 && curAtt.getRange(myNodes[i]) < curAtt.r + 5) {
				ellipse(myNodes[i].x, myNodes[i].y,10,10);
			}
			else {
				ellipse(myNodes[i].x, myNodes[i].y,5,5);
			}
			averageVelocity += (Math.abs(myNodes[i].velocity.x) + Math.abs(myNodes[i].velocity.y))/2;
		}
		//---------- frequence & amplitude -------------//
		float averageVelocity2 = averageVelocity/numbInRange;
		float freq = map( averageVelocity2, -4, 10, 50, 520 );
		// float freq = map( averageVelocity, 0, 10000, 0, 0.6 );
		// float amp = map( numbInRange+300, 0, xCount*yCount, 0f, 1f );
		float amp = map( averageVelocity, 550, 1000, 0f, 0.6f );
		wave.setAmplitude( amp );
		// wn.setAmp(freq);
	  	if(freq < 9999) wave.setFrequency( freq );
    }        
  }
  else {
  	wave.setAmplitude( 0 );
  	for(int i=0; i<myNodes.length; i++) {
		noStroke();
		myNodes[i].update();
		myNodes[i].reduce();
		// fill(0,0,0,(myNodes[i].velocity.x)*100);
		ellipse(myNodes[i].x, myNodes[i].y,5,5);
	}
  }
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
	// myAttractor.r = radius;
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
// -----------------------------------------------------------------
// hand events

void onNewHand(SimpleOpenNI curContext,int handId,PVector pos)
{
  println("onNewHand - handId: " + handId + ", pos: " + pos);
 
  ArrayList<PVector> vecList = new ArrayList<PVector>();
  vecList.add(pos);
  
  handPathList.put(handId,vecList);
  println(handId);
  myAttractors.add(new Attractor(pos.x,pos.y, pos.z));
}

void onTrackedHand(SimpleOpenNI curContext,int handId,PVector pos)
{
  //println("onTrackedHand - handId: " + handId + ", pos: " + pos );
  
  ArrayList<PVector> vecList = handPathList.get(handId);
  if(vecList != null)
  {
    vecList.add(0,pos);
    if(vecList.size() >= handVecListSize)
      // remove the last point 
      vecList.remove(vecList.size()-1); 
  }  
}

void onLostHand(SimpleOpenNI curContext,int handId)
{
  println("onLostHand - handId: " + handId);
  handPathList.remove(handId);
  // myAttractors.remove(handId);
}

// -----------------------------------------------------------------
// gesture events

void onCompletedGesture(SimpleOpenNI curContext,int gestureType, PVector pos)
{
  println("onCompletedGesture - gestureType: " + gestureType + ", pos: " + pos);
  
  int handId = context.startTrackingHand(pos);
  println("hand stracked: " + handId);
}

// -----------------------------------------------------------------
// Keyboard event
void keyPressed()
{

  switch(key)
  {
  case ' ':
    context.setMirror(!context.mirror());
    break;
  case '1':
    context.setMirror(true);
    break;
  case '2':
    context.setMirror(false);
    break;
  }
}

