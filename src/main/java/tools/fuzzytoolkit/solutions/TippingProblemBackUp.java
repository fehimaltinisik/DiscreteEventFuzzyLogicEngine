package main.java.tools.fuzzytoolkit.solutions;

import java.util.HashMap;
import java.util.List;

import main.java.HUD;
import main.java.tools.fuzzytoolkit.FuzzyControlSystem;
import main.java.tools.fuzzytoolkit.FuzzySolution;
import processing.core.PApplet;

public class TippingProblemBackUp extends FuzzyControlSystem{

	HashMap<String, Float> crispInputs = new HashMap<String, Float>();
	HashMap<String, Float> crispOutputs = new HashMap<String, Float>();
		
	public TippingProblemBackUp(PApplet applet, boolean toggleDraw, boolean toggleDrawMinimal) {
		super(applet, toggleDraw, toggleDrawMinimal);
	}

	@Override
	public void solutionFactory() {
		
		solution = new FuzzySolution();
		
		solution.newFuzzyVariable("quality", 0, 10, 10, 3);
		solution.newFuzzyVariable("service", 0, 10, 10, 3);
		solution.newFuzzyVariable("tip", 0, 25, 25, 3);
		
		HashMap<String, Float> crispInputs = new HashMap<String, Float>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		
		{
			put("quality", 6.5f);
			put("service", 9.8f);
		}};
		
		solution.updateCrispInputs(crispInputs);
		
		solution.newActivationRule("QxS", "quality", "service");
		solution.evalActivationOutput("QxS", "0x0", "or", 0, 0);
		solution.evalActivationOutput("QxS", "2x2", "or", 2, 2);
		

		if (toggleDraw) {
			hud = new HUD(applet, camera, 1280, 768);
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
		solution.defuzz("tip", "tipdefuzz", "centroid");
		crispOutputs.put("tip", solution.getDefuzzified("tipdefuzz"));
	}

	@Override
	public void systemUpdate() {
		
		solution.clearActivations();
		
		solution.evalActivationOutput("QxS", "0x0", "or", 0, 0);
		solution.evalActivationOutput("QxS", "2x2", "or", 2, 2);	
		
		solution.activate("QxS", "0x0", "tip", 0);
		solution.activate("QxS", "2x2", "tip", 2);
		solution.activate("service", 1, "tip", 1);
		
		solution.aggregate("tip");
	}

	@Override
	public void debug() {
		System.out.println(solution.getDefuzzified("tipdefuzz"));
	}

	@Override
	public void draw() {
		solution.draw();
		if (toggleDraw) {
			if(!toggleDrawMimimal) {
				HashMap<String, float[]> hash = solution.getFuzzifiedOutputs();
				hash.remove("tip");
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
		
		hud.registerFuzzyVariable("quality", functions.get("quality"));
		hud.registerFuzzyVariable("service", functions.get("service"));

	}

}
