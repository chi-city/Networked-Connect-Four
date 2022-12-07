// --------------------------------------------------

/*
 * Author: Zaid Awaidah
 * 		   Alex Castillo
 * 
 * UIC, Fall 2022
 * CS 342
 *
 * Project 3 - Server
 * 
*/

// --------------------------------------------------

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

//--------------------------------------------------

public class Server {
	// -------------------------

	/* Locals */
	public int count = 1;
	public int player = 1;
	public Consumer<Serializable> callback;
	public int turn;
	public ClientThread client_1;
	public ClientThread client_2;
	public int[][] grid;
	public CFourInfo server_data;

	private TheServer server;
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	public ArrayList<CFourInfo> clients_data = new ArrayList<CFourInfo>();

	// -------------------------

	/* Constructor */
	Server(int port, Consumer<Serializable> call) {
		this.callback = call;
		this.server = new TheServer(port);
		this.server.start();
	}

	// -------------------------

	/*
	 * TheServer
	 * 
	 * Inner class that operates the server
	 * 
	 */
	public class TheServer extends Thread {
		// -------------------------

		/* Locals */
		private int port;

		// -------------------------

		/* Constructor */
		TheServer(int port) {
			this.port = port;
		}

		// -------------------------

		/* Server run */
		public void run() {
			try (ServerSocket socket = new ServerSocket(this.port);) {
				System.out.println("Server is waiting for a client!");
				System.out.println("Server is running on port: " + this.port);

				while (true) {
					ClientThread c = new ClientThread(socket.accept(), count, player);
					callback.accept("client has connected to server: " + "client #" + count);
					clients.add(c);

					c.start();
					count++;
					player++;
				}

			} catch (Exception e) {
				System.out.println("Could not start server.");
			}

		}
	}

	// -------------------------

	/* ClientThread inner class */
	class ClientThread extends Thread {
		// -------------------------

		/* Locals */
		public int count;
		public int players;
		public CFourInfo client_data;

		private Socket connection;
		private ObjectInputStream in;
		public ObjectOutputStream out;

		// -------------------------

		/* Constructor */
		ClientThread(Socket s, int count, int player) {
			this.connection = s;
			this.count = count;
			this.players = player;
		}

		// -------------------------

		/*
		 * clientInfo
		 * 
		 * Send updates to both clients
		 */
		public void clientInfo(CFourInfo data) {
			for (int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				if (t.count == 1) {
					data.client_ID = "C1P1";
					data.setCurrentPlayer(1);
				} else if (t.count == 2) {
					data.client_ID = "C2P2";
					data.setCurrentPlayer(2);
				}

				try {
					t.out.writeObject(data);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// -------------------------

		/* ClientThread run */
		public void run() {
			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			} catch (Exception e) {
				System.out.println("Streams not open");
			}

			// Clients join
			if (count == 1) {
				clients.get(0).client_data = new CFourInfo("", "C1P1", 1, 0, 0, false);
				clientInfo(new CFourInfo("Player 1 Joined.", "C1P1", 1, 0, 0, false));
			} else if (count == 2) {
				clients.get(1).client_data = new CFourInfo("", "C2P2", 2, 0, 0, false);
				clientInfo(new CFourInfo("Player 2 Joined.", "C2P2", 2, 0, 0, false));
			}
			callback.accept("client: " + count + " started player: " + players);

			// Send starting updates
			if (count == 2) {
				client_1 = clients.get(0);
				client_2 = clients.get(1);
				ServerLogic.initial_update(callback, clients, client_1, client_2);
			}

			while (true) {
				try {
					// Receive CFour
					CFourInfo server_data_new = (CFourInfo) in.readObject();
					Server.this.turn = server_data_new.getCurrentPlayer();

					// Active Game
					if (clients.size() == 2 && !server_data_new.playAgain) {
						String message;

						if (server_data_new.getCurrentPlayer() == 1) {
							message = "Player " + server_data_new.getCurrentPlayer() + " move: (row: "
									+ server_data_new.getMoveX() + " col: " + server_data_new.getMoveY() + ").";
							client_1.client_data.setMessage(message);
							client_1.client_data.client_ID = server_data_new.client_ID;
							client_1.client_data.setMoveX(server_data_new.getMoveX());
							client_1.client_data.setMoveY(server_data_new.getMoveY());
							client_1.client_data.setWinCheck(server_data_new.getWinCheck());
							client_1.client_data.setMoveCounter(server_data_new.getMoveCounter());
							client_1.client_data.setNumPlayers(server_data_new.enoughPlayersToStart());
						} else if (server_data_new.getCurrentPlayer() == 2) {
							message = "Player " + server_data_new.getCurrentPlayer() + " move: (row: "
									+ server_data_new.getMoveX() + " col: " + server_data_new.getMoveY() + ").";
							client_2.client_data.setMessage(message);
							client_2.client_data.client_ID = server_data_new.client_ID;
							client_2.client_data.setMoveX(server_data_new.getMoveX());
							client_2.client_data.setMoveY(server_data_new.getMoveY());
							client_2.client_data.setWinCheck(server_data_new.getWinCheck());
							client_2.client_data.setMoveCounter(server_data_new.getMoveCounter());
							client_2.client_data.setNumPlayers(server_data_new.enoughPlayersToStart());
						}
						callback.accept("Player " + server_data_new.getCurrentPlayer() + " move: (row: "
								+ server_data_new.getMoveX() + " col: " + server_data_new.getMoveY() + ").");

						server_data = server_data_new;

						ServerLogic.run_game(Server.this.turn, clients, client_1, client_2, server_data_new, callback);
						// Play Again
					} else if (clients.size() == 2 && server_data_new.playAgain) {
						if (server_data_new.getCurrentPlayer() == 1) {
							client_1.client_data = new CFourInfo("Player 1 Joined", server_data_new.client_ID, 1, 0, 0,
									false);
							client_1.client_data.setWinMove(null);

							if (client_2.client_data.getMoveCounter() == 0) {
								String message = "Player 1 Joined. There are two players. Player 1 goes first.";
								client_1.client_data.setNumPlayers(true);
								client_1.client_data.setMessage(message);
							}

							client_1.out.writeObject(client_1.client_data);

						} else if (server_data_new.getCurrentPlayer() == 2) {
							String message = "Player 2 Joined.";
							client_2.client_data = new CFourInfo(message, server_data_new.client_ID, 2, 0, 0, false);
							client_2.client_data.setWinMove(null);

							if (client_1.client_data.getMoveCounter() == 0) {
								client_1.client_data.setNumPlayers(true);
								client_1.client_data.setMoveCounter(0);
								ServerLogic.run_game(2, clients, client_1, client_2, server_data_new, callback);
							} else {
								client_2.out.writeObject(client_2.client_data);
							}
						}

						server_data = server_data_new;
					}

					// Display new game message
					if (client_1.client_data.getMoveCounter() == 0 && client_2.client_data.getMoveCounter() == 0) {
						callback.accept("Starting a new game.");
					}

				} catch (Exception e) {
					e.printStackTrace();

					ServerLogic.client_leaves(callback, clients, this, Server.this, server_data);
					break;
				}
			}

		}

		// -------------------------
	}

// -------------------------
}

// --------------------------------------------------
