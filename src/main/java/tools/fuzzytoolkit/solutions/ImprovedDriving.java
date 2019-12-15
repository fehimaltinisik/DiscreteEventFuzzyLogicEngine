package main.java.tools.fuzzytoolkit.solutions;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.java.HUD;
import main.java.tools.fuzzytoolkit.FuzzyControlSystem;
import main.java.tools.fuzzytoolkit.FuzzySolution;
import main.java.tools.fuzzytoolkit.FuzzyVariable;
import processing.core.PApplet;

public class ImprovedDriving extends DrivingController{

	public ImprovedDriving(PApplet applet, boolean toggleDraw, boolean toggleDrawMinimal) {
		super(applet, toggleDraw, toggleDrawMinimal);
	}

	@Override
	public void solutionFactory() {
		
		int precision = 20;
		int steeringPrecision = 20;
		
		float correctionMultiplier = 3f / 16;
		
		solution = new FuzzySolution();
		
		FuzzyVariable lateralError = solution.newFuzzyVariable("lateralError", -30.9f, 30.76f, precision);
		lateralError.addMembershipFunction("trimf", -30.9f, -30.9f, 0);
		lateralError.addMembershipFunction("trimf", -30.9f, 0, 30.76f);
		lateralError.addMembershipFunction("trimf", 0, 30.76f, 30.76f);
		lateralError.initFuzzyVariableDependencies();
		lateralError.setName("lateralError");
		solution.registerFuzzyVariable("lateralError", lateralError);
		
		FuzzyVariable angularError = solution.newFuzzyVariable("angularError", (float)-Math.PI, (float)Math.PI, precision);
		angularError.addMembershipFunction("trimf", (float)-Math.PI, (float)-Math.PI, 0);
		angularError.addMembershipFunction("trimf", (float)-Math.PI, 0, (float)Math.PI);
		angularError.addMembershipFunction("trimf", 0, (float)Math.PI, (float)Math.PI);
		angularError.initFuzzyVariableDependencies();
		angularError.setName("angularError");
		solution.registerFuzzyVariable("angularError", angularError);
		
		FuzzyVariable steer = solution.newFuzzyVariable("steer", (float)-Math.PI / steeringPrecision, (float)Math.PI / steeringPrecision, precision);
		steer.addMembershipFunction("trimf", (float)-Math.PI / steeringPrecision, (float)-Math.PI / steeringPrecision, 0);
		steer.addMembershipFunction("trimf", (float)-Math.PI / steeringPrecision / 8, (float)-Math.PI / steeringPrecision / 16, 0);
		steer.addMembershipFunction("trimf", (float)-Math.PI / steeringPrecision, 0, (float)Math.PI / steeringPrecision);
		steer.addMembershipFunction("trimf", 0, (float)Math.PI / steeringPrecision / 16, (float)Math.PI / steeringPrecision / 8);
		steer.addMembershipFunction("trimf", 0, (float)Math.PI / steeringPrecision, (float)Math.PI / steeringPrecision);
		steer.initFuzzyVariableDependencies();
		steer.setName("steer");
		solution.registerFuzzyVariable("steer", steer);
		
		FuzzyVariable steerFeedback = solution.newFuzzyVariable("steerFeedback", (float)-Math.PI / steeringPrecision, (float)Math.PI / steeringPrecision, precision);
		steerFeedback.addMembershipFunction("trimf", (float)-Math.PI / steeringPrecision * correctionMultiplier, 0, (float)Math.PI / steeringPrecision * correctionMultiplier);
		steerFeedback.initFuzzyVariableDependencies();
		steerFeedback.setName("steerFeedback");
		solution.registerFuzzyVariable("steerFeedback", steerFeedback);
		
//		solution.newFuzzyVariable("lateralError", -30f, 30f, precision, 5);
//		solution.newFuzzyVariable("angularError", (float)-Math.PI, (float)Math.PI, precision, 5);
		//solution.newFuzzyVariable("steer", 0, 90.5f, 25, 3);
		
		solution.newActivationRule("lateralleftANDangularleft", "lateralError", "angularError");
		solution.newActivationRule("lateralrightANDangularright", "lateralError", "angularError");
		solution.newActivationRule("lateralrightANDangularleft", "lateralError", "angularError");
		solution.newActivationRule("lateralleftANDangularright", "lateralError", "angularError");

		solution.newActivationRule("lateralleftANDangularbalance", "lateralError", "angularError");
		solution.newActivationRule("lateralrightANDangularbalance", "lateralError", "angularError");
		
		solution.newActivationRule("lateralleftErrorCorrection", "lateralError", "steerFeedback");
		solution.newActivationRule("lateralrightErrorCorrection", "lateralError", "steerFeedback");
				
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
		crispOutputs.put("steer", solution.getDefuzzified("steerDefuzz"));
	}

