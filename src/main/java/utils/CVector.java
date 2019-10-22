package main.java.utils;

import processing.core.PApplet;
import processing.core.PVector;

public class CVector extends PVector {

	private static final long serialVersionUID = 1L;

	public CVector() {
		super();
	}

	CVector(float x, float y) {
		super(x, y);
	}

	public CVector(float x, float y, float z) {
		super(x, y, z);
	}

	public String toString() {
		return String.format("[ %+.2f, %+.2f, %+.2f ]", x, y, z);
	}

	public PVector rotateX(float angle) {
		float cosa = PApplet.cos(angle);
		float sina = PApplet.sin(angle);
		float tempy = y;
		y = cosa * y - sina * z;
		z = cosa * z + sina * tempy;
		return this;
	}

	public PVector rotateY(float angle) {
		float cosa = PApplet.cos(angle);
		float sina = PApplet.sin(angle);
		float tempz = z;
		z = cosa * z - sina * x;
		x = cosa * x + sina * tempz;
		return this;
	}

	public PVector rotateZ(float angle) {
		float cosa = PApplet.cos(angle);
		float sina = PApplet.sin(angle);
		float tempx = x;
		x = cosa * x - sina * y;
		y = cosa * y + sina * tempx;
		return this;
	}
}