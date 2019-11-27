package main.java.app.agents;

import main.java.app.agents.behaviours.adaptation.BorderRecognition;
import main.java.app.agents.controls.Reynolds;
import processing.core.PApplet;
import processing.core.PVector;

public class Rover extends Car{
	
	private float maxVelocity = 2.0f;
	private float maxForce = 0.1f;
	
	private PVector target;
	
	private Reynolds steering = new Reynolds();
	private BorderRecognition borderRecognition = new BorderRecognition();

	public Rover(PApplet applet, PVector position, PVector velocity) {
		super(applet, position, velocity);
	}
	
	public Rover(PApplet applet) {
		super(applet);
	}
	
	public void applyForce(PVector force) {
		acceleration.add(force);
	}
	
	public void update() {
		velocity.add(acceleration);
		velocity.limit(maxVelocity);
		position.add(velocity);
		acceleration.mult(0);
		
		acceleration.mult(0);
		updateHeading();
		
		if (firstPersonCameraEnabled) {
			firstPersonCamera();
		}
	}
	
	public void operate() {
		steering.setAttributes(target, position, velocity, maxVelocity);
		borderRecognition.setAttributes(target, position, velocity, maxVelocity);
		
		PVector desired = steering.arrive();
		PVector steer = PVector.sub(desired, velocity);

		steer.limit(maxForce);
		applyForce(steer);
		
	}
	
	public void setTarget(PVector target) { this.target = target; }
}
