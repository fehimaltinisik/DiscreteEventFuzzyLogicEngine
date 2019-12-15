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
	private HashMap<String, IfThenRule> activationRules = new HashMap<String, IfThenRule>();
	
	public void newFuzzyVariable(String name, float lowerBoundary, float upperBoundary, int precision, int numberOfFunctions) {
		FuzzyVariable fuzzyVariable = new FuzzyVariable();
		
		fuzzyVariable.generateMembershipFunctionsDomain(lowerBoundary, upperBoundary, precision);
		fuzzyVariable.generateMembershipFunctions(numberOfFunctions, "trimf");
		fuzzyVariable.setName(name);
		
		registerFuzzyVariable(name, fuzzyVariable);
	}
	
	public FuzzyVariable newFuzzyVariable(String name, float lowerBoundary, float upperBoundary, int precision) {
		FuzzyVariable fuzzyVariable = new FuzzyVariable();
		
		fuzzyVariable.generateMembershipFunctionsDomain(lowerBoundary, upperBoundary, precision);
		fuzzyVariable.setName(name);
		
		return fuzzyVariable;
	}
	
	public void registerFuzzyVariable(String key, FuzzyVariable variable) {
		fuzzyVariables.put(key, variable);		
	}

	public void newActivationRule(String name, String nameLeft, String nameRight) {
		
		FuzzyVariable left = fuzzyVariables.get(nameLeft);
		FuzzyVariable right = fuzzyVariables.get(nameRight);
		
		IfThenRule rule = new IfThenRule(left, right);
		
		registerActivationRule(name, rule);
	}
	
	public void registerActivationRule(String key, IfThenRule value) {
		activationRules.put(key, value);
	}

	public void evalActivationOutput(String ruleName, String outputName, String operation, int leftPosition, int rightPosition) {
		IfThenRule rule = activationRules.get(ruleName);
		
		if (operation == "or") {
			rule.or(outputName, leftPosition, rightPosition);
		}else if (operation == "and") {
			rule.and(outputName, leftPosition, rightPosition);
		}
	}
	
	public void activate(String ruleName, String ruleOutputName, String variableName, int membershipFunctionNumber) {
		IfThenRule rule = activationRules.get(ruleName);
		float ruleOutput = rule.getRuleOutput(ruleOutputName);
		
		FuzzyVariable outputVariable = fuzzyVariables.get(variableName);
		
		outputVariable.activation(ruleOutput, membershipFunctionNumber);
	}
	
	public void activate(String inputVariableName, int inputVariableMembershipFunction, String outputVariableName, int outputVariableMembershipFunctionNumber) {		
		FuzzyVariable inputVariable = fuzzyVariables.get(inputVariableName);
		FuzzyVariable outputVariable = fuzzyVariables.get(outputVariableName);
		
		float fuzzifiedInput = inputVariable.getFuzzifiedInputs()[inputVariableMembershipFunction];
		
		outputVariable.activation(fuzzifiedInput, outputVariableMembershipFunctionNumber);
	}
	

	public void clearActivations() {
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariableIterator = fuzzyVariables.entrySet().iterator();
		
		while(fuzzyVariableIterator.hasNext()) {
			Map.Entry<String, FuzzyVariable> fuzzyVariableEntry = fuzzyVariableIterator.next();
			fuzzyVariableEntry.getValue().clearActivations();
		}
	}
	
	public void aggregate(String name) {
		FuzzyVariable outputVariable = fuzzyVariables.get(name);
		outputVariable.aggregate();
	}
	
	public void defuzz(String name, String defuzzifiedOutputValueName, String method) {
		FuzzyVariable outputVariable = fuzzyVariables.get(name);
		float defuzzifiedOutputVariable = outputVariable.defuzz(method);
		defuzzifiedOutputs.put(defuzzifiedOutputValueName, defuzzifiedOutputVariable);
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
			Map.Entry<String, FuzzyVariable> fuzzyVariableEntry = fuzzyVariableIterator.next();
			printMembershipFunction(fuzzyVariableEntry.getKey());
		}
	}
	
	public void printMembershipFunction(String key) {
		FuzzyVariable fuzzyVariable = fuzzyVariables.get(key);
		fuzzyVariable.printMembershipFunctions();
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
			Map.Entry<String, FuzzyVariable> fuzzyVariableEntry = fuzzyVariableIterator.next();
			
			float [] n = new float[1 + fuzzyVariableEntry.getValue().numberOfMembershipFunctions()];
			
			n[0] = fuzzyVariableEntry.getValue().getCrispInput();
			
			System.arraycopy(fuzzyVariableEntry.getValue().getFuzzifiedInputs(), 0, n, 1, fuzzyVariableEntry.getValue().numberOfMembershipFunctions());
			
			fuzzifiedOutputs.put(fuzzyVariableEntry.getKey(), n);
		}
	}

	@Override
	public void setUpScene() {		
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariableIterator = fuzzyVariables.entrySet().iterator();

		while(fuzzyVariableIterator.hasNext()) {
			Map.Entry<String, FuzzyVariable> fuzzyVariableEntry = fuzzyVariableIterator.next();
			
			fuzzyVariableEntry.getValue().draw();
			List<float[]> functions = fuzzyVariableEntry.getValue().getGUIDependencies();
			
			variableDefinitions.put(fuzzyVariableEntry.getKey(), functions);
		}

	}

	@Override
	public HashMap<String, List<float []>> getSceneElements() {
		return variableDefinitions;
	}
	
	public void debug() {
		System.out.println("\t<FuzzySolution>\n");
				
		Iterator<Entry<String, FuzzyVariable>> fuzzyVariableIterator = fuzzyVariables.entrySet().iterator();
		Iterator<Entry<String, IfThenRule>> ifThenRuleIterator = activationRules.entrySet().iterator();
		Iterator<Entry<String, Float>> defuzzifiedOutputIterator = defuzzifiedOutputs.entrySet().iterator();

		while(fuzzyVariableIterator.hasNext()) {
			Map.Entry<String, FuzzyVariable> fuzzyVariableEntry = fuzzyVariableIterator.next();
			FuzzyVariable fuzzyVariable = fuzzyVariableEntry.getValue();
			fuzzyVariable.debug();
		}

		while(ifThenRuleIterator.hasNext()) {
			Map.Entry<String, IfThenRule> ifThenRuleEntry = ifThenRuleIterator.next();
			IfThenRule ifThenRule = ifThenRuleEntry.getValue();
			ifThenRule.debug();
		}
		
		System.out.println("\t\t<DefuzzifiedOutputs>");
		while(defuzzifiedOutputIterator.hasNext()) {
			Map.Entry<String, Float> defuzzifiedOutputEntry = defuzzifiedOutputIterator.next();
			System.out.printf(String.format("\t\t\tName: %s, Value: %.2f", defuzzifiedOutputEntry.getKey(), defuzzifiedOutputEntry.getValue()));
		}
		System.out.println("\n\t\t</DefuzzifiedOutputs>");
		
		System.out.println("\t</FuzzySolution>\n");
	}

	public FuzzyVariable getFuzzyVariable(String key) {
		return fuzzyVariables.get(key);
	}
	
}
