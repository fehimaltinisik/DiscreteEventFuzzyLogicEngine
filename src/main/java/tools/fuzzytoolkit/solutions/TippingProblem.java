package main.java.tools.fuzzytoolkit.solutions;

import java.util.HashMap;
import java.util.List;

import main.java.HUD;
import main.java.tools.fuzzytoolkit.FuzzyControlSystem;
import main.java.tools.fuzzytoolkit.FuzzySolution;
import processing.core.PApplet;

public class TippingProblem extends FuzzyControlSystem{

	HashMap<String, Float> crispInputs = new HashMap<String, Float>();
	HashMap<String, Float> crispOutputs = new HashMap<String, Float>();
		
	public TippingProblem(PApplet applet, boolean toggleDraw, boolean toggleDrawMinimal) {
		super(applet, toggleDraw, toggleDrawMinimal);
	}

	@Override
	public void solutionFactory() {
		
		solution = new FuzzySolution();
		
		solution.newFuzzyVariable("quality", 0, 10, 10, 3);
		solution.newFuzzyVariable("service", 0, 10, 10, 3);
		solution.newFuzzyVariable("tip", 0, 25, 75, 3);
		
		HashMap<String, Float> crispInputs = new HashMap<String, Float>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		
		{
			put("quality", 16.5f);
			put("service", 19.8f);
		}};
		
		solution.updateCrispInputs(crispInputs);
		
		solution.newActivationRule("QxS", "quality", "service");
		solution.evalActivationOutput("QxS", "0x0", "or", 0, 0);
		solution.evalActivationOutput("QxS", "2x2", "or", 2, 2);
		// solution.evalActivationOutput("QxS", "3x3", "or", 3, 3);
		// solution.evalActivationOutput("QxS", "4x4", "or", 4, 4);
		

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
		solution.defuzz("tip", "tipdefuzz", "centroid");
		crispOutputs.put("tip", solution.getDefuzzified("tipdefuzz"));
	}

	@Override
	public void systemUpdate() {
		
		solution.clearActivations();
		
		solution.evalActivationOutput("QxS", "0x0", "or", 0, 0);
		solution.evalActivationOutput("QxS", "2x2", "or", 2, 2);	
		// solution.evalActivationOutput("QxS", "3x3", "or", 3, 3);	
		// solution.evalActivationOutput("QxS", "4x4", "or", 4, 4);	
		
		solution.activate("QxS", "0x0", "tip", 0);
		solution.activate("QxS", "2x2", "tip", 2);
		// solution.activate("QxS", "3x3", "tip", 3);
		// solution.activate("QxS", "4x4", "tip", 4);
		solution.activate("service", 1, "tip", 1);
		
		solution.aggregate("tip");
	}

	@Override
	public void debug() {
		System.out.println("<ControlSystemDebug>\n");
		
		solution.debug();
				
		System.out.println("</ControlSystemDebug>\n");	}

	@Override
	public void draw() {
		solution.draw();
		if (toggleDraw) {
			if(!toggleDrawMimimal) {
				HashMap<String, float[]> hash = solution.getFuzzifiedOutputs();
				hash.remove("tip");
				// hud.drawDiscreteFunctions("all");
			}else {
			}
		}
	}

	@Override
	public void guiStateUpdate() {
		
		solution.setUpScene();
		HashMap<String, List<float[]>> functions = solution.getSceneElements();
		
		// hud.registerFuzzyInputVariable("quality", functions.get("quality"));
		// hud.registerFuzzyInputVariable("service", functions.get("service"));

	}

}

//tippingProblem = new TippingProblem(this, true, false);
//tippingProblem.setCamera(camera);
//
//tippingProblem.solutionFactory();
//tippingProblem.registerCrispInputs(new HashMap<String, Float>() {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	{
//		put("quality", f1);
//		put("service", f2);
//	}
//});
//
//tippingProblem.systemUpdate();
//
//tippingProblem.evaluateCrispInputs();
//		
//tippingProblem.draw();
