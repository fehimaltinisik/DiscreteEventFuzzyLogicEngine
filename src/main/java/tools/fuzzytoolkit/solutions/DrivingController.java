package main.java.tools.fuzzytoolkit.solutions;

import java.util.HashMap;
import java.util.List;

import main.java.HUD;
import main.java.tools.fuzzytoolkit.FuzzyControlSystem;
import main.java.tools.fuzzytoolkit.FuzzySolution;
import main.java.tools.fuzzytoolkit.FuzzyVariable;
import processing.core.PApplet;

public abstract class DrivingController extends FuzzyControlSystem{

	HashMap<String, Float> crispInputs = new HashMap<String, Float>();
	HashMap<String, Float> crispOutputs = new HashMap<String, Float>();
		
	public DrivingController(PApplet applet, boolean toggleDraw, boolean toggleDrawMinimal) {
		super(applet, toggleDraw, toggleDrawMinimal);
	}

	@Override
	public abstract void solutionFactory();

	@Override
	public void registerCrispInputs(HashMap<String, Float> crispInputs) {
		solution.updateCrispInputs(crispInputs);
		crispInputs.putAll(crispInputs);
		
	}

	@Override
	public abstract void evaluateCrispOutputs();

	@Override
	public abstract void systemUpdate();

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
	public abstract void guiStateUpdate();
	
	public Float getCrispOutputs(String key) {
		return crispOutputs.get(key);
	}

}
