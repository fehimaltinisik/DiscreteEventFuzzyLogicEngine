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
	
	public FuzzyControlSystem(PApplet applet, boolean toggleDraw, boolean toggleDrawMinimal) {
		this.applet = applet;
		this.toggleDraw = toggleDraw;
		this.toggleDrawMimimal = toggleDrawMinimal;
	}
	
	public abstract void systemUpdate();
	
	public abstract void solutionFactory();
	
	public abstract void registerCrispInputs(HashMap<String, Float> crispInputs);
	
	public abstract void evaluateCrispInputs();
	
	public abstract void debug();
	
	public abstract void guiStateUpdate();
	
	public void toggleDrawing() { 
		toggleDraw = !toggleDraw;
		
		if (toggleDraw) {
			if (hud == null) {
				hud = new HUD(applet, camera, 1280, 768);
			}
		}
		
		guiStateUpdate();
	}
	
	public void toggleDrawingMinimal() { 
		toggleDrawMimimal = !toggleDrawMimimal;
		if (toggleDraw) {
			if (hud == null) {
				hud = new HUD(applet, camera, 1280, 768);
			}
		}
		
		guiStateUpdate();
	}
	
	public void setCamera(PeasyCam camera) { this.camera = camera;}
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


//float qual_crisp = 6.5f;
//float serv_crisp = 9.8f;
//
//float[] x_qual = FuzzyMath.linspace(0, 10, 10);
//float[] x_serv = FuzzyMath.linspace(0, 10, 10);
//float[] x_tip = FuzzyMath.linspace(0, 25, 25);
//
//float [] qual_lo = Membership.trimf(x_qual, 0, 0, 5);
//float [] qual_md = Membership.trimf(x_qual, 0, 5, 10);
//float [] qual_hi = Membership.trimf(x_qual, 5, 10, 10);
//
//float [] serv_lo = Membership.trimf(x_serv, 0, 0, 5);
//float [] serv_md = Membership.trimf(x_serv, 0, 5, 10);
//float [] serv_hi = Membership.trimf(x_serv, 5, 10, 10);
//		
//float [] tip_lo = Membership.trimf(x_tip, 0, 0, 13);
//float [] tip_md = Membership.trimf(x_tip, 0, 13, 25);
//float [] tip_hi = Membership.trimf(x_tip, 13, 25, 25);
//				
//float qual_level_lo = FuzzyOperations.interpolateMembership(x_qual, qual_lo, qual_crisp, true);
//float qual_level_md = FuzzyOperations.interpolateMembership(x_qual, qual_md, qual_crisp, true);
//float qual_level_hi = FuzzyOperations.interpolateMembership(x_qual, qual_hi, qual_crisp, true);
//
//float serv_level_lo = FuzzyOperations.interpolateMembership(x_serv, serv_lo, serv_crisp, true);
//float serv_level_md = FuzzyOperations.interpolateMembership(x_serv, serv_md, serv_crisp, true);
//float serv_level_hi = FuzzyOperations.interpolateMembership(x_serv, serv_hi, serv_crisp, true);
//
//float active_rule1 = FuzzyMath.fmax(qual_level_lo, serv_level_lo);
//
//float[] tip_activation_lo = FuzzyMath.fmin(active_rule1, tip_lo);
//float[] tip_activation_md = FuzzyMath.fmin(serv_level_md, tip_md);
//
//float active_rule3 = FuzzyMath.fmax(qual_level_hi, serv_level_hi);
//
//float[] tip_activation_hi = FuzzyMath.fmin(active_rule3, tip_hi);
//
//float[] aggregated = FuzzyMath.fmax(tip_activation_lo, FuzzyMath.fmax(tip_activation_md, tip_activation_hi));
//
//float tip = FuzzyOperations.defuzz(x_tip, aggregated, "centroid");
