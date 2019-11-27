package main.java.space.items;

import main.java.Drawable;
import processing.core.PApplet;
import processing.core.PVector;

public class Item implements Drawable{
	
	private PApplet applet;
	private PVector position;
	private int mass;
	
	public Item(PApplet applet, PVector position, int mass){
		this.applet = applet;
		this.position = position;
		this.mass = mass;
	}

	@Override
	public void draw() {
		applet.pushMatrix();
		applet.translate(position.x, position.y, position.z);

		applet.fill(255, 255, 255);
		applet.stroke(192);
				
		applet.sphere(mass);

		applet.popMatrix();
	}
	
	public int getMass() { return mass; }
	public PVector getPosition() { return position; }
}
