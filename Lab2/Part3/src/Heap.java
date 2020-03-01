import java.util.Arrays;

/*
  This code is taken from https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Heap.java.html
   and has been modified for CIS 27.

- Tim Chevalier

In this assignment, you will make the following changes:

1. Fill in the sort() method.
2. Fill in the sink() method. 
2. Add code in the main() method to run your tests (see the lab assignment)

For the first two steps: use the code in the book as a model, but change it
so each node in the heap has three children instead of two.

You shouldn't need to add any methods or to change any methods other than
sort(), sink(), and main().
*/

/******************************************************************************  
 *  Sorts a sequence of strings from standard input using heapsort.
 * *
 ******************************************************************************/


/**
 *  The {@code Heap} class provides a static method to sort an array
 *  using <em>heapsort</em>.
 *  <p>
 *  This implementation takes &Theta;(<em>n</em> log <em>n</em>) time
 *  to sort any array of length <em>n</em> (assuming comparisons
 *  take constant time). It makes at most 
 *  2 <em>n</em> log<sub>2</sub> <em>n</em> compares.
 *  <p>
 *  This sorting algorithm is not stable.
 *  It uses &Theta;(1) extra memory (not including the input array).
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Heap {

    // This class should not be instantiated.
    private Heap() { }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param pq the array to be sorted
     */
    public static void sort(Comparable[] pq) {
        int n = pq.length;
       
        /*
         * Fill in this method! Use the code on p. 324 of the book as a model,
         * but change it so each node in the heap has 3 children instead of 2.
         */
    }
    
      /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/

    // n is the length of the sub-array we're working on
    private static void sink(Comparable[] pq, int k, int n) {
    	/*
    	 * Fill in this method! Use the code on p. 316 of the book as a model,
    	 * but change it so each node in the heap has 3 children instead of 2.
    	 */
    }

    /***************************************************************************
     * Helper functions for comparisons and swaps.
     * Indices are "off-by-one" to support 1-based indexing.
     ***************************************************************************/
    private static boolean less(Comparable[] pq, int i, int j) {
        return pq[i-1].compareTo(pq[j-1]) < 0;
    }

    private static void exch(Object[] pq, int i, int j) {
        Object swap = pq[i-1];
        pq[i-1] = pq[j-1];
        pq[j-1] = swap;
    }
    
    /***************************************************************************
     * Testing helper messages
     ***************************************************************************/
 

	private static boolean isSorted(Comparable[] a) {
		for (int i = 1; i < a.length - 1; i++)
			if (less(a, i+1, i)) return false;
		return true;
	}
	
    // print array to standard output
    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("a[%d] = %s\n", i + 1, a[i].toString());
        }
        System.out.println();
    }

	private static String sortString(String s) {
		Character[] charArray = new Character[s.length()];
		for(int i = 0; i < charArray.length; i++) {
			charArray[i] = s.charAt(i);
		}
		Arrays.sort(charArray);
		String result = "";
		for(int i = 0; i < charArray.length; i++) {
			result += charArray[i];
		}
		return result;
	}

	private static Character[] stringToArray(String s) {
		// For these tests, we have to ensure the array begins at 1
		int len = s.length();
		Character[] testArray = new Character[len];
		for(int i = 0; i < len; i++) {
			testArray[i] = s.charAt(i);
		}
		assert(testArray.length == s.length());
		return testArray;
	}
	
	private static String arrayToString(Character[] a) {
		String s = "";
		for(int i = 0; i < a.length; i++) {
			s += a[i];
		}
		assert(s.length() == a.length);
		return s;
	}
	private static void sortTest(String s) {	
		Character[] charArray = stringToArray(s);
		String sortedString = sortString(s);
		sort(charArray);
		String heapSortedString = arrayToString(charArray);
		assert(arrayToString(charArray).equals(sortedString));
		assert(isSorted(charArray));
	}

	
      public static void main(String[] args) {
    	
    	  /* These tests are just to get you started. The assignment asks you to
    	   * test your implementation using 100 randomly ordered distinct keys, and you should do that.
    	   * Add the code for those tests in this main() method. */
  		sortTest("");
  		sortTest("A");
  		sortTest("HE");
  		sortTest("HEA");
  		sortTest("HEAPS");
  		sortTest("HEAPSORT");
		sortTest("HEAPSORTEXAMPLE");
		sortTest("QUICK");
		sortTest("QUICKS");
		sortTest("QUICKSO");
		sortTest("QUICKSORT");
		sortTest("QUICKSORTEXAMPLE");

		sortTest("bottomupmergesortconsistsofasequenceofpassesoverthewholearray");
		sortTest("thefirststepinastudyofcomplexityistoestablishamodelofcomputation.generally,researchersstrivetounderstandthesimplestmodelrelevanttoaproblem.");
    }
}

/******************************************************************************
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
