package main.java.app;

import processing.core.PApplet;
import processing.core.PVector;
import main.java.space.Warehouse;
import peasy.*;

public class Run extends PApplet {

	PeasyCam camera;
	Forklift forklift;
	
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

		warehouse = new Warehouse(this);
		
		warehouse.setTerrainResolution(w, h);
		
		terrain = warehouse.terrainFactory();

		forklift = new Forklift(this, new PVector(-0.0f, 0, terrain[15][15]), new PVector(0, 0, 0));
		
		camera = new PeasyCam(this, 250);

		frameRate(30);
		fill(120, 50, 240);
		noStroke();
	}

	public void draw() {
		background(0);
		lights();

		warehouse.draw();
		warehouse.drawGuideLines();
				
		forklift.operate();
		forklift.update();
		forklift.draw();
		
		// TODO : Test First Person Camera
		// camera();
	}
}

