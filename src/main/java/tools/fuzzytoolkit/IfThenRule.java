package main.java.tools.fuzzytoolkit;

import java.util.HashMap;

public class IfThenRule {
	
	FuzzyVariable left;
	FuzzyVariable right;
	 
	private HashMap<String, Float> ruleOutputs = new HashMap<String, Float>();
	
	public IfThenRule(FuzzyVariable left, FuzzyVariable right) {
		this.left = left;
		this.right = right;
	}
	
	public void and(String name, int leftPosition, int rightPosition) {
		ruleOutputs.put(name, FuzzyMath.fmin(left.getFuzzifiedInputs()[leftPosition], right.getFuzzifiedInputs()[rightPosition]));
	}
	
	public void or(String name, int leftPosition, int rightPosition) {
		ruleOutputs.put(name, FuzzyMath.fmax(left.getFuzzifiedInputs()[leftPosition], right.getFuzzifiedInputs()[rightPosition]));
	}
	
	public float getRuleOutput(String name) {
		return ruleOutputs.get(name);
	}

	public void debug() {
		System.out.printf(String.format("\t\t<IfThenRule: %s o %s>\n", left.getName(), right.getName()));
		
		System.out.printf(String.format("\t\t</IfThenRule>\n"));		
	}
}