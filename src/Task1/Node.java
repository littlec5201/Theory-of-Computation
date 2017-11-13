package Task1;

public class Node {
	private String data;
	private Node leftChild;
	private Node rightChild;
	private Node parent;

	public Node(String data) {
		this.data = data;
		this.leftChild = null;
		this.rightChild = null;
		this.parent = null;
	}

	public Node getLeftChild() {
		return leftChild;
	}

	// Attempts to set nodes left child to leftChild
	public boolean setLeftChild(Node leftChild) {
		// If node has no left child, set nodes left child and return true
		if (this.getLeftChild() == null) {
			this.leftChild = leftChild;
			return true;
		} else {
			// Set node's right child to leftNode
			setRightChild(leftChild);
			return false;
		}
	}

	public Node getRightChild() {
		return rightChild;
	}

	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public String getData() {
		return data;
	}

	public String toString() {
		return getData();
	}
}
