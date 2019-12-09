package main.java.app.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.space.items.Path;
import main.java.tools.fuzzytoolkit.FuzzyControlSystem;
import main.java.tools.fuzzytoolkit.solutions.DrivingProblem;
import processing.core.PApplet;
import processing.core.PVector;

public class FuzzyDrive extends Automobile {
	protected FuzzyControlSystem drivingProblem;

	public FuzzyDrive(PApplet applet, PVector position, PVector velocity) {
		super(applet, position, velocity);
	}

	public FuzzyDrive(PApplet applet) {
		super(applet);
	}

	@Override
	public void calculate() {
		drivingProblem.draw();
		pathFollow();
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
		
		float distance = 0;
		float theta = 0;
		
		for (int i = 0; i < points.size(); i++) {
			PVector a = points.get(i);
			PVector b = points.get((i + 1) % points.size());

			PVector normalPoint = Path.getNormalPoint(predictedLocation, a, b);

			PVector dir = PVector.sub(b, a);

			if (normalPoint.x < PApplet.min(a.x, b.x) || normalPoint.x > PApplet.max(a.x, b.x)
					|| normalPoint.y < PApplet.min(a.y, b.y) || normalPoint.y > PApplet.max(a.y, b.y)) {

				// normalPoint = b.copy();

				a = points.get((i + 1) % points.size());
				b = points.get((i + 2) % points.size());

				dir = PVector.sub(b, a);
				
				distance = PVector.dist(normalPoint, predictedLocation);
				theta = PVector.angleBetween(a, predictedLocation);
			}
		}

//		if (threshold > path.getRadius()) {
//			seek();
//		}
		
		System.out.printf("Distance: %.2f, Theta: %.2f\n", distance, theta);
		
		HashMap<String, Float> crispInputs = new HashMap<String, Float>();
		
		crispInputs.put("lateralError", -3.2f);
		crispInputs.put("angularError", 1.17f);
		
		drivingProblem.registerCrispInputs(crispInputs);
		drivingProblem.systemUpdate();
		drivingProblem.evaluateCrispInputs();
		
		float steering = ((DrivingProblem) drivingProblem).getCrispOutputs("steer");
		PVector steer = PVector.fromAngle(steering);
		
		applyForce(steer);

	}
	
	public void setFuzzyControlSystem(DrivingProblem drivingProblem) {
		this.drivingProblem = drivingProblem;
	}
}
