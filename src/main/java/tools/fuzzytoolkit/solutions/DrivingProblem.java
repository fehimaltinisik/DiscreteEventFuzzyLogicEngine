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
		
		solution = new FuzzySolution();
		
		FuzzyVariable lateralError = solution.newFuzzyVariable("lateralError", -5.9f, 3.75f, 10);
		lateralError.addMembershipFunction("trimf", -5.9f, -5.9f, 0);
		lateralError.addMembershipFunction("trimf", 0, 3.75f, 3.75f);
		lateralError.initFuzzyVariableDependencies();
		lateralError.setName("lateralError");
		solution.registerFuzzyVariable("lateralError", lateralError);
		
		FuzzyVariable angularError = solution.newFuzzyVariable("angularError", (float)-Math.PI, (float)Math.PI, 10);
		angularError.addMembershipFunction("trimf", (float)-Math.PI, (float)-Math.PI, 0);
		angularError.addMembershipFunction("trimf", 0, (float)Math.PI, (float)Math.PI);
		angularError.initFuzzyVariableDependencies();
		angularError.setName("angularError");
		solution.registerFuzzyVariable("angularError", angularError);
		
		FuzzyVariable steer = solution.newFuzzyVariable("steer", (float)-Math.PI / 15, (float)Math.PI / 15, 10);
		steer.addMembershipFunction("trimf", (float)-Math.PI / 15, (float)-Math.PI / 15, 0);
		steer.addMembershipFunction("trimf", 0, (float)Math.PI / 15, (float)Math.PI / 15);
		steer.initFuzzyVariableDependencies();
		steer.setName("steer");
		solution.registerFuzzyVariable("steer", steer);
		
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
	
//		solution.activate("lateralError", 0, "steer", 1);
//		solution.activate("lateralError", 1, "steer", 0);
		solution.activate("angularError", 0, "steer", 1);
		solution.activate("angularError", 1, "steer", 0);
		
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
				HashMap<String, float[]> hash = solution.getFuzzifiedOutputs();
				hash.remove("steer");
				hud.updateDiscreteFunctionsInputs(hash);
				hud.drawDiscreteFunctions("all");
			}else {
			}
		}
	}

	@Override
	public void guiStateUpdate() {
		
		solution.setUpScene();
		HashMap<String, List<float[]>> functions = solution.getSceneElements();
		
		hud.registerFuzzyVariable("lateralError", functions.get("lateralError"));
		hud.registerFuzzyVariable("angularError", functions.get("angularError"));

	}
	
	public Float getCrispOutputs(String key) {
		return crispOutputs.get(key);
	}

}
