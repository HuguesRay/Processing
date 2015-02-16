class BreakCircle {
  ArrayList <Polygon2D> polygons = new ArrayList <Polygon2D> ();
  Voronoi voronoi;
  FloatRange xpos, ypos;
  PolygonClipper2D clip;
  float[] moveSpeeds;
  Vec2D pos, impact;
  float radius;
  int transparency;
  int start;
  VerletParticle2D vp;
  AttractionBehavior abh;
  boolean broken;

  BreakCircle(Vec2D pos, float radius) {
    this.pos = pos;
    this.radius = radius;
    vp = new VerletParticle2D(pos);
    abh = new AttractionBehavior(vp, radius*2.5 + max(0,50-radius), -1.2f, 0.01f);
    physics.addParticle(vp);
    physics.addBehavior(abh);
  }

  void run() {
    if(!broken) {
      moveVerlet();
      displayVerlet();
      checkBreak();
    }
    else {
      moveBreak();
      displayBreak();
    }
  }

  void moveVerlet() {
    pos = vp;
  }

  void displayVerlet() {
    fill(255);
    gfx.circle(pos, radius*2);
  }

  void checkBreak() {
    if(mouse.isInCircle(pos, radius) && mousePressed) {
      physics.removeParticle(vp);
      physics.removeBehavior(abh);
      impact = mouse;
      initiateBreak();
      println("initate Break");
    }
  }

  void initiateBreak() {
    broken = true;
    transparency = 255;
    start = frameCount;
    voronoi = new Voronoi();
    xpos = new BiasedFloatRange(pos.x-radius, pos.x+radius, impact.x, 0.333f);
    ypos = new BiasedFloatRange(pos.y-radius, pos.y+radius, impact.y, 0.5f);
    clip = new SutherlandHodgemanClipper(new Rect(pos.x-radius, pos.y-radius, radius*2, radius*2));
    addPolygons();
    addSpeeds();
  }
  void addPolygons() {
    for (int i=0; i<numPoints; i++) {
      voronoi.addPoint(new Vec2D(xpos.pickRandom(), ypos.pickRandom()));
    }
    for (Polygon2D poly : voronoi.getRegions()) {
      poly = clip.clipPolygon(poly);
      for(Vec2D v : poly.vertices) {
        if(!v.isInCircle(pos,radius)) {
          clipPoint(v);
        }
      }
      polygons.add(new Polygon2D(poly.vertices));
    }
  }
  void addSpeeds() {
    moveSpeeds = new float[polygons.size()];
    for (int i=0; i<moveSpeeds.length; i++) {
      moveSpeeds[i] = random(minSpeed, maxSpeed);
    }
  }
  void moveBreak() {
    for (int i=0; i<polygons.size(); i++) {
      Polygon2D poly = polygons.get(i);
      Vec2D centroid = poly.getCentroid();
      Vec2D targetDir = centroid.sub(impact).normalize();
      targetDir.scaleSelf(moveSpeeds[i]);
      for (Vec2D v : poly.vertices) {
        v.set(v.addSelf(targetDir));
      }
    }
  }
  void displayBreak() {

  }

  void clipPoint(Vec2D v) {
    v.subSelf(pos);
    v.normalize();
    v.scaleSelf(radius);
    v.addSelf(pos);
  }
}