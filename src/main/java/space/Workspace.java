package main.java.space;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jogamp.newt.event.KeyEvent;

import javafx.scene.input.MouseButton;
import main.java.app.agents.AdvancedFuzzyDrive;
import main.java.app.agents.Agent;
import main.java.app.agents.Car;
import main.java.app.agents.FuzzyDrive;
import main.java.space.items.Asset;
import processing.core.PApplet;

// TODO: implement inheritable singleton class.

public abstract class Workspace implements Environment{
	PApplet applet;
	
	protected HashMap<String, Asset> items = new HashMap<String, Asset>();
	protected List<Agent> agents = new ArrayList<Agent>();

	protected List<Agent> observables= new ArrayList<Agent>();
	protected Agent observed = null;
	
	protected final int scale = 20;
	protected int cols, rows, width, height;
	
	protected float[][] terrain;
	
	public int lastKeyHit = 0;
	
	public Workspace(PApplet applet) {
		this.applet = applet;
	}
	
	public Workspace() {}
	
	public void simulate() {
		controls();
		radar();
		for (Agent agent: agents) {
			agent.operate();
			agent.update();
			agent.draw();
		}
	}
	
	public abstract void radar();
	
	public void registerAgents(List<Agent> agents) {
		this.agents.addAll(agents);
		
		for (Agent agent: agents) {
			if(((Car)agent).getObsevable()) {
				observables.add(agent);
				System.out.println(((Car)agent).getName());
			}
			
			if(((Car)agent).getNowObserving()) {
				observed = agent;
			}
		}
		
		System.out.println(String.format("Number of observables: %s", observables.size()));
		
	}
	public void registerItem(String key, Asset item) {
		items.put(key, item);
	}
	
	private void observeNext() {
		
		for(int i = 0; i < observables.size(); i++) {
			Car car = ((Car)observables.get(i % observables.size())); 
			
			if(car.getNowObserving()) {
				
				if(car instanceof AdvancedFuzzyDrive) {
					((AdvancedFuzzyDrive)observables.get(i % observables.size())).toggleObserving();
					Car nextCar = (Car)agents.get((i + 1) % observables.size());
					
					if (nextCar instanceof AdvancedFuzzyDrive) {
						((AdvancedFuzzyDrive)nextCar).toggleObserving();
					}else {
						((FuzzyDrive)nextCar).toggleObserving();
					}
					
					break;
				}else {
					((FuzzyDrive)observables.get(i % observables.size())).toggleObserving();
					Car nextCar = (Car)agents.get((i + 1) % observables.size());
					
					if (nextCar instanceof AdvancedFuzzyDrive) {
						((AdvancedFuzzyDrive)nextCar).toggleObserving();
					}else {
						((FuzzyDrive)nextCar).toggleObserving();
					}
					
					break;
				}
				
			}
		}
		// System.out.println(String.format("Observing: %s", ((Car)agents.get((i + 1) % observables.size() )).getName()));
	}
	
	public boolean registerKey() {
		if (applet.keyPressed) {
			lastKeyHit = applet.keyCode;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}else {
			return false;	
		}
		
	}
	
	public void controls() {
		
		if(registerKey()) {
			if (lastKeyHit == PApplet.UP) {
				System.out.println("up");
			}else if (lastKeyHit == PApplet.DOWN) {
				System.out.println("down");
			}else if (lastKeyHit == PApplet.RIGHT) {
				System.out.println("right");
				observeNext();
			}else if (lastKeyHit == PApplet.LEFT) {
				System.out.println("left");
			}
		}else {
			lastKeyHit = 0;
		}
		
	}
	
	public abstract float [][] terrainFactory();
	public abstract float [][] assetsFactory();
	
	public void drawGuideLines() {
		applet.fill(255);
		applet.stroke(255);
		applet.line(-100, 0, 0, 100, 0, 0);
		applet.text("X", 90, 0, 0);

		// applet.stroke(255);
		applet.line(0, -100, 0, 0, 100, 0);
		applet.text("Y", 0, 90, 0);

		// applet.stroke(255);
		applet.line(0, 0, -100, 0, 0, 100);
		applet.text("Z", 0, 0, 90);
	}
	

	public void setTerrainResolution(int width, int heigth) {
		this.width = width;
		this.height = heigth;
		cols = width / scale;
		rows = height / scale;
	}

	public void setPApplet(PApplet applet) { this.applet = applet; }
}
