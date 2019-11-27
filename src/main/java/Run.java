package main.java;

import processing.core.PApplet;
import processing.core.PVector;
import peasy.*;
import main.java.app.agents.Forklift;
import main.java.app.agents.Rover;
import main.java.engine.fuzzytoolkit.FuzzyMath;
import main.java.space.Environment;
import main.java.space.Workspace;
import main.java.space.WorkspaceBuilder;
import main.java.space.WorkspaceFactory;

public class Run extends PApplet {
	
	int window_width = 1280;
	int window_heigth = 768;

	PeasyCam camera;
	HUD hud;
	
	Rover rover;
	Forklift forklift;
	
	Workspace warehouse;

	float[][] terrain;

	float[] x_qual = FuzzyMath.linspace(1, 10, 9);
	float[] x_serv = FuzzyMath.linspace(1, 10, 9);
	float[] x_tip = FuzzyMath.linspace(1, 25, 24);
	
	public static void main(String[] args) {
		PApplet.main("main.java.Run");
	}

	public void settings() {
		size(window_width, window_heigth, P3D);
	}

	public void setup() {
		
		WorkspaceFactory workspaceFactory = new WorkspaceFactory();
		
		WorkspaceBuilder workspaceBuilder = new WorkspaceBuilder(this);
		workspaceBuilder.setWorkspaceFactory(workspaceFactory);
		
		workspaceBuilder.setWorkspace("warehouse");
		workspaceBuilder.configure();
		
		warehouse = workspaceBuilder.getWorkspace();
		
		terrain = warehouse.terrainFactory();

		rover = new Rover(this, new PVector(-0.0f, 0, terrain[15][15]), new PVector(0, 0, 0));
		// rover.toggleManualDriving();
		// rover.toggleFirstPersonCamera();

		forklift = new Forklift(this, new PVector(-0.0f, 0, terrain[18][18]), new PVector(0, 0, 0));
		forklift.toggleManualDriving();
		// rover.toggleFirstPersonCamera();

		camera = new PeasyCam(this, 250);
		
		hud = new HUD(this, camera, window_width, window_heigth);
		hud.registerDiscreteFunction("x_qual", x_qual);
		hud.registerDiscreteFunction("x_serv", x_serv);
		
		frameRate(30);
		fill(120, 50, 240);
		noStroke();		
	}

	public void draw() {
		background(0);

		warehouse.draw();
		warehouse.drawGuideLines();
		
		warehouse.simulate();
								
		forklift.operate();
		forklift.update();
		forklift.draw();
		
		rover.setTarget(forklift.getPosition());
		
		rover.operate();
		rover.update();
		rover.draw();
		
		hud.drawDiscreteFunctions("all");
	}
}

