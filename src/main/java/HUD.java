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
	
	private String nowObserving = "";

	private float functionsFigureScale = 1.0f;
	private float dataFigureScale = 1.0f;
	private float textInfoSize = 15f;
	private float textDataSize = 12f;
	private float spacingDivider = 3f;
	private float textDetailScaler = 0.95f;
	private float inputMarkerScale = 5;
	
	private int gridXFunctionsPosition = 0;
	private int gridYFunctionsPosition = 0;
	
	private int gridXDataPosition = 0;
	
	private int windowHeigth;
	private int windowWidth;
	
	private int functionsWidthOffset;
	private int functionsheigthOffset;
	
	private int dataHeightInitialOffset = 60;
	private int dataWidthOffset = 30;
	private int dataheigthOffset = dataHeightInitialOffset;
	
	private int xAxisLength = (int) (150 * functionsFigureScale);
	private int yAxisLength = xAxisLength / 2;
		
	private float xAxisScale = 1.50f;
	private float yAxisScale = 1.70f;
	
	private int dataFigureXLength = (int) (150 * functionsFigureScale);
	private float dataFigureYSpacing = textDataSize * 2;
		
	PApplet applet = null;
	PeasyCam camera = null;
	
	public HUD(PApplet applet, PeasyCam camera, int windowWidth, int windowHeigth) {
		this.applet = applet;
		this.camera = camera;
		this.windowWidth = windowWidth;
		this.windowHeigth = windowHeigth;
	}
	
	public void initOffset() {
		functionsWidthOffset = windowWidth - xAxisLength - (int)(xAxisLength * 0.33);
		functionsheigthOffset = (int)(yAxisLength * 0.33);
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
		float textSize = xAxisLength / textInfoSize * functionsFigureScale;
		
		int functionXOffset = functionsWidthOffset - (int)Math.ceil(xAxisLength * xAxisScale) * gridXFunctionsPosition; // TODO : Test xGrid
		int functionYOffset = functionsheigthOffset + (int)Math.ceil(yAxisLength * yAxisScale) * gridYFunctionsPosition;
		
		applet.fill(0, 255, 0);
		applet.stroke(0, 255, 0);
		
		applet.textSize(textSize);
		
		applet.line(functionXOffset, functionYOffset + yAxisLength, functionXOffset + xAxisLength, functionYOffset + yAxisLength);
		applet.text(String.format("%s: %s", "Input", variable.getName()), functionXOffset + xAxisLength * 0.5f, functionYOffset + yAxisLength + 21 * functionsFigureScale);
		
		applet.line(functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange), functionYOffset, functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange), functionYOffset + yAxisLength);
		applet.text("u", functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange) + 5, functionYOffset);
		applet.text("1", functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange) + 5, functionYOffset + yAxisLength * 0.2f);
		
		textSize = xAxisLength / textInfoSize * textDetailScaler * functionsFigureScale;
		applet.textSize(textSize);
		
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
		applet.text(String.format("%.2f", domain[0]), functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[0]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 10 * functionsFigureScale);
		applet.text(String.format("%.2f", domain[domain.length - 1]), functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[domain.length - 1]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 10 * functionsFigureScale);

		applet.fill(255, 0, 0);
		applet.stroke(255, 0, 0);
		
		if (variable.getCrispInput() > domain[domain.length - 1]) {
			applet.circle(functionXOffset + xAxisLength * 0.95f, 
					functionYOffset + yAxisLength, inputMarkerScale);
		}else if(variable.getCrispInput() < domain[0]){
			applet.circle(functionXOffset + xAxisLength * 0.05f, 
					functionYOffset + yAxisLength, inputMarkerScale);
		}else {
			applet.circle(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + variable.getCrispInput()) / domainRange) * xAxisLength * 0.9f, 
					functionYOffset + yAxisLength, 
					inputMarkerScale);
		}
		
