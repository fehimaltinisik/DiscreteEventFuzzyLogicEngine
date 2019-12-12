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
		lineFollow();
		drivingProblem.draw();
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
		PVector dir = null;
		PVector direction = null;
		PVector a = null;
		PVector b = null;
		
		for (int i = 0; i < points.size(); i++) {
			a = points.get(i);
			b = points.get((i + 1) % points.size());

			normalPoint = Path.getNormalPoint(predictedLocation, a, b);
			dir = PVector.sub(b, a);

			if (normalPoint.x < PApplet.min(a.x, b.x) || normalPoint.x > PApplet.max(a.x, b.x)
					|| normalPoint.y < PApplet.min(a.y, b.y) || normalPoint.y > PApplet.max(a.y, b.y)) {

				normalPoint = b.copy();

				a = points.get((i + 1) % points.size());
				b = points.get((i + 2) % points.size());

				dir = PVector.sub(b, a);
			}
						
			float dist = PVector.dist(normalPoint, predictedLocation);
			
			if (dist < threshold) {
				threshold = dist;
				normal = normalPoint;
				direction = dir.copy();
			}
		}
		
		if (normal == null) {
			normal = normalPoint;
		}
		
		if (direction == null) {
			direction = dir;
		}
		
		float d = (position.x - normal.x) * (b.y - normal.y) - (position.y - normal.y) * (b.x - normal.x);
		
		System.out.println(String.format("%.2f, %.2f, %.2f, %.2f", (position.x - a.x), (b.y - a.y), (position.y - a.y), (b.x - a.x)));
		
		distance = PVector.dist(normal, predictedLocation);
		theta = PVector.angleBetween(PVector.sub(predictedLocation, position), direction);
		
		theta = theta * ((d >= 0) ? 1 : -1);
		distance = distance * ((d >= 0) ? 1 : -1);

		HashMap<String, Float> crispInputs = new HashMap<String, Float>();
		
		crispInputs.put("lateralError", distance);
		crispInputs.put("angularError", theta);
		
		drivingProblem.registerCrispInputs(crispInputs);
		drivingProblem.systemUpdate();
		drivingProblem.evaluateCrispOutputs();
		
		float steering = ((DrivingProblem) drivingProblem).getCrispOutputs("steer");
		
		steer(steering * 1.0f);
		
		System.out.printf("Distance: %.2f, Theta: %.2f\n", distance, theta);
		System.out.printf("Acc: %s, Vel: %s Tar: %s\n", acceleration.toString(), velocity.toString(), "");
		
		// System.out.printf("dir: %s, Vel: %s\n", direction.toString(), PVector.sub(predictedLocation, a).toString());
				
		applet.circle(normal.x, normal.y, 5);

		drivingProblem.debug();

	}
	
	public void steer(float rotate) {
		velocity.rotate(rotate);
	}
	
	public void setFuzzyControlSystem(DrivingProblem drivingProblem) {
		this.drivingProblem = drivingProblem;
	}
}
