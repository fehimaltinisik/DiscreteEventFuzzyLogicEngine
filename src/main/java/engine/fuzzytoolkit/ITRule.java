package main.java.engine.fuzzytoolkit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ITRule {
	
	FuzzyVariable left;
	FuzzyVariable right;
	 
	private HashMap<String, Float> ruleOutputs = new HashMap<String, Float>();
	
	public ITRule(FuzzyVariable left, FuzzyVariable right) {
		this.left = left;
		this.right = right;
	}
	
	public void and(String name, int leftPosition, int rightPosition) {
		ruleOutputs.put(name, FuzzyMath.fmin(left.getCrispOutputValues()[leftPosition], right.getCrispOutputValues()[rightPosition]));
	}
	
	public void or(String name, int leftPosition, int rightPosition) {
		ruleOutputs.put(name, FuzzyMath.fmax(left.getCrispOutputValues()[leftPosition], right.getCrispOutputValues()[rightPosition]));
	}
	
	public float getRuleOutput(String name) {
		return ruleOutputs.get(name);
	}
}