package main.java.space;

import main.java.app.Drawable;

public interface Environment extends Drawable{
	public void registerAgents();
	public float [][] terrainFactory();
	public float [][] assetsFactory();
}
