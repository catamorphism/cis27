/*
 * The words.txt file is from https://github.com/dwyl/english-words
 */
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

public class SpellChecker {
	SeparateChainingHashST<String, String> dictionary;
	
	private static void initDictionary(SeparateChainingHashST<String, String> dictionary, String fileName) {
		In input = new In(fileName);
	
		 while (!input.isEmpty()) {
             String s = input.readLine();
         	/*
     		 * TODO: Remove the following line and change it to add an entry into the "dictionary"
     		 * hash table.
     		 */
             System.out.println(s);
         }
	}
	
	public SpellChecker(String fileName) {
		dictionary = new SeparateChainingHashST<String, String>();
		initDictionary(dictionary, fileName);
	}
	
	public static void main(String[] args) {
		 SpellChecker spellChecker = new SpellChecker("words.txt");
		 
		 while(!StdIn.isEmpty()) {
		 /*
		  * TODO: Add code here:
		  * Prompt the user to enter a word
		  * Read in a word
		  * If the word is correctly spelled, print "no mistakes found"
		  * Otherwise, use the five rules to check for mistakes and print a list of suggestions.
		  */
			 
		 }
		 // Note: When running the code, hold down the Control key while pressing D to end the program.
	}
}