// ************** Assignment 2 Part 2 **************

// This file has the following classes:

// 1. TreeGUIDriver and TreeGUI -- the top-level classes
// 	Coding required mainly for undoButton, and some code
//	also for clearButton, insertButton, and deleteButton.
//	OK to add some extra fields in TreeGUI to support coding.
//
// 2. OutputPanel -- for drawing the tree/duptree.
//	No changes required here
//
// 3. TreeMemento -- for saving previous trees/duptrees
//	To be coded by you
//
// 4. AbsTree, Tree, and DupTree -- the foundation classes
//	Only code for the clone() method in AbsTree


import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import javax.swing.*;
/*import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;*/

class TreeGUIDriver {
	public static void main(String[] x) {
		new TreeGUI();
	}
}

public class TreeGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	TreeMemento tm = new TreeMemento();

	AbsTree tree;
		
	boolean is_new_tree = true; // set to false after first insert
	
	int is_null_inbetween = 0;

	Choice tree_kind, element_kind;
	
	public static Font font = new Font("Comic Sans MS", Font.BOLD, 24);

	JTextField input_elem_text, input_elem_text2, min_text, max_text; 
	
	JButton insertButton, deleteButton, undoButton; 
	JButton minButton, maxButton, clearButton;
	
	JPanel inputPanel;  
	OutputPanel outputPanel; 

	public TreeGUI() {
		
		super("GUI for Tree Operations");
		Label tree_kind_label;
		JPanel input1, input2;
		
		//The JPanel input1 details follow

		input1 = new JPanel(new FlowLayout());   //FlowLayout default
		
		tree_kind_label = new Label("Tree Kind:");
		tree_kind_label.setFont(font);
		input1.add(tree_kind_label);
		tree_kind = new Choice();
		tree_kind.setFont(font);
		tree_kind.addItem("Normal Tree");
		tree_kind.setFont(font);
		tree_kind.addItem("Dup Tree");
		tree_kind.setFont(font);
		input1.add(tree_kind);

		input_elem_text = new JTextField("integer");
		input_elem_text.setFont(font);
		input_elem_text.requestFocus(true);
		input_elem_text.selectAll();
		input_elem_text.setEditable(true);
		input_elem_text.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent evt) {
				input_elem_text.setText("");
			}
		});
		insertButton = new JButton("Insert");
		insertButton.setFont(font);
		input1.add(insertButton);
		input1.add(input_elem_text);
		
		input_elem_text2 = new JTextField("integer");
		input_elem_text2.setFont(font);
		input_elem_text2.requestFocus(true);
		input_elem_text2.selectAll();
		input_elem_text2.setEditable(true);
		
		input_elem_text2.addFocusListener(new FocusAdapter() {   //Can we add this ? :: TODO
			public void focusGained(FocusEvent evt) {
				input_elem_text2.setText("");
			}
		});
		
		deleteButton = new JButton("Delete");
		deleteButton.setFont(font);
		input1.add(deleteButton);
		input1.add(input_elem_text2);

		undoButton = new JButton("Undo");
		undoButton.setFont(font);
		input1.add(undoButton);
		
		//The JPanel input2 details follow

		input2 = new JPanel(new FlowLayout());   //FlowLayout default

		minButton = new JButton("Minimum");
		minButton.setFont(font);
		input2.add(minButton);
		min_text = new JTextField(10);
		min_text.setFont(font);
		min_text.setEditable(false);
		input2.add(min_text);

		maxButton = new JButton("Maximum");
		maxButton.setFont(font);
		input2.add(maxButton);
		max_text = new JTextField(10);
		max_text.setFont(font);
		max_text.setEditable(false);
		input2.add(max_text);

		clearButton = new JButton("Clear");
		clearButton.setFont(font);
		input2.add(clearButton);
		
		// The JPanels inputPanel and outputPanel details follow
		
		inputPanel = new JPanel(new BorderLayout());
		outputPanel = new OutputPanel();
		
		inputPanel.add("North", input1);
		inputPanel.add("South", input2);
		
		// Add Button Listeners here for:
		// min, max, clear, insert, delete, undo
		
		minButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (is_new_tree || tree == null) {
					JOptionPane.showMessageDialog(null, "Cannot take min of empty tree");
					return;
				}
				min_text.setText(Integer.toString(tree.min().value));
			}
		});
		
		maxButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (is_new_tree || tree == null) {
					JOptionPane.showMessageDialog(null, "Cannot take max of empty tree");
					return;
				}
				max_text.setText(Integer.toString(tree.max().value));
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				is_new_tree = true;
				tree = null;
				input_elem_text.setText("");
				input_elem_text2.setText("");
				min_text.setText("");
				max_text.setText("");
				outputPanel.clearPanel();
				tm.clear(); //clear the stack
			}
		});
		
		insertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = input_elem_text.getText();
				boolean b = false;  // whether insert changed state of tree
				
				if (is_new_tree) {
					try {
						if (tree_kind.getSelectedIndex() == 0)
							tree = new Tree(Integer.parseInt(s));
						else
							tree = new DupTree(Integer.parseInt(s));
						
					} catch (NumberFormatException e2) {
						JOptionPane.showMessageDialog(null, "Bad integer: " + s + ". Please re-enter.");
						return;
					}
				} // end of if is_new_tree
				else {
			         try {
			        	 if (tree == null) {
			        		 tm.set_state(null);
			        		 is_null_inbetween++;
			        		 if (tree_kind.getSelectedIndex() == 0)
							 	tree = new Tree(Integer.parseInt(s));
			        		 else
							 	tree = new DupTree(Integer.parseInt(s));
			        		 b = true;
			        	 }
			        	 else {
				        	 tm.set_state(tree);
				        	 //outputPanel.printNode(tree);
			        		 b = tree.insert(Integer.parseInt(s)); 
			        		 if(!b)				//Check this behavior with prof once in normal tree duplicate entry :: TODO
			        			 tm.get_state();
			        	 }
			        	 
			         } catch (NumberFormatException e2) {
							 	JOptionPane.showMessageDialog(null, "Bad integer: " + s + ". Please re-enter.");
							 	tm.get_state();
							 	return;
						 }
				}
				is_new_tree = false;
				outputPanel.drawTree(tree);
				input_elem_text.selectAll();
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = input_elem_text2.getText();
				int n = 0;
				try {
					n = Integer.parseInt(s);
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Number format error: " + s + ". Please re-enter.");
					return;
				}
				if (is_new_tree || tree == null) {
					JOptionPane.showMessageDialog(null, "Cannot delete from an empty tree.");
					return;
				}   
				
	        	tm.set_state(tree);
				boolean b = tree.delete(n); // note whether delete changed state of tree
				
				if (b)  {
					 outputPanel.drawTree(tree);
				}
				else // delete will not remove the last value, hence must check this
					if (n == tree.value && tree.left==null && tree.right==null) {
						tree = null; // 
						outputPanel.clearPanel();
					}
					else {
						JOptionPane.showMessageDialog(null, "Cannot delete non-existent value " + n);
						outputPanel.drawTree(tree);
		        	    tm.get_state();
					}
				input_elem_text2.selectAll();
			}
		});
		
		
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			// missing code to be filled in by you
			try {
				tree = tm.get_state();
				if (is_new_tree) {
					JOptionPane.showMessageDialog(null, "No more undo operations are possible");
				} else if( tree == null) {
					outputPanel.clearPanel();
					if(is_null_inbetween > 0) {  //when null is inserted in between stack insert->delete continuous and undo till begin
						is_null_inbetween--; 
					} else {
						is_new_tree = true;
					}
				} else {
	        		 outputPanel.drawTree(tree);
	        	 }
	         } catch (Exception msg) {
	        	 JOptionPane.showMessageDialog(null, msg);
				 return;
	         }
			}	 
		});
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		add("North", inputPanel);
		add("Center", outputPanel);

		setSize(1400, 1000); // for the frame
		setVisible(true);
	}
}

