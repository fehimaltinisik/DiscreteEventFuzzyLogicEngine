package main.java.tools.fuzzytoolkit;

import java.util.HashMap;
import java.util.List;

public interface SceneSetup {
	public void setUpScene();
	
	public HashMap<String, List<float []>> getSceneElements();
}
