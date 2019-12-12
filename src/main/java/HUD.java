package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.java.tools.fuzzytoolkit.FuzzyVariable;
import peasy.PeasyCam;
import processing.core.PApplet;

public class HUD {
	
	private HashMap<String, FuzzyVariable> fuzzyVariables = new HashMap<String, FuzzyVariable>();
	private List<String> fuzzyInputVariables = new ArrayList<String>();
	private List<String> fuzzyOutputVariables = new ArrayList<String>();

	
	private float figureScale = 1.2f;
	private float inputRadius = 5;
	
	int gridXPosition = 0;
	int gridYPosition = 0;
	
	private int windowHeigth;
	private int windowWidth;
	
	private int widthOffset;
	private int heigthOffset;
	
	private int xAxisLength = (int) (150 * figureScale);
	private int yAxisLength = xAxisLength / 2;
		
	PApplet applet = null;
	PeasyCam camera = null;
	
	public HUD(PApplet applet, PeasyCam camera, int windowWidth, int windowHeigth) {
		this.applet = applet;
		this.camera = camera;
		this.windowWidth = windowWidth;
		this.windowHeigth = windowHeigth;
	}
	
	public void initOffset() {
		widthOffset = windowWidth - xAxisLength - (int)(xAxisLength * 0.33);
		heigthOffset = (int)(yAxisLength * 0.33);
	}
	
	public void registerFuzzyVariable(String name, FuzzyVariable variable) {
		fuzzyVariables.put(name, variable);
	}
	
	public void drawFuzzyInputVariable(String fuzzyInputVariableName) {
		this.fuzzyInputVariables.add(fuzzyInputVariableName);
	}

	public void drawFuzzyOutputVariable(String fuzzyOutputVariableName) {
		this.fuzzyOutputVariables.add(fuzzyOutputVariableName);
	};
	
	public int drawInputFunction(FuzzyVariable variable) {
		
		float[] domain = variable.getDomain();
		
		float domainRange = domain[domain.length - 1] - domain[0];
		float textSize = xAxisLength / 15 * figureScale;
		
		int functionXOffset = widthOffset - (int)Math.ceil(xAxisLength * 1.50) * gridXPosition; // TODO : Test xGrid
		int functionYOffset = heigthOffset + (int)Math.ceil(yAxisLength * 1.70) * gridYPosition;
		
		applet.fill(0, 255, 0);
		applet.stroke(0, 255, 0);
		
		applet.textSize(textSize);
		
		applet.line(functionXOffset, functionYOffset + yAxisLength, functionXOffset + xAxisLength, functionYOffset + yAxisLength);
		applet.text(variable.getName(), functionXOffset + xAxisLength * 0.5f, functionYOffset + yAxisLength + 21 * figureScale);
		
		applet.line(functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange), functionYOffset, functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange), functionYOffset + yAxisLength);
		applet.text("u", functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange) + 5, functionYOffset);
		applet.text("1", functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange) + 5, functionYOffset + yAxisLength * 0.2f);
		
		textSize = xAxisLength / 21 * figureScale;
		applet.textSize(textSize);
		
		applet.text(Float.toString(domain[0]), functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[0]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 8 * figureScale);
		for (int i = 0; i < variable.numberOfMembershipFunctions(); i++) {
			float[] function = variable.getMembershipFunction(i);
						
			for (int j = 1; j < function.length; j++) {
				applet.line(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[j - 1]) / domainRange) * xAxisLength * 0.9f, 
							functionYOffset + yAxisLength * 0.2f + (1 - function[j - 1]) * yAxisLength * 0.8f, 
							functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[j]) / domainRange) * xAxisLength * 0.9f, 
							functionYOffset + yAxisLength * 0.2f + (1 - function[j]) * yAxisLength * 0.8f);
							
				// applet.text(Integer.toString((int)domain[j - 1]), functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[j - 1]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 8 * figureScale);
			}
			// applet.text(Integer.toString((int)domain[domain.length - 1]), functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[domain.length - 1]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 8 * figureScale);
		}
		applet.text(Float.toString(domain[domain.length - 1]), functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[domain.length - 1]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 8 * figureScale);
		
		applet.fill(255, 0, 0);
		applet.stroke(255, 0, 0);
		
		if (variable.getCrisp() > domain[domain.length - 1]) {
			applet.circle(functionXOffset + xAxisLength * 0.95f, 
					functionYOffset + yAxisLength, inputRadius);
		}else if(variable.getCrisp() < domain[0]){
			applet.circle(functionXOffset + xAxisLength * 0.05f, 
					functionYOffset + yAxisLength, inputRadius);
		}else {
			applet.circle(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + variable.getCrisp()) / domainRange) * xAxisLength * 0.9f, 
					functionYOffset + yAxisLength, 
					inputRadius);
		}
		
