package main.java.app.agents;

import processing.core.PApplet;
import processing.core.PVector;

public class Forklift extends Car{
	
	float payload_mass = 0;
	
	public Forklift(PApplet applet, PVector position, PVector velocity) {
		super(applet, position, velocity);
	}
	
	public Forklift(PApplet applet) {
		super(applet);
	}

	public void load() {
		
	}
	
	public void unload() {
		
	}
}

