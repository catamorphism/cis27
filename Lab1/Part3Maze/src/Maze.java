import java.util.Vector;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/*
 * Assignment:
 * 1. Fill in the neighbors() and site() methods in the Cell class.
 * 2. Fill in the pickRandomCell() and randomMaze() methods in the Maze class.
 * 3. Run the code. 
 * 4. Based on the output, draw the maze on a piece of paper.
 *   Hint: Draw a 10x10 grid using a pencil and ruler. Then, for each line
 *     of output, erase the wall between the two pairs. For example, if
 *     one line of output is:
 *        0 0 0 1
 *     then erase the wall between cell (0, 0) and cell (0, 1).
 *   Hint 2: While you are working on your code, change ROWS and COLUMNS and
 *    make a 3x3 or 4x4 grid and draw it. That way, you don't spend too much
 *    time drawing the grid in case your code has a bug. 
 *    
 *     
 *   Unless you are doing the extra credit challenge, do NOT add any new methods.
 *   Do not add any new classes.
 *   
 *   For full credit, you must include comments that show the ideas behind your code.
 */

public class Maze {

	/*
	 * Extra credit challenge:
	 * 1. Can you eliminate the Cell class (as well as the "cells" instance variable)
	 * and just use the WeightedQuickUnionUF?
	 * 
	 */
	private class Cell {
		public int row;
		public int column;
		
		public int site() {
			/*
			 * Fill in this method! Write code to translate a pair of ints to an int.
			 */
			return(-1); // REPLACE with your code
		}
		
		public Cell(int r, int c) {
			row = r;
			column = c;
		}
		
		public Vector<Cell> neighbors() {
			Vector<Cell> neighbors = new Vector<>();
			
		/*
		 * Fill in this method! Add all the adjacent
		 * cells to this cell into the "neighbors" vector.
		 */
			
			return neighbors;
		}
	}
	
	private int ROWS = 10;
	private int COLUMNS = 10;
	private Cell[][] cells;
	private WeightedQuickUnionUF uf;
	
	public Maze() {
		uf = new WeightedQuickUnionUF(ROWS * COLUMNS);
		cells = new Cell[ROWS][COLUMNS];
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLUMNS; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}
	
	public Cell start() {
		return cells[0][0];
	}
	
	public Cell end() {
		return cells[ROWS - 1][COLUMNS - 1];
 	}
		
	public static Cell pickRandom(Vector<Cell> cells) {
		return(cells.get(StdRandom.uniform(cells.size())));
	}
	
	private static void printWall(Cell c1, Cell c2) {
		// This prints a single wall that was knocked down.
		System.out.printf("%d %d %d %d\n", c1.row, c1.column, c2.row, c2.column);
	}
	
	public Cell pickRandomCell() {
		/*
		 * Fill in this method! Write the code
		 * to choose a cell randomly.
		 */
		return null; // REPLACE with your code.
	}

	
	public void randomMaze() {
		int startId = start().site();
		int endId = end().site();
		
		/*
		 * Fill in this method! Hints: use uf.connected() and uf.union().
		 */
		
	}	
	
	public static void main(String[] args) {
		Maze m = new Maze();
		m.randomMaze();				
	}
}
