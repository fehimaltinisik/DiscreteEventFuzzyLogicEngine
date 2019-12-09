package main.java.app.agents;

import processing.core.PApplet;
import processing.core.PVector;

public class TestDrive extends Car{
	
	float payload_mass = 0;
	
	public TestDrive(PApplet applet, PVector position, PVector velocity) {
		super(applet, position, velocity);
	}
	
	public TestDrive(PApplet applet) {
		super(applet);
	}
}

