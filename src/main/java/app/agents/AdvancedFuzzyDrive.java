package main.java.app.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.text.AbstractDocument.LeafElement;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;

import main.java.space.items.Path;
import main.java.tools.fuzzytoolkit.FuzzyControlSystem;
import main.java.tools.fuzzytoolkit.solutions.DrivingController;
import main.java.tools.fuzzytoolkit.solutions.ImprovedDriving;
import processing.core.PApplet;
import processing.core.PVector;

public class AdvancedFuzzyDrive extends Automobile {
	protected DrivingController drivingController;

	public AdvancedFuzzyDrive(PApplet applet, PVector position, PVector velocity) {
		super(applet, position, velocity);
	}

	public AdvancedFuzzyDrive(PApplet applet) {
		super(applet);
	}

	@Override
	public void calculate() {
		lineFollow();
	}
	
	public HashMap<String, Float> calculateSteeringInputs() {
		Path path = (Path) assets.get("Path");

		PVector predict = velocity.copy();

		predict.normalize();
		predict.mult(25);

		// PVector predictedLocation = position.copy();
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
		PVector globalA = null;
		PVector globalB = null;
		
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
				globalA = a.copy();
				globalB = b.copy();
			}	
		}
		
		if (normal == null) {
			normal = normalPoint;
		}
		
		if (direction == null) {
			direction = dir;
		}
		
		if (globalA == null || globalB == null) {
			globalA = a.copy();
			globalB = b.copy();
		}
		
		distance = PVector.dist(normal, predictedLocation);
		theta = PVector.angleBetween(PVector.sub(predictedLocation, position).normalize(), direction.normalize());
		
		PVector vehicleOrientation = PVector.sub(predictedLocation, position);
		// PVector vehicleOrientation = PVector.sub(position, predictedLocation);
		
		float u = (position.x - globalA.x);
		float v = (globalB.y - globalA.y);
		float w = (position.y - globalA.y);
		float x = (globalB.x - globalA.x);
		
		float p = vehicleOrientation.x * direction.y;
		float r = vehicleOrientation.y * direction.x;
		float s = vehicleOrientation.dot(direction);
		
		
		float lateralErrorOrientation = Math.signum(u * v - w * x);
		float angularErrorOrientation = Math.signum(s) * Math.signum(p - r);
		
//		System.out.println(String.format("%.2f, %.2f, %.2f, %.2f", angularErrorOrientation, p, r, s));
//		System.out.println(String.format("%.2f, %.2f, %.2f, %.2f, %.2f", Math.signum(lateralErrorOrientation), u, v, w, x));
//		System.out.println(String.format("%s, %s, %s", globalA.toString(), globalB.toString(), position.toString()));
//		System.out.println(String.format("%.2f, %.2f", distance, theta));
		
//		applet.circle(normal.x, normal.y, 5);
//		applet.circle(globalA.x, globalA.y, 5);
//		applet.circle(globalB.x, globalB.y, 5);
//		applet.text("a", globalA.x, globalA.y);
//		applet.text("b", globalB.x, globalB.y);

		distance = distance * lateralErrorOrientation * -1;
		// theta = theta * angularErrorOrientation * -1;
		if (0 < theta && theta < Math.PI / 2) {
			theta = theta * angularErrorOrientation * -1;
		}else {
			theta = theta * angularErrorOrientation;
		}

		HashMap<String, Float> crispInputs = new HashMap<String, Float>();
		
		crispInputs.put("lateralError", distance);
		crispInputs.put("angularError", theta);
	
		// System.out.printf("Distance: %.2f, Theta: %.2f\n", distance, theta);
		// System.out.printf("Acc: %s, Vel: %s Tar: %s\n", acceleration.toString(), velocity.toString(), "");
		// System.out.printf("dir: %s, Vel: %s\n", direction.toString(), PVector.sub(predictedLocation, a).toString());
				
		return crispInputs;
	}

	public HashMap<String, Float> separationSteeringInputs() {
		float angle = 0;
		float distance = 0;
		float distanceTemp = 1000000;
		float desiredseparation = 30;
		
		for (int i = 0; i < surroundings.size(); i++) {
			Automobile other = (Automobile) surroundings.get(i);
			float d = PVector.dist(position, other.getPosition());

			if ((d > 0) && (d < desiredseparation)) {

				if (d < distanceTemp) {
					distance = d;
					distanceTemp = d;
					
					PVector orient1 = PVector.fromAngle(heading);
					PVector orient2 = PVector.fromAngle(other.getHeading());
					
					angle = PVector.angleBetween(orient1, orient2);
				
					float angularErrorOrientation = Math.signum(orient1.dot(orient2)) * Math.signum(orient1.x * orient2.y - orient2.x * orient1.y);

					if (0 < angle && angle < Math.PI / 2) {
						angle = angle * angularErrorOrientation * -1;
					}else {
						angle = angle * angularErrorOrientation;
					}
					
				}
			}
		}

		
		HashMap<String, Float> crispInputs = new HashMap<String, Float>();
		
		crispInputs.put("sptAngle", angle);
		crispInputs.put("sptDistance", distance);

		return crispInputs;
	}
	public void lineFollow() {
		
		HashMap<String, Float> steeringCrispInputs = calculateSteeringInputs();
		HashMap<String, Float> separationCrispInputs = separationSteeringInputs();
		
		steeringCrispInputs.putAll(separationCrispInputs);
		
		drivingController.registerCrispInputs(steeringCrispInputs);
		drivingController.systemUpdate();
		drivingController.evaluateCrispOutputs();
		
		// drivingController.debug();
		if(nowObserving) {
			drivingController.draw();	
		}

		float steeringAngle = drivingController.getCrispOutputs("steer");
		
		steer(steeringAngle);
	}
	
	public void experiment() {
		HashMap<String, Float> steeringCrispInputs = calculateSteeringInputs();
		HashMap<String, Float> separationCrispInputs = separationSteeringInputs();
		
		steeringCrispInputs.putAll(separationCrispInputs);
		
		drivingController.registerCrispInputs(steeringCrispInputs);
		drivingController.systemUpdate();
		drivingController.evaluateCrispOutputs();
		
		// drivingController.debug();
		if(nowObserving) {
			drivingController.draw();	
		}		
	}
	
	public void steer(float rotate) {
		velocity.rotate(rotate);
	}
	
	public void toggleObserving() {
		nowObserving = !nowObserving;
		drivingController.toggleDrawing();
	}
	
	public void setFuzzyControlSystem(DrivingController drivingController) {
		this.drivingController = drivingController;
	}
}
