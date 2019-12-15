package main.java.tools.fuzzytoolkit.solutions;

import com.sun.javafx.binding.StringFormatter;

import processing.core.PApplet;

public class DrivingControllerFactory {
	
	private PApplet applet;
	
	private boolean toggleDraw = false;
	private boolean toggleMinimalDraw = false;
	
	public DrivingControllerFactory(PApplet applet) {
		this.applet = applet;
	}
	
	public DrivingController newDrivingController(String controllerSelection) {
		if (controllerSelection == "Basic") {
			return new BasicDriving(applet, toggleDraw, toggleMinimalDraw);
		}else if(controllerSelection == "Improved") {
			return new ImprovedDriving(applet, toggleDraw, toggleMinimalDraw);
		}else if(controllerSelection == "Advanced") {
			return new AdvancedDriving(applet, toggleDraw, toggleMinimalDraw);
		}else {
			throw new IllegalArgumentException(String.format("Illegal argumnet: %s. Choose either %s, %s", controllerSelection, "Basic", "Improved"));
		}
	}
	
	public void setToggleDraw(boolean toggleDraw) {
		this.toggleDraw = toggleDraw;
	}
	public void setToggleMinimalDraw(boolean toggleMinimalDraw) {
		this.toggleMinimalDraw = toggleMinimalDraw;
	}
}
