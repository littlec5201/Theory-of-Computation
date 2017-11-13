package Task1;

public class Action {

	// Variables that will be used throughout the class
	private String action;

	public Action(String action) {
		this.action = action;
	}

	public String getActionName() {
		return this.action;
	}

	public String toString() {
		return this.getActionName();
	}
}
