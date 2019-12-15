package main.java.tools.fuzzytoolkit;

import java.util.HashMap;

import main.java.Drawable;
import main.java.HUD;
import peasy.PeasyCam;
import processing.core.PApplet;

public abstract class FuzzyControlSystem implements Drawable{
	
	protected HUD hud;
	protected PApplet applet;
	protected PeasyCam camera;
	protected FuzzySolution solution;
	
	protected boolean toggleDraw = false;
	protected boolean toggleDrawMimimal = false;
	protected String observerName;
	
	public FuzzyControlSystem(PApplet applet, boolean toggleDraw, boolean toggleDrawMinimal) {
		this.applet = applet;
		this.toggleDraw = toggleDraw;
		this.toggleDrawMimimal = toggleDrawMinimal;
	}
	
	public abstract void systemUpdate();
	
	public abstract void solutionFactory();
	
	public abstract void registerCrispInputs(HashMap<String, Float> crispInputs);
	
	public abstract void evaluateCrispOutputs();
	
	public abstract void debug();
	
	public abstract void guiStateUpdate();
	
	public void toggleDrawing() { 
		toggleDraw = !toggleDraw;
		
		if (!toggleDraw) {
			hud = null;
		}else {
			guiStateUpdate();
			System.out.println(String.format("Now Observing: %s", observerName));
		}
		// guiStateUpdate();
	}
	
	public void toggleDrawingMinimal() { 
		toggleDrawMimimal = !toggleDrawMimimal;
		if (toggleDraw) {
			if (hud == null) {
				hud = new HUD(applet, camera, 1280, 768);
			}
		}
		
		// guiStateUpdate();
	}
	
	public void setCamera(PeasyCam camera) { this.camera = camera;}
	
	public String getObserverName() {
		return observerName;
	}

	public void setObserverName(String observerName) {
		this.observerName = observerName;
	}

	
	
}

//FuzzySolution solution = new FuzzySolution();
//
//solution.newFuzzyVariable("quality", 0, 10, 10, 3);
//solution.newFuzzyVariable("service", 0, 10, 10, 3);
//solution.newFuzzyVariable("tip", 0, 25, 25, 3);
//
//HashMap<String, Float> crispInputs = new HashMap<String, Float>(){/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//{
//	put("quality", 6.5f);
//	put("service", 9.8f);
//}};
//
//solution.updateCrispInputs(crispInputs);
//
//solution.newActivationRule("QxS", "quality", "service");
//solution.evalActivationOutput("QxS", "0x0", "or", 0, 0);
//solution.evalActivationOutput("QxS", "2x2", "or", 2, 2);
//
//solution.activate("QxS", "0x0", "tip", 0);
//solution.activate("QxS", "2x2", "tip", 2);
//solution.activate("service", 1, "tip", 1);
//
//solution.aggregate("tip");
//solution.defuzz("tip", "tipdefuzz", "centroid");
//
//System.out.println(solution.getDefuzzified("tipdefuzz"));
