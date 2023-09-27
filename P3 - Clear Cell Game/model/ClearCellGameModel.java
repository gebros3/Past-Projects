package model;

import java.util.Random;

/**
 * This class extends GameModel and implements the logic of the clear cell game,
 * specifically.
 * 
 * @author Dept of Computer Science, UMCP
 */

public class ClearCellGameModel extends GameModel {
	//Score keeps track of the player's score
	private int score;
	Random random;

	/* Include whatever instance variables you think are useful. */

	/**
	 * Defines a board with empty cells. It relies on the super class constructor to
	 * define the board.
	 * 
	 * @param rows   number of rows in board
	 * @param cols   number of columns in board
	 * @param random random number generator to be used during game when rows are
	 *               randomly created
	 */
	public ClearCellGameModel(int rows, int cols, Random random) {
		super(rows, cols);
		this.random = random;
		score = 0;
	}

	/**
	 * The game is over when the last row (the one with index equal to
	 * board.length-1) contains at least one cell that is not empty.
	 */
	//returns true if one of the cells is not empty
	public boolean isGameOver() {
		//loops through the last row, returns true if boardcell isn't empty
		for (BoardCell b : super.getBoard()[board.length - 1]) {
			if (b != BoardCell.EMPTY) {
				return true;
			}
		}
		return false;

	}

	/**
	 * Returns the player's score. The player should be awarded one point for each
	 * cell that is cleared.
	 * 
	 * @return player's score
	 */
	//returns score
	public int getScore() {
		return score;
	}

	/**
	 * This method must do nothing in the case where the game is over.
	 * 
	 * As long as the game is not over yet, this method will do the following:
	 * 
	 * 1. Shift the existing rows down by one position. 2. Insert a row of random
	 * BoardCell objects at the top of the board. The row will be filled from left
	 * to right with cells obtained by calling
	 * BoardCell.getNonEmptyRandomBoardCell(). (The Random number generator passed
	 * to the constructor of this class should be passed as the argument to this
	 * method call.)
	 */
	public void nextAnimationStep() {
		BoardCell[][] temp = new BoardCell[super.getRows()][super.getCols()];
		
		if (!isGameOver()) {
			//sets first row to random board values
			for (int i = 0; i < temp[0].length; i++) {
				temp[0][i] = BoardCell.getNonEmptyRandomBoardCell(random);
			}
			/*loops through the rest of the board, setting the values of each
			 *  cell to the row above
			 */
			for (int i = 1; i < super.getBoard().length; i++) {
				for (int j = 0; j < super.getBoard()[i].length; j++) {
					temp[i][j] = super.getBoardCell(i - 1, j);
				}
			}
			super.board = temp;
		}

	}

	/**
	 * This method is called when the user clicks a cell on the board. If the
	 * selected cell is not empty, it will be set to BoardCell.EMPTY, along with any
	 * adjacent cells that are the same color as this one. (This includes the cells
	 * above, below, to the left, to the right, and all in all four diagonal
	 * directions.)
	 * 
	 * If any rows on the board become empty as a result of the removal of cells
	 * then those rows will "collapse", meaning that all non-empty rows beneath the
	 * collapsing row will shift upward.
	 * 
	 * @throws IllegalArgumentException with message "Invalid row index" for invalid
	 *                                  row or "Invalid column index" for invalid
	 *                                  column. We check for row validity first.
	 */
	public void processCell(int rowIndex, int colIndex) {
		//throws exceptions if rowIndex and colIndex are out of bounds
		if (rowIndex >= super.getRows()) {
			throw new IllegalArgumentException("Invalid row index");
		}
		if (colIndex >= super.getCols()) {
			throw new IllegalArgumentException("Invalid column index");
		}
		BoardCell color = super.board[rowIndex][colIndex];
		/*loops through a 1 cell radius surrounding the parameter, if it's the
		same color, score increments
		*/
		if (super.board[rowIndex][colIndex] != BoardCell.EMPTY) {
			for (int i = rowIndex - 1; i <= rowIndex + 1; i++) {
				for (int j = colIndex - 1; j <= colIndex + 1; j++) {
					if (i >= 0 && i <= super.getBoard().length - 1 && j >=
							0 && j <= super.getBoard()[0].length - 1) {
						if (super.getBoardCell(i, j) == color) {
							super.board[i][j] = BoardCell.EMPTY;
							score++;
						}
					}
				}
			}
		}
		//for loop collapse row, sets last row to empty
		for (int i = 0; i < super.getBoard().length-1; i++) {
			if (rowIsEmpty(super.getBoard()[i])) {
				for (int row = i; row < super.getBoard().length-1; row++) {
					for (int col = 0; col < super.getBoard()[i].length; col++) {
						super.board[row][col] = super.board[row+1][col];

					}
				}
				for (int col = 0; col < super.getBoard()[0].length; col++) {
					super.board[super.getBoard().length-1][col] = BoardCell.EMPTY;
				}
			}
		}

	}
	//helper method, checks to see if the row is completely empty
	private static boolean rowIsEmpty(BoardCell[] row) {
		boolean rowEmpty = true;
		for (BoardCell b: row) {
			if (b != BoardCell.EMPTY) {
				rowEmpty = false;
			}
		}
		return rowEmpty;
	}

}