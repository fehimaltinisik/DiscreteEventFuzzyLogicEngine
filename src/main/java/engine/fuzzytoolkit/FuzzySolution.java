package main.java.engine.fuzzytoolkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FuzzySolution {
	private HashMap<String, FuzzyVariable> fuzzyVariables = new HashMap<String, FuzzyVariable>();
	private HashMap<String, Float> defuzzifiedOutputs = new HashMap<String, Float>();
	private HashMap<String, ITRule> activationRules = new HashMap<String, ITRule>();
	
	public void newFuzzyVariable(String name, float lowerBoundary, float upperBoundary, int precision, int numberOfFunctions) {
		FuzzyVariable fuzzyInputVariable = new FuzzyVariable();
		
		fuzzyInputVariable.generateMembershipFunctionsDomain(lowerBoundary, upperBoundary, precision);
		fuzzyInputVariable.generateMembershipFunctions(numberOfFunctions, "trimf");
		
		registerFuzzyVariable(name, fuzzyInputVariable);
	}
	
	public void registerFuzzyVariable(String key, FuzzyVariable value) {
		fuzzyVariables.put(key, value);
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
	
	public void aggregate(String name) {
		FuzzyVariable outputVariable = fuzzyVariables.get(name);
		outputVariable.aggregate();
	}
	
	public void defuzz(String nameName, String defuzzName, String method) {
		FuzzyVariable outputVariable = fuzzyVariables.get(nameName);
		defuzzifiedOutputs.put(defuzzName, outputVariable.defuzz(method));
	}
	
	public void updateCrispInput(Map.Entry<String, Float> crispInput) {
		FuzzyVariable variable = fuzzyVariables.get(crispInput.getKey());
		variable.updateCrispInputs(crispInput.getValue(), true);
	}
	
	public void updateCrispInputs(HashMap<String, Float> updatedCrispInputs) {
		Iterator<Entry<String, Float>> updatedCrispInputsIterator = updatedCrispInputs.entrySet().iterator();
		
		while(updatedCrispInputsIterator.hasNext()) {
			Map.Entry<String, Float> crispInput = (Map.Entry<String, Float>)updatedCrispInputsIterator.next();
			updateCrispInput(crispInput);
		}
	}
	
	public void printMembershipFunctions() {
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariableIterator = fuzzyVariables.entrySet().iterator();
		
		while(fuzzyVariableIterator.hasNext()) {
			Map.Entry<String, FuzzyVariable> fuzzyVariable = (Map.Entry<String, FuzzyVariable>)fuzzyVariableIterator.next();
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

	
}
