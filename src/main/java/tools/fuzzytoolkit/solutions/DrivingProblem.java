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
		
		solution.registerFuzzyVariable("lateralError", lateralError);
		
		//solution.newFuzzyVariable("lateralError", -5.9f, 3.75f, 10, 3);
		solution.newFuzzyVariable("angularError", (float)-Math.PI / 2, (float)Math.PI / 2, 10, 3);
		solution.newFuzzyVariable("steer", 0, 90.5f, 25, 3);
		
		HashMap<String, Float> crispInputs = new HashMap<String, Float>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		
		{
			put("lateralError", -3.2f);
			put("angularError", 1.17f);
		}};
		
		solution.updateCrispInputs(crispInputs);
		
		// solution.newActivationRule("LxA", "lateralError", "angularError");
		// solution.evalActivationOutput("LxA", "0x0", "or", 0, 0);
		// solution.evalActivationOutput("LxA", "2x2", "or", 2, 2);

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
	public void evaluateCrispInputs() {
		solution.defuzz("steer", "steerDefuzz", "centroid");
		crispOutputs.put("steer", solution.getDefuzzified("steerDefuzz"));
	}

	@Override
	public void systemUpdate() {
		
		solution.clearActivations();
		
		// solution.evalActivationOutput("QxS", "0x0", "or", 0, 0);
		// solution.evalActivationOutput("QxS", "2x2", "or", 2, 2);	
		
		// solution.activate("QxS", "0x0", "tip", 0);
		// solution.activate("QxS", "2x2", "tip", 2);
		solution.activate("lateralError", 0, "steer", 0);
		solution.activate("lateralError", 1, "steer", 1);
		solution.activate("angularError", 0, "steer", 0);
		solution.activate("angularError", 1, "steer", 1);
		
		solution.aggregate("steer");
	}

	@Override
	public void debug() {
		System.out.printf("%.2f", solution.getDefuzzified("steer"));
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
		
		hud.registerDiscreteFunction("lateralError", functions.get("lateralError"));
		hud.registerDiscreteFunction("angularError", functions.get("angularError"));

	}
	
	public Float getCrispOutputs(String key) {
		return crispOutputs.get(key);
	}

}
