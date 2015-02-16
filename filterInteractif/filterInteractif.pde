PShape currentShape;

int tileCount = 30;
float tileWidth, tileHeight;
float shapeSize = 10;
float newShapeSize = shapeSize;
float shapeAngle = 90;
float maxDist;

color shapeColor = color(255,211,67);

void setup() {
	size(1336, 720);
	background(155, 155, 155, 0.1);
	smooth();

	tileWidth = width/float(tileCount);
	tileHeight = height/float(tileCount);
	maxDist = sqrt(sq(width)+sq(height));

	currentShape = loadShape("module_1.svg");
}

void draw() {
	background(155, 155, 155, 0.1);
	smooth();

	for(int gridY=0; gridY<tileCount; gridY++) {
		//println(gridY);
		for (int gridX=0; gridX<tileCount; gridX++) {
			
			float posX = tileWidth*gridX + tileWidth/2;
			float posY = tileHeight+gridY + tileWidth/2;

			float angle = atan2(mouseY-posY, mouseX-posX) + radians(shapeAngle);

			newShapeSize = shapeSize;
			currentShape.disableStyle();
			fill(shapeColor);

			pushMatrix();
			translate(posX, posY);
			rotate(angle);
			shapeMode(CENTER);

			noStroke();
			shape(currentShape, 0,0, newShapeSize, newShapeSize);
			popMatrix();
		}
	}
}