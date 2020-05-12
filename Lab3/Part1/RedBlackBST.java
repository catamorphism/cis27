/*
  https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/RedBlackBST.java.html
   and has been modified for CIS 27.

- Tim Chevalier
*/
/*
 * For Lab 3:
 * - You will be changing code between lines 355-776, and possibly the main() method.
 * - In Part 1, you will change the put() and delete() methods as well.
 * 
 * 
 * 1. I've already added a blackHeight instance variable to the RedBlackBST class.
 *   However, it's always 0. Your task is to figure out what code in the put() and delete()
 *   methods could change the black height, and modify that code so the blackHeight
 *   instance variable is updated.
 * 2. Modify the joinHelper() method as per the TODO comment.
 * 3. Modify the fixup() method (line 379) as per the TODO comment.
 * 4. Modify the findBlackNodeWithLargestKey() and findBlackNodeWithSmallestKey() methods
 *     as per the TODO comments.
 *     
 *  Your final submission must compile with no compile-time errors, and must run the tests
 *  that are already implemented with no assertion failures.   
 */

/******************************************************************************
 *  Compilation:  javac RedBlackBST.java
 *  Execution:    java RedBlackBST < input.txt
 *  Dependencies: StdIn.java StdOut.java  
 *  Data files:   https://algs4.cs.princeton.edu/33balanced/tinyST.txt  
 *    
 *  A symbol table implemented using a left-leaning red-black BST.
 *  This is the 2-3 version.
 *
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *  
 *  % java RedBlackBST < tinyST.txt
 *  A 8
 *  C 4
 *  E 12
 *  H 5
 *  L 11
 *  M 9
 *  P 10
 *  R 3
 *  S 0
 *  X 7
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdIn;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
/*
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/33balanced">Section 3.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class RedBlackBST<Key extends Comparable<Key>, Value> {

	private static final boolean RED = true;
	private static final boolean BLACK = false;

	// Denotes the number of black links from the root to any leaf of this tree
	private int blackHeight = 0;

	private Node root; // root of the BST

	// BST helper node data type
	private class Node {
		private Key key; // key
		private Value val; // associated data
		private Node left, right; // links to left and right subtrees
		private boolean color; // color of parent link

		public Node(Key key, Value val, boolean color) {
			this.key = key;
			this.val = val;
			this.color = color;
		}
	}

	/**
	 * Initializes an empty symbol table.
	 */
	public RedBlackBST() {
	}

	/***************************************************************************
	 * Node helper methods.
	 ***************************************************************************/
	// is node x red; false if x is null ?
	private boolean isRed(Node x) {
		if (x == null)
			return false;
		return x.color == RED;
	}

	// number of node in subtree rooted at x; 0 if x is null
	private int size(Node x) {
		if (x == null)
			return 0;
		return size(x.left) + size(x.right) + 1;
	}

	/**
	 * Returns the number of key-value pairs in this symbol table.
	 * 
	 * @return the number of key-value pairs in this symbol table
	 */
	public int size() {
		return size(root);
	}

	/**
	 * Is this symbol table empty?
	 * 
	 * @return {@code true} if this symbol table is empty and {@code false}
	 *         otherwise
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/***************************************************************************
	 * Standard BST search.
	 ***************************************************************************/

	/**
	 * Returns the value associated with the given key.
	 * 
	 * @param key the key
	 * @return the value associated with the given key if the key is in the symbol
	 *         table and {@code null} if the key is not in the symbol table
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public Value get(Key key) {
		if (key == null)
			throw new IllegalArgumentException("argument to get() is null");
		return get(root, key);
	}

	// value associated with the given key in subtree rooted at x; null if no such
	// key
	private Value get(Node x, Key key) {
		while (x != null) {
			int cmp = key.compareTo(x.key);
			if (cmp < 0)
				x = x.left;
			else if (cmp > 0)
				x = x.right;
			else
				return x.val;
		}
		return null;
	}

	/**
	 * Does this symbol table contain the given key?
	 * 
	 * @param key the key
	 * @return {@code true} if this symbol table contains {@code key} and
	 *         {@code false} otherwise
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public boolean contains(Key key) {
		return get(key) != null;
	}

	/***************************************************************************
	 * Red-black tree insertion.
	 ***************************************************************************/

	/**
	 * Inserts the specified key-value pair into the symbol table, overwriting the
	 * old value with the new value if the symbol table already contains the
	 * specified key. Deletes the specified key (and its associated value) from this
	 * symbol table if the specified value is {@code null}.
	 *
	 * @param key the key
	 * @param val the value
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void put(Key key, Value val) {
		if (key == null)
			throw new IllegalArgumentException("first argument to put() is null");
		if (val == null) {
			delete(key);
			return;
		}

		root = put(root, key, val);
		root.color = BLACK;
		assert (check());
	}

	// insert the key-value pair in the subtree rooted at h
	private Node put(Node h, Key key, Value val) {
		if (h == null)
			return new Node(key, val, RED); // Does not change black height

		int cmp = key.compareTo(h.key);
		if (cmp < 0)
			h.left = put(h.left, key, val);
		else if (cmp > 0)
			h.right = put(h.right, key, val);
		else
			h.val = val;

		// fix-up any right-leaning links
		if (isRed(h.right) && !isRed(h.left))
			h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left))
			h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right))
			flipColors(h);

		return h;
	}

	/***************************************************************************
	 * Red-black tree deletion.
	 ***************************************************************************/

	private void fixupRoot() {
		// if both children of root are black, set root to red
		if (!isRed(root.left) && !isRed(root.right)) {
			root.color = RED;
		}
	}

	/**
	 * Removes the smallest key and associated value from the symbol table.
	 * 
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public void deleteMin() {
		if (isEmpty())
			throw new NoSuchElementException("BST underflow");

		fixupRoot();

		root = deleteMin(root);
		if (!isEmpty())
			root.color = BLACK;
		assert check();
	}

	// delete the key-value pair with the minimum key rooted at h
	private Node deleteMin(Node h) {
		if (h.left == null)
			return null;

		if (!isRed(h.left) && !isRed(h.left.left))
			h = moveRedLeft(h);

		h.left = deleteMin(h.left);
		return balance(h);
	}

	/**
	 * Removes the largest key and associated value from the symbol table.
	 * 
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public void deleteMax() {
		if (isEmpty())
			throw new NoSuchElementException("BST underflow");

		fixupRoot();

		root = deleteMax(root);
		if (!isEmpty())
			root.color = BLACK;
		assert check();
	}

	// delete the key-value pair with the maximum key rooted at h
	private Node deleteMax(Node h) {
		if (isRed(h.left))
			h = rotateRight(h);

		if (h.right == null)
			return null;

		if (!isRed(h.right) && !isRed(h.right.left))
			h = moveRedRight(h);

		h.right = deleteMax(h.right);

		return balance(h);
	}

	/**
	 * Removes the specified key and its associated value from this symbol table (if
	 * the key is in this symbol table).
	 *
	 * @param key the key
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void delete(Key key) {
		if (key == null)
			throw new IllegalArgumentException("argument to delete() is null");
		if (!contains(key))
			return;

		fixupRoot();

		root = delete(root, key);
		if (!isEmpty())
			root.color = BLACK;
		assert check();
	}

	// delete the key-value pair with the given key rooted at h
	private Node delete(Node h, Key key) {
		assert get(h, key) != null;

		if (key.compareTo(h.key) < 0) {
			if (!isRed(h.left) && !isRed(h.left.left))
				h = moveRedLeft(h);
			h.left = delete(h.left, key);
		} else {
			if (isRed(h.left))
				h = rotateRight(h);
			if (key.compareTo(h.key) == 0 && (h.right == null))
				return null;
			if (!isRed(h.right) && !isRed(h.right.left))
				h = moveRedRight(h);
			if (key.compareTo(h.key) == 0) {
				Node x = min(h.right);
				h.key = x.key;
				h.val = x.val;
				// h.val = get(h.right, min(h.right).key);
				// h.key = min(h.right).key;
				h.right = deleteMin(h.right);
			} else
				h.right = delete(h.right, key);
		}
		return balance(h);
	}

	/********
	 * join()
	 */

	/*
	 * join() replaces this with a tree that contains all the key-value pairs in this,
	 * as well as k and v,
	 * as well as all the key-value pairs in t.
	 * join() should allocate only one new Node.
	 * After join() returns, this tree should satisfy the red-black tree invariants.
	 * It should make ~lg(N) comparisons.
	 */
	public void join(Key k, Value v, RedBlackBST<Key, Value> t) {

		assert (max().compareTo(k) < 1); // k must be >= all keys in this
		assert (k.compareTo(t.min()) <= 0); // k must be <= all keys in t

		int oldSize1 = size();
		int oldSize2 = t.size();

		root = joinHelper(k, v, t);
		blackHeight = Math.max(this.blackHeight, t.blackHeight);

		root = fixup(root);
		if (root.color == RED) {
			blackHeight++;
			root.color = BLACK;
		}

		//StdOut.println("after join and fixup, this = " + this);
		//printTree(this);
		//StdOut.println("new size = " + size() + " old size1 = " + oldSize1 + " old size2 = " + oldSize2);
		
		assert (size() == oldSize1 + oldSize2 + 1);
		assert (check());
	}

    private Node fixup(Node h) {
    	if (h == null) {
    		return null;
    	}
    	/*
    	 * TODO: Add code here to restore red-black tree invariants.
    	 * Look at put() for an example. Do you need to do all of the checks
    	 * that put() does?
    	 */
        return h;
    }

	private Node joinHelper(Key k, Value v, RedBlackBST<Key, Value> t) {

		boolean tIsTaller = true;
		Node y;

		/*
		 * Two cases: either this has a larger black height than t, so we
		 * find a node in this whose black height equals t's black height;
		 * or t has a larger black height than this, so we find a node in t
		 * whose black height equals this's black height
		 */
		if (blackHeight >= t.blackHeight) {
			tIsTaller = false;
			y = findBlackNodeWithLargestKey(t.blackHeight);
		} else {
			y = t.findBlackNodeWithSmallestKey(blackHeight);
		}

	//	StdOut.println("y = " + y.key + ", k = " + k + " this.blackHeight = " + blackHeight + " and t.blackHeight is "
	//			+ t.blackHeight);

		Node y_left = y.left;
		Node y_right = y.right;
		Key y_key = y.key;
		Value y_value = y.val;
		boolean y_color = y.color;

		Node result = null;

		// Two cases: either k >= y.key and k <= t.min(),
		// or k >= t.max() and k <= y.key

		if (!tIsTaller) {
			assert (k.compareTo(y.key) >= 0 && k.compareTo(t.min()) <= 0);

		/*
		 * TODO: Fill in this code! Transform y, t, and root to produce a tree
		 * that has all the keys in this, all the keys in t, and also k.
		 */
		} else {
			assert (k.compareTo(y.key) <= 0 && k.compareTo(max()) >= 0);
			/*
			 * TODO: Fill in this code! Transform y, t, and root to produce a tree
			 * that has all the keys in this, all the keys in t, and also k.
			 */	
		}

		// StdOut.println("after join, y = " + y.key);
		// StdOut.println("after join, this = " + this);
		printTree(this);
		y.color = RED;
		assert(result != null);
		return result;

	}

	/*
	 * The black height of a node is defined as the number of black links on any path
	 * from that node to a leaf.
	 * Out of all the nodes in subtrees of this tree, findBlackNodeWithLargestKey() returns
	 * the one with the largest key.
	 */
	private Node findBlackNodeWithLargestKey(int black_height) {
			// StdOut.println("black height of root = " + black_height);
		assert (black_height <= blackHeight);
		if (blackHeight == black_height) {
			// In this case, we should return the root, since the only possible other key
			// with the same black height is the root's left child, which must be <= the
			// root
			return root;
		} else {
			return findBlackNodeWithLargestKey(root, blackHeight, black_height);
		}
	}

	/* Similar to findBlackNodeWithLargestKey(), but here we pick the node with
	 * the smallest key. */
	private Node findBlackNodeWithSmallestKey(int black_height) {
		// Returns a black node whose black height is black_height,
		// and has the largest possible key of all such nodes

		// StdOut.println("black height of root = " + black_height);
		assert (black_height <= blackHeight);
		if (blackHeight == black_height) {
			// This is the only black node whose black height is black_height,
			// because if there is a left child, that node is either red; or it's
			// black but has black height black_height - 1
			return root;
		} else {
			return findBlackNodeWithSmallestKey(root, blackHeight, black_height);
		}
	}

	// Assuming that a subtree rooted at <parent> has black-height
	// <parentsBlackHeight>, find
	// the black node whose black height is black_height and has the biggest possible
	// key of all such nodes, and return it
	private Node findBlackNodeWithLargestKey(Node parent, int parentsBlackHeight, int black_height) {

		/*
		StdOut.println("The tree rooted at " + parent.key + " has black height " + parentsBlackHeight
				+ ".\nSearching for a subtree with black height " + black_height);
		 */
		
		Node result = null;
		
		// this must be the node with the greatest black height in this case --
		// the right child has lower black height and the left child must be smaller
		if (parentsBlackHeight == black_height) {
			result = parent;
		} else if (parent.left == null && parent.right == null) {
			// A leaf has black height 1. So the desired black height must be 1 if we've
			// reached
			// this case.
			assert (black_height == 1);
			return parent;
		} else if (parent.right == null) {
			/*
			 * TODO: Fill in this code! What to we do if the right child is null?
			 * Hint: Use recursion.
			 */

		} else {
			/*
			 * TODO: Fill in this code! What to we do if the right child is non-null?
			 * Hint: Use recursion.
			 */
		}
		assert(result != null);
		return result;
	}

	// Assuming that a subtree rooted at <parent> has black-height
	// <parentsBlackHeight>, find
	// the black node whose black height is black_height and has the smallest possible
	// key of all such nodes, and return it
	private Node findBlackNodeWithSmallestKey(Node parent, int parentsBlackHeight, int black_height) {

		/*
		StdOut.println("(smallest) The tree rooted at " + parent.key + " has black height " + parentsBlackHeight
				+ ".\nSearching for a subtree with black height " + black_height);
		 */
		
		Node result = null;
		
		// This must be the smallest black node whose black height is black_height,
		// because if there is a left child, that node is either red; or it's
		// black but has black height black_height - 1
		if (parentsBlackHeight == black_height) {
			result = parent;
		} else if (parent.left == null && parent.right == null) {

			// A leaf has black height 1. So the desired black height must be 1 if we've
			// reached
			// this case.
			assert (black_height == 1);
			result = parent;
		} else if (parent.left == null) {
			/*
			 * TODO: Fill in this code! What to we do if the left child is null?
			 * Hint: Use recursion.
			 */
		} else {
			/*
			 * TODO: Fill in this code! What to we do if the left child is non-null?
			 * Hint: Use recursion.
			 */

		}
		assert(result != null);
		return result;
	}
	
	// testing only
		private void testFindBlackNode() {
			// build table from black height -> node
			// then for each possible black height <= the entire tree's black height, check
			// that findBlackNodeWithLargestKey() returns the right
			// node as per the table

			testFindBlackNode(root);
		}

		// testing only
		@SuppressWarnings("unchecked")
		private void testFindBlackNode(Node root) {
			if (root == null) {
				return;
			}

			HashMap<Node, Integer> blackHeightTable = new HashMap<Node, Integer>();
			buildTable(root, blackHeightTable);

			int blackHeightForThisNode = blackHeightTable.get(root);

			// StdOut.println("node = " + root.key.toString() + " black height = " + blackHeightForThisNode);

			// for each bh from 0 to blackHeightForThisNode:
			// call findBlackNodeWithLargestKey(bh);
			// check the result against the table
			for (int bh = 1; bh <= blackHeightForThisNode; bh++) {
				// find the answer using the method that we're testing,
				// findBlackNodeWithLargestKey()
				Node blackNodeWithLargestKey = findBlackNodeWithLargestKey(root, blackHeightForThisNode, bh);
				Key k = blackNodeWithLargestKey.key;
				//StdOut.println(
				//		"in the tree rooted at " + root.key + " the largest node with black height " + bh + " is " + k);

				// StdOut.println("bh = " + bh + " key = " + k.toString());

				// find the answer using the table (all nodes with this black height), then
				// make sure each of those nodes has a key <= k

				for (Map.Entry<Node, Integer> pair : blackHeightTable.entrySet()) {
					if (((Integer) pair.getValue()) == bh) {
						Node n = (Node) pair.getKey();
						// StdOut.println("node " + n.key + " has black height " + bh);
						assert (k.compareTo(n.key) >= 0);
					}
				}
			}
		}

		// testing only
		private void buildTable(Node r, HashMap<Node, Integer> blackHeightTable) {
			// for each node in the tree: map node -> black height

			if (r == null) {
				return;
			}

			blackHeightTable.put(r, findBlackHeightOfNode(r));

			if (r.left != null) {
				buildTable(r.left, blackHeightTable);
			}
			if (r.right != null) {
				buildTable(r.right, blackHeightTable);
			}

		}

	private static String randomString() {
		String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		int len = StdRandom.uniform(1, 16);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < len; i++) {
			char c = CHARS.charAt(StdRandom.uniform(0, CHARS.length() - 1));
			result.append(c);
		}
		return result.toString();
	}

	private static void testJoin(RedBlackBST<Integer, String> t1, Integer k, String s,
			RedBlackBST<Integer, String> t2) {
		int oldSize = t1.size();
		int oldSize2 = t2.size();
		t1.join(k, s, t2);
		assert (t1.check());
		assert (t1.size() == (oldSize + oldSize2 + 1));
		assert (t1.get(k).equals(s));
	}

	private void printTree(RedBlackBST<Key, Value> t) {
		printNode(t.root);
	}

	private void printNode(Node n) {
		if (n == null) {
			return;
		} else {
			StdOut.println(n.key + "(" + (n.color == RED ? "red" : "black") + ")" + "("
					+ (n.left == null ? "null" : n.left.key) + "," + (n.right == null ? "null" : n.right.key) + ")");
			printNode(n.left);
			printNode(n.right);
		}
	}

	private static void smallJoinTest() {

		int SIZE1 = 7;
		int SIZE2 = 5;
		for (int i = 0; i < 100; i++) {
			RedBlackBST<Integer, String> st1 = new RedBlackBST<Integer, String>();
			int[] ints1 = { 1, 14, 27, 19, 12, 10, 28 };
			for (int j = 0; j < SIZE1; j++) {
				Integer v = ints1[j];
				StdRandom.uniform(0, 30);
				st1.put(v, randomString());
				// StdOut.println("st1 putting " + v);
			}
			// find maxKey + 1
			Integer k = st1.max() + 1;
			String s = randomString();
			// StdOut.println("finished building st1, k = " + k + " size = " + st1.size());
			// now construct a random tree with keys > k
			RedBlackBST<Integer, String> t2 = new RedBlackBST<Integer, String>();
			int[] ints2 = { 39, 33, 37, 31, 32 };
			for (int j = 0; j < SIZE2; j++) {
				Integer v = ints2[j];
				t2.put(v, randomString());
				// StdOut.println("t2 putting " + v);
			}
			// StdOut.println("finished building t2, about to call testJoin, size = " +
			// t2.size());
			// st1.printTree(st1);
			// StdOut.println("t2 = " + t2);
			// t2.printTree(t2);
			testJoin(st1, k, s, t2);
		}

	}

	private static void bigJoinTest() {
		int SIZE1 = 100;
		int SIZE2 = SIZE1;
		for (int i = 0; i < 100; i++) {
			// test join: first create a random tree with integer keys
			RedBlackBST<Integer, String> st1 = new RedBlackBST<Integer, String>();

			for (int j = 0; j < SIZE1; j++) {
				Integer v = StdRandom.uniform(0, 1000);
				st1.put(v, randomString());
				// StdOut.println("st1 putting " + v);
			}
			// find maxKey + 1
			Integer k = st1.max() + 1;
			String s = randomString();
			// StdOut.println("finished building st1, k = " + k + " size = " + st1.size());
			// now construct a random tree with keys > k
			RedBlackBST<Integer, String> t2 = new RedBlackBST<Integer, String>();

			for (int j = 0; j < SIZE2; j++) {
				Integer v = StdRandom.uniform(k + 1, 2000);
				t2.put(v, randomString());
				// StdOut.println("t2 putting " + v);
			}
			// StdOut.println("finished building t2, about to call testJoin, size = " +
			// t2.size());
			// st1.printTree(st1);
			// StdOut.println("t2 = " + t2);
			// t2.printTree(t2);
			testJoin(st1, k, s, t2);
		}
	}

	private static void testFindBlackNode1() {
		RedBlackBST<String, Integer> st = new RedBlackBST<String, Integer>();

		// Initialize with 100 random keys
		for (int i = 0; i < 100; i++) {
			String s = randomString();
			Integer v = StdRandom.uniform(0, 1000);
			StdOut.println(s + " -> " + v);
			st.put(s, StdRandom.uniform(0, 1000));
		}

		StdOut.println("keys: ");
		for (String k : st.keys()) {
			StdOut.println(k + " / " + k.toString());
		}
		st.testFindBlackNode();

	}

	/***************************************************************************
	 * Red-black tree helper functions.
	 ***************************************************************************/

	// make a left-leaning link lean to the right
	private Node rotateRight(Node h) {
		assert (h != null) && isRed(h.left);
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = x.right.color;
		x.right.color = RED;
		return x;
	}

	// make a right-leaning link lean to the left
	private Node rotateLeft(Node h) {
		assert (h != null) && isRed(h.right);
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = x.left.color;
		x.left.color = RED;
		return x;
	}

	// flip the colors of a node and its two children
	private void flipColors(Node h) {
		// h must have opposite color of its two children
		assert (h != null) && (h.left != null) && (h.right != null);
		assert (!isRed(h) && isRed(h.left) && isRed(h.right)) || (isRed(h) && !isRed(h.left) && !isRed(h.right));
		h.color = !h.color;
		h.left.color = !h.left.color;
		h.right.color = !h.right.color;
	}

	// Assuming that h is red and both h.left and h.left.left
	// are black, make h.left or one of its children red.
	private Node moveRedLeft(Node h) {
		assert (h != null);
		assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

		flipColors(h);
		if (isRed(h.right.left)) {
			h.right = rotateRight(h.right);
			h = rotateLeft(h);
			flipColors(h);
		}
		return h;
	}

	// Assuming that h is red and both h.right and h.right.left
	// are black, make h.right or one of its children red.
	private Node moveRedRight(Node h) {
		assert (h != null);
		assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
		flipColors(h);
		if (isRed(h.left.left)) {
			h = rotateRight(h);
			flipColors(h);
		}
		return h;
	}

	// restore red-black tree invariant
	private Node balance(Node h) {
		assert (h != null);

		if (isRed(h.right))
			h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left))
			h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right))
			flipColors(h);

		return h;
	}

	/***************************************************************************
	 * Ordered symbol table methods.
	 ***************************************************************************/

	/**
	 * Returns the smallest key in the symbol table.
	 * 
	 * @return the smallest key in the symbol table
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public Key min() {
		if (isEmpty())
			throw new NoSuchElementException("calls min() with empty symbol table");
		return min(root).key;
	}

	// the smallest key in subtree rooted at x; null if no such key
	private Node min(Node x) {
		assert x != null;
		if (x.left == null)
			return x;
		else
			return min(x.left);
	}

	/**
	 * Returns the largest key in the symbol table.
	 * 
	 * @return the largest key in the symbol table
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public Key max() {
		if (isEmpty())
			throw new NoSuchElementException("calls max() with empty symbol table");
		return max(root).key;
	}

	// the largest key in the subtree rooted at x; null if no such key
	private Node max(Node x) {
		assert x != null;
		if (x.right == null)
			return x;
		else
			return max(x.right);
	}

	/**
		 * Return the key in the symbol table of a given {@code rank}. This key has the
		 * property that there are {@code rank} keys in the symbol table that are
		 * smaller. In other words, this key is the ({@code rank}+1)st smallest key in
		 * the symbol table.
		 *
		 * @param rank the order statistic
		 * @return the key in the symbol table of given {@code rank}
		 * @throws IllegalArgumentException unless {@code rank} is between 0 and
		 *                                  <em>n</em>–1
		 */
	
	  public Key select(int rank) {
		  	if (rank < 0 || rank >= size()) { 
		  		throw new  IllegalArgumentException("argument to select() is invalid: " + rank);
		  	}
		  	return select(root, rank);
	  }
	  
	  // Return key in BST rooted at x of given rank. 
	  // Precondition: rank is in legal range. 
	  private Key select(Node x, int rank) {
		  if (x == null) return null;
		  int leftSize = size(x.left);
		  if (leftSize > rank) return select(x.left, rank); 
		  else if (leftSize < rank) return select(x.right, rank - leftSize - 1);
	  else return x.key;
		  }
	  
	 /**
		 * Return the number of keys in the symbol table strictly less than {@code key}.
		 * 
		 * @param key the key
		 * @return the number of keys in the symbol table strictly less than {@code key}
		 * @throws IllegalArgumentException if {@code key} is {@code null}
		 */
	
	  public int rank(Key key) {
		  if (key == null) throw new
		  IllegalArgumentException("argument to rank() is null");
		  return rank(key,
				  root);
		  }

	  // number of keys less than key in the subtree rooted at x
	  private int rank(Key key, Node x) {
		  if (x == null) return 0;
		  int cmp = key.compareTo(x.key);
		  if (cmp < 0) return rank(key, x.left);
		  else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
		  else return size(x.left);
		  }
	  
	 /***************************************************************************
		 * Range count and range search.
		 ***************************************************************************/
	/*
	
	*//**
		 * Returns all keys in the symbol table as an {@code Iterable}. To iterate over
		 * all of the keys in the symbol table named {@code st}, use the foreach
		 * notation: {@code for (Key key : st.keys())}.
		 * 
		 * @return all keys in the symbol table as an {@code Iterable}
		 */
	
	  public Iterable<Key> keys() { 
		  if (isEmpty()) return new Queue<Key>(); 
		  return
			  keys(min(), max()); 
		  }
	  
	 /**
		 * Returns all keys in the symbol table in the given range, as an
		 * {@code Iterable}.
		 *
		 * @param lo minimum endpoint
		 * @param hi maximum endpoint
		 * @return all keys in the symbol table between {@code lo} (inclusive) and
		 *         {@code hi} (inclusive) as an {@code Iterable}
		 * @throws IllegalArgumentException if either {@code lo} or {@code hi} is
		 *                                  {@code null}
		 */
	  public Iterable<Key> keys(Key lo, Key hi) {
	        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
	        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

	        Queue<Key> queue = new Queue<Key>();
	        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
	        keys(root, queue, lo, hi);
	        return queue;
	    } 
	  
	  // add the keys between lo and hi in the subtree rooted at x
	    // to the queue
	    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) { 
	        if (x == null) return; 
	        int cmplo = lo.compareTo(x.key); 
	        int cmphi = hi.compareTo(x.key); 
	        if (cmplo < 0) keys(x.left, queue, lo, hi); 
	        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key); 
	        if (cmphi > 0) keys(x.right, queue, lo, hi); 
	    } 

	/***************************************************************************
	 * Check integrity of red-black tree data structure.
	 ***************************************************************************/
	private boolean check() {
		if (!isBST())
			StdOut.println("Not in symmetric order");
		if (!isRankConsistent())
			StdOut.println("Ranks not consistent");
		if (!is23())
			StdOut.println("Not a 2-3 tree");
		if (!isBalanced())
			StdOut.println("Not balanced");
		if (!isBlackHeightConsistent())
			StdOut.println("blackHeight not correct");
		return isBST() && isRankConsistent() && is23() && isBalanced() && isBlackHeightConsistent();
	}

	// does this binary tree satisfy symmetric order?
	// Note: this test also ensures that data structure is a binary tree since order
	// is strict
	private boolean isBST() {
		return isBST(root, null, null);
	}

	// is the tree rooted at x a BST with all keys strictly between min and max
	// (if min or max is null, treat as empty constraint)
	// Credit: Bob Dondero's elegant solution
	private boolean isBST(Node x, Key min, Key max) {
		if (x == null)
			return true;
		if (min != null && x.key.compareTo(min) <= 0)
			return false;
		if (max != null && x.key.compareTo(max) >= 0)
			return false;
		return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
	}

	// check that ranks are consistent
	private boolean isRankConsistent() {
		for (int i = 0; i < size(); i++)
			if (i != rank(select(i)))
				return false;
		for (Key key : keys())
			if (key.compareTo(select(rank(key))) != 0)
				return false;
		return true;
	}

	// Does the tree have no red right links, and at most one (left)
	// red links in a row on any path?
	private boolean is23() {
		return is23(root);
	}

	private boolean is23(Node x) {
		if (x == null)
			return true;
		if (isRed(x.right))
			return false;
		if (x != root && isRed(x) && isRed(x.left))
			return false;
		return is23(x.left) && is23(x.right);
	}

	// do all paths from root to leaf have same number of black edges?
	private boolean isBalanced() {
		int black = findBlackHeight();
		return isBalanced(root, black);
	}

	// find the number of black edges on the path to the leftmost leaf
	private int findBlackHeight() {
		return findBlackHeightOfNode(root);
	}

	private int findBlackHeightOfNode(Node r) {
		int black = 0; // number of black links on path from root to min
		Node x = r;
		while (x != null) {
			if (!isRed(x))
				black++;
			x = x.left;
		}
		return black;
	}

	// is blackHeight instance variable consistent with dynamically
	// computed black height?
	private boolean isBlackHeightConsistent() {
		int computedBlackHeight = findBlackHeight();
		return (blackHeight == computedBlackHeight);
	}

	// does every path from the root to a leaf have the given number of black links?
	private boolean isBalanced(Node x, int black) {
		if (x == null)
			return black == 0;
		if (!isRed(x))
			black--;
		return isBalanced(x.left, black) && isBalanced(x.right, black);
	}

	
	/**
	 * Unit tests the {@code RedBlackBST} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {

		testFindBlackNode1();

		/* Add other tests for join() here if you like */
		smallJoinTest();
		bigJoinTest();
	}
}

/******************************************************************************
 * Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 * This file is part of algs4.jar, which accompanies the textbook
 *
 * Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne, Addison-Wesley
 * Professional, 2011, ISBN 0-321-57351-X. http://algs4.cs.princeton.edu
 *
 *
 * algs4.jar is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * algs4.jar is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * algs4.jar. If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
