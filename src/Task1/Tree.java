package Task1;

public class Tree {
	private Node root;
	private Node current;

	public Tree() {
		this.root = null;
		this.current = null;
	}

	public void setRoot(Node node) {
		this.root = node;
	}

	public Node getRoot() {
		return root;
	}

	public Node getCurrent() {
		return current;
	}

	// Inserts node between the current node and its parent (if it has any)
	public void setCurrent(Node node) {
		// If current node is null, set node to root of tree
		if (getCurrent() == null) {
			this.current = node;
			setRoot(current);
		} else {
			// Set nodes left child to the current node
			node.setLeftChild(current);
			// If the current node has a parent
			if (current.getParent() != null) {
				// Set node's parent as currents parent
				node.setParent(current.getParent());
				// Set current's parent's left child to current
				current.getParent().setLeftChild(node);
			}
			// Set current's parent to node, then make current node, node
			current.setParent(node);
			current = node;
			// Current has no parents, therefore, must be set to root
			if (current.getParent() == null) {
				setRoot(current);
			}
		}
	}

	// Attempts to insert node as current's left child
	public void insertLeft(Node node) {
		if (current == null) {
			setCurrent(node);
		} else {
			// Set nodes parent to current
			node.setParent(current);
			// Attempts to set current's left child to node
			// If not successful, sets current's right child to node
			boolean left = current.setLeftChild(node);
			// Set current based on where node was inserted
			if (left) {
				current = current.getLeftChild();
			} else {
				current = current.getRightChild();
			}
		}
	}

	public void insertRight(Node node) {
		if (current == null) {
			current = node;
			setRoot(node);
		} else {
			// Sets current's right child to node, then makes current, current's
			// right child
			current.setRightChild(node);
			node.setParent(current);
			current = current.getRightChild();
		}
	}

	// Goes one level up the tree if the current node has a parent
	public void goToParent() {
		if (current.getParent() != null) {
			current = current.getParent();
		}
	}
}
