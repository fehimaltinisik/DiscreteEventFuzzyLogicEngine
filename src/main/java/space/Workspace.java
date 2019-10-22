package main.java.space;

import processing.core.PApplet;

// TODO: implement inheritable singleton class.

public class Workspace {
	PApplet applet;
	
	protected final int scale = 20;
	protected int cols, rows, width, height;
	
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
	
	public void drawGuideLines() {
		applet.stroke(255);
		applet.line(-100, 0, 0, 100, 0, 0);
		applet.text("X", 90, 0, 0);

		applet.stroke(255);
		applet.line(0, -100, 0, 0, 100, 0);
		applet.text("Y", 0, 90, 0);

		applet.stroke(255);
		applet.line(0, 0, -100, 0, 0, 100);
		applet.text("Z", 0, 0, 90);
	}
}
