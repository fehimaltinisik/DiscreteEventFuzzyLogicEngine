package main;

import processing.core.PApplet;
import processing.core.PVector;
import engines.Agent;
import engines.ForkLift;
import peasy.*;

public class Run extends PApplet {

	PeasyCam camera;
	ForkLift car;

	int rows, cols, scale = 20;

	int w = 600;
	int h = 600;

	float[][] terrain;

	public static void main(String[] args) {
		PApplet.main("main.Run");
	}

	public void settings() {
		size(1280, 768, P3D);
	}

	public void setup() {
				
		cols = w / scale;
		rows = h / scale;

		float terrain_roughness = (float) 0.1;

		terrain = new float[rows][cols];

		float yoff = 0;
		for (int y = 0; y < rows; y++) {
			float xoff = 0;
			for (int x = 0; x < cols; x++) {
				// terrain[x][y] = map(noise(xoff, yoff), 0, 1, -50, 50);
				terrain[x][y] = 0;
				xoff += terrain_roughness;
			}
			yoff += terrain_roughness;
		}

		car = new ForkLift(this, new PVector(-0.0f, 0, terrain[15][15]), new PVector(0, 0, 0));

		frameRate(30);
		fill(120, 50, 240);
		
		camera = new PeasyCam(this, 100);

		noStroke();
	}

	public void draw() {
		background(0);
		lights();
		
//		camera(car.position.x + 150 * cos(car.heading + PI),
//		car.position.y + 150 * sin(car.heading + PI), 100, car.position.x,
//		car.position.y, car.position.z, (float) 0, (float) 0, (float) -1.0);
		

		drawAxis();

		pushMatrix();
		translate(-w / 2, -h / 2);

		fill(128, 0, 128);
		stroke(127);

		for (int y = 0; y < rows - 1; y++) {
			beginShape(TRIANGLE_STRIP);
			for (int x = 0; x < cols; x++) {
				vertex(x * scale, y * scale, terrain[x][y]);
				vertex(x * scale, (y + 1) * scale, terrain[x][y + 1]);
			}
			endShape();
		}

		popMatrix();

		// car.position.z = terrain[15 + (int) car.position.x / scale][15 + (int) car.position.y / scale] + 3;
		// camera.lookAt(car.position.x, car.position.y, car.position.z);
				
		car.operate();
		car.update();
		car.draw();

	}

	public void drawAxis() {
		stroke(255);
		line(-100, 0, 0, 100, 0, 0);
		// fill(255);
		text("X", 90, 0, 0);

		stroke(255);
		line(0, -100, 0, 0, 100, 0);
		// fill(255);
		text("Y", 0, 90, 0);

		stroke(255);
		line(0, 0, -100, 0, 0, 100);
		// fill(255);
		text("Z", 0, 0, 90);

	}

}

