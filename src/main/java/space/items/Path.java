package main.java.space.items;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Path extends Asset{
	PApplet applet;

	private ArrayList<PVector> points;
	
	private PVector start;
	private PVector end;

	private float radius;

	public Path(PApplet applet) {
		this.applet = applet;

		radius = 5;

		start = new PVector(-150, -150);
		end = new PVector(150, 150);
	}

	@Override
	public void draw() {

		applet.beginShape();

		applet.noFill();	
		applet.stroke(0, 255, 0);
		
		for (PVector v : getPoints()) {
			applet.vertex(v.x, v.y);
		}
		applet.endShape();
		
	}
	
	public void addPoint(float x, float y, float z) {
		PVector point = new PVector(x, y, z);
		getPoints().add(point);
	}

	public PVector getStart() {
		return start;
	}

	public void setStart(PVector start) {
		this.start = start;
	}

	public PVector getEnd() {
		return end;
	}

	public void setEnd(PVector end) {
		this.end = end;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public static PVector getNormalPoint(PVector p, PVector a, PVector b) {
		PVector ap = PVector.sub(p, a);
		PVector ab = PVector.sub(b, a);

		ab.normalize();
		ab.mult(ap.dot(ab));
		PVector normalPoint = PVector.add(a, ab);

		return normalPoint;
	}

	public ArrayList<PVector> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<PVector> points) {
		this.points = points;
	}
}
