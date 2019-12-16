package main.java.app.agents;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import javax.naming.OperationNotSupportedException;

import com.sun.javafx.css.CalculatedValue;

import main.java.app.agents.options.Drivable;
import main.java.app.agents.options.Experimentable;
import main.java.app.agents.options.Interactable;
import main.java.app.agents.options.Tracable;
import main.java.utils.CVector;

public class Car extends Agent implements Drivable, Interactable, Tracable, Experimentable{
	PApplet applet;
	
	protected String name;
	
	protected PVector position;
	protected CVector velocity;	
	protected PVector acceleration;
	
	protected boolean observable = false;
	protected boolean nowObserving = false;
	protected boolean debugging = false;
	protected boolean firstPersonCameraEnabled = false;
	protected boolean manualDrivingEnabled = false;
	
	protected float maxVelocity = 1.0f;
	protected float maxForce = 0.15f;
	protected float mass = 10;
	
	protected float scale = 1;
	protected float heading = 0;
	
	protected int cRed = 255, cGreen = 0, cBlue = 0;

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
	
	@Override
	public void firstPersonCamera() {
		applet.camera(position.x + 150 * PApplet.cos(heading + PConstants.PI),
			position.y + 150 * PApplet.sin(heading + PConstants.PI), 100, position.x,
			position.y, position.z, 0, 0, (float) -1.0);
	}
	
	
	// FIXME : Access modifier is private.
	protected void updateHeading() {
		if (velocity.mag() > 0.05)
			heading = velocity.heading();
	}
	
	public void calculate() {
		thrust();
		steerLeft();
	}

	public void operate() {		
		if (manualDrivingEnabled) {
			experimental();
			manualControl();
		}else {
			calculate();
		}
	}

	@Override
	public void update() {
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
	public void manualControl() {
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
			keys[0] = false;
			keys[1] = false;
			keys[2] = false;
			keys[3] = false;
		}
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
		}else {
			velocity.sub(velocity.mult(0.01f));
		}
	}

	@Override
	public void steerLeft() {
		PVector force;
		force = PVector.fromAngle(heading - PConstants.HALF_PI / 4);
		acceleration.add(force);
	}
	
	@Override
	public void steerRight() {
		PVector force;
		force = PVector.fromAngle(heading + PConstants.HALF_PI / 4);
		acceleration.add(force);
	}
	
	
	// FIXME: Causes Method overriding.
	public void experimental() {
		experiment();
	}
	
	public void experiment() {
		System.out.printf(String.format("This is an experiment of: %s class/n", getClass().getName()));
	}
	
	public void toggleFirstPersonCamera() {
		firstPersonCameraEnabled = !firstPersonCameraEnabled;
	}
	
	public void toggleManualDriving() {
		manualDrivingEnabled = !manualDrivingEnabled;
		if(manualDrivingEnabled) {
			System.out.println(String.format("Manual Takeover: %s", name));
		}else {
			System.out.println(String.format("Autonomous Takeover: %s", name));
		}
	}
	
	public void toggleObserving() {
		nowObserving = !nowObserving;
	}
	
	public void toggleDebugging() {
		debugging = !debugging;
	}
		
	@Override
	public void draw() {
		applet.pushMatrix();
		applet.translate(position.x, position.y, position.z);

		applet.noFill();
		applet.stroke(255, 0, 0);
		
		if (manualDrivingEnabled || nowObserving) {
			applet.circle(PVector.fromAngle(heading).x * scale, PVector.fromAngle(heading).y * scale, 40 * scale);
			applet.circle(PVector.fromAngle(heading).x * scale, PVector.fromAngle(heading).y * scale, 65 * scale);
			applet.textSize(20 * scale);
			applet.text(name, 20 * scale, 20 * scale);
		}
				
		applet.fill(cRed, cGreen, cBlue);
		applet.stroke(192);
		
		applet.beginShape(PConstants.TRIANGLE_STRIP);
						
		applet.rotateZ(heading - PConstants.HALF_PI);
		
		// BOTTOM
		applet.vertex(10 * scale, 0, 0);
		applet.vertex(0, 0, 0);
		applet.vertex(5 * scale, 20 * scale, 0);
		
		// LEFT
		applet.vertex(10 * scale, -0, 0);
		applet.vertex(10 * scale, -0, 5 * scale);
		applet.vertex(5 * scale, 20 * scale, 0);
		applet.vertex(5 * scale, 20 * scale, 5 * scale);

		// UP
		applet.vertex(10 * scale, 0, 5 * scale);
		applet.vertex(0, 0, 5 * scale);
		applet.vertex(5 * scale, 20 * scale, 5 * scale);
		
		// RIGHT
		applet.vertex(0, 0, 5 * scale);
		applet.vertex(0, 0, 0);		
		applet.vertex(5 * scale, 20 * scale, 5 * scale);
		applet.vertex(5 * scale, 20 * scale, 0);
		
		applet.endShape();

		applet.popMatrix();
	}
	
	public float getHeading() { return heading; }
	
	public PVector getPosition() { return position; }
	
	public void setColor(int cRed, int cGreen, int cBlue) {
		this.cRed = cRed;
		this.cGreen = cGreen;
		this.cBlue = cBlue;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getMaxVelocity() {
		return maxVelocity;
	}

	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}
	
	public boolean getNowObserving() {
		return nowObserving;
	}
	
	public void setNowObserved(boolean nowObserving) {
		this.nowObserving = nowObserving;
	}

	public boolean getDebugging() {
		return debugging;
	}
	
	public void setDebugging(boolean debugging) {
		this.debugging = debugging;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObservable(boolean observable) {
		this.observable = observable;
	}

	public boolean getObsevable() {
		return observable;
	}
	
	public boolean isManualDrivingEnabled() {
		return manualDrivingEnabled;
	}

	public void setManualDrivingEnabled(boolean manualDrivingEnabled) {
		this.manualDrivingEnabled = manualDrivingEnabled;
	}

	public boolean isFirstPersonCameraEnabled() {
		return firstPersonCameraEnabled;
	}

	public void setFirstPersonCameraEnabled(boolean firstPersonCameraEnabled) {
		this.firstPersonCameraEnabled = firstPersonCameraEnabled;
	}

	
	
}
