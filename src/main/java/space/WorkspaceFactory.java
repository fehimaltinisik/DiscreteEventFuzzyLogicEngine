package main.java.space;

public class WorkspaceFactory {
	
	Workspace workspace;
	
	public Workspace getWarehouse() {
		return new Warehouse();
	}
	
	public Workspace getDesert() {
		return new Desert();
	}
	
	public Workspace getWorkspace(String workspaceSelection) throws IllegalArgumentException{
		
		if (workspaceSelection == "warehouse") {
			workspace = getWarehouse();
		}else if (workspaceSelection == "desert") {
			workspace = getDesert(); 
		}else {
			// TODO : State Problem
			throw new IllegalArgumentException();
		}
				
		return workspace;
	}
}
