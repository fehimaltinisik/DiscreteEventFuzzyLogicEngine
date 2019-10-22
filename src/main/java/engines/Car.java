package main.java.engines;

import processing.core.PApplet;
import processing.core.PVector;
import main.java.utils.CVector;

public class Car extends Agent implements Drivable, Interactable{
	PApplet applet;
	
	protected PVector position;
	protected CVector velocity;	
	protected PVector acceleration;
	
	private float max_velocity = 2.0f;
	private float mass = 10;
	
	protected float heading = 0;

	private boolean[] keys = new boolean[4];
	
	public Car(PApplet applet, PVector position, PVector velocity) {
		this.applet = applet;
		this.position = position;
		this.velocity = new CVector(velocity.x, velocity.y, velocity.z);
		this.acceleration = new PVector(0, 0, 0);
		
		heading = velocity.heading() - PApplet.HALF_PI;
	}
	
	private void updateHeading() {
		if (velocity.mag() > 0.05)
			heading = velocity.heading();
	}
	
	public void thrust() {
		PVector force;
		force = CVector.fromAngle(heading);
		acceleration.add(force);
	}
	
	public void breaks() {
		if (velocity.mag() < 0.05) {
			velocity.sub(velocity);
			return;
		}
		PVector force;
		force = CVector.fromAngle(heading + PApplet.PI);
		acceleration.add(force);
	}

	public void steerLeft() {
		PVector force;
		force = PVector.fromAngle(heading - PApplet.HALF_PI);
		acceleration.add(force);
	}
	
	public void steerRight() {
		PVector force;
		force = PVector.fromAngle(heading + PApplet.HALF_PI);
		acceleration.add(force);
	}
	
	public void update() {
		
		acceleration.div(mass);
		velocity.add(acceleration);
		velocity.limit(max_velocity);
		position.add(velocity);
		
		acceleration.mult(0);
		updateHeading();
	}
	
	public void operate() {
		registerKeys();
		
		if (keys[0]) 
			thrust();
			
		if (keys[1]) 
			steerLeft();
		
		if (keys[3])
			steerRight();
		
		if (keys[2]) 
			breaks();
	}

	public void registerKeys() {

		if (applet.keyPressed) {
			if (applet.key == 'w') {
				keys[0] = true;
			} else if (applet.key == 'a') {
				keys[1] = true;
			} else if (applet.key == 's') {
				keys[2] = true;
			} else if (applet.key == 'd') {
				keys[3] = true;
			}
		} else {
			// TODO : Enhance
			keys[0] = false;
			keys[1] = false;
			keys[2] = false;
			keys[3] = false;
		}
	}
	
	public float getHeading() { return heading; }
	public PVector getPosition() { return position; }
}
