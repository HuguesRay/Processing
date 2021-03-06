import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class veryAttractor extends PApplet {

//Attractors and Fractals
//Grant Schindler, 2008

//Variables and Coefficients (Initial Values) ------------------------------------
float a = 1.78125f, b = -0.78125f, c = 1.90625f, d = 2.65625f, e = 0.7f, f = -1.1f;  //Coefficients
float x = 0.6f, y = 0.9f, z = 0.3f;     //Variables
float xn,yn,zn,la,lb,lc,ld;          //Temporary Copies of Variables, Coefficients

int N = 800, u, v;                   //Image Size, Pixel Coordinates
PImage img = createImage(N,N,RGB);   //Image Itself
float K = N*0.2f;                     //Image Scaling Constant

float[] r = {0.0f,0.0f,0.0f};           //Color (float)
int[] clr = {0,0,0};                 //Color (int)
float[][][] fimg;                    //Point Accumulation Array

int nrSamples = 1000000;
int nrPasses = 16;
int nrIterations = nrSamples/nrPasses;
int imageProgress=0, total=0; //Bookkeeping
boolean noise = true, init = true, hide = false;           //Display Options

public void update(){
  la = a; lb = b; lc = c; ld = d; //Add Noise to Coefficients for Smoothness
  if (noise) { la += random(-0.001f, 0.001f);  lb += random(-0.001f, 0.001f);
               lc += random(-0.001f, 0.001f);  ld += random(-0.001f, 0.001f);}
  
  xn = sin (la*y) - cos(lb*x); //*************************************************
  yn = sin (lc*x) - cos(ld*y); //** Update Temp Variables -- Magic Lies Here  ****
  zn = sin ( e*x) - cos( f*z); //*************************************************
  
  x = xn;  y = yn;  z = zn;       //Set Original to Temp
  u = (int)((x+2.5f) * K);         //Convert to 2D Image Space for Plotting
  v = (int)((y+2.5f) * K);
  
  r[0] = z * 0.9f + (1.0f-z) * 0.6f; //Map Z-Coordinate to Color
  r[1] = z * 0.2f + (1.0f-z) * 0.4f;
  r[2] = z * 0.5f + (1.0f-z) * 0.9f;
}

public void accumulatePoints(){
  if (imageProgress < nrPasses){
    colorMode(RGB, 1.0f);
    for (int i = 0; i < nrIterations; i++){
      update();                                           //Compute Next Point
      fimg[u][v][0] += r[0];  fimg[u][v][1] += r[1];      //Add New Point to Total
      fimg[u][v][2] += r[2];  fimg[u][v][3] += 1.0f;
      if (i < 2000) {stroke(r[0],r[1],r[2]); point(u,v);} //Draw Points
    }
    colorMode(RGB, 255);
    imageProgress++;  if (imageProgress <= nrPasses) total += nrIterations;
  }
}

public void draw(){
  
  background(0);
  float a0 = a, b0 = b, c0 = c, d0 = d;                //Adjust Coefficients Based On Mouse Position
  if (active == 1) { ma = mouseX; mb = mouseY; a = fromMouse(ma); b = fromMouse(mb);}
  if (active == 2) { mc = mouseX; md = mouseY; c = fromMouse(mc); d = fromMouse(md);}
  if (!(a == a0 && b == b0 && c == c0 && d == d0)) {init = true;} //Mouse Moved - Parameters Changed
  
  //Initialize Collection Bins
  if (init) {
    for (int i=0; i < N; i++) {
      for (int j=0; j < N; j++) {
        for (int k=0; k < 4; k++) {
          fimg[i][j][k] = 1.0f;     //Set All to 1.0 (Log-Valued Zero)
    }}}
    imageProgress = 0; total = 0; init = false;
  }

  accumulatePoints();
  //-- Render Accumulated Points -------------------------------------------------------
  if (imageProgress == nrPasses && total == nrSamples){

    //Find Max Value - //
    float t1 = millis();
    float max = -1.0f;
    for (int i=0; i < N; i++) {
      for (int j=0; j < N; j++) {
          if (fimg[i][j][3] > max){
            max = fimg[i][j][3];}}}

    //Adjust Values and Fill Image
    float logval, logmax = log(max);
    float M = (logmax * logmax) / 255.0f;  //Precomputation for ratio (log(val)/log(max))^2
    
    img.loadPixels();
    for (int i=0; i < N; i++) {
      for (int j=0; j < N; j++) {
        for (int k=0; k < 3; k++) {
          logval = log(fimg[i][j][k]);
          clr[k] = (int) (logval * logval / M);
        }
        img.pixels[j*N + i] = color(clr[0],clr[1],clr[2]);
    }}
    img.updatePixels(); //
    float t2 = millis(); println("Render: " + (t2-t1));
    total++;
  }

  if (total >= nrSamples) {image(img,0,0,N,N);} //Draw Rendered Image
  
  //Draw Indicators for Coefficients
  if (!hide) {  stroke(200);
    fill(100,0,0); ellipse(ma, mb, (active == 1) ? 20 : 10, (active == 1) ? 20: 10);
    fill(0,0,100); ellipse(mc, md, (active == 2) ? 20 : 10, (active == 2) ? 20: 10);}
}

//------------------------------------------------------------------
//-- User Interface ------------------------------------------------
//------------------------------------------------------------------
public int   toMouse(float a) {return (int)((float)N * (a + 4.0f)/8.0f);}
public float fromMouse(int   X) {return -4.0f + 8.0f * (float)X/(float)N;}
int ma,mb,mc,md,active = 0;  //Mouse Coordinates of Coefficients

public void setup(){
  size(N,N);
  frameRate(30);
  fimg = new float[N][N][4];
  ma = toMouse(a); mb = toMouse(b); mc = toMouse(c); md = toMouse(d);
}

public void keyPressed() {if (key == 'n') noise = !noise; if (key == 'h') hide = !hide;}

public void mousePressed(){
  if (active != 0) active = 0;
  else if (sq(mouseX-mc) + sq(mouseY-md) < sq(10.0f)) {active = 2;}
  else if (sq(mouseX-ma) + sq(mouseY-mb) < sq(10.0f)) {active = 1;}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "veryAttractor" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
