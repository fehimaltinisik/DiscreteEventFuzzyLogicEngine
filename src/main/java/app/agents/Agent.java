package main.java.app.agents;

import main.java.Drawable;
import processing.core.PVector;

public abstract class Agent implements Drawable{
	public abstract void spawn(PVector position, PVector velocity);
	public abstract void operate();
	public abstract void update();
}
