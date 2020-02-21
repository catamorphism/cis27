	
import java.util.Comparator;

public class LinkedList {
/*
 * insert:
 * 	take a node and insert it into this list
 * delete:
 *  delete a node from the list
 * length:
 *  return the number of nodes in the list, as an integer
 * head:
 *  returns the first element of the list
 * tail:
 *  returns all but the first element of the list
 */

	private boolean empty;
	private NonEmptyList head;
	
	private class NonEmptyList {
		private Comparable val;
		private NonEmptyList next;
		
		// Creates a new list of length 1
		public NonEmptyList(Comparable v) {
			val = v;
			next = null;
		}
		
		public int length() {
			if (next == null) {
				return 1;
			} else {
				return(1 + next.length());
			}
		}
		
		public String toString() {
			String result = val.toString() + " -> ";
			if (next == null) {
				result += "<empty>";
			} else {
				result += next.toString();
			}
			return result;
		}
		
		public boolean equals(NonEmptyList other) {
			//System.out.printf("equals: %s with %s\n", toString(), other.toString());
			if (other == null || !val.equals(other.val)) {
				return false;
			}
			if (next == null) {
				return(other.next == null);
			}
			return(next.equals(other.next));
		}
		
		public void insertAtEnd(Comparable v) {
			if(next == null) {
				next = new NonEmptyList(v);
			} else {
				next.insertAtEnd(v);
			}
		}
	}
			
	public LinkedList() {
		// create an empty list
		empty = true;
		head = null;
	}
		
	public int length() {
		if (empty) {
			return 0;
		} else {
			return head.length();
		}
	}
	
	public boolean equals(LinkedList other) {
	//	System.out.printf("equals: %s with %s\n", toString(), other.toString());
		if(other == null) {
			return false;
		}
		if (empty) {
			return(other.empty);
		}
		return(head.equals(other.head));
	}
		
	public String toString() {
		if (empty) {
			return "<empty>";
		} else {
			return(head.toString());
		}
	}
		
	public static boolean less(Comparable one, Comparable theOther) {
		return (one.compareTo(theOther) < 0);
	}
		
	// Return value is the node that is the new last-sorted element
	public static NonEmptyList insertSorted(NonEmptyList firstSorted,
			NonEmptyList lastSorted, NonEmptyList toInsert) {
	/*
	 * Assuming that all nodes from
	 * 	firstSorted to lastSorted are sorted,
	 * 	insert toInsert into the list, maintaining the sorted order.
	 */
			
		// Fail fast if there's a bug in our code
		assert(firstSorted != null);
		assert(lastSorted != null);
		assert(toInsert != null);
	
		assert(toInsert != firstSorted);
		assert(toInsert != lastSorted);
		assert(toInsert.next == null);
				
		//		System.out.printf("inserting %s into %s, last sorted element is %s", 
		//					toInsert.val.toString(), firstSorted.toString(), lastSorted.toString());
	
		// check if toInsert is the smallest node
		if (less(toInsert.val, firstSorted.val)) {
			// toInsert is smallest node - insert it in front
			toInsert.next = firstSorted;
			return(lastSorted);
		} else {
			// check if we reached the last sorted node
			if (firstSorted == lastSorted) {
				// toInsert must be the biggest node -- insert it in back
				
				// Save off a pointer to lastSorted's next, which is also the first unsorted node
				NonEmptyList firstUnsortedNode = lastSorted.next;
				// Make lastSorted's next be toInsert
				lastSorted.next = toInsert;
				// Make toInsert's next element the first unsorted node
				toInsert.next = firstUnsortedNode;
				// Make sure we don't have a bug that creates a circular list
				assert(toInsert.next != toInsert);
				// Return the new last sorted element (which is toInsert)
				return(toInsert);
			}
			else {
				// we're not yet at the end of the sorted list
				// so, insert into tail
				NonEmptyList newLast = insertSorted(firstSorted.next, lastSorted, toInsert);
				// In the case where toInsert is the new first element in the tail of the sorted list:
				// make firstSorted point to it
				if (less(toInsert.val, firstSorted.next.val)) {
					firstSorted.next = toInsert;
				}
				return(newLast);
			}
		}
	}

