// Assignment 3 Part 1

// Organization of the File:

// 1. Classes Tree_Test and DupTree_Test -- the tester classes
//
// 2. Classes AbsTree, Tree, and DupTree
//
// 3. Class AbsTree_Iterator

// *********************************************************

import java.util.Stack;

import com.google.java.contract.Ensures;
import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;

class Tree_Test {

	public static void main(String[] args) {
		AbsTree tr = new Tree(100);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(20);
		tr.insert(75);
		tr.insert(20);
		tr.insert(90);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(75);
		tr.insert(90);
		
		System.out.println("Initial Tree: "); print(tr); 
		
		tr.delete(50);
		tr.delete(125);
		tr.delete(150);
		tr.delete(75);
		tr.delete(90);
		tr.delete(20);
		
		System.out.println("Tree before last delete: "); print(tr); 
		
		System.out.println("Attempt to delete last value must throw contract exception: ");
		tr.delete(100);
		
	}
	
	public static void print(AbsTree tr) {
		AbsTree_Iterator it = new AbsTree_Iterator(tr);
		while (it.hasNext()) 
			System.out.print(it.next() + " ");
		System.out.println();
	}
}

class DupTree_Test {

	public static void main(String[] args) {
		AbsTree tr = new DupTree(100);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(20);
		tr.insert(75);
		tr.insert(20);
		tr.insert(90);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(75);
		tr.insert(90);
		
		System.out.println("Initial DupTree: ");  print(tr);  
		
		tr.delete(50);
		tr.delete(125);
		tr.delete(150);
		tr.delete(75);
		tr.delete(90);
		tr.delete(20);
		tr.delete(100);
		
		System.out.println("DupTree after some delete's: ");  print(tr);  

		tr.delete(50);
		tr.delete(125);
		tr.delete(150);
		tr.delete(75);
		tr.delete(90);
		
		System.out.println("DupTree before last delete: "); print(tr); 
		System.out.println("Attempt to delete last value: ");
		
		tr.delete(20);
	}
	
	public static void print(AbsTree tr) {
		Tree_Test.print(tr);
	}
}

// ***************************************************************

// This was an abstract class but is now defined
// as a non-abstract class for CoFoJa's convenience

@Invariant("ordered()")
class AbsTree {
	public AbsTree(int n) {
		value = n;
		left = null;
		right = null;
	}

	// @Requires("true")
	@Ensures("member(n)")
	public boolean insert(int n) {
		if (value == n)
			return count_duplicates();
		else if (value < n)
			if (right == null) {
				right = add_node(n);
				return true;
			} else
				return right.insert(n);
		else if (left == null) {
			left = add_node(n);
			return true;
		} else
			return left.insert(n);
	}
	
	boolean ordered() {
		return
		(left == null || value > left.max().value && left.ordered())
				&&
		(right == null || value < right.min().value && right.ordered());
	}
	
	boolean member(int n) {
		return
			  (value == n) 
			  	||
			  (value < n && (right != null && right.member(n)))
				||
			  (value > n && (left != null && left.member(n)));
	}
	
	public AbsTree_Iterator iterator() {
		return new AbsTree_Iterator(this);
	}
	
	// The pre-condition should apply to trees and duptrees and should
        // that the last value cannot be deleted from a tree or duptree

	@Requires("member(n) && min().value != max().value || get_count() > 1")
	
