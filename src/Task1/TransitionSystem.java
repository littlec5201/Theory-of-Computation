package Task1;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class TransitionSystem {

	// Variables that will be used throughout the class
	private ArrayList<State> states;
	private ArrayList<Action> actions;
	private ArrayList<Transition> transitions;
	private ArrayList<State> initialStates;
	private ArrayList<AP> apList;

	public TransitionSystem(ArrayList<State> states, ArrayList<Action> actions, ArrayList<Transition> transitions,
			ArrayList<State> initialStates, ArrayList<AP> apList) {
		this.states = states;
		this.actions = actions;
		this.transitions = transitions;
		this.initialStates = initialStates;
		this.apList = apList;
	}

	public ArrayList<State> getStates() {
		return states;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

	public ArrayList<State> getInitialStates() {
		return initialStates;
	}

	public ArrayList<AP> getApList() {
		return apList;
	}

	public String toString() {
		String stateString = "S = " + states + "\n";

		String actionString = "A = " + actions + "\n";

		String transitionString = "-> = " + transitions + "\n";

		String initialString = "I = " + initialStates + "\n";

		String apString = "AP = " + apList + "\n";

		String labelString = "";
		for (int i = 0; i < states.size(); i++) {
			labelString += "L(" + states.get(i).getStateName() + ")" + " = " + states.get(i).getApList();
			if (i < states.size() - 1) {
				labelString += ", ";
			}
		}
		labelString += "\n";
		return "" + stateString + actionString + transitionString + initialString + apString + labelString;
	}
}