//		applet.circle(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + x[0]) / domainRange) * xAxisLength * 0.9f, 
//				functionYOffset + yAxisLength, inputRadius);
		
		float[] fuzzifiedInputs = variable.getFuzzifiedInputs(); 

		float xPos;
		
		xPos = variable.getCrispInput();
		
		if (variable.getCrispInput() >= domain[domain.length - 1]){
			xPos = domain[domain.length - 1];
		}else if (variable.getCrispInput() <= domain[0]){
			xPos = domain[0];
		}else {
			xPos = variable.getCrispInput();
		}
		
		for (int i = 0; i < fuzzifiedInputs.length; i++) {
				
				applet.circle(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + xPos) / domainRange) * xAxisLength * 0.9f, 
						functionYOffset + yAxisLength * 0.2f + (1 - fuzzifiedInputs[i]) * yAxisLength * 0.8f, 
						inputMarkerScale);	
		}
		
		applet.stroke(255, 0, 0);
		
		applet.strokeWeight(1.5f);
		applet.line(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + xPos)/ domainRange) * xAxisLength * 0.9f, functionYOffset, 
				functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + xPos )/ domainRange)* xAxisLength * 0.90f, functionYOffset + yAxisLength);
		applet.strokeWeight(1);
		applet.text(String.format("%.2f", variable.getCrispInput()), functionXOffset + xAxisLength * ((Math.abs(domain[0]) + xPos)/ domainRange), functionYOffset + yAxisLength + 10 * functionsFigureScale);
		
		return functionYOffset + (int)Math.ceil(yAxisLength * 1.70) * gridYFunctionsPosition + 1;
	}
	
	public int drawOutputFunction(FuzzyVariable variable) {
		
		float[] domain = variable.getDomain();
		float[] aggregation = variable.getAggregated();
		
		float domainRange = domain[domain.length - 1] - domain[0];
		float textSize = xAxisLength / textInfoSize * functionsFigureScale;
		
		int functionXOffset = functionsWidthOffset - (int)Math.ceil(xAxisLength * xAxisScale) * gridXFunctionsPosition; // TODO : Test xGrid
		int functionYOffset = functionsheigthOffset + (int)Math.ceil(yAxisLength * yAxisScale) * gridYFunctionsPosition;
		
		applet.fill(0, 255, 0);
		applet.stroke(0, 255, 0);
		
		applet.textSize(textSize);
		
		applet.line(functionXOffset, functionYOffset + yAxisLength, functionXOffset + xAxisLength, functionYOffset + yAxisLength);
		applet.text(String.format("%s: %s", "Output", variable.getName()), functionXOffset + xAxisLength * 0.5f, functionYOffset + yAxisLength + 21 * functionsFigureScale);
		
		applet.line(functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange), functionYOffset, functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange), functionYOffset + yAxisLength);
		applet.text("u", functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange) + 5, functionYOffset);
		applet.text("1", functionXOffset + xAxisLength * (Math.abs(domain[0]) / domainRange) + 5, functionYOffset + yAxisLength * 0.2f);

		textSize = xAxisLength / textInfoSize * textDetailScaler * functionsFigureScale;
		applet.textSize(textSize);
		
		applet.text(String.format("%.2f", domain[0]), functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[0]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 10 * functionsFigureScale);
		applet.text(String.format("%.2f", domain[domain.length - 1]), functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[domain.length - 1]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 10 * functionsFigureScale);
		
		applet.beginShape();
		
		for (int i = 0; i < domain.length; i++) {
			applet.vertex(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[i]) / domainRange) * xAxisLength * 0.9f, 
					functionYOffset + yAxisLength);
		}

		for (int i = aggregation.length - 1; i >= 0; i--) {			
			applet.vertex(functionXOffset + xAxisLength * 0.05f + ((Math.abs(domain[0]) + domain[i]) / domainRange) * xAxisLength * 0.9f, functionYOffset + yAxisLength * 0.2f + (1 - aggregation[i]) * yAxisLength * 0.8f);
		}
		
		applet.endShape(PApplet.CLOSE);

		applet.stroke(255, 0, 0);
		applet.fill(255, 0, 0);

		applet.strokeWeight(3);
		applet.line(functionXOffset + xAxisLength * ((Math.abs(domain[0]) + variable.getCrispOutput())/ domainRange), functionYOffset, functionXOffset + xAxisLength * ((Math.abs(domain[0]) + variable.getCrispOutput())/ domainRange), functionYOffset + yAxisLength);
		applet.strokeWeight(1);
		applet.text(String.format("%.2f", variable.getCrispOutput()), functionXOffset + xAxisLength * ((Math.abs(domain[0]) + variable.getCrispOutput())/ domainRange), functionYOffset + yAxisLength + 10 * functionsFigureScale);
		applet.text("z*", functionXOffset + xAxisLength * ((Math.abs(domain[0]) + variable.getCrispOutput())/ domainRange), functionYOffset);

		return functionYOffset  + (int)Math.ceil(yAxisLength * yAxisScale) * gridYFunctionsPosition + 1;
	}

	private int drawOutputVariable(FuzzyVariable variable) {
		float textSize = textDataSize * dataFigureScale;
		float spacing  = textSize + textSize / spacingDivider;
		
		int dataXOffset = dataWidthOffset + (int)Math.ceil(dataFigureXLength * dataFigureScale) * gridXDataPosition; // TODO : Test xGrid
		int dataYOffset = dataheigthOffset + (int)Math.ceil(dataFigureYSpacing);
		
		applet.textSize(textSize);
		applet.fill(0, 255, 0);
		applet.stroke(0, 255, 0);
		
		String variableName = variable.getName();
		String defuzzificationMethod = variable.getMethod();
		float lowerBound = variable.getLowerBound();
		float upperBound = variable.getUpperBound();
		float crispOutput = variable.getCrispOutput();
		
		applet.text(String.format("Output Variable: %s, (%.2f, %.2f)", variableName, lowerBound, upperBound), dataXOffset, dataYOffset+=spacing);		
		applet.text(String.format("Defuzzification Method: %s", defuzzificationMethod), dataXOffset, dataYOffset+=spacing);
		applet.text(String.format("Crisp Output: %.2f", crispOutput), dataXOffset, dataYOffset+=spacing);
		
		dataheigthOffset = dataYOffset;
		
		return dataYOffset + 60;
	}

	private int drawInputVaraible(FuzzyVariable variable) {
		float textSize =  textDataSize * dataFigureScale;
		float spacing  = textSize + textSize / spacingDivider;

		int dataXOffset = dataWidthOffset + (int)Math.ceil(dataFigureXLength * dataFigureScale) * gridXDataPosition; // TODO : Test xGrid
		int dataYOffset = dataheigthOffset + (int)Math.ceil(dataFigureYSpacing);
		
		applet.textSize(textSize);
		applet.fill(0, 255, 0);
		applet.stroke(0, 255, 0);
		
		String variableName = variable.getName();
		float lowerBound = variable.getLowerBound();
		float upperBound = variable.getUpperBound();
		float crispInput = variable.getCrispInput();
		float[] fuzzifiedInputs = variable.getFuzzifiedInputs();
		
		applet.text(String.format("Input Variable: %s, (%.2f, %.2f)", variableName, lowerBound, upperBound), dataXOffset, dataYOffset+=spacing);
		applet.text(String.format("Crisp Input: %.2f", crispInput), dataXOffset, dataYOffset+=spacing);
		
		for (int i = 0; i < variable.numberOfMembershipFunctions(); i++) {
			applet.text(String.format("u%d(%.2f): %.2f", i, crispInput, fuzzifiedInputs[i]), dataXOffset, dataYOffset+=spacing);
		}
		
		dataheigthOffset = dataYOffset;
		
		return dataYOffset + 60;
	}

	private void drawInputData() {
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariablesIterator = this.fuzzyVariables.entrySet().iterator();
		
		while (fuzzyVariablesIterator.hasNext()) {
	        Map.Entry<String, FuzzyVariable> fuzzyVariablePair = fuzzyVariablesIterator.next();
		    
	        FuzzyVariable fuzzyVariable = fuzzyVariablePair.getValue();
	        
	        if (fuzzyInputVariables.contains(fuzzyVariable.getName())) {
	        	int nextYOffset = drawInputVaraible(fuzzyVariable);
		        
		        if (nextYOffset > windowHeigth - 150) {
		        	gridXDataPosition++;
		        	dataheigthOffset = dataHeightInitialOffset;
		        }
		        
	        }
		}
	}

	private void drawOutputData() {
Iterator<Entry<String, FuzzyVariable>> fuzzyVariablesIterator = this.fuzzyVariables.entrySet().iterator();
		
		while (fuzzyVariablesIterator.hasNext()) {
	        Map.Entry<String, FuzzyVariable> fuzzyVariablePair = fuzzyVariablesIterator.next();
		    
	        FuzzyVariable fuzzyVariable = fuzzyVariablePair.getValue();
	        
	        if (fuzzyOutputVariables.contains(fuzzyVariable.getName())) {
	        	int nextYOffset = drawOutputVariable(fuzzyVariable);
		        
		        if (nextYOffset > windowHeigth - 150) {
		        	gridXDataPosition++;
		        	dataheigthOffset = dataHeightInitialOffset;
		        }
		        
	        }
		}	
	}

	public void drawInputFunctions() {
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariablesIterator = this.fuzzyVariables.entrySet().iterator();
		
		while (fuzzyVariablesIterator.hasNext()) {
	        Map.Entry<String, FuzzyVariable> fuzzyVariablePair = fuzzyVariablesIterator.next();
		    
	        FuzzyVariable fuzzyVariable = fuzzyVariablePair.getValue();
	        
	        if (fuzzyInputVariables.contains(fuzzyVariable.getName())) {
	        	int nextYOffset = drawInputFunction(fuzzyVariable);
		        
	        	gridYFunctionsPosition++;
	        	
		        if (nextYOffset > windowHeigth) {
		        	gridXFunctionsPosition++;
		        	gridYFunctionsPosition = 0;
		        }
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

	        	gridYFunctionsPosition++;
    
		        if (nextYOffset > windowHeigth) {
		        	gridXFunctionsPosition++;
		        	gridYFunctionsPosition = 0;
		        }
		    }
		}
	}
	
	private void drawAssets() {
		camera.beginHUD();
		
		applet.fill(0, 255, 0);
		applet.stroke(0, 255, 0);
		
		applet.textSize(15);
		applet.text(String.format("Observing: %s", nowObserving), 30, 30);
		
		applet.textSize(15);
		applet.text(String.format("Next Vehicle: KEY_RIGHT"), 30, 700);
		applet.text(String.format("Manual Takeover: KEY_UP"), 30, 720);
		applet.text(String.format("TPS Camera: KEY_DOWNT"), 30, 740);
		
		camera.endHUD();
		
	}


	public void draw() {
		camera.beginHUD();
		
		drawInputFunctions();
		drawInputData();
		
		drawOutputFunctions();
		drawOutputData();
		
		camera.endHUD();
		drawAssets();
		
		gridXFunctionsPosition = 0;
		gridYFunctionsPosition = 0;
		
		gridXDataPosition = 0;
		
		dataheigthOffset = dataHeightInitialOffset;
	}
	
	
	public void setScale(float scale) {this.functionsFigureScale = scale;}

	public String getNowObserving() {
		return nowObserving;
	}

	public void setNowObserving(String nowObserving) {
		this.nowObserving = nowObserving;
	}
	
}
