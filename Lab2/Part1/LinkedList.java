
public class LinkedList {
	/*
	 * Defines a doubly linked list (I've named it LinkedList to avoid conflicts
	 * with your Lab1 project)
	 */

    // A LinkedList (doubly linked list) is a reference to the first node.
    // That reference is null if and only if the list is empty.
	private ListNode head;

    // Returns an empty list
	public LinkedList() {
		head = null;
	}

    // Returns the first node in the list, which may be null
	public ListNode head() {
		return head;
	}
	
    // Sets the head of the list to <newHead>
	public void setHead(ListNode newHead) {
		head = newHead;
	}
	
    // Returns the length of the list
    // (It's assumed that for the last element in the list, n, n.next() == null)
	public int length() {
		if (head == null) {
			return 0;
		} else {
			return head.length();
		}
	}
	
    // Adds <a> onto the front of the list
	public void insert(Comparable a) {
		int oldLen = length();
		ListNode newHead = new ListNode(a);
		if (oldLen != 0) {
			newHead.setNext(head);
			head.setPrev(newHead);
		}
		head = newHead;
		assert(oldLen + 1 == length());
	}

    // Returns true if the list is in sorted order
	public boolean isSorted() {
		int len = length();
		if (len <= 1) {
			return true;
		} else {
			return head.isSorted();
		}
	}
	
	public String toString() {
		if (length() == 0) {
			return "<empty>";
		} else {
			return head.toString();
		}
	}
	
	 public static LinkedList arrayToList(Comparable[] a) {
		 LinkedList result = new LinkedList();
		 for(int i = a.length - 1; i >= 0; i--) {
			 result.insert(a[i]);
		 }
		 return(result);
	 }
	 
	 public boolean equals(Object o) {
		 if (this == o) {
			 return true;
		 }
		 if (o == null) {
			 return false;
		 }
		 if (o instanceof LinkedList) {
			 LinkedList other = (LinkedList) o;
			 return ((head == null) && (other.head == null) ||
					 head.equals(other.head));
		 } else {
			 return false;
		 }
	 }
}