package main.java.app.agents.controls;

import main.java.app.agents.controls.steering.Steering;
import main.java.space.items.Path;
import processing.core.PApplet;
import processing.core.PVector;

public class Reynolds implements Steering{
	
	PVector target;
	PVector position;
    PVector velocity;
    
    float maxVelocity;
    
    public void setAttributes(PVector target, PVector position, PVector velocity, float maxVelocity) {
    	this.target = target;
    	this.position = position;
    	this.velocity = velocity;
    	this.maxVelocity = maxVelocity;
    }
	
	public PVector seek() {
		
		PVector desired = PVector.sub(target, position);
	    desired.normalize();
	    desired.mult(maxVelocity);
	    
		return desired;
	}
	
	public PVector arrive() {
		PVector desired = PVector.sub(target, position);
		float distance = desired.mag();
		desired.normalize();
		
		if(distance < 30) {
			float multiplier = PApplet.map(distance, 0, 100, 0, maxVelocity);
			desired.mult(multiplier);
		}else {
			desired.mult(maxVelocity);
		}
		
		return desired;
	}
	
	public PVector pathFollow(Path path) {
		PVector predict = velocity.copy();
		
		predict.normalize();
		predict.mult(25);
		
		PVector predictedLocation = PVector.add(position, predict);
		
		PVector a = PVector.sub(predictedLocation, path.getStart());
		PVector b = PVector.sub(path.getEnd(), path.getStart());
		
		b.normalize();
		b.mult(a.dot(b));
		
		PVector normalPoint = PVector.add(path.getStart(), b);
		
		b.normalize();
		
		float distance = PVector.dist(predictedLocation, normalPoint);
		
		PVector desired;
		
		if (distance > path.getRadius()) {
			target = normalPoint;
			desired = seek();
		}else {
			desired = predict.copy();
		}
		
		// System.out.println(predictedLocation.toString());
		
		return desired;
	}
}