//		applet.circle(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + x[0]) / domainRange) * xAxisLength * 0.9f, 
//				functionYOffset + yAxisLength, inputRadius);
		
		float[] fuzzifiedInputs = variable.getFuzzifiedInputs(); 

		for (int i = 0; i < fuzzifiedInputs.length; i++) {
			
				float xPos = variable.getCrisp();
				
				if (variable.getCrisp() >= domain[domain.length - 1]){
					xPos = domain[domain.length - 1];
				}else if (variable.getCrisp() <= domain[0]){
					xPos = domain[0];
				}else {
					xPos = variable.getCrisp();
				}
				
				applet.circle(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + xPos) / domainRange) * xAxisLength * 0.9f, 
						functionYOffset + yAxisLength * 0.2f + (1 - fuzzifiedInputs[i]) * yAxisLength * 0.8f, 
						inputRadius);	
		}
		return functionYOffset = heigthOffset + (int)Math.ceil(yAxisLength * 1.50) * gridYPosition + 1;
	}
	
	public int drawOutputFunction(FuzzyVariable variable) {
		
		float[] domain = variable.getDomain();
		float[] aggregation = variable.getAggregated();
		
		float domainRange = domain[domain.length - 1] - domain[0];
		float textSize = xAxisLength / 15 * figureScale;
		
		int functionXOffset = widthOffset - (int)Math.ceil(xAxisLength * 1.50) * gridXPosition; // TODO : Test xGrid
		int functionYOffset = heigthOffset + (int)Math.ceil(yAxisLength * 1.70) * gridYPosition;
		
		applet.fill(0, 255, 0);
		applet.stroke(0, 255, 0);
		
		applet.textSize(textSize);
		
		applet.line(functionXOffset, functionYOffset + yAxisLength, functionXOffset + xAxisLength, functionYOffset + yAxisLength);
		applet.text(variable.getName(), functionXOffset + xAxisLength * 0.5f, functionYOffset + yAxisLength + 21 * figureScale);
		
		applet.line(functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange), functionYOffset, functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange), functionYOffset + yAxisLength);
		applet.text("u", functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange) + 5, functionYOffset);
		applet.text("1", functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange) + 5, functionYOffset + yAxisLength * 0.2f);
		
		textSize = xAxisLength / 21 * figureScale;
		applet.textSize(textSize);
		
		applet.beginShape();
		
		for (int i = 0; i < domain.length; i++) {
			applet.vertex(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[i]) / domainRange) * xAxisLength * 0.9f, 
					functionYOffset + yAxisLength);
	
//			applet.circle(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[i]) / domainRange) * xAxisLength * 0.9f, 
//					functionYOffset + yAxisLength, 
//					inputRadius);
//			for (int j = 1; j < function.length; j++) {
//				applet.line(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[j - 1]) / domainRange) * xAxisLength * 0.9f, 
//							functionYOffset + yAxisLength * 0.2f + (1 - function[j - 1]) * yAxisLength * 0.8f, 
//							functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[j]) / domainRange) * xAxisLength * 0.9f, 
//							functionYOffset + yAxisLength * 0.2f + (1 - function[j]) * yAxisLength * 0.8f);
//			}
		}

		for (int i = 0; i < aggregation.length; i++) {
			
			float xPoint = functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[i]) / domainRange) * xAxisLength * 0.9f; 
			
			applet.vertex(xPoint, functionYOffset + yAxisLength * 0.2f + (1 - aggregation[i]) * yAxisLength * 0.8f);
		}

//		for (int i = aggregation.length - 1; i >= 0; i--) {
//			
//			float xPoint = functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[domain.length - 1 - i]) / domainRange) * xAxisLength * 0.9f; 
//			
//			if (xPoint != 0)
//			applet.vertex(xPoint, functionYOffset + yAxisLength * 0.2f + (1 - aggregation[i]) * yAxisLength * 0.8f);
//			
////			applet.circle(xPoint, 
////					functionYOffset + yAxisLength * 0.2f + (1 - aggregation[i]) * yAxisLength * 0.8f, 
////					inputRadius);
//			
////			for (int j = 1; j < function.length; j++) {
////				applet.line(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[j - 1]) / domainRange) * xAxisLength * 0.9f, 
////							functionYOffset + yAxisLength * 0.2f + (1 - function[j - 1]) * yAxisLength * 0.8f, 
////							functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[j]) / domainRange) * xAxisLength * 0.9f, 
////							functionYOffset + yAxisLength * 0.2f + (1 - function[j]) * yAxisLength * 0.8f);
////			}
//		}
		
