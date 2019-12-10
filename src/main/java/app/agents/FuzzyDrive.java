package main.java.app.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;

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
		lineFollow();
	}

	public void lineFollow() {

		Path path = (Path) assets.get("Path");

		PVector predict = velocity.copy();

		predict.normalize();
		predict.mult(25);

		PVector predictedLocation = PVector.add(position, predict);

		ArrayList<PVector> points = path.getPoints();
		
		float threshold = 1000000;
		float distance = 0;
		float theta = 0;
		
		PVector normalPoint = null;
		PVector normal = null;
		PVector direction = null;
		PVector a = null;
		PVector b;
		
		for (int i = 0; i < points.size(); i++) {
			a = points.get(i);
			b = points.get((i + 1) % points.size());

			normalPoint = Path.getNormalPoint(predictedLocation, a, b);
			PVector dir = PVector.sub(b, a);

			if (normalPoint.x < PApplet.min(a.x, b.x) || normalPoint.x > PApplet.max(a.x, b.x)
					|| normalPoint.y < PApplet.min(a.y, b.y) || normalPoint.y > PApplet.max(a.y, b.y)) {

				normalPoint = b.copy();

				a = points.get((i + 1) % points.size());
				b = points.get((i + 2) % points.size());

				dir = PVector.sub(b, a);
			}
			
			// applet.circle(normalPoint.x, normalPoint.y, 5);
			
			float dist = PVector.dist(normalPoint, predictedLocation);
			
			if (dist < threshold) {
				threshold = dist;
				normal = normalPoint;
				direction = dir.copy();
			}

		}

		distance = PVector.dist(normal, predictedLocation);
		theta = PVector.angleBetween(PVector.sub(predictedLocation, a), direction);
		
		HashMap<String, Float> crispInputs = new HashMap<String, Float>();
		
		crispInputs.put("lateralError", distance);
		crispInputs.put("angularError", theta);
		
		drivingProblem.registerCrispInputs(crispInputs);
		drivingProblem.systemUpdate();
		drivingProblem.evaluateCrispOutputs();
		
		float steering = ((DrivingProblem) drivingProblem).getCrispOutputs("steer");
		PVector steer = PVector.fromAngle(PApplet.radians(steering));
		
		// System.out.printf("Distance: %.2f, Theta: %.2f\n", distance, theta);
		// System.out.printf("Steer: %s, Streering: %.2f\n", steer.toString(), steering);
		
		applyForce(steer);
		
		// System.out.printf("Acc: %s, Vel: %s Tar: %s\n", acceleration.toString(), velocity.toString(), "");
		// System.out.printf("dir: %s, Vel: %s\n", direction.toString(), PVector.sub(predictedLocation, a).toString());
		
		steer.mult(25);
		
		applet.circle(steer.x, steer.y, 5);

		drivingProblem.debug();

	}
	
	public void setFuzzyControlSystem(DrivingProblem drivingProblem) {
		this.drivingProblem = drivingProblem;
	}
}
