package main.java.space;

import java.util.List;

import main.java.app.agents.Agent;
import main.java.space.items.Asset;
import main.java.space.items.Item;
import processing.core.PApplet;

public class Desert extends Workspace{
	
	private float terrain_roughness = .01f;
	
	public Desert(PApplet applet) {
		super(applet);
	}
	
	public Desert() {
		
	}
	
	@Override
	public void registerAgents(List<Agent> agents) {
		// TODO Auto-generated method stub
	}

	@Override
	public float[][] terrainFactory() {
		float[][] terrain = new float[rows][cols];

		float yoff = 0;
		for (int y = 0; y < rows; y++) {
			float xoff = 0;
			for (int x = 0; x < cols; x++) {
				terrain[x][y] = PApplet.map(applet.noise(xoff, yoff), 0, 1, -50, 50);
				xoff += terrain_roughness;
			}
			yoff += terrain_roughness;
		}
		
		return terrain;
	}

	@Override
	public float[][] assetsFactory() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setTerrainRoughness(float terrain_roughness) { this.terrain_roughness = terrain_roughness; }

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerItem(String key, Asset item) {
		// TODO Auto-generated method stub
		
	}

}
