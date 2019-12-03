package main.java.engine.fuzzytoolkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FuzzyVariable{
	private float range;
	private float lowerBound;
	private float upperBound;
	
	private float[]	domain;
	private float[] aggregated;
	private float[] crispOutputValues;
	
	private List<float []> activations = new ArrayList<float []>();
	private List<float []> membershipFunctions = new ArrayList<float []>();
	
	public FuzzyVariable() {}

	public void generateMembershipFunctionsDomain(float lowerBoundary, float upperBoundary, int precision) {
		range = upperBoundary - lowerBoundary;
		domain = FuzzyMath.linspace(lowerBoundary, upperBoundary, precision);
	}
	
	public void generateMembershipFunctions(int numberOfFunctions, String functionsType) {
		// TODO : Use Factory Pattern Here
		// 0 0 5, 0 5 10, 5 10 15, 10 15 15;

		int i = 0;
		float step = range / (numberOfFunctions - 1);
		
		membershipFunctions.add(Membership.trimf(domain, 0, 0, step));		
		for (i = 0; i < numberOfFunctions - 1; i++) {
			membershipFunctions.add(Membership.trimf(domain, i * step, i * step + step, i * step + 2 * step));
			
		}
		membershipFunctions.add(Membership.trimf(domain, i * step, i * step + step, i * step + step));
		
		initFuzzyVariableDependencies();
	}
	
	public void initFuzzyVariableDependencies() {
		crispOutputValues = new float[membershipFunctions.size()];
		Arrays.fill(crispOutputValues, 0);
	}
	
	public void updateCrispInputs(float crisp, boolean zeroOutsideX) {
		for (int i = 0; i < membershipFunctions.size() && i < crispOutputValues.length; i++) {
			crispOutputValues[i] = FuzzyOperations.interpolateMembership(domain, membershipFunctions.get(i), crisp, zeroOutsideX);
		}
	}
	
	public float defuzz(String method) {
		return FuzzyOperations.defuzz(domain, aggregated, method);
	}

	public void print() {
		System.out.printf(String.format("Domain : (%.2f, %.2f), Range : %.2f\n", lowerBound, upperBound, range));
		System.out.printf(String.format("Domain : %s\n", Arrays.toString(domain)));
		
		for (float[] func: membershipFunctions) {
			System.out.printf(String.format("Memebership function : %s\n", Arrays.toString(func)));
		}
	}
	
	public void aggregate() {
		
		aggregated = new float[activations.get(0).length];
		
		Arrays.fill(aggregated, 0);
		
		for (int i = activations.size() - 1; i >= 0; i--) {
			System.arraycopy(FuzzyMath.fmax(activations.get(i), aggregated), 0, aggregated, 0, aggregated.length);
		}		
	}
	
	
	
	public void activation(float clip, int functionPosition) {
		 activations.add(FuzzyMath.fmin(clip, membershipFunctions.get(functionPosition)));
	}
	
	public float[] getMembershipFunction(int index) {
		return membershipFunctions.get(index);
	}
	
	public float[] getCrispOutputValues() {
		return crispOutputValues;
	}
}