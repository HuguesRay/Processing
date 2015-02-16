import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import toxi.geom.*; 
import toxi.processing.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class geoms extends PApplet {




ArrayList<Polygon2D> polygons = new ArrayList<Polygon2D>();
ArrayList<Vec2D> points = new ArrayList<Vec2D>();
int draggedPolygon = -1;
ToxiclibsSupport gfx;
boolean onPolygon;
Vec2D mouse;

public void setup() {
  size(1280, 720);
  gfx = new ToxiclibsSupport(this);
  noStroke();
  smooth();
}

public void draw() {
  //println("lal");
  background(255);
  mouse = new Vec2D(mouseX,mouseY);


  onPolygon = false;

  for(Polygon2D p : polygons) {
    if (p.containsPoint(mouse)) {
      onPolygon = true;
      fill(255,0,0);
    }
    else {
      fill(0);
    }
    gfx.polygon2D(p);
  }

  fill(0);
  for (Vec2D p : points) {
    ellipse(p.x, p.y, 5, 5);
  }
}

public void mousePressed() {
  if(!onPolygon) {
    points.add(mouse);
    if(mouseButton == RIGHT && points.size() >=3) {
      polygons.add(new Polygon2D(points));
      points.clear();
    }
  }
}

public void mouseDragged() {
  if (draggedPolygon == -1) {
    for (int i=0; i<polygons.size(); i++) {
      if(polygons.get(i).containsPoint(mouse)) {
        draggedPolygon = i;
      }
    }
  }
  else {
    Vec2D change = new Vec2D(mouseX-pmouseX, mouseY-pmouseY);
    Polygon2D p = polygons.get(draggedPolygon);
    for (Vec2D v : p.vertices) {
      v.addSelf(change);
    }
  }
}

public void mouseReleased() {
  draggedPolygon = -1;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "geoms" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
