import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class arc_rotate extends PApplet {


Minim minim;
AudioPlayer onde1;
AudioPlayer onde2;
AudioPlayer onde3;
AudioPlayer onde4;
AudioPlayer onde5;
AudioPlayer onde6;
AudioPlayer onde7;
AudioInput input;


ArrayList<monArc> mesArcs = new ArrayList<monArc>();
float opacity = 150;
int savedTime;
int totalTime = 1000;


public void setup(){
  size(1400,750);
  background(0);
  savedTime = millis();
  minim = new Minim(this);
  onde1 = minim.loadFile("g.wav", 1400);
  onde2 = minim.loadFile("f.wav", 1400);
  onde3 = minim.loadFile("e.wav", 1400);
  onde4 = minim.loadFile("d.wav", 1400);
  onde5 = minim.loadFile("c.wav", 1400);
  onde6 = minim.loadFile("b.wav", 1400);
  onde7 = minim.loadFile("a.wav", 1400);
  //loop();
}
 
public void draw(){
  background(0);
  int passedTime = millis() - savedTime;
  if (passedTime > totalTime) {
    //println( " 5 seconds have passed! " );
    savedTime = millis(); // Save the current time to restart the timer!
    int rand = PApplet.parseInt(random(1, 1400));
    mesArcs.add(new monArc(rand, mouseY));
  }
  //monArc delete = mesArcs.get(1);
  //println(delete.opOnde);
  //if(delete.opOnde;
  //stroke(opacity,opacity,255);
  strokeWeight(4);
  smooth();
  noFill();
  
  //dispatch display de toutes les arcs
  for (int k = 0; k < mesArcs.size(); k++) {
   monArc unoArc = mesArcs.get(k);
   unoArc.display();
   if(unoArc.finished()) {
     mesArcs.remove(k);
     //println("delete l'arc "+k);
   }
  }
}

//sur clic souris
public void mousePressed() {
  mesArcs.add(new monArc(mouseX, mouseY));
  totalTime = 999999999;
}

//CLASS DES ARCS
class monArc {
  float opOnde = 255;
  int x;
  int y;
  int radius = 100;
  int ang = 0;
  AudioPlayer monOnde;
  float hasard = random(0,300);
  
  //constructor
  monArc(int mX,int mY) {
    //opOnde = opOnde - 0.1;
    x = mX;
    y = mY;
    radius = x-(width/2);
    radius = radius*2;
    if(radius <0) {
      radius = radius*-1;
    }
    opacity = radius/3.92f;
   monOnde = musical(radius);
  }
  
 //animation des arcs
 public void display() {
  stroke(opacity,opacity,255, opOnde);
  ang += 1;
  ellipseMode(CENTER);
  arc(width/2, height/2, radius, radius, tan(ang), tan(ang+hasard));
  opOnde = opOnde - 2;
  //stroke(255, opOnde);
  // affichage onde de son
  //for(int i = 0; i < monOnde.bufferSize() - 1; i++)
  //{
  //  line(i, 300 + monOnde.mix.get(i)*50, i+1, 300 + monOnde.mix.get(i+1)*50);
  //}
  
 }
 
 public boolean finished() {
   if(opOnde<=0) {
     return true;
   }
   else {
     return false;
   }
 }
 
 //son d'instanciation
 public AudioPlayer musical(int note) {
   AudioPlayer onde = null;
    onde1.pause();
    onde2.pause();
    onde3.pause();
    onde4.pause();
    onde5.pause();
    onde6.pause();
    onde7.pause();
    onde1.rewind();
    onde2.rewind();
    onde3.rewind();
    onde4.rewind();
    onde5.rewind();
    onde6.rewind();
    onde7.rewind();
    if(note > 0 && note < 200) {
      onde1.play();
      onde = onde1;
    }
    if(note > 200 && note < 400) {
      onde2.play();
      onde = onde2;
    }
    if(note > 400 && note < 600) {
      onde3.play();
      onde = onde3;
    }
    if(note > 600 && note < 800) {
      onde4.play();
      onde = onde4;
    }
    if(note > 800 && note < 1000) {
      onde5.play();
      onde = onde5;
    }
    if(note > 1000 && note < 1200) {
      onde6.play();
      onde = onde6;
    }
    if(note > 1200 && note < 1400) {
      onde7.play();
      onde = onde7;
    }
    return onde;
 }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "arc_rotate" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
