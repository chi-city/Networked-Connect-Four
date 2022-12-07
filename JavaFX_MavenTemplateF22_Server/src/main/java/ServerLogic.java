// --------------------------------------------------

/*
 * Author: Zaid Awaidah
 * 
 * 
 * UIC, Fall 2022
 * CS 342
 *
 * Project 3 - ServerLogic
 * 
*/

// -------------------------------------------------

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

//-------------------------------------------------

public class ServerLogic {
	// -------------------------

	/*
	 * initial_update
	 * 
	 * sends initial updates to both clients
	 * 
	 */
	public static void initial_update(Consumer<Serializable> callback, ArrayList<Server.ClientThread> clients,
			Server.ClientThread client_1, Server.ClientThread client_2) {

		// Let client 1 begin
		if (clients.size() == 2) {
			callback.accept("There are two players. Staring game.");
			System.out.println("Client 1: " + client_1.client_data.client_ID);
			System.out.println("Client 2: " + client_2.client_data.client_ID);

			client_1.client_data = new CFourInfo("There are two players, starting game. Player 1 goes first", "C1P1", 1,
					0, 0, false);
			client_1.client_data.setNumPlayers(true);

			client_2.client_data.setNumPlayers(false);
			client_2.client_data.setMessage("There are two players, starting game. Player 1 goes first.");

			// send initial updates
			try {
				client_1.out.writeObject(client_1.client_data);
				client_2.out.writeObject(client_2.client_data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
	}

	// -------------------------

	/*
	 * client_leaves
	 * 
	 * handles client leaving
	 * 
	 */
	public static void client_leaves(Consumer<Serializable> callback, ArrayList<Server.ClientThread> clients,
			Server.ClientThread client, Server server, CFourInfo update) {
		callback.accept("OOOOPPs...Something wrong with the socket from client: " + client.count + "....closing down!");
		callback.accept("Player: " + client.players + " has left the game.");

		server.count--;
		server.player--;
		client.players--;
		clients.remove(client);

		// Send update to remaining client
		if (clients.size() != 0) {

			String message = "Player " + client.client_data.getCurrentPlayer()
					+ " has left the game. Please wait while someone reconnects.";

			clients.get(0).client_data = new CFourInfo(message, "C1P1", 1, 0, 0, false);
			clients.get(0).client_data.setMoveCounter(0);

			try {
				clients.get(0).out.writeObject(clients.get(0).client_data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return;
	}

	// -------------------------

	/*
	 * run_game
	 * 
	 * sends updates to each client individually and handles win checking
	 */
	public static void run_game(int turn, ArrayList<Server.ClientThread> clients, Server.ClientThread client_1,
			Server.ClientThread client_2, CFourInfo update, Consumer<Serializable> callback) {

		// check current server grid for win
		boolean win = CFourLogic.checkForWin(update.getCurrentGrid(), turn, update.getWinMove());
		CFourInfo data_1 = client_1.client_data;
		CFourInfo data_2 = client_2.client_data;

		// Player 1s Turn
		if (turn == 1) {
			if (win) {
				String winning_move = " Winning moves(Row,Col): " + "(" + update.getWinMove()[0] + "), " + "("
						+ update.getWinMove()[1] + "), " + "(" + update.getWinMove()[2] + "), " + "("
						+ update.getWinMove()[3] + ").";

				// Reflect Client Changes
				client_1.client_data = new CFourInfo("Player 1 Wins." + winning_move, data_1.client_ID,
						data_1.getCurrentPlayer(), data_1.getMoveX(), data_1.getMoveY(), data_1.getWinCheck());
				client_1.client_data.setNumPlayers(false);
				client_1.client_data.setWinCheck(true);
				client_1.client_data.setWinner(turn);
				client_1.client_data.setMoveCounter(update.getMoveCounter());
				client_1.client_data.setWinMove(update.getWinMove());

				client_2.client_data = new CFourInfo("Player 1 Wins." + winning_move, data_2.client_ID,
						data_2.getCurrentPlayer(), data_2.getMoveX(), data_2.getMoveY(), data_2.getWinCheck());
				client_2.client_data.setNumPlayers(false);
				client_2.client_data.setWinCheck(true);
				client_2.client_data.setWinner(turn);
				client_2.client_data.setMoveCounter(update.getMoveCounter());
				client_2.client_data.setWinMove(update.getWinMove());

				callback.accept("Player 1 Wins." + winning_move);

			} else {
				// Reflect Client Changes
				client_1.client_data = new CFourInfo(data_1.getMessage(), data_1.client_ID, data_1.getCurrentPlayer(),
						data_1.getMoveX(), data_1.getMoveY(), data_1.getWinCheck());
				client_1.client_data.setNumPlayers(false);
				client_1.client_data.setMoveCounter(update.getMoveCounter());

				client_2.client_data = new CFourInfo(data_1.getMessage(), data_2.client_ID, data_2.getCurrentPlayer(),
						data_2.getMoveX(), data_2.getMoveY(), data_2.getWinCheck());
				client_2.client_data.setNumPlayers(true);
				client_2.client_data.setMoveCounter(update.getMoveCounter());
			}

			// update turn
			turn = 2;

			// Player 2s Turn
		} else if (turn == 2) {
			if (win) {
				String winning_move = " Winning moves(Row,Col): " + "(" + update.getWinMove()[0] + "), " + "("
						+ update.getWinMove()[1] + "), " + "(" + update.getWinMove()[2] + "), " + "("
						+ update.getWinMove()[3] + ").";

				client_1.client_data = new CFourInfo("Player 2 Wins." + winning_move, data_1.client_ID,
						data_1.getCurrentPlayer(), data_1.getMoveX(), data_1.getMoveY(), data_1.getWinCheck());
				client_1.client_data.setNumPlayers(false);
				client_1.client_data.setWinCheck(true);
				client_1.client_data.setWinner(turn);
				client_1.client_data.setMoveCounter(update.getMoveCounter());
				client_1.client_data.setWinMove(update.getWinMove());

				client_2.client_data = new CFourInfo("Player 2 Wins." + winning_move, data_2.client_ID,
						data_2.getCurrentPlayer(), data_2.getMoveX(), data_2.getMoveY(), data_2.getWinCheck());
				client_2.client_data.setNumPlayers(false);
				client_2.client_data.setWinCheck(true);
				client_2.client_data.setWinner(turn);
				client_2.client_data.setMoveCounter(update.getMoveCounter());
				client_2.client_data.setWinMove(update.getWinMove());

				callback.accept("Player 2 Wins." + winning_move);

			} else {
				// Reflect Client Changes
				client_1.client_data = new CFourInfo(data_2.getMessage(), data_1.client_ID, data_1.getCurrentPlayer(),
						data_1.getMoveX(), data_1.getMoveY(), data_1.getWinCheck());
				client_1.client_data.setNumPlayers(true);
				client_1.client_data.setMoveCounter(update.getMoveCounter());

				client_2.client_data = new CFourInfo(data_2.getMessage(), data_2.client_ID, data_2.getCurrentPlayer(),
						data_2.getMoveX(), data_2.getMoveY(), data_2.getWinCheck());
				client_2.client_data.setNumPlayers(false);
				client_2.client_data.setMoveCounter(update.getMoveCounter());
			}

			// update turn
			turn = 1;
		}

		// Update Grids
		for (int i = 0; i < update.currentGrid.length; i++) {
			for (int j = 0; j < update.currentGrid[i].length; j++) {
				client_1.client_data.currentGrid[i][j] = update.currentGrid[i][j];
				client_2.client_data.currentGrid[i][j] = update.currentGrid[i][j];
			}
		}

		// Tie Condition
		if (update.getMoveCounter() == 42 && !win) {
			client_1.client_data = new CFourInfo("Tie game was reached. No one wins.", data_1.client_ID,
					data_1.getCurrentPlayer(), data_1.getMoveX(), data_1.getMoveY(), data_1.getWinCheck());
			client_1.client_data.setNumPlayers(false);
			client_1.client_data.setWinCheck(true);
			client_1.client_data.setWinner(0);
			client_2.client_data.setMoveCounter(update.getMoveCounter());
			client_1.client_data.setWinMove(null);

			client_2.client_data = new CFourInfo("Tie game was reached. No one wins.", data_2.client_ID,
					data_2.getCurrentPlayer(), data_2.getMoveX(), data_2.getMoveY(), data_2.getWinCheck());
			client_2.client_data.setNumPlayers(false);
			client_2.client_data.setWinCheck(true);
			client_2.client_data.setWinner(0);
			client_2.client_data.setMoveCounter(update.getMoveCounter());
			client_2.client_data.setWinMove(null);

			callback.accept("TIE GAME WAS REACHED. WAITING FOR PLAYER REPLIES.");
		}

		try {
			client_1.out.writeObject(client_1.client_data);
			client_2.out.writeObject(client_2.client_data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -------------------------
}

// -------------------------------------------------