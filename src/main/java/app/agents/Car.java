package main.java.app.agents;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import main.java.app.agents.options.Drivable;
import main.java.app.agents.options.Interactable;
import main.java.app.agents.options.Tracable;
import main.java.utils.CVector;

public class Car extends Agent implements Drivable, Interactable, Tracable{
	PApplet applet;
	
	protected PVector position;
	protected CVector velocity;	
	protected PVector acceleration;
	
	protected boolean firstPersonCameraEnabled = false;
	protected boolean manualDrivingEnabled = false;
	
	private float maxVelocity = 3.0f;
	private float mass = 10;
	
	protected float heading = 0;

	private boolean[] keys = new boolean[4];
	
	public Car(PApplet applet, PVector position, PVector velocity) {
		this.applet = applet;
		this.position = position;
		this.velocity = new CVector(velocity.x, velocity.y, velocity.z);
		this.acceleration = new PVector(0, 0, 0);
		
		heading = velocity.heading() - PConstants.HALF_PI;
	}
	
	public Car(PApplet applet) {
		this.applet = applet;
		
	}
	
	@Override
	public void spawn(PVector position, PVector velocity) {
		this.position = position;
		this.velocity = new CVector(velocity.x, velocity.y, velocity.z);
		this.acceleration = new PVector(0, 0, 0);
		heading = velocity.heading() - PConstants.HALF_PI;
	}
	
	// FIXME : Access modifier is private.
	protected void updateHeading() {
		if (velocity.mag() > 0.05)
			heading = velocity.heading();
	}
	
	@Override
	public void thrust() {
		PVector force;
		force = PVector.fromAngle(heading);
		acceleration.add(force);
	}
	
	@Override
	public void breaks() {
		if (velocity.mag() < 0.05) {
			velocity.sub(velocity);
			return;
		}
		PVector force;
		force = PVector.fromAngle(heading + PConstants.PI);
		acceleration.add(force);
	}

	@Override
	public void steerLeft() {
		PVector force;
		force = PVector.fromAngle(heading - PConstants.HALF_PI);
		acceleration.add(force);
	}
	
	@Override
	public void steerRight() {
		PVector force;
		force = PVector.fromAngle(heading + PConstants.HALF_PI);
		acceleration.add(force);
	}
	
	@Override
	public void update() {
		
		acceleration.div(mass);
		velocity.add(acceleration);
		velocity.limit(maxVelocity);
		position.add(velocity);
		
		acceleration.mult(0);
		updateHeading();
		
		if (firstPersonCameraEnabled) {
			firstPersonCamera();
		}
	}
	
	@Override
	public void draw() {		
		applet.pushMatrix();
		applet.translate(position.x, position.y, position.z);

		applet.fill(255, 0, 0);
		applet.stroke(192);
				
		applet.beginShape(PConstants.TRIANGLE_STRIP);
						
		applet.rotateZ(heading - PConstants.HALF_PI);
		
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
	
	@Override
	public void operate() {
		
		if (manualDrivingEnabled) {
			registerKeys();
			
			if (keys[0]) 
				thrust();
				
			if (keys[1]) 
				steerLeft();
			
			if (keys[3])
				steerRight();
			
			if (keys[2]) 
				breaks();
		}else {
			thrust();
			steerLeft();
		}
		
	}

	@Override
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
	
	public void toggleFirstPersonCamera() {
		firstPersonCameraEnabled = !firstPersonCameraEnabled;
	}
	
	public void toggleManualDriving() {
		manualDrivingEnabled = !manualDrivingEnabled;
	}
	
	@Override
	public void firstPersonCamera() {
		applet.camera(position.x + 150 * PApplet.cos(heading + PConstants.PI),
			position.y + 150 * PApplet.sin(heading + PConstants.PI), 100, position.x,
			position.y, position.z, 0, 0, (float) -1.0);
	}
	
	public float getHeading() { return heading; }
	public PVector getPosition() { return position; }
}