	public boolean delete(int n) {  

		AbsTree t = find(n);

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

	protected void case1(AbsTree t, AbsTree root) { // remove the leaf
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

	protected void case2(AbsTree t, AbsTree root) { // remove internal node
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

	protected void case3(AbsTree t, String side) { // replace t.value and t.count
		if (side == "right") {
			AbsTree min_right_t = t.right.min();
			if (min_right_t.left == null && min_right_t.right == null)
				case1(min_right_t, this); // min_right_t is a leaf node
			else
				case2(min_right_t, this); // min_right_t is a non-leaf node
			t.value = min_right_t.value;
			t.set_count(min_right_t.get_count());
		} else {
			AbsTree max_left_t = t.left.max();
			if (max_left_t.left == null && max_left_t.right == null)
				case1(max_left_t, this); // max_left_t is a leaf node
			else
				case2(max_left_t, this); // max_left_t is a non-leaf node
			t.value = max_left_t.value;
			t.set_count(max_left_t.get_count());

		}
	}

	// @Requires("true")
	@Ensures("result == null || result.value == old(n)")
	protected AbsTree find(int n) {
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

	public AbsTree min() {
		if (left != null)
			return left.min();
		else
			return this;
	}

	public AbsTree max() {
		if (right != null)
			return right.max();
		else
			return this;
	}
	
	public void print() {
		if (left != null) left.print();
		print_node();   
		if (right != null) right.print();
	}
	
    	// Following were Protected Abstract Methods. Since
	// CoFoJa seems to have a problem with abstract methods
	// they are defined in the manner below.  These methods
	// are over-ridden by the Tree and DupTree subclasses.
	
	protected  AbsTree add_node(int n) { return null; }
	protected  boolean count_duplicates() {return false; }
	protected  void print_node() { }
	protected  int get_count() {return 0;} 
	protected  void set_count(int v) { }
	
	protected int value;
	protected AbsTree left;
	protected AbsTree right;
}

class Tree extends AbsTree {
	public Tree(int n) {
		super(n);
	}
	
	// this definition included here for symmetry
	// but could also be omitted
	public boolean insert(int n) {
		return super.insert(n);
	}

	// state the post-condition for Tree.delete
	// (the pre-condition is from AbsTree.delete)
	@Ensures("!super.member(old(n))")
	public boolean delete(int n) {
		return super.delete(n);
	}
	
	protected AbsTree add_node(int n) {
		return new Tree(n);
	}

	protected boolean count_duplicates() {
		return false;
	}
	
	protected int get_count() {
		return 1;
	}

	protected void set_count(int v) {
	}
	
	protected void print_node() {
		System.out.print(value + " ");
	}
}

class DupTree extends AbsTree {
	public DupTree(int n) {
		super(n);
		count = 1;
	};
	
	// state the post-condition for DupTree.insert
	// 	(the pre-condition is "true")

	@Ensures("n_count > 0 && n_count - old_n_count == 1") //member 
	public boolean insert(int n) {
		if(member(n))
			old_n_count = find(n).get_count();
		else
			old_n_count = 0;
		if(super.insert(n)) {
			n_count = find(n).get_count();
			return true;
		}
		return false;
		// Invoke super.insert(n) to perform insertion
		// into duptree.  Write extra code to facilitae
		// the statement of the post-condition.
	}
	
	// state the pre-condition for DupTree.delete
	// (the pre-condition is from AbsTree.delete)
	@Ensures("old_n_count - n_count == 1")
	public boolean delete(int n) {
		old_n_count = find(n).get_count();
		
		if(super.delete(n)) {
			if(member(n))
				n_count = find(n).get_count();
			else
				n_count = 0;
			return true;
		}
		return false;
		// Invoke super.delete(n) to delete n from duptree.
		// Write extra code to facilitate the statement
		// of the post-condition.
	}

	protected AbsTree add_node(int n) {
		return new DupTree(n);
	}

	protected boolean count_duplicates() {
		count++;
		return true;
	}
	
	protected int get_count() {
		return count;
	}

	protected void set_count(int v) {
		count = v;
	}
	
	protected void print_node() {
		System.out.print(value + ":" + count + " ");
	}

	protected int count;
	
	// Important:
	// Helpful to have two fields, old_n_count and n_count, which hold
	// the values before- and after- insertion/deletion for the count field 
	// associated with the object containing the value n -- the input
	// parameter of insert and delete.  These two fields are useful
	// in writing the post-condition for DupTree.insert and DupTree.delete,
	// but they need to be appropriately set in the code for DupTree.insert
	// DupTree.delete.

	protected int old_n_count, n_count = 1;
}

//**************************************************************************

// See A3 Part 1 for a description of the iterator contracts.

class AbsTree_Iterator {
	
	@Requires("root != null && root.ordered()")
	@Ensures("stack.peek().value == root.min().value && hasNext()") 
	public AbsTree_Iterator(AbsTree root) {
		stack_left_spine(root);
	}

	public boolean hasNext() {
		return !stack.isEmpty() || count > 0;
	}

	@Requires("hasNext()")
	@Ensures({"(old(value) < result && old(count) == 0) || (result == old(value) && old(count) - count == 1 && stack.size() == old(stack.size()))", "old(value) <= result"})
	public int next() {
		if (count == 0) {
			AbsTree node = stack.pop();
			value = node.value;
			count = node.get_count();
			stack_left_spine(node.right);
		}
		count--;
		return value;
	}

	@Requires("true")
	@Ensures({"node == null || stack.peek().value == node.min().value", "stack.isEmpty() || value <= stack.peek().value"})
	private void stack_left_spine(AbsTree node) {
		if (node == null)
			return;
		stack.push(node);
		while (node.left != null) {
			stack.push(node.left);
			node = node.left;
		}
	}

	private Stack<AbsTree> stack = new Stack<AbsTree>();
	private int value;
	private int count = 0;
}