// **************** The OutputPanel class *************************
//		    (no changes required)

// The output area, where the tree is drawn
class OutputPanel extends Panel {
	private static final long serialVersionUID = 1L;
	Image treeImage; // buffer to keep the latest tree representation
	int imageWidth, imageHeight;

	// draw the tree on the output panel
	public void drawTree(AbsTree tree) {
		Graphics g;
		Dimension d = this.size();
		if (imageWidth != d.width || imageHeight != d.height) {
			treeImage = createImage(d.width, d.height);
			imageWidth = d.width;
			imageHeight = d.height;
		}
		;
		// Clear the image
		g = treeImage.getGraphics();
		g.setFont(TreeGUI.font);
		g.setColor(getBackground());
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.black);

		drawNode(g, imageWidth / 2, tree, imageWidth / 2, 0);
		getGraphics().drawImage(treeImage, 0, 0, this);
	}

	private void drawNode(Graphics g, int subtreeW, AbsTree tree, int x, int y) {
		AbsTree left, right;
		//System.out.println(tree.get_value());
		g.drawString(tree.get_value(), x - 15, y + 15);
		left = tree.left;
		right = tree.right;
		if (left != null) {
			g.drawLine(x, y + 15, x - subtreeW / 2, y + 115);
			drawNode(g, subtreeW / 2, left, x - subtreeW / 2, y + 115);
		}
		;
		if (right != null) {
			g.drawLine(x, y + 15, x + subtreeW / 2, y + 115);
			drawNode(g, subtreeW / 2, right, x + subtreeW / 2, y + 115);
		}
	}

	public void clearPanel() {
		Graphics g;
		// Clear the image
		g = treeImage.getGraphics();
		g.setColor(getBackground());
		g.fillRect(0, 0, imageWidth, imageHeight);
		g.setColor(Color.black);
		getGraphics().drawImage(treeImage, 0, 0, this);
	}
	
	public void printNode(AbsTree tree) {
		AbsTree left, right;
		System.out.println(tree.get_value());
		left = tree.left;
		right = tree.right;
		if (left != null) {
			printNode(left);
		}
		if (right != null) {
			printNode(right);
		}
	}

}

