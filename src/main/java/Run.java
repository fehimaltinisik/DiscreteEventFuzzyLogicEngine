package main.java;

import java.util.HashMap;

import main.java.app.agents.Automobile;
import main.java.app.agents.TestDrive;
import main.java.space.Workspace;
import main.java.space.WorkspaceBuilder;
import main.java.space.WorkspaceFactory;
import main.java.space.items.Path;
import main.java.tools.fuzzytoolkit.solutions.TippingProblem;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PVector;

public class Run extends PApplet {
	
	int window_width = 1280;
	int window_heigth = 768;
	
	int maximumCameraDistance = 1000;

	PeasyCam camera;
	HUD hud;
	
	Automobile automobile;
	TestDrive forklift;
	
	Workspace street;
	
	Path path = new Path(this);

	float[][] terrain;
	
	TippingProblem tippingProblem;
	
	public boolean debug = false;
	public int debugFrame = 8;
	
	public static void main(String[] args) {
		PApplet.main("main.java.Run");
	}

	@Override
	public void settings() {
		size(window_width, window_heigth, P3D);
	}

	@Override
	public void setup() {
		camera = new PeasyCam(this, 250);
		camera.setMaximumDistance(maximumCameraDistance);
				
		WorkspaceFactory workspaceFactory = new WorkspaceFactory();
		
		WorkspaceBuilder workspaceBuilder = new WorkspaceBuilder(this, camera);
		workspaceBuilder.setWorkspaceFactory(workspaceFactory);
		
		workspaceBuilder.setWorkspace("street");
		workspaceBuilder.configure();
		
		street = workspaceBuilder.getWorkspace();
		terrain = street.terrainFactory();

		forklift = new TestDrive(this, new PVector(-0.0f, 0, terrain[18][18]), new PVector(0, 0, 0));
		forklift.toggleManualDriving();
		// forklift.toggleFirstPersonCamera();
		
		frameRate(30);
		fill(120, 50, 240);
		noStroke();
		
//		tippingProblem = new TippingProblem(this, false, false);
//		tippingProblem.setCamera(camera);
//		
//		tippingProblem.solutionFactory();
//		tippingProblem.registerCrispInputs(new HashMap<String, Float>() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			{
//				put("quality", 6.5f);
//				put("service", 9.8f);
//			}
//		});
//		
//		tippingProblem.systemUpdate();
//		
//		tippingProblem.evaluateCrispOutputs();
//				
//		tippingProblem.debug();
			
	}

	@Override
	public void draw() {
		
		if (debug) {
			if(debugFrame-- == 0)
				System.exit(0);
		}
		
		background(0);

		street.draw();
		street.drawGuideLines();
		
		street.simulate();
	}
}

