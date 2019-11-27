package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import peasy.PeasyCam;
import peasy.PeasyDragHandler;
import processing.core.PApplet;

public class HUD {
	
	private HashMap<String, float []> discreteFunctions = new HashMap<String, float []>();
	
	int windowHeigth;
	int windowWidth;
	
	int widthOffset;
	int heigthOffset;
	
	// int positionRegister = 0;
	
	PApplet applet = null;
	PeasyCam camera = null;
	
	public HUD(PApplet applet, PeasyCam camera, int windowWidth, int windowHeigth) {
		this.applet = applet;
		this.camera = camera;
		this.windowWidth = windowWidth;
		this.windowHeigth = windowHeigth;
		
		widthOffset = windowWidth - 200;
		heigthOffset = 25;
	}
	
	public void registerDiscreteFunction(String name, float [] function) {
		discreteFunctions.put(name, function);
	}
	
	public void drawDiscreteFunction(String key, float[] data, int position) {
	
		int xAxisLength = 150;
		int yAxisLength = 80;
		
		int functionXOffset = widthOffset;
		int functionYOffset = heigthOffset + (int)Math.ceil(yAxisLength * 1.50) * position;
		
		System.out.printf("%d, %d", functionXOffset, functionYOffset);

		applet.fill(0, 255, 0);
		applet.stroke(0, 255, 0);
		
		applet.line(functionXOffset, functionYOffset + yAxisLength, functionXOffset + xAxisLength, functionYOffset + yAxisLength);
		applet.text(key, functionXOffset + xAxisLength - 10, functionYOffset + yAxisLength + 10);
		
		applet.line(functionXOffset + xAxisLength / 2, functionYOffset, functionXOffset + xAxisLength / 2, functionYOffset + yAxisLength);
		applet.text("u", functionXOffset + xAxisLength / 2 + 5, functionYOffset);
				
		position++;
	}
	
	public void drawDiscreteFunctions(String key) {
		
		camera.beginHUD();
		
		if (key == "all") {
			
			int counter = 0;
			
			Iterator<Entry<String, float[]>> discreteFunctions = this.discreteFunctions.entrySet().iterator();
			
			while (discreteFunctions.hasNext()) {
		        Map.Entry<String, float[]> pair = (Map.Entry<String, float[]>)discreteFunctions.next();
		        drawDiscreteFunction(pair.getKey(), pair.getValue(), counter);
		        counter++;
			}
		}
		
		camera.endHUD();
	}
	
	
}
