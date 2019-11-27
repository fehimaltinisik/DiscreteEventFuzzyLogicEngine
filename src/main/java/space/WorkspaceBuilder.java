package main.java.space;

import java.util.ArrayList;
import java.util.List;

import main.java.app.agents.Agent;
import main.java.app.agents.Forklift;
import main.java.engine.DiscreteEventEngine;
import main.java.engine.fuzzytoolkit.FuzzyInferenceEngine;
import main.java.space.items.Item;
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
		workspace.setDiscreteEventEngine(new DiscreteEventEngine());
		
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
			workspace.registerItems(products);
		}
		
	}
	
	public void setWorkspace(String workspaceSelection) {
		workspace = workspaceFactory.getWorkspace(workspaceSelection);
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}

}
