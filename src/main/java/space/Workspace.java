package main.java.space;

import processing.core.PApplet;

public class Workspace {
	PApplet applet;
	
	protected final int scale = 20;
	protected int cols, rows;
	protected int width, height;
	
	protected float[][] terrain;
			
	public Workspace(PApplet applet) {
		this.applet = applet;
	}
	
	public void setTerrainResolution(int width, int heigth) {
		this.width = width;
		this.height = heigth;
		
		cols = width / scale;
		rows = height / scale;
	}
}
