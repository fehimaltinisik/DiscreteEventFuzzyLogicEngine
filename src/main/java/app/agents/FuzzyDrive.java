package main.java.app.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.space.items.Asset;
import main.java.space.items.Path;
import processing.core.PApplet;
import processing.core.PVector;

public class FuzzyDrive extends Automobile{
	
	List<Agent> radar = new ArrayList<Agent>();
	
	private float maxVelocity = 2.0f;
	private float maxForce = 0.05f;
	
	private PVector target;
	
	private Path path;
	
	public FuzzyDrive(PApplet applet, PVector position, PVector velocity) {
		super(applet, position, velocity);
	}
	
	public FuzzyDrive(PApplet applet) {
		super(applet);
	}
	
	public void applyForce(PVector force) {
		acceleration.add(force);
	}
	
	@Override
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
	
	@Override
	public void operate() {
		pathFollow();
		separate();
	}
	
	public void updateRadar(List<Agent> vehicles) {
		radar.clear();
		radar.addAll(vehicles);
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
	public void separate () {
	    float desiredseparation = 20 * 2;
	    PVector steer = new PVector(0, 0, 0);
	    int count = 0;
		// For every boid in the system, check if it's too close
	    for (int i = 0 ; i < radar.size(); i++) {
	      FuzzyDrive other = (FuzzyDrive) radar.get(i);
	      float d = PVector.dist(position, other.position);
	      // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
	      if ((d > 0) && (d < desiredseparation)) {
	        // Calculate vector pointing away from neighbor
	        PVector diff = PVector.sub(position, other.position);
	        diff.normalize();
	        diff.div(d);        // Weight by distance
	        steer.add(diff);
	        count++;            // Keep track of how many
	      }
	    }
	    // Average -- divide by how many
	    if (count > 0) {
	      steer.div((float)count);
	    }

	    // As long as the vector is greater than 0
	    if (steer.mag() > 0) {
	      // Implement Reynolds: Steering = Desired - Velocity
	      steer.normalize();
	      steer.mult(maxVelocity);
	      steer.sub(velocity);
	      steer.limit(maxForce);
	    }

	    applyForce(steer.mult(1.5f));
	    
	  }
}
