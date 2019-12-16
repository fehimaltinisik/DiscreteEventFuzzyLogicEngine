package main.java.space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.java.app.agents.Car;
import main.java.app.agents.AdvancedFuzzyDrive;
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
			
			boolean deployTestPath = false;
			boolean deployReynoldsAgent = false;
			boolean deployReynodlsAgents = false;
			boolean deployFuzzyDrivenVehicles = true;
			
			int numberOfReynodlsAgents = 20;
			int numberOfFuzzyDrivenVehicles = 20;
			
			if (deployTestPath) {
				points = new ArrayList<>(Arrays.asList( 
						new PVector(+200, +200),
						new PVector(-200, +200),
						new PVector(-200, -200),
						new PVector(+200, -200),
						new PVector(+200, +180)
						));
			}else {
				points = new ArrayList<>(Arrays.asList( 
						new PVector(+200, +200),
						new PVector(-200, +200),
						new PVector(-200, -200),
						new PVector(-135, -200),
						new PVector(-135, +40),
						new PVector(-0, +120),
						new PVector(+100, +100),
						new PVector(+100, -80),
						new PVector(-40, -200),
						new PVector(-40, -240),
						//new PVector(-160, -200),
						// new PVector(-160, -200),
						new PVector(+200, -240),
						new PVector(+200, +180)
						));
			}
			
			path.setPoints(points);
			path.setRadius(15);
			
			if(deployReynoldsAgent) {
				Agent smartDrive = new SmartDrive(applet);
				
				smartDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250, rand.nextInt(250 + 250) - 250, 0), new PVector(rand.nextFloat(), 0, 0));
				smartDrive.registerAsset("Path", path);
				((SmartDrive) smartDrive).setScale(0.5f);
				
				((Car)smartDrive).toggleManualDriving();
				((Car)smartDrive).setColor(255, 0, 255);
				
				agents.add(smartDrive);				
			}
			
			if (deployReynodlsAgents) {
				for(int i = 0; i < numberOfReynodlsAgents; i++) {
					Agent smartDrive = new SmartDrive(applet);
					
					smartDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250, rand.nextInt(250 + 250) - 250, 0), new PVector(0, 0, 0));
					smartDrive.registerAsset("Path", path);
					((SmartDrive) smartDrive).setScale(0.5f);
					((SmartDrive) smartDrive).setMaxForce(.20f);
					((SmartDrive) smartDrive).setMaxVelocity(2f);
					
					agents.add(smartDrive);
				}
			}
			
			if(deployFuzzyDrivenVehicles) {
				for(int i = 0; i < numberOfFuzzyDrivenVehicles; i++) {
					Agent fuzzyDrive = new FuzzyDrive(applet);
					
					fuzzyDrive.spawn(new PVector(rand.nextInt(150 + 150) - 150, rand.nextInt(150 + 150) - 150, 0), new PVector(rand.nextFloat() * 2, rand.nextFloat() * 2, 0));
					fuzzyDrive.registerAsset("Path", path);
					((Car) fuzzyDrive).setColor(255, 255, 255);
					((Car) fuzzyDrive).setScale(0.5f);
					((Car) fuzzyDrive).setMaxVelocity(2.0f);
					
					DrivingController drivingController = drivingControllerFactory.newDrivingController("Improved");
					drivingController.setCamera(camera);
					drivingController.solutionFactory();
					
					((FuzzyDrive) fuzzyDrive).setFuzzyControlSystem(drivingController);
					agents.add(fuzzyDrive);
				}
				
			}

		
			// Basic Drive
			
			Agent fuzzyDrive = new FuzzyDrive(applet);
						
			fuzzyDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250,
										// 20, 
										rand.nextInt(250 + 250) - 250,
										// 210, 
										0), 
					new PVector(-.45f, 0.67f, 0));
			fuzzyDrive.registerAsset("Path", path);
			((Car) fuzzyDrive).setColor(255, 255, 0);
			((Car) fuzzyDrive).setScale(0.5f);
			((Car) fuzzyDrive).setMaxVelocity(1.5f);
			((Car) fuzzyDrive).setObservable(true);
			((Car) fuzzyDrive).setName("Basic Fuzzy Vehicle");
			// ((Car) fuzzyDrive).toggleManualDriving();
			
			// drivingControllerFactory.setToggleDraw(false);
			DrivingController drivingController = drivingControllerFactory.newDrivingController("Basic");
			drivingController.setCamera(camera);
			drivingController.setObserverName(((Car)fuzzyDrive).getName());
			drivingController.solutionFactory();
			drivingController.guiStateUpdate();

			((FuzzyDrive) fuzzyDrive).setFuzzyControlSystem(drivingController);
			agents.add(fuzzyDrive);
			
			
			// Improved 
			fuzzyDrive = new FuzzyDrive(applet);
			
			fuzzyDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250,
										// 20, 
										rand.nextInt(250 + 250) - 250,
										// 210, 
										0), 
					new PVector(-.35f, 0.67f, 0));
			fuzzyDrive.registerAsset("Path", path);
			((Car) fuzzyDrive).setColor(127, 0, 127);
			((Car) fuzzyDrive).setScale(0.5f);
			((Car) fuzzyDrive).setMaxVelocity(2.5f);
			((Car) fuzzyDrive).setObservable(true);
			((Car) fuzzyDrive).setName("Improved Fuzzy Vehicle");
			// ((Car) fuzzyDrive).toggleManualDriving();
			
			// drivingControllerFactory.setToggleDraw(false);
			drivingController = drivingControllerFactory.newDrivingController("Improved");
			drivingController.setCamera(camera);
			drivingController.setObserverName(((Car)fuzzyDrive).getName());
			drivingController.solutionFactory();
			drivingController.guiStateUpdate();

			((FuzzyDrive) fuzzyDrive).setFuzzyControlSystem(drivingController);
			agents.add(fuzzyDrive);
			
			// Advanced
			fuzzyDrive = new AdvancedFuzzyDrive(applet);
			
			fuzzyDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250,
										// 20, 
										rand.nextInt(250 + 250) - 250,
										// 210, 
										0), 
					new PVector(-.75f, 0.67f, 0));
			fuzzyDrive.registerAsset("Path", path);
			((Car) fuzzyDrive).setColor(255, 165, 0);
			((Car) fuzzyDrive).setScale(0.5f);
			((Car) fuzzyDrive).setMaxVelocity(2.5f);
			((Car) fuzzyDrive).setObservable(true);
			((Car) fuzzyDrive).setName("Advanced Fuzzy Vehicle");
			//((Car) fuzzyDrive).toggleManualDriving();
			
			// drivingControllerFactory.setToggleDraw(false);
			drivingController = drivingControllerFactory.newDrivingController("Advanced");
			drivingController.setCamera(camera);
			drivingController.setObserverName(((Car)fuzzyDrive).getName());
			drivingController.solutionFactory();
			drivingController.toggleDrawing();
			drivingController.guiStateUpdate();

			((AdvancedFuzzyDrive) fuzzyDrive).setFuzzyControlSystem(drivingController);
			((AdvancedFuzzyDrive) fuzzyDrive).setNowObserved(true);
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
