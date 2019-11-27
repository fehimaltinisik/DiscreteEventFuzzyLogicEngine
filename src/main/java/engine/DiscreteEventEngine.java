package main.java.engine;

import java.util.ArrayList;
import java.util.List;

import main.java.app.agents.Agent;

public class DiscreteEventEngine extends Engine{
	
	List<Agent> elements = new ArrayList<Agent>();

	@Override
	public void step() {
		// TODO Auto-generated method stub
		for (Agent element: elements) {
			element.operate();
			element.update();
			element.draw();
		}
	}

	public void registerElements(List<Agent> elements) {
		this.elements.addAll(elements);
		
	}
	
}