//		applet.vertex(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[0]) / domainRange) * xAxisLength * 0.9f, 
//				functionYOffset + yAxisLength);
//		
		applet.endShape(PApplet.CLOSE);
		
		applet.text(Float.toString(domain[0]), functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[0]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 8 * figureScale);
		
		applet.text(Float.toString(domain[domain.length - 1]), functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[domain.length - 1]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 8 * figureScale);
		
		applet.fill(255, 0, 0);
		applet.stroke(255, 0, 0);
		
//		if (variable.getCrisp() > domain[domain.length - 1]) {
//			applet.circle(functionXOffset + xAxisLength * 0.95f, 
//					functionYOffset + yAxisLength, inputRadius);
//		}else if(variable.getCrisp() < domain[0]){
//			applet.circle(functionXOffset + xAxisLength * 0.05f, 
//					functionYOffset + yAxisLength, inputRadius);
//		}else {
//			applet.circle(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + variable.getCrisp()) / domainRange) * xAxisLength * 0.9f, 
//					functionYOffset + yAxisLength, 
//					inputRadius);
//		}
		
//		applet.circle(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + x[0]) / domainRange) * xAxisLength * 0.9f, 
//				functionYOffset + yAxisLength, inputRadius);
		
//		float[] fuzzifiedInputs = variable.getFuzzifiedInputs(); 
//
//		for (int i = 0; i < fuzzifiedInputs.length; i++) {
//			
//				float xPos = variable.getCrisp();
//				
//				if (variable.getCrisp() >= domain[domain.length - 1]){
//					xPos = domain[domain.length - 1];
//				}else if (variable.getCrisp() <= domain[0]){
//					xPos = domain[0];
//				}else {
//					xPos = variable.getCrisp();
//				}
//				
//				applet.circle(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + xPos) / domainRange) * xAxisLength * 0.9f, 
//						functionYOffset + yAxisLength * 0.2f + (1 - fuzzifiedInputs[i]) * yAxisLength * 0.8f, 
//						inputRadius);	
//		}
		return functionYOffset = heigthOffset + (int)Math.ceil(yAxisLength * 1.50) * gridYPosition + 1;
	}

	public void drawInputFunctions() {
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariablesIterator = this.fuzzyVariables.entrySet().iterator();
		
		while (fuzzyVariablesIterator.hasNext()) {
	        Map.Entry<String, FuzzyVariable> fuzzyVariablePair = fuzzyVariablesIterator.next();
		    
	        FuzzyVariable fuzzyVariable = fuzzyVariablePair.getValue();
	        
	        if (fuzzyInputVariables.contains(fuzzyVariable.getName())) {
	        	int nextYOffset = drawInputFunction(fuzzyVariable);
		        
		        if (nextYOffset > windowHeigth) {
		        	gridXPosition++;
		        	gridYPosition = 0;
		        }
		        
		        gridYPosition++;
	        }
		}
	}
	
	public void drawOutputFunctions() {
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariablesIterator = this.fuzzyVariables.entrySet().iterator();
		
		while (fuzzyVariablesIterator.hasNext()) {
	        Map.Entry<String, FuzzyVariable> fuzzyVariablePair = fuzzyVariablesIterator.next();
		    
	        FuzzyVariable fuzzyVariable = fuzzyVariablePair.getValue();
	        
	        if (fuzzyOutputVariables.contains(fuzzyVariable.getName())) {
	        	int nextYOffset = drawOutputFunction(fuzzyVariable);
		        
		        if (nextYOffset > windowHeigth) {
		        	gridXPosition++;
		        	gridYPosition = 0;
		        }
		        
		        gridYPosition++;
	        }
		}
	}
	
	public void draw() {
		camera.beginHUD();
		
		drawInputFunctions();
		drawOutputFunctions();
		
		camera.endHUD();
		
		gridXPosition = 0;
		gridYPosition = 0;
	}
		
	public void setScale(float scale) {this.figureScale = scale;}
	
}
