package main.java.app;

import processing.core.PApplet;
import processing.core.PVector;
import main.java.engine.ForkLift;
import main.java.space.Warehouse;
import peasy.*;

public class Run extends PApplet {

	PeasyCam camera;
	ForkLift car;
	
	Warehouse warehouse;

	int rows, cols, scale = 20;

	int w = 600;
	int h = 600;
	
	float[][] terrain;

	public static void main(String[] args) {
		PApplet.main("main.java.app.Run");
	}

	public void settings() {
		size(1280, 768, P3D);
	}

	public void setup() {
				
//		cols = w / scale;
//		rows = h / scale;

		warehouse = new Warehouse(this);
		
		warehouse.setTerrainResolution(w, h);
		
		terrain = warehouse.terrainFactory();

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
		
		warehouse.draw();
				
		car.operate();
		car.update();
		car.draw();
	}

	public void drawAxis() {
		stroke(255);
		line(-100, 0, 0, 100, 0, 0);
		text("X", 90, 0, 0);

		stroke(255);
		line(0, -100, 0, 0, 100, 0);
		text("Y", 0, 90, 0);

		stroke(255);
		line(0, 0, -100, 0, 0, 100);
		text("Z", 0, 0, 90);
	}

}

