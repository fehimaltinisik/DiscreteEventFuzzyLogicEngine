package main.java.space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.java.app.agents.Car;
import main.java.app.agents.Agent;
import main.java.app.agents.TestDrive;
import main.java.app.agents.FuzzyDrive;
import main.java.app.agents.SmartDrive;
import main.java.space.items.Asset;
import main.java.space.items.Item;
import main.java.space.items.Path;
import main.java.tools.fuzzytoolkit.solutions.DrivingProblem;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PVector;

public class WorkspaceBuilder {
	
	private PApplet applet;
	private PeasyCam camera;
	private WorkspaceFactory workspaceFactory;
	private Workspace workspace;
	
	int width = 600;
	int height = 600;
	
	public WorkspaceBuilder(PApplet applet, PeasyCam camera) {
		this.applet = applet;
		this.camera = camera;
	}
	
	public void setWorkspaceFactory(WorkspaceFactory workspaceFactory) {
		this.workspaceFactory = workspaceFactory;
	}
	
	public void configure() {
		workspace.setPApplet(applet);
		workspace.setTerrainResolution(width, height);
		
		if (workspace instanceof Warehouse) {
			
			List<Agent> agents = new ArrayList<Agent>();
			List<Item> products = new ArrayList<Item>();

			Agent forklift1 = new TestDrive(applet); 
			Agent forklift2 = new TestDrive(applet); 
			Agent forklift3 = new TestDrive(applet); 
			
			Item item1 = new Item(applet, new PVector(-150, 150, 0), 5); 
			Item item2 = new Item(applet, new PVector(-50, 50, 0), 3); 
			Item item3 = new Item(applet, new PVector(50, -50, 0), 3);
			
			forklift1.spawn(new PVector(-0.0f, 0, 0), new PVector(0, 0, 0));
			forklift2.spawn(new PVector(-150.0f, -150, 0), new PVector(0, 0, 0));
			forklift3.spawn(new PVector(150.0f, 150, 0), new PVector(0, 0, 0));
			
			agents.add(forklift1);
			agents.add(forklift2);
			agents.add(forklift3);
			
			products.add(item1);
			products.add(item2);
			products.add(item3);
			
			workspace.registerAgents(agents);
			// workspace.registerItems(products);
			
		}else if(workspace instanceof Street) {
			Random rand = new Random();
			
			List<Agent> agents = new ArrayList<Agent>();
			List<Asset> assets = new ArrayList<Asset>();
			
			Path path = new Path(applet);
			
			ArrayList<PVector> points = new ArrayList<>(Arrays.asList( 
					new PVector(+200, +200),
					new PVector(-200, +200),
					new PVector(-200, -200),
					new PVector(+200, -200),
					// new PVector(250, 0),
					// new PVector(160, 160),
					// new PVector(0, 250),
					// new PVector(-160, 160),
					new PVector(+200, +200)
					));
			
			path.setPoints(points);
			path.setRadius(15);
			
//			for(int i = 0; i < 20; i++) {
//				Agent smartDrive = new SmartDrive(applet);
//				
//				smartDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250, rand.nextInt(250 + 250) - 250, 0), new PVector(rand.nextFloat(), 0, 0));
//				smartDrive.registerAsset("Path", path);
//				((SmartDrive) smartDrive).setScale(0.5f);
//				((SmartDrive) smartDrive).setMaxForce(.10f);
//				
//				agents.add(smartDrive);
//			}
//			
//			Agent smartDrive = new SmartDrive(applet);
//			
//			smartDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250, rand.nextInt(250 + 250) - 250, 0), new PVector(rand.nextFloat(), 0, 0));
//			smartDrive.registerAsset("Path", path);
//			((SmartDrive) smartDrive).setScale(0.5f);
//			
//			((Car)smartDrive).toggleManualDriving();
//			((Car)smartDrive).setColor(255, 0, 255);
//			
//			agents.add(smartDrive);
//			
//			for(int i = 0; i < 5; i++) {
//				Agent fuzzyDrive = new FuzzyDrive(applet);
//				
//				fuzzyDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250, rand.nextInt(250 + 250) - 250, 0), new PVector(rand.nextFloat(), rand.nextFloat(), 0));
//				fuzzyDrive.registerAsset("Path", path);
//				((Car) fuzzyDrive).setColor(0, 0, 255);
//				
//				DrivingProblem drivingProblem = new DrivingProblem(applet, false, false);
//				drivingProblem.setCamera(camera);
//				drivingProblem.solutionFactory();
//				
//				((FuzzyDrive) fuzzyDrive).setFuzzyControlSystem(drivingProblem);
//				agents.add(fuzzyDrive);
//			}
//			
			Agent fuzzyDrive = new FuzzyDrive(applet);
			
			fuzzyDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250, rand.nextInt(250 + 250) - 250, 0), new PVector(rand.nextFloat(), rand.nextFloat(), 0));
			fuzzyDrive.registerAsset("Path", path);
			((Car) fuzzyDrive).setColor(0, 255, 0);
			
			DrivingProblem drivingProblem = new DrivingProblem(applet, true, false);
			drivingProblem.setCamera(camera);
			drivingProblem.solutionFactory();
			
			((FuzzyDrive) fuzzyDrive).setFuzzyControlSystem(drivingProblem);
			agents.add(fuzzyDrive);
			
			workspace.registerAgents(agents);
			
			assets.add(path);
			workspace.registerItem("Path", path);
		}
	}
	
	public void setWorkspace(String workspaceSelection) {
		workspace = workspaceFactory.getWorkspace(workspaceSelection);
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}

}
