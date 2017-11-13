package Task1;
import java.util.ArrayList;

public class State {

	// Variables that will be used throughout the class
	private String stateName;
	private ArrayList<Transition> transitionList;
	private ArrayList<AP> apList;
	private ArrayList<String> apString;

	public State(String stateName) {
		transitionList = new ArrayList<Transition>();
		apList = new ArrayList<AP>();
		apString = new ArrayList<String>();
		this.stateName = stateName;
	}

	/**
	 * Method generates a hashCode for the state so that it can be added to the
	 * reachableState HashSet.
	 *
	 * @return the hashCode generated based on stateName
	 */
	public int hashCode() {
		int hashCode = 0;
		for (char c : stateName.toCharArray()) {
			hashCode += (int) c;
		}
		return hashCode;
	}

	/**
	 * This method attempts to add a transition into the ArrayList of
	 * transitions. A check is done to ensure that an identical transition has
	 * not already been added.
	 *
	 * @param transition
	 *            the transition to be added to the ArrayList
	 * @return whether the transition was added or not
	 */
	public boolean addTransition(Transition transition) {
		for (Transition t : transitionList) {
			if (t.getStartState().equals(transition.getStartState())) {
				if (t.getAction().equals(transition.getAction())) {
					if (t.getEndState().equals(transition.getEndState())) {
						return false;
					}
				}
			}
		}
		transitionList.add(transition);
		return true;
	}

	public boolean addAP(AP ap) {
		for (AP current : apList) {
			if (current.getAP().equals(ap.getAP())) {
				return false;
			}
		}
		apList.add(ap);
		return true;
	}

	public String getStateName() {
		return stateName;
	}

	public ArrayList<Transition> getTransitionList() {
		return transitionList;
	}

	public ArrayList<AP> getApList() {
		return apList;
	}

	public String toString() {
		return this.getStateName();
	}

	public ArrayList<String> getAPString() {
		for (AP current : apList) {
			apString.add(current.getAP());
		}
		return apString;
	}


}