	public void insert(Integer n) {
		NonEmptyList next = head;
		head = new NonEmptyList(n);
		if (empty) {
			empty = false;
		} else {
			// Set the next element of the new node to be this list's head
			head.next = next;
		}
	}
	
	public void insertAtEnd(Integer n) {
		if(empty) {
			insert(n);
		} else {
			head.insertAtEnd(n);
		}
	}
	
	private void testInsert(Integer newVal) {
		// Test for insert
		insert(newVal);
		// Test for head
		assert head.val.equals(newVal);
	}
	
	public static void sort(LinkedList l) {
		// Sort the linked list using insertion sort
	
		// An empty list is already sorted
		if (l.empty) {
			return;
		}
		
		// To start with: the first element of the list is the first one
		// that's already sorted, and the last element that's already sorted
		// is undefined (that is, null)
		NonEmptyList firstSorted = l.head;
		NonEmptyList lastSorted = null;
		NonEmptyList nextElement = firstSorted;
		boolean firstTime = true;
		
		while(nextElement != null) {
			if (firstTime) {
				// System.out.printf("firstTime = true, firstSorted = %s, lastSorted = %s, nextElement = %s\n",  firstSorted.toString(), lastSorted.toString(), nextElement.toString());
				
				// The first time around the loop: set the last element that was already sorted
				// to the first element in the list, and advance nextElement.
				firstTime = false;				
				lastSorted = nextElement;
				nextElement = lastSorted.next;				
			} else {
				// System.out.printf("lastSorted = %s\n", lastSorted.toString());
				
				// nextElement points to lastSorted.next, so it's safe to set lastSorted.next to null without
				// losing anything
				lastSorted.next = null;
				
				// Save a pointer to nextElement.next, and temporarily set nextElement's tail to null
				NonEmptyList nextToDo = nextElement.next;
				nextElement.next = null;
				
				// System.out.printf("before insert: firstSorted = %s, lastSorted = %s, nextElement = %s\n", firstSorted.toString(), lastSorted.toString(), nextElement.toString());
				
				// Insert nextElement into the sorted list from firstSorted to lastSorted.
				// Update lastSorted (in case nextElement ends up being the largest element in the sorted list)
				lastSorted = insertSorted(firstSorted, lastSorted, nextElement);
				// Update firstSorted if firstSorted ends up being the first element in the entire list
				if(less(nextElement.val,  firstSorted.val)) {
					firstSorted = nextElement;
				}
				if(nextToDo != null) {
					// System.out.printf("after insert: firstSorted = %s, lastSorted = %s, nextToDo = %s\n", firstSorted.toString(), lastSorted.toString(), nextToDo.toString());
				}
				// The next element to insert is the tail of nextElement, which we saved
				nextElement = nextToDo;
			}

		}
		// Finally, update the list head to firstSorted in case the new first element is different
		l.head = firstSorted;
	}
	
	public static LinkedList arrayToList(Integer[] a) {
		LinkedList result = new LinkedList();
		for (Integer element: a) {
			result.insertAtEnd(element);
		}
		return(result);
	}
	
	public static void main(String[] args) {
		LinkedList l = new LinkedList();
		
		l.testInsert(5);
		
		
		// Tests for sorting
		Integer[][] unsortedArrays = {{3, 1, 4, 1, 5}, {1}, {}};
		Integer[][] sortedArrays = {{1, 1, 3, 4, 5}, {1}, {}};
		
		for(int i = 0; i < unsortedArrays.length; i++) {
			Integer[] unsortedArray = unsortedArrays[i];
			Integer[] sortedArray = sortedArrays[i];
		
			LinkedList listToSort = arrayToList(unsortedArray);
			System.out.printf("before sorting: %s", listToSort.toString());
			// why static? because the first element could change
			sort(listToSort);
			System.out.printf("after sorting: %s", listToSort.toString());
			assert(listToSort.length() == unsortedArray.length);
			assert(listToSort.equals(arrayToList(sortedArray)));
		
		}
	}
}
