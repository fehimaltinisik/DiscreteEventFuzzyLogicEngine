package main.java.tools.fuzzytoolkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.java.Drawable;

public class FuzzyVariable implements Drawable{
	
	private float crispInput;
	private float crispOutput;
	private float range;
	private float lowerBound;
	private float upperBound;
	
	private String name = "";
	
	private float[]	domain;
	private float[] aggregated;
	private float[] fuzzifiedInputs;
	
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
		fuzzifiedInputs = new float[membershipFunctions.size()];
		Arrays.fill(fuzzifiedInputs, 0);
	}
	
	public void updateCrispInputs(float crisp, boolean zeroOutsideX) {
		this.crispInput = crisp;
		
		for (int i = 0; i < membershipFunctions.size() && i < fuzzifiedInputs.length; i++) {
			float interp = FuzzyOperations.interpolateMembership(domain, membershipFunctions.get(i), crisp, zeroOutsideX);
			fuzzifiedInputs[i] =  Math.max(0, interp);
		}
	}
	
	public float defuzz(String method) {
		crispOutput  = FuzzyOperations.defuzz(domain, aggregated, method);
		return crispOutput;
	}

	public void printMembershipFunctions() {
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
	}
	
	public float[] getMembershipFunction(int index) {
		return membershipFunctions.get(index);
	}
	
	public float[] getFuzzifiedInputs() {
		return fuzzifiedInputs;
	}

	public List<float[]> getGUIDependencies() {
		return guiDependencies;
	}
	
	public float getCrisp() {
		return crispInput;
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void debug() {
		System.out.printf(String.format("\t\t<FuzzyVariable: %s>\n", name));
		
		System.out.printf(String.format("\t\t\tCrisp Input: %.2f, Fuzzified Inputs: %s\n", crispInput, Arrays.toString(fuzzifiedInputs)));
		for (float[] activation: activations) {
			System.out.printf(String.format("\t\t\tActivation: %s\n", Arrays.toString(activation)));
		}
		System.out.printf(String.format("\t\t\tDomain: %s\n", Arrays.toString(domain)));
		for (float[] membership: membershipFunctions) {
			System.out.printf(String.format("\t\t\tMembership: %s\n", Arrays.toString(membership)));
		}
		System.out.printf(String.format("\t\t\tAggregated: %s\n", Arrays.toString(aggregated)));
		System.out.printf(String.format("\t\t\tCrisp Output: %.2f\n", crispOutput));
		System.out.printf(String.format("\t\t\tDomain : (%.2f, %.2f), Range : %.2f\n", lowerBound, upperBound, range));
	
		System.out.printf(String.format("\t\t</FuzzyVariable: %s>\n", name));
	}

	public float[] getDomain() {
		return domain;
	}

	public float[] getAggregated() {
		return aggregated;
	}
}