public class Deque<A> {

	private class DoubleNode {
		// Fill in!
		DoubleNode(A value) {
			// Fill in!
		}
		public A value() throws Exception {
			throw new Exception("value not yet implemented");
		}
	}
	
	public void insertAtBeginning(A dataToInsert) {
		// Fill in!
	}
	
	public DoubleNode head() throws Exception {
		// Fill in!
		throw new Exception("empty deque!");
	}
	
	public int length() {
		// Fill in!
		return 0;
	}
	
	/*
	 * Other methods to implement:
	 * Insert at the end
	 * Remove from the beginning
	 * Remove from the end
	 * Insert before a given node
	 * Insert after a given node
	 * Remove a given node
         * Move to front (move an object to the front)
	 * Move to end (move an object to the end)
	 */
	public static void main(String[] args) {
		// Write your unit tests here!
		Deque<Integer> d = new Deque<Integer>();
		try {
			d.insertAtBeginning(42);
			assert(d.length() == 1);
			assert(d.head().value() == 42);
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		System.out.println("All tests passed!");
		/* Write unit tests for all other methods */
	}

}
