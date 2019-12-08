package main.java.app.agents.options;

import java.util.List;

import main.java.app.agents.Agent;

public interface Sensors {
	public void registerTrafficInformation(List<Agent> agents);
}
