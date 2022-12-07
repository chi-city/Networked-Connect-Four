// --------------------------------------------------

/*
 * Author: Zaid Awaidah
 * 		   Alex Castillo
 * 
 * UIC, Fall 2022
 * CS 342
 *
 * Project 3 - CFourLogic Tests
 * 
*/

// --------------------------------------------------

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

//--------------------------------------------------

public class C4LogicTest {
	// -----------------------------

	private CFourInfo C4_grid_1;
	private int[][] grid_1;
	private String[] temp;

	// -----------------------------

	@BeforeEach
	void setup() {
		C4_grid_1 = new CFourInfo();
		grid_1 = C4_grid_1.getCurrentGrid();
		temp = new String[4];
	}

	// -----------------------------

	@Test
	void board_game_1() {
		// descending diagonal win, player 1
		grid_1[5][0] = 2;
		grid_1[4][0] = 1;
		grid_1[3][0] = 2;
		grid_1[2][0] = 1;

		grid_1[3][1] = 1;
		grid_1[4][1] = 2;
		grid_1[5][1] = 1;

		grid_1[5][2] = 2;
		grid_1[4][2] = 1;

		grid_1[5][3] = 1;

		assertEquals(true, CFourLogic.checkForWin(grid_1, 1, temp), "Expecting descending diagonal win for player 1.");

	}

	// -----------------------------

	@Test
	void board_game_2() {
		// vertical win, player 2
		grid_1[5][0] = 2;
		grid_1[4][0] = 2;
		grid_1[3][0] = 2;
		grid_1[2][0] = 2;

		grid_1[5][1] = 1;
		grid_1[4][1] = 1;
		grid_1[5][2] = 1;
		grid_1[4][2] = 1;

		assertEquals(true, CFourLogic.checkForWin(grid_1, 2, temp), "Expecting vertical win for player 2.");
	}

	// -----------------------------

	@Test
	void board_game_3() {
		// vertical win, player 2
		grid_1[5][0] = 2;
		grid_1[4][0] = 2;
		grid_1[3][0] = 2;
		grid_1[2][0] = 2;

		grid_1[5][1] = 1;
		grid_1[4][1] = 1;
		grid_1[5][2] = 1;
		grid_1[4][2] = 1;

		assertEquals(false, CFourLogic.checkForWin(grid_1, 1, temp), "Expecting loss for player 1.");

	}

	// -----------------------------

	@Test
	void board_game_4() {
		// horizontal win, player 2
		grid_1[5][6] = 2;
		grid_1[5][5] = 2;
		grid_1[5][4] = 2;
		grid_1[5][3] = 2;

		grid_1[5][2] = 1;
		grid_1[4][6] = 1;
		grid_1[4][5] = 1;
		grid_1[4][4] = 1;

		assertEquals(true, CFourLogic.checkForWin(grid_1, 2, temp), "Expecting horizontal win for player 2.");

	}

	// -----------------------------

	@Test
	void board_game_5() {
		// vertical win, player 1
		grid_1[5][6] = 1;
		grid_1[4][6] = 1;
		grid_1[3][6] = 1;
		grid_1[2][6] = 1;

		grid_1[5][0] = 2;
		grid_1[5][1] = 2;
		grid_1[4][0] = 2;
		grid_1[4][1] = 2;

		assertEquals(true, CFourLogic.checkForWin(grid_1, 1, temp), "Expecting vertical win for player 1.");

	}

	// -----------------------------

	@Test
	void board_game_6() {
		// ascending diagonal, player 2
		grid_1[5][0] = 2;
		grid_1[5][1] = 1;
		grid_1[5][2] = 2;
		grid_1[5][3] = 1;

		grid_1[4][1] = 2;
		grid_1[4][2] = 1;
		grid_1[4][3] = 2;

		grid_1[3][2] = 2;
		grid_1[3][3] = 1;

		grid_1[2][3] = 2;

		assertEquals(true, CFourLogic.checkForWin(grid_1, 2, temp), "Expecting ascending diagonal win for player 2.");

	}

	// -----------------------------

	@Test
	void board_game_7() {
		// vertical win, player 2
		grid_1[0][6] = 2;
		grid_1[1][6] = 2;
		grid_1[2][6] = 2;
		grid_1[3][6] = 2;
		grid_1[4][6] = 1;
		grid_1[5][6] = 1;

		assertEquals(true, CFourLogic.checkForWin(grid_1, 2, temp), "Expecting vertical win for player 2.");

	}

	// -----------------------------

	@Test
	void board_game_8() {
		// horizontal win, player 1
		grid_1[3][3] = 1;
		grid_1[3][4] = 1;
		grid_1[3][5] = 1;
		grid_1[3][6] = 1;

		assertEquals(true, CFourLogic.checkForWin(grid_1, 1, temp), "Expecting horizontal win for player 1.");

	}

	// -----------------------------

	@Test
	void board_game_9() {
		// descending diagonal win, player 2
		grid_1[0][0] = 2;
		grid_1[1][1] = 2;
		grid_1[2][2] = 2;
		grid_1[3][3] = 2;

		assertEquals(true, CFourLogic.checkForWin(grid_1, 2, temp), "Expecting descending diagonal win for player 2.");

	}

	// -----------------------------

	@Test
	void board_game_10() {
		// ascending diagonal win, player 1
		grid_1[3][2] = 1;
		grid_1[2][3] = 1;
		grid_1[1][4] = 1;
		grid_1[0][5] = 1;

		assertEquals(true, CFourLogic.checkForWin(grid_1, 1, temp), "Expecting ascending diagonal win for player 2.");

	}

	// -----------------------------
}

//--------------------------------------------------