package main.java.space;

import java.util.ArrayList;
import java.util.List;

import main.java.app.agents.Agent;
import main.java.app.agents.Forklift;
import main.java.space.items.Item;
import processing.core.PApplet;

public class Warehouse extends Workspace{
	
	private List<Forklift> forklifts = new ArrayList<Forklift>();
	private List<Item> products = new ArrayList<Item>();
	
	public Warehouse(PApplet applet) {
		super(applet);
	}
	
	public Warehouse() {
	
	}
	
	@Override
	public void registerAgents(List<Agent> agents) {
		
		List<Forklift> forklifts = (List<Forklift>)(List<?>) agents;
		this.forklifts.addAll(forklifts);
		discreteEventEngine.registerElements((List<Agent>)(List<?>) this.forklifts);
	}

	@Override
	public float[][] terrainFactory() {
		
		terrain = new float[rows][cols];

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				terrain[x][y] = 0;
			}
		}
		
		return terrain;
	}

	@Override
	public float[][] assetsFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void draw() {
		applet.pushMatrix();
		applet.translate(-width / 2, -height / 2);

		applet.fill(128, 0, 128);
		applet.stroke(127);

		for (int y = 0; y < rows - 1; y++) {
			applet.beginShape(PApplet.TRIANGLE_STRIP);
			for (int x = 0; x < cols; x++) {
				applet.vertex(x * scale, y * scale, terrain[x][y]);
				applet.vertex(x * scale, (y + 1) * scale, terrain[x][y + 1]);
			}
			applet.endShape();
		}

		applet.popMatrix();
		
		for(Item item: products) {
			item.draw();
		}
	}

	@Override
	public void simulate() {
		discreteEventEngine.step();
		
	}

	@Override
	public void registerItems(List<Item> items) {
		this.products.addAll(items);
		
	}
	
}
