package main.java;

import processing.core.PApplet;
import processing.core.PVector;
import peasy.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import main.java.app.agents.Forklift;
import main.java.app.agents.Automobile;
import main.java.space.Workspace;
import main.java.space.WorkspaceBuilder;
import main.java.space.WorkspaceFactory;
import main.java.space.items.Path;
import main.java.tools.fuzzytoolkit.*;
import main.java.tools.fuzzytoolkit.solutions.TippingProblem;

public class Run extends PApplet {
	
	int window_width = 1280;
	int window_heigth = 768;
	
	int maximumCameraDistance = 1000;

	PeasyCam camera;
	HUD hud;
	
	Automobile automobile;
	Forklift forklift;
	
	Workspace street;
	
	Path path = new Path(this);

	float[][] terrain;
	
	TippingProblem tippingProblem;
	
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
		
		workspaceBuilder.setWorkspace("street");
		workspaceBuilder.configure();
		
		street = workspaceBuilder.getWorkspace();
		terrain = street.terrainFactory();

		automobile = new Automobile(this, new PVector(10.0f, 10, terrain[15][15]), new PVector(1, 0, 0));
		// rover.toggleManualDriving();
		// rover.toggleFirstPersonCamera();
		automobile.setPath(path);

		forklift = new Forklift(this, new PVector(-0.0f, 0, terrain[18][18]), new PVector(0, 0, 0));
		forklift.toggleManualDriving();
		// rover.toggleFirstPersonCamera();

		camera = new PeasyCam(this, 250);
		camera.setMaximumDistance(maximumCameraDistance);
		
		hud = new HUD(this, camera, window_width, window_heigth);
		
		ArrayList<PVector> points = new ArrayList<>(Arrays.asList( 
				new PVector(-250, 0),
				new PVector(-160, -160),
				new PVector(0, -250),
				new PVector(160, -160),
				new PVector(250, 0),
				new PVector(160, 160),
				new PVector(0, 250),
				new PVector(-160, 160),
				new PVector(-250, 0)
				));
		
		path.setPoints(points);
		
//		qual.add(x_qual);
//		qual.add(qual_lo);
//		qual.add(qual_md);
//		qual.add(qual_hi);
//		
//		serv.add(x_serv);
//		serv.add(serv_lo);
//		serv.add(serv_md);
//		serv.add(serv_hi);
//		
//		hud.registerDiscreteFunction("x_qual", qual);
//		hud.registerDiscreteFunction("x_serv", serv);
//		
//		hud.updateDiscreteFunctionsInputs(crips);
		
		frameRate(30);
		fill(120, 50, 240);
		noStroke();
		
		tippingProblem = new TippingProblem(this, true, false);
		tippingProblem.setCamera(camera);
		
		tippingProblem.solutionFactory();
				
	}
	
	float f1 = 6.5f;
	float f2 = 9.8f;

	public void draw() {
		background(0);

		street.draw();
		street.drawGuideLines();
		
		street.simulate();
								
		forklift.operate();
		forklift.update();
		forklift.draw();
		
		tippingProblem.registerCrispInputs(new HashMap<String, Float>() {
			{
				put("quality", f1);
				put("service", f2);
			}
		});
		
		tippingProblem.systemUpdate();
		
		tippingProblem.evaluateCrispInputs();
		
		tippingProblem.debug();
		
		tippingProblem.draw();
		
		System.exit(0);
		
	}
}

