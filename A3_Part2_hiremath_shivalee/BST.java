import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

// Assignment 3 Part 2

// This file has classes Tree, DupTree, and TreeIterator
//			(do not change any of these classes)


//********************* Class Tree  ***************************

class Tree implements Iterable<Integer> { // Defines one node of a binary search tree
	
	public Tree(int n) {
		value = n;
		left = null;
		right = null;
	}
	
	public void insert(int n) {
		if (value == n)
			return;
		if (value < n)
			if (right == null)
				right = new Tree(n);
			else
				right.insert(n);
		else if (left == null)
			left = new Tree(n);
		else
			left.insert(n);
	}
	
	public boolean delete(int n) { // cannot delete last value in tree
		Tree t = find(n);

		if (t == null) { // n is not in the tree
			return false;
		}

		if (t.get_count() > 1) {
			t.set_count(t.get_count() - 1);
			return true;
		}

		if (t.left == null && t.right == null) { // n is a leaf value
			if (t != this) {
				case1(t, this);
				return true;
			} else
				return false;
		}
		if (t.left == null || t.right == null) { // t has one subtree only
			if (t != this) { // check whether t is the root of the tree
				case2(t, this);
				return true;
			} else {
				if (t.right == null)
					case3(t, "left");
				else
					case3(t, "right");
				return true;
			}
		}
		// t has two subtrees; go with smallest in right subtree of t
		case3(t, "right");
		return true;
	}

	protected void case1(Tree t, Tree root) { // remove the leaf
		if (t.value > root.value)
			if (root.right == t)
				root.right = null;
			else
				case1(t, root.right);
		else if (root.left == t)
			root.left = null;
		else
			case1(t, root.left);
	}

	protected void case2(Tree t, Tree root) { // remove internal node
		if (t.value > root.value)
			if (root.right == t)
				if (t.right == null)
					root.right = t.left;
				else
					root.right = t.right;
			else
				case2(t, root.right);
		else if (root.left == t)
			if (t.right == null)
				root.left = t.left;
			else
				root.left = t.right;
		else
			case2(t, root.left);
	}

	protected void case3(Tree t, String side) { // replace t.value and t.count
		if (side == "right") {
			Tree min_right_t = t.right.min();
			if (min_right_t.left == null && min_right_t.right == null)
				case1(min_right_t, this); // min_right_t is a leaf node
			else
				case2(min_right_t, this); // min_right_t is a non-leaf node
			t.value = min_right_t.value;
			t.set_count(min_right_t.get_count());
		} else {
		    Tree max_left_t = t.left.max();
			if (max_left_t.left == null && max_left_t.right == null)
				case1(max_left_t, this); // max_left_t is a leaf node
			else
				case2(max_left_t, this); // max_left_t is a non-leaf node
			t.value = max_left_t.value;
			t.set_count(max_left_t.get_count());

		}
	}
	
	protected Tree find(int n) {
		if (value == n)
			return this;
		else if (value < n)
			if (right == null)
				return null;
			else
				return right.find(n);
		else if (left == null)
			return null;
		else
			return left.find(n);
	}

	public Tree min() {
		if (left != null)
			return left.min();
		else
			return this;
	}

	public Tree max() {
		if (right != null)
			return right.max();
		else
			return this;
	}

	public Iterator<Integer> iterator() {
		return new TreeIterator(this);
	}
	
	public int get_count() {
		return 1;
	}
	
	protected  void set_count(int v) { }

	protected int value;
	protected Tree left;
	protected Tree right;
}


// ****************** Class DupTree *******************


class DupTree extends Tree {
	public DupTree(int n) {
		super(n);    // superclass initialization
		count = 1;
	};
	
	public void insert(int n) {
		if (value == n) {
			count++;
			return;
		}
		if (value < n)
			if (right == null)
				right = new DupTree(n);
			else
				right.insert(n);
		else if (left == null)
			left = new DupTree(n);
		else
			left.insert(n);
	}
	
	public int get_count() {
		return count;
	}
	
	protected void set_count(int v) {
		count = v;
	}

	protected int count;
}

// ***************** Class TreeIterator *************************

class TreeIterator implements Iterator<Integer> {

public TreeIterator(Tree root) {
	stack_left_spine(root);
}

public boolean hasNext() {
	return !stack.isEmpty() || count > 0;
}

public Integer next() {
	if (count == 0) {
		node = stack.pop();
		value = node.value;
		count = node.get_count();
	}
	if (count == 1) 
		stack_left_spine(node.right);
	count--;
	return value;
}

private void stack_left_spine(Tree node) {
	if (node == null) return;
	stack.push(node);
	while (node.left != null) {
		stack.push(node.left);
		node = node.left;
	}
}

private Stack<Tree> stack = new Stack<Tree>();
private Tree node;
private int value;
private int count = 0;
}
 