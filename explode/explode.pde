import toxi.geom.*;
import toxi.geom.mesh2d.*;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;
import toxi.util.datatypes.*;
import toxi.processing.*;

ArrayList <BreakCircle> circles = new ArrayList <BreakCircle> ();
VerletPhysics2D physics;
ToxiclibsSupport gfx;
FloatRange radius;
Vec2D origin, mouse;

int MaxCircles = 90;
int numPoints = 50;
int minSpeed = 2;
int maxSpeed = 14;

void setup() {
  size(1280, 720);
  smooth();
  noStroke();
  gfx = new ToxiclibsSupport(this);
  physics = new VerletPhysics2D();
  physics.setDrag(0.05f);
  physics.setWorldBounds(new Rect(0,0,width,height));
  radius = new BiasedFloatRange(30,100,30,0.06f);
  origin = new Vec2D(width/2,height/2);
  reset();
}

void draw() {
  removeAddCircles();
  background(255,0,0);
  physics.update();

  mouse = new Vec2D(mouseX,mouseY);
  for (BreakCircle bc : circles) {
    bc.run();
  }
}

void removeAddCircles() {
  for (int i=circles.size()-1; i>=0; i--) {
    if (circles.get(i).transparency <=0) {
      circles.remove(i);
      if(circles.size() < MaxCircles) {
        circles.add(new BreakCircle(origin,radius.pickRandom()));
        circles.add(new BreakCircle(origin,radius.pickRandom()));
      }
    }
  }
}

void reset() {
  println("reset initiate first circle");
  for (BreakCircle bc : circles) {
    physics.removeParticle(bc.vp);
    physics.removeBehavior(bc.abh);
  }
  circles.clear();
  circles.add(new BreakCircle(origin,200));
}