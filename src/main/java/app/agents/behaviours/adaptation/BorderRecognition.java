package main.java.app.agents.behaviours.adaptation;

import processing.core.PVector;

public class BorderRecognition {
	PVector target;
	PVector position;
    PVector velocity;
    
    float maxVelocity;
    float angle = 1.57079632679f;
    int deterringDistance = 75;
    
    public void setAttributes(PVector target, PVector position, PVector velocity, float maxVelocity) {
    	this.target = target;
    	this.position = position;
    	this.velocity = velocity;
    	this.maxVelocity = maxVelocity;
    }
    
	public PVector wanderAway(float sign) {
		PVector deterring = null;
		int forceMultiplier;
		
		if(position.x < -300 + deterringDistance) {
//			deterring = velocity.rotate(angle).mult(() / deterringDistance;
			forceMultiplier = Math.abs(-300 + (int) position.x);
		}else if(position.y < -300 + deterringDistance){
//			deterring = velocity.rotate(angle).mult(() / deterringDistance;
			forceMultiplier = Math.abs(-300 + (int) position.y);
		}else if(position.x > 300 - deterringDistance) {
//			deterring = velocity.rotate(angle).mult(() / deterringDistance;
			forceMultiplier = Math.abs(300 - (int) position.x);
		}else if(position.y > 300 - deterringDistance) {
//			deterring = velocity.rotate(angle).mult(() / deterringDistance;
			forceMultiplier = Math.abs(300 - (int) position.y);
		}else {
			return new PVector();
		}
		
		deterring = velocity.rotate(angle * sign).mult((forceMultiplier + deterringDistance / 2) / deterringDistance);
		
		return deterring; 
	}
}
