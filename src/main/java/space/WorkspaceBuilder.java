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
import main.java.tools.fuzzytoolkit.solutions.BasicDriving;
import main.java.tools.fuzzytoolkit.solutions.DrivingController;
import main.java.tools.fuzzytoolkit.solutions.DrivingControllerFactory;
import main.java.tools.fuzzytoolkit.solutions.ImprovedDriving;
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
		DrivingControllerFactory drivingControllerFactory = new DrivingControllerFactory(applet);
		
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
			ArrayList<PVector> points;
			
			points = new ArrayList<>(Arrays.asList( 
					new PVector(+200, +200),
					new PVector(-200, +200),
					new PVector(-200, -200),
					new PVector(+200, -200),
					// new PVector(250, 0),
					// new PVector(160, 160),
					// new PVector(0, 250),
					// new PVector(-160, 160),
					new PVector(+200, +180)
					));
			
//			points = new ArrayList<>(Arrays.asList( 
//					new PVector(+200, +200),
//					new PVector(-200, +200),
//					new PVector(-200, -200),
//					new PVector(+200, -200),
//					// new PVector(250, 0),
//					// new PVector(160, 160),
//					// new PVector(0, 250),
//					// new PVector(-160, 160),
//					new PVector(+200, +180)
//			));
//			
			path.setPoints(points);
			path.setRadius(15);
			
			for(int i = 0; i < 5; i++) {
				Agent smartDrive = new SmartDrive(applet);
				
				smartDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250, rand.nextInt(250 + 250) - 250, 0), new PVector(0, 0, 0));
				smartDrive.registerAsset("Path", path);
				((SmartDrive) smartDrive).setScale(0.5f);
				((SmartDrive) smartDrive).setMaxForce(.20f);
				((SmartDrive) smartDrive).setMaxVelocity(4f);
				
				agents.add(smartDrive);
			}
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
//			for(int i = 0; i < 10; i++) {
//				Agent fuzzyDrive = new FuzzyDrive(applet);
//				
////				fuzzyDrive.spawn(new PVector(rand.nextInt(170 + 170) - 170, rand.nextInt(170 + 170) - 170, 0), new PVector(rand.nextFloat() * 2, rand.nextFloat() * 2, 0));
//				fuzzyDrive.spawn(new PVector(20, 190, 0), new PVector(rand.nextFloat() * -1 - 1, rand.nextFloat() * 1 + 1, 0));
//				fuzzyDrive.registerAsset("Path", path);
//				((Car) fuzzyDrive).setColor(255, 255, 255);
//				((Car) fuzzyDrive).setScale(0.5f);
//				((Car) fuzzyDrive).setMaxVelocity(4.0f);
//				
//				DrivingController drivingController = drivingControllerFactory.newDrivingController("Basic");
//				drivingController.setCamera(camera);
//				drivingController.solutionFactory();
//				
//				((FuzzyDrive) fuzzyDrive).setFuzzyControlSystem(drivingController);
//				agents.add(fuzzyDrive);
//			}
			
			Agent fuzzyDrive = new FuzzyDrive(applet);
			
			fuzzyDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250,
										// 20, 
										rand.nextInt(250 + 250) - 250,
										// 210, 
										0), 
					new PVector(-1.15f, 0.67f, 0));
			fuzzyDrive.registerAsset("Path", path);
			((Car) fuzzyDrive).setColor(0, 255, 0);
			((Car) fuzzyDrive).setScale(0.5f);
			((Car) fuzzyDrive).setMaxVelocity(2.5f);
			// ((Car) fuzzyDrive).toggleManualDriving();
			
			drivingControllerFactory.setToggleDraw(true);
			DrivingController drivingController = drivingControllerFactory.newDrivingController("Improved");
			drivingController.setCamera(camera);
			drivingController.solutionFactory();
			
			((FuzzyDrive) fuzzyDrive).setFuzzyControlSystem(drivingController);
			
			agents.add(fuzzyDrive);
			assets.add(path);
			
			workspace.registerAgents(agents);
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
