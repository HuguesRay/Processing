import toxi.geom.*;
import toxi.processing.*;

ArrayList<Polygon2D> polygons = new ArrayList<Polygon2D>();
ArrayList<Vec2D> points = new ArrayList<Vec2D>();
int draggedPolygon = -1;
ToxiclibsSupport gfx;
boolean onPolygon;
Vec2D mouse;

void setup() {
  size(1280, 720);
  gfx = new ToxiclibsSupport(this);
  noStroke();
  smooth();
}

void draw() {
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

void mousePressed() {
  if(!onPolygon) {
    points.add(mouse);
    if(mouseButton == RIGHT && points.size() >=3) {
      polygons.add(new Polygon2D(points));
      points.clear();
    }
  }
}

void mouseDragged() {
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

void mouseReleased() {
  draggedPolygon = -1;
}