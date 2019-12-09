package main.java.tools.fuzzytoolkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.java.Drawable;

public class FuzzyVariable implements Drawable{
	private float crisp;
	private float range;
	private float lowerBound;
	private float upperBound;
	
	private float[]	domain;
	private float[] aggregated;
	private float[] crispOutputValues;
	
	private List<float []> guiDependencies = new ArrayList<float []>();
	private List<float []> activations = new ArrayList<float []>();
	private List<float []> membershipFunctions = new ArrayList<float []>();
	
	public FuzzyVariable() {}

	public void generateMembershipFunctionsDomain(float lowerBoundary, float upperBoundary, int precision) {
		this.lowerBound = lowerBoundary;
		this.upperBound = upperBoundary;
		
		range = upperBoundary - lowerBoundary;
		domain = FuzzyMath.linspace(lowerBoundary, upperBoundary, precision);
	}
	
	public void addMembershipFunction(String functionsType, float ... args) {
		if (functionsType == "trimf") {
			if (args.length == 3) {
				membershipFunctions.add(Membership.trimf(domain, args[0], args[1], args[2]));
			}else {
				throw new IllegalArgumentException("Membership function 'trimf' requires exactly 3 arguments");
			}	
		}else if(functionsType == "trapmf") {
			if (args.length == 4) {
				membershipFunctions.add(Membership.trapmf(domain, args[0], args[1], args[2], args[3]));
			}else {
				throw new IllegalArgumentException("Membership function 'trapmf' requires exactly 4 arguments");
			}
		}else if(functionsType == "gaussianmf") {
			if (args.length == 2) {
				membershipFunctions.add(Membership.gaussianmf(domain, args[0], args[1]));
			}else {
				throw new IllegalArgumentException("Membership function 'gaussianmf' requires exactly 2 arguments");
			}
		}else {
			throw new IllegalArgumentException(String.format("Unrecognized Membership Function: %s. Provide either 'trimf', 'trapmf' or 'gaussianmf'", functionsType));
		}
	}
	
	public void generateMembershipFunctions(int numberOfFunctions, String functionsType) {
		// TODO : Use Factory Pattern Here
		// 0 0 5, 0 5 10, 5 10 15, 10 15 15;

		int i = 0;
		float step = range / (numberOfFunctions - 1);
		
		membershipFunctions.add(Membership.trimf(domain, lowerBound, lowerBound, lowerBound + step));		
		for (; i < numberOfFunctions - 2; i++) {
			membershipFunctions.add(Membership.trimf(domain, lowerBound + i * step, lowerBound +  i * step + step, lowerBound +  i * step + 2 * step));
			
		}
		membershipFunctions.add(Membership.trimf(domain,lowerBound +  i * step, lowerBound + i * step + step, lowerBound +  i * step + step));
		
		initFuzzyVariableDependencies();
	}
	
	public void initFuzzyVariableDependencies() {
		crispOutputValues = new float[membershipFunctions.size()];
		Arrays.fill(crispOutputValues, 0);
	}
	
	public void updateCrispInputs(float crisp, boolean zeroOutsideX) {
		this.crisp = crisp;
		
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
	
	public void clearActivations() {
		activations.clear();
	}
	
	public void activation(float clip, int functionPosition) {
		 activations.add(FuzzyMath.fmin(clip, membershipFunctions.get(functionPosition)));
	}
	
	public void aggregate() {
		
		aggregated = new float[activations.get(0).length];
		
		Arrays.fill(aggregated, 0);
		
		for (int i = activations.size() - 1; i >= 0; i--) {			
			System.arraycopy(FuzzyMath.fmax(activations.get(i), aggregated), 0, aggregated, 0, aggregated.length);
		}
		
		// System.out.println(Arrays.toString(aggregated));
	}
	
	public float[] getMembershipFunction(int index) {
		return membershipFunctions.get(index);
	}
	
	public float[] getCrispOutputValues() {
		return crispOutputValues;
	}

	public List<float[]> getGUIDependencies() {
		return guiDependencies;
	}
	
	public float getCrisp() {
		return crisp;
	}
	
	@Override
	public void draw() {
		guiDependencies.clear();
		guiDependencies.addAll(membershipFunctions);
		guiDependencies.add(0, domain);
	}

	public int numberOfMembershipFunctions() {
		return membershipFunctions.size();
	}
}