import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.video.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class video_manipulation extends PApplet {

// Step 1. Import the video library




Minim minim;
AudioPlayer song;
FFT fft;

int sampleRate = 44100;
int timeSize = 1024;


Capture video;

public void setup() {
  size(600, 450);
  video = new Capture(this, 320, 240);
  video.start();
  
  minim = new Minim(this);
  song = minim.loadFile("Montr\u00e9al-Nord.mp3", 2048);
  song.loop();
  fft = new FFT( song.bufferSize(), song.sampleRate() ); // make a new fft

  // calculate averages based on a miminum octave width of 11 Hz
  // split each octave into 1 bands - this should result in 12 averages
  fft.logAverages(11, 1); // results in 12 averages, each corresponding to an octave, the first spanning 0 to 11 Hz. 
}

public void captureEvent(Capture video) {
  video.read();
}

public void draw() {
  float avg = 0;
  background(255);
  fft.forward(song.mix); // perform forward FFT on songs mix buffer
  //float bw = fft.getBandWidth(); // returns the width of each frequency band in the spectrum (in Hz).
  //println(bw); // returns 21.5332031 Hz for spectrum [0] & [512]
  for (int i = 0; i < 12; i++) {  // 12 is the number of bands 
  
    int lowFreq;

    if ( i == 0 ) {
      lowFreq = 0;
    } 
    else {
      lowFreq = (int)((sampleRate/2) / (float)Math.pow(2, 12 - i));
    }

    int hiFreq = (int)((sampleRate/2) / (float)Math.pow(2, 11 - i));

    // we're asking for the index of lowFreq & hiFreq
    int lowBound = fft.freqToIndex(lowFreq); // freqToIndex returns the index of the frequency band that contains the requested frequency
    int hiBound = fft.freqToIndex(hiFreq); 
    //println("range " + i + " = " + "Freq: " + lowFreq + " Hz - " + hiFreq + " Hz " + "indexes: " + lowBound + "-" + hiBound);
    avg = fft.calcAvg(lowBound, hiBound);
    // println(avg);
  }


  println(avg);
  // Tinting using mouse location
  tint(avg*20, avg*5, 255);

  // A video image can also be tinted and resized just as with a PImage.
  image(video, 0, 0, width, height);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "video_manipulation" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
