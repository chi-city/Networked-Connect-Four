//--------------------------------------------------

/*
 * Author: Zaid Awaidah
 * 		   Alex Castillo
 * 
 * UIC, Fall 2022
 * CS 342
 *
 * Project 3 - Client CFourInfo
 * 
*/

//--------------------------------------------------

import java.io.Serializable;

// --------------------------------------------------

public class CFourInfo implements Serializable {
	// -------------------------

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	// -------------------------

	/* Locals */
	private String message;
	private int moveX;
	private int moveY;
	private boolean have2players;
	private int moveCounter;
	private boolean win_check;
	private int winner;
	private int currentPlayer;
	private String[] winning_move;

	public boolean playAgain;
	public String client_ID;
	public int[][] currentGrid;

	// -------------------------

	/* constructor */
	CFourInfo() {
		this.message = new String();
		this.have2players = false;
		this.win_check = false;
		this.moveX = 0;
		this.moveY = 0;
		this.winner = 0;
		this.moveCounter = 0;
		this.currentPlayer = 0;
		this.currentGrid = new int[6][7];
		this.winning_move = new String[4];
		this.playAgain = false;

		for (int i = 0; i < this.currentGrid.length; i++)
			for (int j = 0; j < this.currentGrid[i].length; j++)
				this.currentGrid[i][j] = 0;
	}

	// -------------------------

	/* constructor */
	CFourInfo(String message, String ID, int player, int move_x, int move_y, boolean win) {
		this.client_ID = ID;
		this.message = message;
		this.have2players = false;
		this.win_check = win;
		this.currentPlayer = player;
		this.moveX = move_x;
		this.moveY = move_y;
		this.winning_move = new String[4];
		this.playAgain = false;

		if (this.currentGrid == null) {
			this.currentGrid = new int[6][7];

			for (int i = 0; i < this.currentGrid.length; i++)
				for (int j = 0; j < this.currentGrid[i].length; j++)
					this.currentGrid[i][j] = 0;
		}

	}

	// -------------------------

	/* setMoveX */
	public void setMoveX(int move_x) {
		this.moveX = move_x;
	}

	// -------------------------

	/* setMoveY */
	public void setMoveY(int move_y) {
		this.moveY = move_y;
	}

	// -------------------------

	/* setNumPlayers */
	public void setNumPlayers(boolean two_players) {
		this.have2players = two_players;
	}

	// -------------------------

	/* setWinCheck */
	public void setWinCheck(boolean check_game) {
		this.win_check = check_game;
	}

	// -------------------------

	/* setWinMove */
	public void setWinMove(String[] win_move) {
		this.winning_move = win_move;
	}

	// -------------------------

	/* setCurrentPlayer */
	public void setCurrentPlayer(int current_player) {
		this.currentPlayer = current_player;
	}

	// -------------------------

	/* setWinner */
	public void setWinner(int winner) {
		this.winner = winner;
	}

	// -------------------------

	/* setMoveCounter */
	public void setMoveCounter(int counter) {
		this.moveCounter = counter;
	}

	// -------------------------

	/* setMessage */
	public void setMessage(String message) {
		this.message = message;
	}

	// -------------------------

	/* setGridPosition */
	public void setGridPosition(int row, int col, int value) {
		this.currentGrid[row][col] = value;
	}
	// -------------------------

	/* enoughPlayersToStart */
	public boolean enoughPlayersToStart() {
		return have2players;
	}

	// -------------------------

	/* getCurrentGrid */
	public int[][] getCurrentGrid() {
		return currentGrid;
	}

	// -------------------------

	/* getMoveY */
	public int getMoveY() {
		return this.moveY;
	}

	// -------------------------

	/* getMoveX */
	public int getMoveX() {
		return this.moveX;
	}

	// -------------------------

	/* getWinner */
	public int getWinner() {
		return this.winner;
	}

	// -------------------------

	/* getWinCheck */
	public boolean getWinCheck() {
		return this.win_check;
	}

	// -------------------------

	/* getCurrentPlayer */
	public int getCurrentPlayer() {
		return this.currentPlayer;
	}

	// -------------------------

	/* getMoveCounter */
	public int getMoveCounter() {
		return this.moveCounter;
	}

	// -------------------------

	/* getMessage */
	public String getMessage() {
		return this.message;
	}

	// -------------------------

	/* getWinMove */
	public String[] getWinMove() {
		return this.winning_move;
	}

	// -------------------------
}

//--------------------------------------------------
