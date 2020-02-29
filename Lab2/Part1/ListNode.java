/*
A doubly linked list node contains a value, as well as
a reference to the previous node and a reference to the next node.
Either or both may be null. If prev is null, this is the first node
in the list. If next is null, this is the last node in the list.
 */
public class ListNode {
	private Comparable val;
	private ListNode prev;
	private ListNode next;

    // Returns a new list node with the given value,
    // whose previous and next are both set to null
	public ListNode(Comparable v) {
		val = v;
		prev = null;
		next = null;
	}

    // Sets this node's previous node to <p>
	public void setPrev(ListNode p) {
		prev = p;
	}

    // Sets this node's next node to <p>
	public void setNext(ListNode p) {
		next = p;
	}

    // Returns this node's previous node, which may be null
	public ListNode prev() {
		return prev;
	}

    // Returns this node's next node, which may be null
	public ListNode next() {
		return next;
	}

    // Returns the last node in the list starting at this node
    // (It's assumed that the last node has its next field set to null)
	public ListNode last() {
		ListNode result = this;
		while (result.next != null) {
			result = result.next;
		}
		return result;
	}
	
    // Returns the data stored in this list node
	public Comparable value() {
		return val;
	}

	private boolean leq(Comparable v1, Comparable v2) {
		return v1.compareTo(v2) < 1;
	}

    // Returns true if the list starting at this list node
    // is in sorted order
	public boolean isSorted() {
		if (next == null) {
			return true;
		} else {
			return(leq(val, next.val) && next.isSorted());
		}
	}
	
    // Returns the length of the list starting at this node
	public int length() {
		int len = 0;
		for(ListNode h = this; h != null; h = h.next()) {
			len++;
		}
		return len;
	}
	
	public String toString() {
		String result = "";
		for(ListNode h = this; h != null; h = h.next()) {
			result = result + h.val.toString() + " -> ";
		}
		result += "<empty>";
		return result;
	}
	
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (o == this) {
			return true;
		} else if (o instanceof ListNode) {
			ListNode other = (ListNode) o;
			return (val == other.val &&
					((next == null && other.next == null) ||
								(next.equals(other.next))));
		} else {
			return false;
		}
	
	}
	
}
