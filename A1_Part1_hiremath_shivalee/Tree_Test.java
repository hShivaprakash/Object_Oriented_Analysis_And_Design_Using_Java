// Assignment 1 Part 1: Starter Code

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
		
		tr.delete(20);
		tr.delete(20);
		tr.delete(20);
		tr.delete(150);
		tr.delete(100);
		tr.delete(150);
		tr.delete(125);
		tr.delete(125);
		tr.delete(50);
		tr.delete(50);
		tr.delete(50);
		tr.delete(75);
		tr.delete(90);
		tr.delete(75);
		tr.delete(90);
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
		
		tr.delete(20);
		tr.delete(20);
		tr.delete(20);
		tr.delete(150);
		tr.delete(100);
		tr.delete(150);
		tr.delete(125);
		tr.delete(125);
		tr.delete(50);
		tr.delete(50);
		tr.delete(50);
		tr.delete(75);
		tr.delete(90);
		tr.delete(75);
		tr.delete(90);
	}
}

abstract class AbsTree {
	public AbsTree(int n) {
		value = n;
		left = null;
		right = null;
	}

	public void insert(int n) {
		if (value == n)
			count_duplicates();
		else if (value < n)
			if (right == null) {
				right = add_node(n);
				right.parent = this;
			} else
				right.insert(n);
		else if (left == null) {
			left = add_node(n);
			left.parent = this;
		} else
			left.insert(n);
	}

	public void delete(int n) {  
		AbsTree t = find(n);

		if (t == null) { // n is not in the tree
			System.out.println("Unable to delete " + n + " -- not in the tree! " + t);
			return;
		}
		
		System.out.println("Object found " + " " + t.value);
		
		int c = t.get_count();
		if (c > 1) {
			t.set_count(c-1);
			return;
		}

		if (t.left == null && t.right == null) { // n is a leaf value
			if (t != this)
				case1(t);
			else
				System.out.println("Unable to delete " + n + " -- tree will become empty!");
			return;
		}
		if (t.left == null || t.right == null) { // t has one subtree only
			if (t != this) { // check whether t is the root of the tree
				case2(t);
				return;
			} else {
				if (t.right == null)
					case3L(t);
				else
					case3R(t);
				return;
			}
		}
		// t has two subtrees; go with smallest in right subtree of t
		case3R(t);
	}

	protected void case1(AbsTree t) { // remove the leaf
		// to be filled by you
	    if(t.parent.left == t)
	    	t.parent.left = null;
	    else
	    	t.parent.right = null;
		t.parent = null;
	}

	protected void case2(AbsTree t) { // remove internal node
		// to be filled by you
		if(t.parent.left == t)
			if(t.left != null) 
	    		t.parent.left = t.left;
			else
				t.parent.left = t.right;
	    else
	    	if(t.left != null) 
	    		t.parent.right = t.left;
			else
				t.parent.right = t.right;
		
		if(t.left != null)  {
    		t.left.parent = t.parent;
			t.left = null;
		}
		else {
			t.right.parent = t.parent;
			t.right = null;
		}
		
		t.parent = null;
	}

	protected void case3L(AbsTree t) { // replace t.value and t.count
		// to be filled by you
		AbsTree x = t.left.max();
		int temp_val = t.value;
		int temp_count = t.get_count();
		t.value = x.value;
		t.set_count(x.get_count());
		x.value = temp_val;
		x.set_count(temp_count);
		
		if(x.left != null) {
			if(x.parent != t) {
				x.parent.right = x.left;
				x.left.parent = x.parent;
			}
			else if(x.parent == t){
				x.parent.left = x.left;
				x.left.parent = x.parent;
			}
			x.left = null;
		}
		else {
			if(x.parent != t)
				x.parent.right = null;
			else if(x.parent == t) {
				t.left = null;		
			}	
		}
		x.parent = null;
	}

	protected void case3R(AbsTree t) { // replace t.value //and count?
		// to be filled by you
		AbsTree x = t.right.min();
		int temp_val = t.value;
		int temp_count = t.get_count();
		t.value = x.value;
		t.set_count(x.get_count());
		x.value = temp_val;
		x.set_count(temp_count);
		
		if(x.right != null) {
			if(x.parent != t) {
				x.parent.left = x.right;
				x.right.parent = x.parent;
			}
			else if(x.parent == t){
				x.parent.right = x.right;
				x.right.parent = x.parent;
			}
			x.right = null;
		}
		else {
			if(x.parent != t)
				x.parent.left = null;
			else if(x.parent == t) {
				t.right = null;		
			}	
		}
		x.parent = null;
	}

	private AbsTree find(int n) {
		// to be filled by you
		if (value == n)
			return this;
		else if (value < n) {
			if (right != null)
				return right.find(n);
		}
		else {
			if (left != null)
				return left.find(n);
		}
		return null;
	}

	public AbsTree min() {
		if(this.left == null)
			return this;
		else
			return left.min();
	}

	public AbsTree max() {
		if(this.right == null)
			return this;
		else
			return right.max();
	}

	protected int value;
	protected AbsTree left;
	protected AbsTree right;
	protected AbsTree parent;

	protected abstract AbsTree add_node(int n);
	protected abstract void count_duplicates();
	protected abstract int get_count();
	protected abstract void set_count(int v);
}

class Tree extends AbsTree {
	public Tree(int n) {
		super(n);
	}

	protected AbsTree add_node(int n) {
		return new Tree(n);
	}

	protected void count_duplicates() {
		;
	}

	protected int get_count() {
		// to be filled by you
		return 1;
	}

	protected void set_count(int v) {
		// to be filled by you
	}
}

class DupTree extends AbsTree {
	public DupTree(int n) {
		super(n);
		count = 1;
	};

	protected AbsTree add_node(int n) {
		return new DupTree(n);
	}

	protected void count_duplicates() {
		count++;
	}

	protected int get_count() {
		// to be filled by you
		return count;
	}

	protected void set_count(int v) {
		// to be filled by you
		count = v;
	}

	protected int count;
}
