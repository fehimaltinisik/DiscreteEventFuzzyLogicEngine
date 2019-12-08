package main.java.space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.java.app.agents.Agent;
import main.java.app.agents.Forklift;
import main.java.app.agents.SmartDrive;
import main.java.app.agents.Automobile;
import main.java.space.items.Asset;
import main.java.space.items.Item;
import main.java.space.items.Path;
import processing.core.PApplet;
import processing.core.PVector;

public class WorkspaceBuilder {
	
	private PApplet applet;
	private WorkspaceFactory workspaceFactory;
	private Workspace workspace;
	
	int width = 600;
	int height = 600;
	
	public WorkspaceBuilder(PApplet applet) {
		this.applet = applet;
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

			Agent forklift1 = new Forklift(applet); 
			Agent forklift2 = new Forklift(applet); 
			Agent forklift3 = new Forklift(applet); 
			
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
			
			for(int i = 0; i < 20; i++) {
				Agent smartDrive = new SmartDrive(applet);
				smartDrive.spawn(new PVector(rand.nextInt(250 + 250) - 250, rand.nextInt(250 + 250) - 250, 0), new PVector(rand.nextFloat(), rand.nextFloat(), 0));
				smartDrive.registerAsset("Path", path);
				agents.add(smartDrive);
			}
			
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
