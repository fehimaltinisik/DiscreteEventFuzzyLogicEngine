package main.java.app.agents.controls;

import main.java.app.agents.controls.steering.Steering;
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
}
