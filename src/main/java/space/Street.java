package main.java.space;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.java.app.agents.Agent;
import main.java.app.agents.SmartDrive;
import main.java.space.items.Asset;
import processing.core.PApplet;
import processing.core.PConstants;

public class Street extends Workspace{
	
	@Override
	public void radar() {		
		for(Agent agent : agents) {
			((SmartDrive)agent).updateRadar(agents);
		}
	}

	@Override
	public void draw() {
		applet.pushMatrix();
		applet.translate(-width / 2, -height / 2);

		applet.fill(128, 0, 128);
		applet.stroke(127);

		for (int y = 0; y < rows - 1; y++) {
			applet.beginShape(PConstants.TRIANGLE_STRIP);
			for (int x = 0; x < cols; x++) {
				applet.vertex(x * scale, y * scale, terrain[x][y]);
				applet.vertex(x * scale, (y + 1) * scale, terrain[x][y + 1]);
			}
			applet.endShape();
		}

		applet.popMatrix();
		
		Iterator<Entry<String, Asset>> itemsIterator = items.entrySet().iterator();
		
		while (itemsIterator.hasNext()) {
	        Map.Entry<String, Asset> agent = itemsIterator.next();
		    agent.getValue().draw();
		}
	}

	@Override
	public float[][] terrainFactory() {
		terrain = new float[rows][cols];

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				terrain[x][y] = 0;
			}
		}
		
		return terrain;
	}

	@Override
	public float[][] assetsFactory() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
