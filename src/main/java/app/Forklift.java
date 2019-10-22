package main.java.app;

import processing.core.PApplet;
import processing.core.PVector;

public class Forklift extends Car implements Drawable{
	
	float payload_mass = 0;
	
	public Forklift(PApplet applet, PVector position, PVector velocity) {
		super(applet, position, velocity);
	}
	
	public void draw() {		
		applet.pushMatrix();
		applet.translate(position.x, position.y, position.z);

		applet.fill(255, 0, 0);
		applet.stroke(192);
				
		applet.beginShape(PApplet.TRIANGLE_STRIP);
						
		applet.rotateZ(heading - PApplet.HALF_PI);
		
		// BOTTOM
		applet.vertex(10, 0, 0);
		applet.vertex(0, 0, 0);
		applet.vertex(5, 20, 0);
		
		// LEFT
		applet.vertex(10, -0, 0);
		applet.vertex(10, -0, 5);
		applet.vertex(5, 20, 0);
		applet.vertex(5, 20, 5);

		// UP
		applet.vertex(10, 0, 5);
		applet.vertex(0, 0, 5);
		applet.vertex(5, 20, 5);
		
		// RIGHT
		applet.vertex(0, 0, 5);
		applet.vertex(0, 0, 0);		
		applet.vertex(5, 20, 5);
		applet.vertex(5, 20, 0);
		
		applet.endShape();

		applet.popMatrix();
		
	}
	
	public void load() {
		
	}
	
	public void unload() {
		
	}
}