	@Override
	public void systemUpdate() {
		
		solution.clearActivations();
		
		// Basic Rules
		solution.evalActivationOutput("lateralleftANDangularleft", 
				"lateralleftANDangularleftOut", "and", 0, 0);
	
		solution.evalActivationOutput("lateralrightANDangularright", 
				"lateralrightANDangularrightOut", "and", 2, 2);
		
		solution.evalActivationOutput("lateralrightANDangularleft", 
				"lateralrightANDangularleftOut", "and", 2, 0);
		
		solution.evalActivationOutput("lateralleftANDangularright", 
				"lateralleftANDangularrightOut", "and", 0, 2);
		
		// Improved Rules
//		solution.evalActivationOutput("lateralleftANDangularbalance", 
//				"lateralleftANDangularbalanceOut", "and", 0, 1);
//		
//		solution.evalActivationOutput("lateralrightANDangularbalance", 
//				"lateralrightANDangularbalanceOut", "and", 2, 1);
		
		solution.activate("lateralleftANDangularleft", "lateralleftANDangularleftOut", "steer", 4);
		solution.activate("lateralrightANDangularright", "lateralrightANDangularrightOut", "steer", 0);
		solution.activate("lateralrightANDangularleft", "lateralrightANDangularleftOut", "steer", 2);
		solution.activate("lateralleftANDangularright", "lateralleftANDangularrightOut", "steer", 2);
		
		solution.aggregate("steer");

		solution.defuzz("steer", "steerDefuzz", "centroid");
		crispOutputs.put("steer", solution.getDefuzzified("steerDefuzz"));
		
		float steerfb = crispOutputs.get("steer");
		
		solution.updateCrispInputs(new HashMap<String, Float>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put("steerFeedback", steerfb);
		}});

		solution.evalActivationOutput("lateralleftErrorCorrection", 
				"lateralleftErrorCorrectionOut", "and", 0, 0);
		
		solution.evalActivationOutput("lateralrightErrorCorrection", 
				"lateralrightErrorCorrectionOut", "and", 2, 0);
		
		solution.activate("lateralleftErrorCorrection", "lateralleftErrorCorrectionOut", "steer", 3);
		solution.activate("lateralrightErrorCorrection", "lateralrightErrorCorrectionOut", "steer", 1);
		
		solution.aggregate("steer");
		
//		solution.activate("lateralleftANDangularbalance", "lateralleftANDangularbalanceOut", "steer", 3);
//		solution.activate("lateralrightANDangularbalance", "lateralrightANDangularbalanceOut", "steer", 1);	

//		if (-0.01f < level0 && level0 < 0.01f) {
//			solution.clearActivations();
//			
//			solution.activate("lateralleftANDangularbalance", "lateralleftANDangularbalanceOut", "steer", 3);
//			solution.activate("lateralrightANDangularbalance", "lateralrightANDangularbalanceOut", "steer", 1);	
//
////			solution.activate("lateralError", 0, "steer", 4);
////			solution.activate("lateralError", 2, "steer", 3);
//
//			solution.aggregate("steer");
//			
//			System.out.println(String.format("Level0 Condition %.2f", level0));
//		}
	}

	@Override
	public void debug() {
		System.out.println("<ControlSystemDebug>\n");
		
		solution.debug();
		
		System.out.printf("%.2f", crispOutputs.get("steer"));
		
		System.out.println("</ControlSystemDebug>\n");
	}

	@Override
	public void guiStateUpdate() {
		
		if(toggleDraw) {
			hud = new HUD(applet, camera, 1280, 768);
			hud.initOffset();
			hud.setNowObserving(observerName);
			solution.setUpScene();
			
			hud.registerFuzzyVariable("lateralError", solution.getFuzzyVariable("lateralError"));
			hud.registerFuzzyVariable("angularError", solution.getFuzzyVariable("angularError"));
			hud.registerFuzzyVariable("steerFeedback", solution.getFuzzyVariable("steerFeedback"));
			hud.registerFuzzyVariable("steer", solution.getFuzzyVariable("steer"));
			
			hud.drawFuzzyInputVariable("lateralError");
			hud.drawFuzzyInputVariable("angularError");
			hud.drawFuzzyInputVariable("steerFeedback");
			hud.drawFuzzyOutputVariable("steer");	
		}else {
			hud = null;
		}
		
	}
	
//	public Float getCrispOutputs(String key) {
//		System.out.println("sdfsd");
//		return crispOutputs.get(key);
//	}
}
