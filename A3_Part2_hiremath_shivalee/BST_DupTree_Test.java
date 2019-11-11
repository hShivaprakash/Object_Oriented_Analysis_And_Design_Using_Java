import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BST_DupTree_Test {

	static DupTree dtr;
	static List<Integer> al = new ArrayList<Integer>();
	static Random r = new Random();

	@BeforeAll
	public static void setup() {
	 	// code to be filled in by you
		int a = (int)(r.nextInt(24));
		dtr = new DupTree(a);
		al.add(a);
		for (int i=0; i<24; i++) {
			int e = (int)(r.nextInt(24));
			dtr.insert(e);
			al.add(e);
		}
		Collections.sort(al);
		
		Iterator<Integer> itr1 = dtr.iterator();
		System.out.println("DupTree created in Setup:");
		while(itr1.hasNext()) {
			int t = itr1.next();
			System.out.print(t + " ");
		}
		System.out.println(" ");
		
		System.out.println("Sorted ArrayList created in Setup:");
		for (int x : al) 
			System.out.print(x + " ");
		System.out.println(" ");
		
	}

	@AfterEach
	void check_invariant() {
		// code to be filled in by you
		assertTrue(ordered(dtr));
		System.out.println("DupTree invariant maintained");
	}
	
	@Test
	void test_insert() {
		// code to be filled in by you 
		System.out.println("----------------------------");
		
		System.out.println("Testing DupTree insert ...");
		System.out.println("Creating ArrayList iterator and Comparing elements pair-wise ...");
		assertTrue(ordered(dtr));
		
		Iterator<Integer> d_itr = dtr.iterator();
		Iterator<Integer> d_its = al.iterator();
			
		while(d_itr.hasNext() && d_its.hasNext()) {
			assertTrue(d_itr.next().equals(d_its.next()));
		}

		assertTrue(!d_itr.hasNext());
		assertTrue(!d_its.hasNext());
		System.out.println("... DupTree insert test passed");
	}
	
	@Test
	void test_delete() {
		// code to be filled in by you
		//Testing DupTree delete: inserted value = 6 with count = 1
		System.out.println("----------------------------");
		assertTrue(ordered(dtr));
		
		int v = (int)(Math.random()*24);
		dtr.insert(v);
		al.add(v);
		Collections.sort(al);
		assertTrue(ordered(dtr));
		int c1 = get_count(dtr,v);
		System.out.println("Testing DupTree delete: inserted value = " + v + " with count = " + c1);
		
		dtr.delete(v);
		al.remove(al.indexOf(v));
		Collections.sort(al);
		
		int c2 = get_count(dtr,v);
		System.out.println("After DupTree delete: value = " + v + ", count = " + c2);
		
		assertEquals(c2, c1-1);
		System.out.println("DupTree delete test passed");
	}		

	public int get_count(DupTree tr, int v) {
		// code to be filled in by you
		if(tr.find(v) != null)
			return tr.find(v).get_count();
		else
			return 0;
	}

	public boolean ordered(Tree tr) {
		// code to be filled in by you
		return (tr.left == null ||
				(tr.value > tr.left.max().value && ordered(tr.left))) 
				&&
				(tr.right == null ||
				tr.value < tr.right.min().value && ordered(tr.right));
		
	}
}