void setup() {
  size(640, 360);
  background(102);
}
int size = 55;
void draw() {

}
void mouseReleased() {
  ellipse(mouseX, mouseY, size, size);
  size=size+10;
}
