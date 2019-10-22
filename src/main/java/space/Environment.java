package main.java.space;

import main.java.engine.Drawable;

public interface Environment extends Drawable{
	public void registerAgents();
	public float [][] terrainFactory();
	public float [][] assetsFactory();
}