//**************** The TreeMemento class *************************
//		   (to be coded by you)

class TreeMemento {
	private Stack<AbsTree> state = new Stack<AbsTree>();
	//OutputPanel outputPanel = new OutputPanel();
	
	public void set_state(AbsTree t) {
		if (t == null)
			state.push(null);
		else
		// fill in code here
		state.push(t.clone());
	}

	public AbsTree get_state() {
		// fill in code here
		if(is_empty())
			return null;
		//AbsTree tree = state.pop();
		//System.out.println("Popped Print");
		//outputPanel.printNode(state.pop());
		return state.pop();//tree;
	}
	
	public void clear() {
		// fill in code here
		state.clear();  //Check this behavior with prof once in normal tree duplicate entry :: TODO
	}
	
	boolean is_empty() { 
		// fill in code here
		return state.isEmpty();
	}
}

// *********************************************************
//  Binary Search Tree (of integers) with Duplicates in Java
// *********************************************************

abstract class AbsTree implements Cloneable {
	public AbsTree(int n) {
		value = n;
		left = null;
		right = null;
	}

	public AbsTree clone() {
		 // fill in code here
		AbsTree t = null;
		try {
			t = (AbsTree) super.clone();
		} catch (Exception e) {
			System.out.println("Clone Exception!!!");
		}
		if(left != null)
			t.left = left.clone();
		if(right != null)
			t.right = right.clone();
		return t;	
	}

	void print() {
		if (left != null)
			left.print();
		print_node();
		if (right != null)
			right.print();
	}

	public boolean insert(int n) {
		if (value == n) 
			return count_duplicates();
		else if (value < n) {
			if (right == null)
				right = add_node(n);
			else
				return right.insert(n);
			return true;
		}
		else { if (left == null) 
			 	 left = add_node(n);
			 else
				 return left.insert(n);
			 return true;
		}
	}

	public boolean delete(int n) { // assume > 1 nodes in tree
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

	private AbsTree find(int n) {
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

	protected int value;
	protected AbsTree left;
	protected AbsTree right;

	// Protected Abstract Methods

	protected abstract AbsTree add_node(int n);

	protected abstract boolean count_duplicates();

	protected abstract void print_node();

	protected abstract String get_value();

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

	protected boolean count_duplicates() {
		return false;
	}

	protected int get_count() {
		return 1;
	}

	protected void set_count(int v) {

	}

	public String get_value() {
		return (value + " ");
	}

	protected void print_node() {
		System.out.print(value + "  ");
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

	protected boolean count_duplicates() {
		count++;
		return true;
	}

	protected void print_node() {
		System.out.print(value + "/" + count + " ");
	}

	public String get_value() {
		return (value + "/" + count);
	}

	protected int get_count() {
		return count;
	}

	protected void set_count(int v) {
		count = v;
	}

	protected int count;
}