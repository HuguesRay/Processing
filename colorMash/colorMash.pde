import generativedesign.*;
import processing.pdf.*;
import java.util.Calendar;

boolean savePDF = false;

PImage img;
color[] colors;

String sortMode = null;



void setup(){
  size(900, 900);
  colorMode(HSB, 360, 100, 100, 100);
  noStroke();
  noCursor();
  img = loadImage("pic1.jpg"); 
}


void draw(){
  if (savePDF) {
    beginRecord(PDF, timestamp()+".pdf");
    colorMode(HSB, 360, 100, 100, 100);
    noStroke();
  }

  int tileCount = width / max(mouseX, 1);
  float rectSize = width / float(tileCount);

  // get colors from image
  int i = 0; 
  colors = new color[tileCount*tileCount];
  for (int gridY=0; gridY<tileCount; gridY++) {
    for (int gridX=0; gridX<tileCount; gridX++) {
      int px = (int) (gridX * rectSize);
      int py = (int) (gridY * rectSize);
      colors[i] = img.get(px, py);
      i++;
    }
  }

  // sort colors
  if (sortMode != null) colors = GenerativeDesign.sortColors(this, colors, sortMode);
  

  // draw grid
  i = 0;
  for (int gridY=0; gridY<tileCount; gridY++) {
    for (int gridX=0; gridX<tileCount; gridX++) {
      fill(colors[i]);
      rect(gridX*rectSize, gridY*rectSize, rectSize, rectSize);
      i++;
    }
  }

  if (savePDF) {
    savePDF = false;
    endRecord();
  }
}


void keyReleased(){
  if (key=='c' || key=='C') GenerativeDesign.saveASE(this, colors, timestamp()+".ase");
  if (key=='s' || key=='S') saveFrame(timestamp()+"_##.png");
  if (key=='p' || key=='P') savePDF = true;

  if (key == '1') img = loadImage("pic1.jpg");
  //if (key == '2') img = loadImage("pic2.jpg"); 
  //if (key == '3') img = loadImage("pic3.jpg"); 

  if (key == '4') sortMode = null;
  if (key == '5') sortMode = GenerativeDesign.HUE;
  if (key == '6') sortMode = GenerativeDesign.SATURATION;
  if (key == '7') sortMode = GenerativeDesign.BRIGHTNESS;
  if (key == '8') sortMode = GenerativeDesign.GRAYSCALE;
}


// timestamp
String timestamp() {
  Calendar now = Calendar.getInstance();
  return String.format("%1$ty%1$tm%1$td_%1$tH%1$tM%1$tS", now);
}



