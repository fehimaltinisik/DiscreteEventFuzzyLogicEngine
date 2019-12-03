package main.java.app.agents;

import java.util.ArrayList;

import main.java.space.items.Path;
import processing.core.PApplet;
import processing.core.PVector;

public class Rover extends Car{
	
	private float maxVelocity = 2.0f;
	private float maxForce = 0.05f;
	
	private PVector target;
	
	private Path path;
	
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
		pathFollow();
	}
	
	public void seek() {
		
		PVector desired = PVector.sub(target, position);
	    desired.normalize();
	    desired.mult(maxVelocity);
	    
	    PVector steer = PVector.sub(desired,velocity);
	    steer.limit(maxForce);
	    applyForce(steer);
	    
	}
	
	public void arrive() {
		PVector desired = PVector.sub(target, position);
		float distance = desired.mag();
		desired.normalize();
		
		if(distance < 30) {
			float multiplier = PApplet.map(distance, 0, 100, 0, maxVelocity);
			desired.mult(multiplier);
		}else {
			desired.mult(maxVelocity);
		}
		
		PVector steer = PVector.sub(desired,velocity);
	    steer.limit(maxForce);
	    applyForce(steer);
	    
	}
	
	public void pathFollow() {
		
		Path path = (Path) assets.get("Path");
		
		float threshold = 1000000;

		PVector normal = null;
		PVector predict = velocity.copy();
		
		predict.normalize();
		predict.mult(25);
		
		PVector predictedLocation = PVector.add(position, predict);
		
		ArrayList<PVector> points = path.getPoints();
		
		for (int i = 0; i < points.size(); i++) {
			PVector a = points.get(i);
			PVector b = points.get((i + 1) % points.size());
			
			PVector normalPoint = Path.getNormalPoint(predictedLocation, a, b);
			
			PVector dir = PVector.sub(b, a);
			
			if (normalPoint.x < PApplet.min(a.x, b.x)
					|| normalPoint.x > PApplet.max(a.x, b.x) 
						|| normalPoint.y < PApplet.min(a.y, b.y) 
							|| normalPoint.y > PApplet.max(a.y, b.y)) {
			    
				normalPoint = b.copy();
				
				a = points.get((i + 1) % points.size());
			    b = points.get((i + 2) % points.size());  // Path wraps around
			     
			    dir = PVector.sub(b, a);			
			
			}
			
			float distance = PVector.dist(normalPoint, predictedLocation);
			
			if (distance < threshold) {
				threshold = distance;

				normal = normalPoint;
				
				dir.normalize();
		        dir.mult(10);
		        
		        target = normal.copy();
		        target.add(dir);
			}
		}
		
		if (threshold > path.getRadius()) {
	      seek();
		}
	}
	
	public void setPath(Path path) { this.path = path; }
	
	public void setTarget(PVector target) { this.target = target; }
}
