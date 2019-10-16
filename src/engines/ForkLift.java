package engines;

import engines.Drivable;
import processing.core.PApplet;
import processing.core.PVector;
import utils.CVector;

public class ForkLift extends Agent implements Drivable {
	PApplet applet;
	
	private float max_velocity = 2.0f;
	
	private float mass = 10;
	public float heading = 0;
	
	public PVector position;
	public CVector velocity;	
	
	PVector acceleration;

	public boolean[] keys = new boolean[4];

	public ForkLift(PApplet applet, PVector position, PVector velocity) {
		this.applet = applet;
		this.position = position;
		this.velocity = new CVector(velocity.x, velocity.y, velocity.z);
		this.acceleration = new PVector(0, 0, 0);
		
		heading = velocity.heading() - PApplet.HALF_PI;
	}
	
	void updateHeading() {
		if (velocity.mag() > 0.05)
			heading = velocity.heading();
	}
	
	@Override
	public void draw() {
		updateHeading();
		
		applet.pushMatrix();
		applet.translate(position.x, position.y, position.z);

		applet.fill(255, 0, 0);
		applet.stroke(192);
				
		applet.beginShape(PApplet.TRIANGLE_STRIP);
		
		// applet.rotateZ(velocity.heading() - PApplet.HALF_PI);
				
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
	
	@Override
	public void update() {
				
		acceleration.div(mass);
		velocity.add(acceleration);
		velocity.limit(max_velocity);
		position.add(velocity);
		
		acceleration.mult(0);
		
	}

	public void thrust() {
		PVector force = new PVector();
		force = CVector.fromAngle(heading);
		acceleration.add(force);
	}
	
	public void breaks() {
		if (velocity.mag() < 0.05) {
			velocity.sub(velocity);
			return;
		}

		PVector force = new PVector();
		force = CVector.fromAngle(heading + PApplet.PI);
		acceleration.add(force);
	}

	public void steer() {
		PVector force = new PVector();

		if (keys[1]) {
			force = PVector.fromAngle(heading - PApplet.HALF_PI);
		}
		if (keys[3]) {
			force = PVector.fromAngle(heading + PApplet.HALF_PI);
		}		
		acceleration.add(force);
	}
	
	@Override
	public void operate() {
		registerKeys();
		
		if (keys[0]) 
			thrust();
			
		if (keys[1]  || keys[3]) 
			steer();
		
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

	@Override
	public void infere() {
		// TODO Auto-generated method stub
		
	}
}

