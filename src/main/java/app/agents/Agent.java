package main.java.app.agents;

import java.util.HashMap;

import main.java.Drawable;
import main.java.space.items.Asset;
import processing.core.PVector;

public abstract class Agent implements Drawable{
	protected HashMap<String, Asset> assets = new HashMap<String, Asset>();
	
	public void registerAsset(String key, Asset asset) {
		assets.put(key, asset);
	}
	
	public abstract void spawn(PVector position, PVector velocity);
	public abstract void operate();
	public abstract void update();
}
