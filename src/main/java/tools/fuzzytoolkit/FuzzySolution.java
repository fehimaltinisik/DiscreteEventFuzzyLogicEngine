package main.java.tools.fuzzytoolkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.java.Drawable;

// FIXME: Add Functionality of Non Symmetrical Functions

public class FuzzySolution implements Drawable, SceneSetup{
	private HashMap<String, List<float[]>> variableDefinitions = new HashMap<String, List<float[]>>();
	private HashMap<String, float[]> fuzzifiedOutputs = new HashMap<String, float[]>();
	private HashMap<String, FuzzyVariable> fuzzyVariables = new HashMap<String, FuzzyVariable>();
	private HashMap<String, Float> defuzzifiedOutputs = new HashMap<String, Float>();
	private HashMap<String, ITRule> activationRules = new HashMap<String, ITRule>();
	
	public void newFuzzyVariable(String name, float lowerBoundary, float upperBoundary, int precision, int numberOfFunctions) {
		FuzzyVariable fuzzyVariable = new FuzzyVariable();
		
		fuzzyVariable.generateMembershipFunctionsDomain(lowerBoundary, upperBoundary, precision);
		fuzzyVariable.generateMembershipFunctions(numberOfFunctions, "trimf");
		
		registerFuzzyVariable(name, fuzzyVariable);
	}
	
	public FuzzyVariable newFuzzyVariable(String name, float lowerBoundary, float upperBoundary, int precision) {
		FuzzyVariable fuzzyVariable = new FuzzyVariable();
		
		fuzzyVariable.generateMembershipFunctionsDomain(lowerBoundary, upperBoundary, precision);
		
		return fuzzyVariable;
	}
	
	public void registerFuzzyVariable(String key, FuzzyVariable variable) {
		fuzzyVariables.put(key, variable);		
	}

	public void newActivationRule(String name, String nameLeft, String nameRight) {
		
		FuzzyVariable left = fuzzyVariables.get(nameLeft);
		FuzzyVariable right = fuzzyVariables.get(nameRight);
		
		ITRule rule = new ITRule(left, right);
		
		registerActivationRule(name, rule);
	}
	
	public void registerActivationRule(String key, ITRule value) {
		activationRules.put(key, value);
	}

	public void evalActivationOutput(String ruleName, String outputName, String operation, int leftPosition, int rightPosition) {
		ITRule rule = activationRules.get(ruleName);
		
		if (operation == "or") {
			rule.or(outputName, leftPosition, rightPosition);
		}else if (operation == "and") {
			rule.and(outputName, leftPosition, rightPosition);
		}
	}
	
	public void activate(String ruleName, String ruleOutputName, String variableName, int membershipFunctionNumber) {
		ITRule rule = activationRules.get(ruleName);
		float ruleOutput = rule.getRuleOutput(ruleOutputName);
		
		FuzzyVariable outputVariable = fuzzyVariables.get(variableName);
		
		outputVariable.activation(ruleOutput, membershipFunctionNumber);
	}
	
	public void activate(String inputVariableName, int inputVariableMembershipFunction, String outputVariableName, int outputVariableMembershipFunctionNumber) {		
		FuzzyVariable inputVariable = fuzzyVariables.get(inputVariableName);
		FuzzyVariable outputVariable = fuzzyVariables.get(outputVariableName);
		
		float crispOutput = inputVariable.getCrispOutputValues()[inputVariableMembershipFunction];
		
		outputVariable.activation(crispOutput, outputVariableMembershipFunctionNumber);
	}
	

	public void clearActivations() {
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariableIterator = fuzzyVariables.entrySet().iterator();
		
		while(fuzzyVariableIterator.hasNext()) {
			Map.Entry<String, FuzzyVariable> fuzzyVariable = fuzzyVariableIterator.next();
			fuzzyVariable.getValue().clearActivations();
		}
	}
	
	public void aggregate(String name) {
		FuzzyVariable outputVariable = fuzzyVariables.get(name);
		outputVariable.aggregate();
	}
	
	public void defuzz(String nameName, String defuzzName, String method) {
		FuzzyVariable outputVariable = fuzzyVariables.get(nameName);
		float crispOutput = outputVariable.defuzz(method);
		defuzzifiedOutputs.put(defuzzName, crispOutput);
	}
	
	public void updateCrispInput(Map.Entry<String, Float> crispInput) {
		FuzzyVariable variable = fuzzyVariables.get(crispInput.getKey());
		variable.updateCrispInputs(crispInput.getValue(), true);
	}
	
	public void updateCrispInputs(HashMap<String, Float> updatedCrispInputs) {
		Iterator<Entry<String, Float>> updatedCrispInputsIterator = updatedCrispInputs.entrySet().iterator();
		
		while(updatedCrispInputsIterator.hasNext()) {
			Map.Entry<String, Float> crispInput = updatedCrispInputsIterator.next();
			updateCrispInput(crispInput);
		}
	}
	
	public void printMembershipFunctions() {
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariableIterator = fuzzyVariables.entrySet().iterator();
		
		while(fuzzyVariableIterator.hasNext()) {
			Map.Entry<String, FuzzyVariable> fuzzyVariable = fuzzyVariableIterator.next();
			printMembershipFunction(fuzzyVariable.getKey());
		}
	}
	
	public void printMembershipFunction(String key) {
		FuzzyVariable fuzzyVariable = fuzzyVariables.get(key);
		fuzzyVariable.print();
	}
	
	public float getDefuzzified(String name) {
		return defuzzifiedOutputs.get(name);
	}
	
	public HashMap<String, float[]> getFuzzifiedOutputs() {	
		return fuzzifiedOutputs;
	}
	
	@Override
	public void draw() {
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariableIterator = fuzzyVariables.entrySet().iterator();

		while(fuzzyVariableIterator.hasNext()) {
			Map.Entry<String, FuzzyVariable> fuzzyVariable = fuzzyVariableIterator.next();
			
			float [] n = new float[1 + fuzzyVariable.getValue().numberOfMembershipFunctions()];
			
			n[0] = fuzzyVariable.getValue().getCrisp();
			
			System.arraycopy(fuzzyVariable.getValue().getCrispOutputValues(), 0, n, 1, fuzzyVariable.getValue().numberOfMembershipFunctions());
			
			fuzzifiedOutputs.put(fuzzyVariable.getKey(), n);
		}
	}

	@Override
	public void setUpScene() {		
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariableIterator = fuzzyVariables.entrySet().iterator();

		while(fuzzyVariableIterator.hasNext()) {
			Map.Entry<String, FuzzyVariable> fuzzyVariable = fuzzyVariableIterator.next();
			
			fuzzyVariable.getValue().draw();
			List<float[]> functions = fuzzyVariable.getValue().getGUIDependencies();
			
			variableDefinitions.put(fuzzyVariable.getKey(), functions);
		}

	}

	@Override
	public HashMap<String, List<float []>> getSceneElements() {
		return variableDefinitions;
	}
	
}
