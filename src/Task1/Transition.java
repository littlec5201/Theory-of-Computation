package Task1;

public class Transition {

	// Variables that will be used throughout the class
	private State startState;
	private Action action;
	private State endState;

	/**
	 * Constructor for a transition object.
	 *
	 * @param startState the state the transition begins at
	 * @param action the action performed on startState to transition to endState
	 * @param endState the state the transition ends at
	 */
	public Transition(State startState, Action action, State endState) {
		this.startState = startState;
		this.action = action;
		this.endState = endState;
	}

	public State getStartState() {
		return startState;
	}

	public Action getAction() {
		return action;
	}

	public State getEndState() {
		return endState;
	}

	public String toString() {
		return "<" + getStartState() + "," + getAction() + "," + getEndState() + ">";
	}
}
