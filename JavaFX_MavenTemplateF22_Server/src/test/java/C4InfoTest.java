// --------------------------------------------------

/*
 * Author: Zaid Awaidah
 *
 * UIC, Fall 2022
 * CS 342
 *
 * Project 3 - CFourInfo Tests
 *
 */

// --------------------------------------------------

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;

//--------------------------------------------------

public class C4InfoTest {
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
	void test1() {
		CFourInfo test = new CFourInfo();
		assertEquals(false, test.getWinCheck(), "Expecting false, there should be no win.");

		test.setWinCheck(true);
		assertEquals(true, test.getWinCheck(), "Expecting true, there should be a win now.");
	}

	// -----------------------------

	@Test
	void test2() {
		CFourInfo test = new CFourInfo();

		assertEquals(0, test.getCurrentPlayer(), "Since both players have not joined, current player should be 0.");
	}

	// -----------------------------

	@Test
	void test3() {
		CFourInfo test = new CFourInfo();
		assertEquals(0, test.getMoveCounter(), "No moves have been made, should be 0.");

		test.setMoveCounter(test.getMoveCounter() + 1);
		assertEquals(1, test.getMoveCounter(), "A move has been made, should be 1.");
	}

	// -----------------------------

	@Test
	void test4() {
		CFourInfo test = new CFourInfo();
		test.setMessage("Testing...");

		assertEquals("Testing...", test.getMessage(), "Expecting 'Testing...'.");
	}

	// -----------------------------

	@Test
	void test5() {
		CFourInfo test = new CFourInfo();
		test.setCurrentPlayer(2);

		assertEquals(2, test.getCurrentPlayer(), "Expecting 2.");
	}

	// -----------------------------

	@Test
	void test6() {
		CFourInfo test = new CFourInfo();
		int[][] tempGrid = test.getCurrentGrid();
		int[][] tempGrid2 = new int[6][7];

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				assertEquals(tempGrid2[i][j], tempGrid[i][j], "Should be empty.");
			}
		}
	}

	// -----------------------------

	@Test
	void test7() {
		CFourInfo test = new CFourInfo();
		test.setGridPosition(1, 1, 1);

		assertEquals(1, test.getCurrentGrid()[1][1], "Expecting 1.");
	}

	// -----------------------------

	@Test
	void test8() {
		CFourInfo test = new CFourInfo();
		ArrayIndexOutOfBoundsException thrown = Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
			test.setGridPosition(10, 10, 50);
		}, "Expected out of bounds exception.");
	}

	// -----------------------------

	@Test
	void test9() {
		CFourInfo test = new CFourInfo();
		ArrayIndexOutOfBoundsException thrown = Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
			test.setGridPosition(-5, 1, 1);
		}, "Expected out of bounds exception.");

	}

	// -----------------------------

	@Test
	void test10() {
		CFourInfo test = new CFourInfo();
		test.setWinner(2);

		assertEquals(2, test.getWinner(), "Expecting winner to be player 2.");

	}

	// -----------------------------
}

//--------------------------------------------------
