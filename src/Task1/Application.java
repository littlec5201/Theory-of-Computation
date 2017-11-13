package Task1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

public class Application {

	// Variables that will be used throughout the class
	private HashSet<State> reachableStates = new HashSet<State>();
	private Stack<State> states = new Stack<State>();
	private Stack<State> reachableStack = new Stack<State>();
	private ArrayList<State> visitedStates = new ArrayList<State>();
	private boolean b = false;
	private State counterExample = null;

	private Tree tree;
	private Stack<Node> nodeStack = new Stack<Node>();

	public static void main(String[] args) {
		// Choose between using the Task 1 (standard example) or Task 2
		// (Petersons mutual exclusion
		// algorithm turned into a TS) Transition System.
		// This then chooses the appropriate .txt file, and loads it into the
		// program.
		Scanner input = new Scanner(System.in);
		System.out.println("Task 1 or Task 2: ");
		int task = input.nextInt();
		File file = null;
		if (task == 1) {
			file = new File("Test.txt");
		} else if (task == 2) {
			file = new File("output.txt");
		}
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Creates instance of itself to use methods in the class.
		Application a = new Application();

		// Takes first line of text file and turns them into States.
		String inputStates = "";
		try {
			inputStates = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputStates = inputStates.substring(inputStates.indexOf('{') + 1, inputStates.indexOf('}'));
		ArrayList<State> stateArrayList = new ArrayList<State>();
		String[] stateSplit = inputStates.split(",");
		for (String s : stateSplit) {
			State state = new State(s.trim());
			stateArrayList.add(state);
		}

		// Takes the next line of text file and turns them into Actions
		String inputActions = "";
		try {
			inputActions = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputActions = inputActions.substring(inputActions.indexOf('{') + 1, inputActions.indexOf('}'));
		ArrayList<Action> actionArrayList = new ArrayList<Action>();
		String[] actionSplit = inputActions.split(",");
		for (String s : actionSplit) {
			Action action = new Action(s.trim());
			actionArrayList.add(action);
		}

		// Takes the next line of text file and turns them into Transitions
		// (State,Action,State)
		String inputTransitions = "";
		try {
			inputTransitions = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputTransitions = inputTransitions.substring(inputTransitions.indexOf('{') + 1, inputTransitions.indexOf('}'));
		ArrayList<Transition> transitionArrayList = new ArrayList<Transition>();
		String[] transitionString = inputTransitions.split(">");
		for (String s : transitionString) {
			String newString = s.substring(s.indexOf('<') + 1);
			String[] transitionSplit = newString.split(",");
			State startState = null;
			State endState = null;
			for (State current : stateArrayList) {
				if (current.getStateName().equals(transitionSplit[0].trim())) {
					startState = current;
				}
				if (current.getStateName().equals(transitionSplit[2].trim())) {
					endState = current;
				}
			}
			Action action = null;
			for (Action current : actionArrayList) {
				if (current.getActionName().equals(transitionSplit[1].trim())) {
					action = current;
				}
			}
			Transition transition = new Transition(startState, action, endState);
			transitionArrayList.add(transition);
			for (State current : stateArrayList) {
				if (transition.getStartState().getStateName().equals(current.getStateName())) {
					current.addTransition(transition);
				}
			}
		}

		// Takes the next line of text file containing initial states
		// Holds onto them for now as work with states is not done yet.
		String inputInitials = "";
		try {
			inputInitials = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputInitials = inputInitials.substring(inputInitials.indexOf('{') + 1, inputInitials.indexOf('}'));

		// Takes the next line of text file and turns them into Action
		// Propositions (AP)
		String inputAPs = "";
		try {
			inputAPs = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputAPs = inputAPs.substring(inputAPs.indexOf('{') + 1, inputAPs.indexOf('}'));
		ArrayList<AP> apArrayList = new ArrayList<AP>();
		String[] apSplit = inputAPs.split(",");
		for (String s : apSplit) {
			AP ap = new AP(s.trim());
			apArrayList.add(ap);
		}

		// Using the next line of the text file, the program adds the AP's
		// listed to the corresponding states APList
		String inputStateLabels = "";
		try {
			inputStateLabels = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] labelSplit = inputStateLabels.split("}");

		for (String s : labelSplit) {
			s += "}";
			String stateName = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
			String labels = s.substring(s.indexOf('{') + 1, s.indexOf('}'));
			String[] labelArray = labels.split(",");
			ArrayList<AP> currentAPList = new ArrayList<AP>();

			for (String label : labelArray) {
				for (AP current : apArrayList) {
					if (current.getAP().equals(label)) {
						currentAPList.add(current);
					}
				}
			}

			for (State current : stateArrayList) {
				if (current.getStateName().equals(stateName)) {
					for (AP currentAP : currentAPList) {
						current.addAP(currentAP);
					}
				}
			}
		}

		// Adds Initial states that were stored before into the initial
		// ArrayList
		ArrayList<State> initialArrayList = new ArrayList<State>();
		String[] initialSplit = inputInitials.split(",");
		for (String s : initialSplit) {
			for (State current : stateArrayList) {
				if (current.getStateName().equals(s.trim())) {
					initialArrayList.add(current);
				}
			}
		}

		// Creates new Transition System Object using all the ArrayLists made
		// from the text file
		TransitionSystem ts = new TransitionSystem(stateArrayList, actionArrayList, transitionArrayList,
				initialArrayList, apArrayList);

		// Adds all states into the Applications State Variable & all initial
		// states into reachable states variable
		for (State s : stateArrayList) {
			a.states.add(s);
		}
		for (State s : initialArrayList) {
			a.addReachableStates(s);
		}

		// Prints out the chosen Transition System (defined in toString in
		// TransitionSystem class
		if (task == 1) {
			System.out.println("Task 1: Test Transition System: ");
		} else if (task == 2) {
			System.out.println("Task 2: Transition System for " + (stateArrayList.size() - 1) + " processes");
		}
		System.out.println(ts);
		System.out.println();

		// Prompts user to enter propositional formula
		System.out.println("Enter your propositional formula:");
		input.nextLine();
		String formula = input.nextLine();
		int leftBracketCount = 0;
		int rightBracketCount = 0;

		char[] formulaChar = formula.toCharArray();

		// Check that left and right bracket have same count
		for (int i = 0; i < formulaChar.length; i++) {
			if (formulaChar[i] == '(') {
				leftBracketCount++;
			}
			if (formulaChar[i] == ')') {
				rightBracketCount++;
			}
		}
		// If there are the same number of left and right brackets, enter this
		// section of code
		// Else, print error message
		if (leftBracketCount == rightBracketCount) {
			// Break string up into string array where there is a space
			String[] tokens = formula.split(" ");
			ArrayList<String> tokenList = new ArrayList<String>();
			// Go through the entered propositional formula and break components
			// up into tokens
			for (String current : tokens) {
				if (current.contains("(") && current.contains(")")) {
					char[] c = current.toCharArray();
					tokenList.add("" + c[1]);
				} else if (current.contains("(")) {
					String adding = "";
					// Break current string up into char array, so each
					// character can be checked
					char[] c = current.toCharArray();
					for (int i = 0; i < c.length; i++) {
						if (c[i] == '(') {
							tokenList.add("" + c[i]);
						} else {
							adding += c[i];
							if (i == c.length - 1) {
								tokenList.add(adding);
								adding = "";
							}
						}
					}
				} else if (current.contains(")")) {
					String adding = "";
					// Break current string up into char array, so each
					// character can be checked
					char[] c = current.toCharArray();
					for (int i = 0; i < c.length; i++) {
						if (c[i] == ')') {
							tokenList.add("" + c[i]);
						} else {
							adding += c[i];
							if (c[i + 1] == ')') {
								tokenList.add(adding);
								adding = "";
							}
						}
					}
				} else {
					tokenList.add(current);
				}
			}

			// Build tree based on token list just created
			a.tree = a.buildParseTree(tokenList);

			// Boolean list will be used to hold at most 2 boolean values, so
			// different tree subsections can be compared and evaluated
			ArrayList<Boolean> booleanList = new ArrayList<Boolean>();

			// Node list will contain list of nodes to be evaluated for a
			// section of the tree
			ArrayList<Node> nodeList = new ArrayList<Node>();
			boolean eval = false;

			// Pushes all nodes into a stack
			a.traverse(a.tree.getRoot());

			while (!a.nodeStack.isEmpty()) {
				// Pops a node off the stack and stores it
				Node n = a.nodeStack.pop();
				String name = n.getData();

				// Check to see what node we are dealing with
				switch (name) {
				case "IMP":
					if (booleanList.isEmpty()) {
						eval = a.call(n, nodeList);
						nodeList.clear();
						booleanList.add(eval);
					} else {
						boolean existing = booleanList.get(0);
						booleanList.clear();
						boolean result = a.call(n, nodeList);
						booleanList.add(existing == result);
					}
					break;
				case "AND":
					if (booleanList.isEmpty()) {
						eval = a.call(n, nodeList);
						nodeList.clear();
						booleanList.add(eval);
					} else {
						boolean existing = booleanList.get(0);
						booleanList.clear();
						booleanList.add(existing && a.call(n, nodeList));
					}
					break;
				case "OR":
					if (booleanList.isEmpty()) {
						eval = a.call(n, nodeList);
						nodeList.clear();
						booleanList.add(eval);
					} else {
						boolean existing = booleanList.get(0);
						booleanList.clear();
						booleanList.add(existing || a.call(n, nodeList));
					}
					break;
				default:
					if (n.getParent() != null) {
						if (n.getParent().getData().equals("NOT")) {
							nodeList.add(n);
							nodeList.add(a.nodeStack.pop());
						} else {
							nodeList.add(n);
						}
					} else {
						nodeList.add(n);
					}
					if (a.nodeStack.isEmpty()) {
						eval = a.call(n, nodeList);
						booleanList.add(eval);
					}
					break;
				}
			}
			System.out.println(formula + " == " + booleanList.get(0));
		} else {
			System.out.println("Formula error: Mismatched brackets");
		}

	}

	public boolean call(Node n, ArrayList<Node> nodeList) {
		for (State state : states) {
			if (!containsNodes(state, nodeList, n)) {
				return false;
			}
		}
		return true;
	}

	public boolean containsNodes(State currentState, ArrayList<Node> nodeList, Node operator) {
		ArrayList<Boolean> booleanList = new ArrayList<Boolean>();

		boolean current = false;
		// Iterate through Nodes in nodeList
		for (int i = 0; i < nodeList.size(); i++) {
			// Check current node isn't "NOT"
			if (!nodeList.get(i).getData().equals("NOT")) {
				// Check to see if current node data matches ap in current state
				// label
				check: for (AP ap : currentState.getApList()) {
					if (nodeList.get(i).getData().equals(ap.getAP())) {
						current = true;
						break check;
					}
				}
				// If next node is not, set current to opposite of what it is
				// currently
				if (i + 1 < nodeList.size()) {
					if (nodeList.get(i + 1).getData().equals("NOT")) {
						current = !current;
					}
				}
				booleanList.add(current);
				current = false;
			}
		}
		// Checks the operator and performs a check to see whether the condition
		// holds
		switch (operator.getData()) {
		case "IMP":
			if (booleanList.size() == 2) {
				return (booleanList.get(0) == booleanList.get(1) || booleanList.get(0) == true);
			}
		case "AND":
			if (booleanList.size() == 2) {
				return (booleanList.get(0) && booleanList.get(1));
			}
		case "OR":
			if (booleanList.size() == 2) {
				return (booleanList.get(0) || booleanList.get(1));
			}
		default:
			return booleanList.get(0);
		}
	}

	// Builds tree based on tokenlist passed as parameter
	public Tree buildParseTree(ArrayList<String> tokenList) {
		Tree working = new Tree();
		for (int i = 0; i < tokenList.size(); i++) {
			String token = tokenList.get(i);
			Node node = new Node(token);
			switch (token) {
			case "(":
				break;
			case ")":
				// End of current equation, so go up a level
				working.goToParent();
				break;
			case "IMP":
			case "AND":
			case "OR":
				// Set current node to node
				working.setCurrent(node);
				break;
			case "NOT":
				working.insertLeft(node);
				break;
			default:
				if (working.getCurrent() == null) {
					working.insertLeft(node);
				} else {
					if (working.getCurrent().getLeftChild() == null) {
						working.insertLeft(node);
					} else {
						working.insertRight(node);
					}
				}
				break;
			}
		}
		return working;
	}

	// Recursive method that adds nodes in the tree to a stack
	public void traverse(Node root) {
		if (!nodeStack.contains(root)) {
			nodeStack.push(root);
		}
		if (root.getLeftChild() != null) {
			traverse(root.getLeftChild());
		}
		if (root.getRightChild() != null) {
			traverse(root.getRightChild());
		}
	}

	// Given a starting state, this recursive method will determine all states
	// that can be reached from it
	public void addReachableStates(State currentState) {
		reachableStates.add(currentState);
		reachableStack.push(currentState);
		visitedStates.add(currentState);
		for (Transition t : currentState.getTransitionList()) {
			if (!reachableStates.contains(t.getEndState())) {
				reachableStates.add(t.getEndState());
				reachableStack.push(t.getEndState());
			}
		}
		while (!reachableStack.isEmpty() && visitedStates.contains(reachableStack.peek())) {
			reachableStack.pop();
		}
		if (!reachableStack.isEmpty()) {
			addReachableStates(reachableStack.pop());
		}
		visitedStates.clear();
	}
}