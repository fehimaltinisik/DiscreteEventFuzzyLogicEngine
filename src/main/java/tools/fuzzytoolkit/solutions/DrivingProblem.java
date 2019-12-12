package main.java.tools.fuzzytoolkit.solutions;

import java.util.HashMap;
import java.util.List;

import main.java.HUD;
import main.java.tools.fuzzytoolkit.FuzzyControlSystem;
import main.java.tools.fuzzytoolkit.FuzzySolution;
import main.java.tools.fuzzytoolkit.FuzzyVariable;
import processing.core.PApplet;

public class DrivingProblem extends FuzzyControlSystem{

	HashMap<String, Float> crispInputs = new HashMap<String, Float>();
	HashMap<String, Float> crispOutputs = new HashMap<String, Float>();
		
	public DrivingProblem(PApplet applet, boolean toggleDraw, boolean toggleDrawMinimal) {
		super(applet, toggleDraw, toggleDrawMinimal);
	}

	@Override
	public void solutionFactory() {
		
		
		int precision = 20;
		
		solution = new FuzzySolution();
		
		FuzzyVariable lateralError = solution.newFuzzyVariable("lateralError", -25.9f, 13.76f, precision);
		lateralError.addMembershipFunction("trimf", -25.9f, -25.9f, 0);
		lateralError.addMembershipFunction("trimf", 0, 13.76f, 13.76f);
		lateralError.initFuzzyVariableDependencies();
		lateralError.setName("lateralError");
		solution.registerFuzzyVariable("lateralError", lateralError);
		
		FuzzyVariable angularError = solution.newFuzzyVariable("angularError", (float)-Math.PI, (float)Math.PI, precision);
		angularError.addMembershipFunction("trimf", (float)-Math.PI, (float)-Math.PI, 0);
		angularError.addMembershipFunction("trimf", 0, (float)Math.PI, (float)Math.PI);
		angularError.initFuzzyVariableDependencies();
		angularError.setName("angularError");
		solution.registerFuzzyVariable("angularError", angularError);
		
		FuzzyVariable steer = solution.newFuzzyVariable("steer", (float)-Math.PI / 30, (float)Math.PI / 30, precision);
		steer.addMembershipFunction("trimf", (float)-Math.PI / 30, (float)-Math.PI / 30, 0);
		steer.addMembershipFunction("trimf", (float)-Math.PI / 30, 0, (float)Math.PI / 30);
		steer.addMembershipFunction("trimf", 0, (float)Math.PI / 30, (float)Math.PI / 30);
		steer.initFuzzyVariableDependencies();
		steer.setName("steer");
		solution.registerFuzzyVariable("steer", steer);
		
		solution.newActivationRule("lateralleftANDangularleft", "lateralError", "angularError");
		solution.newActivationRule("lateralrightANDangularright", "lateralError", "angularError");
		solution.newActivationRule("lateralrightANDangularleft", "lateralError", "angularError");
		solution.newActivationRule("lateralleftANDangularright", "lateralError", "angularError");

		//solution.newFuzzyVariable("lateralError", -5.9f, 3.75f, 10, 3);
		//solution.newFuzzyVariable("angularError", (float)-Math.PI / 2, (float)Math.PI / 2, 10, 3);
		//solution.newFuzzyVariable("steer", 0, 90.5f, 25, 3);
				
		if (toggleDraw) {
			hud = new HUD(applet, camera, 1280, 768);
			hud.initOffset();
			guiStateUpdate();
		}
		
	}

	@Override
	public void registerCrispInputs(HashMap<String, Float> crispInputs) {
		solution.updateCrispInputs(crispInputs);
		crispInputs.putAll(crispInputs);
		
	}

	@Override
	public void evaluateCrispOutputs() {
		solution.defuzz("steer", "steerDefuzz", "centroid");
		crispOutputs.remove("steer");
		crispOutputs.put("steer", solution.getDefuzzified("steerDefuzz"));
	}

	@Override
	public void systemUpdate() {
		
		solution.clearActivations();
		
		solution.evalActivationOutput("lateralleftANDangularleft", 
				"lateralleftANDangularleftOut", "and", 0, 0);
	
		solution.evalActivationOutput("lateralrightANDangularright", 
				"lateralrightANDangularrightOut", "and", 1, 1);
		
		solution.evalActivationOutput("lateralrightANDangularleft", 
				"lateralrightANDangularleftOut", "and", 1, 0);
		
		solution.evalActivationOutput("lateralleftANDangularright", 
				"lateralleftANDangularrightOut", "and", 0, 1);
		
		solution.activate("lateralleftANDangularleft", "lateralleftANDangularleftOut", "steer", 2);
		solution.activate("lateralrightANDangularright", "lateralrightANDangularrightOut", "steer", 0);
		solution.activate("lateralrightANDangularleft", "lateralrightANDangularleftOut", "steer", 1);
		solution.activate("lateralleftANDangularright", "lateralleftANDangularrightOut", "steer", 1);

//		solution.activate("lateralError", 0, "steer", 1);
//		solution.activate("lateralError", 1, "steer", 0);
//		solution.activate("angularError", 0, "steer", 1);
//		solution.activate("angularError", 1, "steer", 0);
		
		solution.aggregate("steer");
	}

	@Override
	public void debug() {
		System.out.println("<ControlSystemDebug>\n");
		
		solution.debug();
		
		// System.out.printf("%.2f", crispOutputs.get("steer"));
		
		System.out.println("</ControlSystemDebug>\n");
	}

	@Override
	public void draw() {
		solution.draw();
		if (toggleDraw) {
			if(!toggleDrawMimimal) {
				hud.draw();
			}else {
			}
		}
	}

	@Override
	public void guiStateUpdate() {
		
		solution.setUpScene();
		
		hud.registerFuzzyVariable("lateralError", solution.getFuzzyVariable("lateralError"));
		hud.registerFuzzyVariable("angularError", solution.getFuzzyVariable("angularError"));
		hud.registerFuzzyVariable("steer", solution.getFuzzyVariable("steer"));
		
		hud.drawFuzzyInputVariable("lateralError");
		hud.drawFuzzyInputVariable("angularError");
		hud.drawFuzzyOutputVariable("steer");

	}
	
	public Float getCrispOutputs(String key) {
		return crispOutputs.get(key);
	}

}
