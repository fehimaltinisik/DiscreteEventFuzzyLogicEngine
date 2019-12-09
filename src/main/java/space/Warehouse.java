package main.java.space;

import java.util.ArrayList;
import java.util.List;

import main.java.app.agents.Agent;
import main.java.space.items.Asset;
import main.java.space.items.Item;
import processing.core.PApplet;
import processing.core.PConstants;

public class Warehouse extends Workspace{
	
	private List<Item> items = new ArrayList<Item>();
	
	public Warehouse(PApplet applet) {
		super(applet);
	}
	
	public Warehouse() {
	
	}
	
	@Override
	public void registerAgents(List<Agent> agents) {
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
			applet.beginShape(PConstants.TRIANGLE_STRIP);
			for (int x = 0; x < cols; x++) {
				applet.vertex(x * scale, y * scale, terrain[x][y]);
				applet.vertex(x * scale, (y + 1) * scale, terrain[x][y + 1]);
			}
			applet.endShape();
		}

		applet.popMatrix();
		
		for(Item item: items) {
			item.draw();
		}
	}

	@Override
	public void simulate() {
		
	}

	@Override
	public void registerItem(String key, Asset item) {
		// this.products.addAll(items);
		
	}

	@Override
	public void radar() {
		// TODO Auto-generated method stub
		
	}
	
}
