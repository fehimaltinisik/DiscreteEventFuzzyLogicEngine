package main.java.space;

public class WorkspaceFactory {
	
	Workspace workspace;
	
	private Workspace getWarehouse() {
		return new Warehouse();
	}
	
	private Workspace getDesert() {
		return new Desert();
	}
	
	private Workspace getStreet() {
		return new Street();
	}
	
	public Workspace getWorkspace(String workspaceSelection) throws IllegalArgumentException{
		
		if (workspaceSelection == "warehouse") {
			workspace = getWarehouse();
		}else if (workspaceSelection == "desert") {
			workspace = getDesert(); 
		}else if (workspaceSelection == "street") {
			workspace = getStreet(); 
		}else {
			// TODO : State Problem
			throw new IllegalArgumentException();
		}
				
		return workspace;
	}
}
