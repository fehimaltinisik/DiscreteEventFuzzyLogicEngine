package main.java.space;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.java.app.agents.Agent;
import main.java.space.items.Asset;
import main.java.space.items.Item;
import processing.core.PApplet;

public class Street extends Workspace{
	
	private HashMap<String, Asset> assets = new HashMap<String, Asset>();
	private List<Agent> vehicles = new ArrayList<Agent>();

	@Override
	public void simulate() {
		for(Agent agent : vehicles) {
			agent.operate();
			agent.update();
			agent.draw();
		}
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
		
		Iterator<Entry<String, Asset>> assetsIterator = this.assets.entrySet().iterator();
		
		while (assetsIterator.hasNext()) {
	        Map.Entry<String, Asset> agent = (Map.Entry<String, Asset>)assetsIterator.next();
		    agent.getValue().draw();
		}
	}

	@Override
	public void registerAgents(List<Agent> agents) {
		vehicles.addAll(agents);
	}

	@Override
	public void registerItem(String key, Asset item) {
		assets.put(key, item);
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
	
}
