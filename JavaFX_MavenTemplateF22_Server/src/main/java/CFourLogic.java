// --------------------------------------------------

/*
 * Author: Zaid Awaidah
 * 		   Alex Castillo
 * 
 * UIC, Fall 2022
 * CS 342
 *
 * Project 3 - CFourLogic
 * 
*/

// --------------------------------------------------

public class CFourLogic {
	// -------------------------

	private static int Height, Width;
	private static int player;

	// -------------------------

	/*
	 * checkForWin
	 * 
	 * checks for win following connect4 rules
	 */
	public static boolean checkForWin(int[][] currentGrid, int activePlayer, String[] winningMove) {
		// Grid is 7 columns by 6 rows
		Width = currentGrid.length;
		Height = currentGrid[0].length;
		player = activePlayer;

		// horizontal check
		if (checkHorizontal(currentGrid, winningMove))
			return true;
		// vertical check
		else if (checkVertical(currentGrid, winningMove))
			return true;
		// ascending diagonal check
		else if (checkAscDiagnoal(currentGrid, winningMove))
			return true;
		// descending diagonal check
		else if (checkDescDiagnoal(currentGrid, winningMove))
			return true;

		// No win
		return false;
	}

	// -------------------------

	/* checkHorizontal */
	private static boolean checkHorizontal(int[][] currentGrid, String[] winningMove) {
		for (int j = 0; j < Height - 3; j++) {
			for (int i = 0; i < Width; i++) {
				if (currentGrid[i][j] == player && currentGrid[i][j + 1] == player && currentGrid[i][j + 2] == player
						&& currentGrid[i][j + 3] == player) {
					winningMove[0] = String.valueOf(i) + "," + String.valueOf(j);
					winningMove[1] = String.valueOf(i) + "," + String.valueOf((j + 1));
					winningMove[2] = String.valueOf(i) + "," + String.valueOf((j + 2));
					winningMove[3] = String.valueOf(i) + "," + String.valueOf((j + 3));
					return true;
				}
			}
		}
		return false;
	}

	// -------------------------

	/* checkVertical */
	private static boolean checkVertical(int[][] currentGrid, String[] winningMove) {
		for (int i = 0; i < Width - 3; i++) {
			for (int j = 0; j < Height; j++) {
				if (currentGrid[i][j] == player && currentGrid[i + 1][j] == player && currentGrid[i + 2][j] == player
						&& currentGrid[i + 3][j] == player) {
					winningMove[0] = String.valueOf(i) + "," + String.valueOf(j);
					winningMove[1] = String.valueOf((i + 1)) + "," + String.valueOf(j);
					winningMove[2] = String.valueOf((i + 2)) + "," + String.valueOf(j);
					winningMove[3] = String.valueOf((i + 3)) + "," + String.valueOf(j);
					return true;
				}
			}
		}
		return false;
	}

	// -------------------------

	/* checkAscDiagnoal */
	private static boolean checkAscDiagnoal(int[][] currentGrid, String[] winningMove) {
		for (int i = 3; i < Width; i++) {
			for (int j = 0; j < Height - 3; j++) {
				if (currentGrid[i][j] == player && currentGrid[i - 1][j + 1] == player
						&& currentGrid[i - 2][j + 2] == player && currentGrid[i - 3][j + 3] == player) {
					winningMove[0] = String.valueOf(i) + "," + String.valueOf(j);
					winningMove[1] = String.valueOf((i - 1)) + "," + String.valueOf((j + 1));
					winningMove[2] = String.valueOf((i - 2)) + "," + String.valueOf((j + 2));
					winningMove[3] = String.valueOf((i - 3)) + "," + String.valueOf((j + 3));
					return true;
				}
			}
		}
		return false;
	}

	// -------------------------

	/* checkDescDiagnoal */
	private static boolean checkDescDiagnoal(int[][] currentGrid, String[] winningMove) {
		for (int i = 3; i < Width; i++) {
			for (int j = 3; j < Height; j++) {
				if (currentGrid[i][j] == player && currentGrid[i - 1][j - 1] == player
						&& currentGrid[i - 2][j - 2] == player && currentGrid[i - 3][j - 3] == player) {
					winningMove[0] = String.valueOf(i) + "," + String.valueOf(j);
					winningMove[1] = String.valueOf((i - 1)) + "," + String.valueOf((j - 1));
					winningMove[2] = String.valueOf((i - 2)) + "," + String.valueOf((j - 2));
					winningMove[3] = String.valueOf((i - 3)) + "," + String.valueOf((j - 3));
					return true;
				}
			}
		}
		return false;
	}

	// -------------------------
}

//--------------------------------------------------
