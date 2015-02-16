import processing.video.*;

Capture cam1;
Capture cam2;
Capture cam3;
Capture cam4;

void setup() {
  size(1280, 720);

  String[] cameras = Capture.list();
  
  if (cameras.length == 0) {
    println("There are no cameras available for capture.");
    exit();
  } else {
    //println("Available cameras:");
    for (int i = 0; i < cameras.length; i++) {
      println(cameras[i]);
    }
    
    // The camera can be initialized directly using an 
    // element from the array returned by list():
    cam1 = new Capture(this, width, height, 30);

    cam1.start();  

  }      
}

void draw() {
  if (cam1.available() == true) {
    cam1.read();


  }
  image(cam1, 0, 0, width, height);
  //filter(BLUR, random(2,254));
  int power = mouseY/3+1;
  println(power);
  filter(BLUR, power);
  // The following does the same, and is faster when just drawing the image
  // without any additional resizing, transformations, or tint.
  //set(0, 0, cam);
}