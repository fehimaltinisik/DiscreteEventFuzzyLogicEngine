package main.java.space;

import java.util.List;

import main.java.app.agents.Agent;
import main.java.engine.fuzzytoolkit.FuzzyInferenceEngine;
import main.java.space.items.Asset;
import main.java.space.items.Item;
import processing.core.PApplet;

// TODO: implement inheritable singleton class.

public abstract class Workspace implements Environment{
	PApplet applet;
	
	protected final int scale = 20;
	protected int cols, rows, width, height;
	
	protected float[][] terrain;
				
	public Workspace(PApplet applet) {
		this.applet = applet;
	}
	
	public Workspace() {
		
	}

	public abstract void registerAgents(List<Agent> agents);
	public abstract void registerItem(String key, Asset item);
	
	public abstract float [][] terrainFactory();
	public abstract float [][] assetsFactory();
	
	public void drawGuideLines() {
		applet.stroke(255);
		applet.line(-100, 0, 0, 100, 0, 0);
		applet.text("X", 90, 0, 0);

		// applet.stroke(255);
		applet.line(0, -100, 0, 0, 100, 0);
		applet.text("Y", 0, 90, 0);

		// applet.stroke(255);
		applet.line(0, 0, -100, 0, 0, 100);
		applet.text("Z", 0, 0, 90);
	}
	

	public void setTerrainResolution(int width, int heigth) {
		this.width = width;
		this.height = heigth;
		cols = width / scale;
		rows = height / scale;
	}

	public void setPApplet(PApplet applet) { this.applet = applet; }
}
