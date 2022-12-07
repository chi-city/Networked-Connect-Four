// --------------------------------------------------

/*
 * Author: Zaid Awaidah
 * 
 * UIC, Fall 2022
 * CS 342
 *
 * Project 3 - GameButton
 * 
*/

// --------------------------------------------------

import javafx.scene.control.Button;

// --------------------------------------------------

public class GameButton extends Button {
	// -------------------------

	/* Locals */
	private int row;
	private int col;

	// -------------------------

	/* Constructor */
	public GameButton() {
		this.setMinHeight(75);
		this.setMinWidth(75);
		this.setStyle("-fx-background-color: lightgrey");
	}

	// -------------------------

	/*
	 * setPosition
	 * 
	 * sets button position
	 */
	public void setPosition(int row, int col) {
		this.row = row;
		this.col = col;
	}

	// -------------------------

	/* getRow */
	public int getRow() {
		return this.row;
	}

	// -------------------------

	/* getCol */
	public int getCol() {
		return this.col;
	}

	// -------------------------
}

// --------------------------------------------------
