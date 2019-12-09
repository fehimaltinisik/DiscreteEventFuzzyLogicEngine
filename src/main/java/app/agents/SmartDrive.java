package main.java.app.agents;

import java.util.ArrayList;
import java.util.List;

import main.java.space.items.Path;
import processing.core.PApplet;
import processing.core.PVector;

public class SmartDrive extends Automobile {

	public SmartDrive(PApplet applet, PVector position, PVector velocity) {
		super(applet, position, velocity);
	}

	public SmartDrive(PApplet applet) {
		super(applet);
	}

	@Override
	public void calculate() {
		pathFollow();
		separate();
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

			if (normalPoint.x < PApplet.min(a.x, b.x) || normalPoint.x > PApplet.max(a.x, b.x)
					|| normalPoint.y < PApplet.min(a.y, b.y) || normalPoint.y > PApplet.max(a.y, b.y)) {

				normalPoint = b.copy();

				a = points.get((i + 1) % points.size());
				b = points.get((i + 2) % points.size()); // Path wraps around

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

	public void separate() {
		float desiredseparation = 20;
		PVector steer = new PVector(0, 0, 0);
		int count = 0;

		for (int i = 0; i < surroundings.size(); i++) {
			Automobile other = (Automobile) surroundings.get(i);
			float d = PVector.dist(position, other.position);

			if ((d > 0) && (d < desiredseparation)) {

				PVector diff = PVector.sub(position, other.position);
				diff.normalize();
				diff.div(d);
				steer.add(diff);
				count++;
			}
		}

		if (count > 0) {
			steer.div((float) count);
		}

		if (steer.mag() > 0) {

			steer.normalize();
			steer.mult(maxVelocity);
			steer.sub(velocity);
			steer.limit(maxForce);
		}

		applyForce(steer.mult(1.5f));

	}
}
