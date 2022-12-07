// --------------------------------------------------

/*
 * Author: Zaid Awaidah
 * 		   Alex Castillo
 * 
 * UIC, Fall 2022
 * CS 342
 *
 * Project 3 - Client GUI
 * 
*/

// --------------------------------------------------

import javafx.application.Application;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.*;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.scene.control.*;

import javafx.animation.PauseTransition;

// --------------------------------------------------

public class Connect4Client extends Application {
	// -------------------------

	/* Locals */

	// Networking objects
	Stage gameplayStage = new Stage();
	Client clientConnection;
	Socket socketClient;
	Consumer<Serializable> callback;
	ObjectOutputStream out;
	ObjectInputStream in;

	private int player = 1;
	private String player1Color = "red";
	private String player2Color = "blue";

	// Welcome screen objects
	private Image logoImage;
	private ImageView logoImageView;
	private VBox welcomeImageAndMsgVbox;
	private VBox welcomeVbox;
	private Label welcomeLabel;
	private Label infoLabel;
	private TextField ipAddressTextField;
	private TextField portTextField;
	private Button customizeButton;
	private Button connectToServerButton;

	// Customize screen objects
	private VBox customizeVbox;
	private Button changeBackgroundButton;
	private Button exitCustomizeButton;

	// Game-play Screen objects
	private int counterForBackgroundSelection = 1;
	private VBox gameplayMasterVbox;
	private Label playerIdentifierLocalLabel;
	private Button exitGameButton;
	private ListView<String> gameInfoListView;
	private GridPane gameboardGridPane = new GridPane();
	private GameButton r0c0;
	private GameButton r0c1;
	private GameButton r0c2;
	private GameButton r0c3;
	private GameButton r0c4;
	private GameButton r0c5;
	private GameButton r0c6;
	private GameButton r1c0;
	private GameButton r1c1;
	private GameButton r1c2;
	private GameButton r1c3;
	private GameButton r1c4;
	private GameButton r1c5;
	private GameButton r1c6;
	private GameButton r2c0;
	private GameButton r2c1;
	private GameButton r2c2;
	private GameButton r2c3;
	private GameButton r2c4;
	private GameButton r2c5;
	private GameButton r2c6;
	private GameButton r3c0;
	private GameButton r3c1;
	private GameButton r3c2;
	private GameButton r3c3;
	private GameButton r3c4;
	private GameButton r3c5;
	private GameButton r3c6;
	private GameButton r4c0;
	private GameButton r4c1;
	private GameButton r4c2;
	private GameButton r4c3;
	private GameButton r4c4;
	private GameButton r4c5;
	private GameButton r4c6;
	private GameButton r5c0;
	private GameButton r5c1;
	private GameButton r5c2;
	private GameButton r5c3;
	private GameButton r5c4;
	private GameButton r5c5;
	private GameButton r5c6;
	private ArrayList<GameButton> game_buttons;

	// Results Screen objects
	private VBox resultsVbox;
	private Label resultsLabel;
	private Image resultsImage;
	private ImageView resultsImageView;
	private Button playAgainButton;
	private Button exitResultsButton;

	private String player_move = new String();
	private CFourInfo server_update;

	private PauseTransition pause;
	private Pair<Integer, Integer> pair;
	private ArrayList<Pair<Integer, Integer>> moves;

	// -------------------------

	/*
	 * process_win
	 * 
	 *
	 * 
	 * Handles client win
	 */
	public void process_win(CFourInfo new_data) {
		// Update Board
		for (GameButton button : game_buttons) {
			if (new_data.currentGrid[button.getRow()][button.getCol()] != 0) {
				color_buttons(button);
			}
		}

		// Retrieve winning moves
		if (new_data.getWinMove() != null) {
			moves = new ArrayList<Pair<Integer, Integer>>();
			for (String move : new_data.getWinMove()) {
				pair = new Pair<>(Integer.parseInt(String.valueOf(move.charAt(0))),
						Integer.parseInt(String.valueOf(move.charAt(2))));
				moves.add(pair);
			}
			System.out.println("HERE:" + moves);
		}

		int counter = 0;

		// Highlight winning buttons
		for (GameButton button : game_buttons) {
			if (counter != 4) {
				for (Pair<Integer, Integer> I : moves) {
					if (button.getRow() == I.getKey() && button.getCol() == I.getValue()) {
						button.setStyle("-fx-background-color: darkgreen;");
						counter++;
					}
				}
			}
		}

		// Transition to result scene
		pause = new PauseTransition(Duration.seconds(3));
		pause.play();
		pause.setOnFinished(a -> {
			displayResults(new_data);
		});
	}

	// -------------------------

	/* color_buttons */
	private void color_buttons(Button button) {
		if (button.getStyle().length() > 30) {
			if (player == 1) {
				button.setStyle("-fx-background-color: " + player2Color + ";");
			} else if (player == 2) {
				button.setStyle("-fx-background-color: " + player1Color + ";");
			}
		}
		button.setDisable(true);
	}

	// -------------------------

	/*
	 * displayResults
	 * 
	 * Displays results scene
	 */
	private void displayResults(CFourInfo data) {
		// Result scene components
		if (clientConnection.data.getWinner() != 0)
			resultsLabel = new Label(
					"Game over! The results are...\nplayer " + clientConnection.data.getWinner() + " wins!");
		else
			resultsLabel = new Label("TIE GAME! You may play again or exit.");
		resultsLabel.setStyle("-fx-font-size: 27; -fx-font-weight: bolder; -fx-text-fill: white;");

		try {
			resultsImage = new Image("Results.gif");
			resultsImageView = new ImageView(resultsImage);
		} catch (Exception eee) {
			eee.printStackTrace();
		}

		resultsImageView.setFitWidth(296);
		resultsImageView.setFitHeight(217);
		playAgainButton = new Button("Play Again");
		playAgainButton.setMinWidth(150);
		playAgainButton.setMinHeight(20);
		playAgainButton.setStyle("-fx-font-size: 20; -fx-font-weight: bolder; -fx-background-color: green;");
		exitResultsButton = new Button("Exit Game");
		exitResultsButton.setMinWidth(150);
		exitResultsButton.setMinHeight(20);
		exitResultsButton.setStyle("-fx-font-size: 20; -fx-font-weight: bolder;-fx-background-color: red;");
		resultsVbox = new VBox(resultsLabel, resultsImageView, playAgainButton, exitResultsButton);
		resultsVbox.setAlignment(Pos.CENTER);
		resultsVbox.setSpacing(4);
		BorderPane root4 = new BorderPane(resultsVbox);

		// Set Background
		try {
			Image img = new Image("welcome_and_results_screen_background.jpg");
			BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true,
					true);
			BackgroundImage bckGImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER, bSize);
			Background background = new Background(bckGImg);
			root4.setBackground(background);
		} catch (Exception eee) {
			eee.printStackTrace();
		}

		// Show Results scene
		Stage resultsStage = new Stage();
		resultsStage.setTitle("Connect 4 - Client - Results");
		resultsStage.setScene(new Scene(root4, 400, 400));
		resultsStage.show();

		// Play Again event handler
		playAgainButton.setOnAction(eee -> {
			// clear moves
			moves.clear();

			// update server
			data.playAgain = true;
			data.setWinCheck(false);

			// reset grid
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					data.currentGrid[i][j] = 0;
				}
			}

			// Reset button colors
			for (GameButton button : game_buttons) {
				button.setStyle("-fx-background-color: lightgray");
			}
			resultsStage.close();

			// send updates
			clientConnection.send(data);
		});

		// Exit event handler
		exitResultsButton.setOnAction(eee -> {
			data.playAgain = false;
			Platform.exit();
			System.exit(0);
		});
	}

	// -------------------------

	/*
	 * verifyValidMove
	 * 
	 * verifies whether player move is valid
	 */
	private boolean verifyValidMove(int r, int c, CFourInfo data) {
		int lower_row = r + 1;

		// Checks if on bottom row... if true no need to check, is valid
		if (r == 5) {
			return true;
		}

		// Button pressed is already occupied!
		if (data.currentGrid[lower_row][c] != 0) {
			return true;
		}

		return false;
	}

	// -------------------------

	/* game_button_handler */
	private void game_button_handler(ArrayList<GameButton> game_buttons, CFourInfo data) {
		for (GameButton b : game_buttons) {
			b.setOnAction(e -> {
				if (verifyValidMove(b.getRow(), b.getCol(), data) == false) {
					Alert invalidMove = new Alert(Alert.AlertType.ERROR);
					invalidMove.setContentText("Invalid move! Try again.");
					invalidMove.show();
					return;
				}

				if (player == 1) {
					b.setStyle("-fx-background-color: " + player1Color + ";");
				} else if (player == 2) {
					b.setStyle("-fx-background-color: " + player2Color + ";");
				}

				this.player_move = "Player " + data.getCurrentPlayer() + " move: " + "(row: " + b.getRow() + ", col: "
						+ b.getCol() + ")";

				server_update = new CFourInfo(this.player_move, data.client_ID, data.getCurrentPlayer(), b.getRow(),
						b.getCol(), false);
				server_update.setNumPlayers(clientConnection.data.enoughPlayersToStart());

				// Update grid
				for (int i = 0; i < data.currentGrid.length; i++) {
					for (int j = 0; j < data.currentGrid[i].length; j++) {
						server_update.currentGrid[i][j] = data.currentGrid[i][j];
					}
				}
				server_update.setGridPosition(b.getRow(), b.getCol(), player);
				server_update.setMoveCounter(data.getMoveCounter() + 1);

				// Disable after action
				b.setDisable(true);

				// send update
				clientConnection.send(server_update);
			});
		}
	}

	// -------------------------

	/*
	 * setGameBoardBackground
	 * 
	 * sets game board background
	 * 
	 */
	private void setGameBoardBackground(BorderPane root) {
		Image img = null;
		if (counterForBackgroundSelection == 2) {
			img = new Image("gameplay_background_2.jpg");
		} else if (counterForBackgroundSelection == 3) {
			img = new Image("gameplay_background_3.jpg");
		} else {
			img = new Image("gameplay_background_1.jpg");
		}
		try {
			BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true,
					true);
			BackgroundImage bckGImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER, bSize);
			Background background = new Background(bckGImg);
			root.setBackground(background);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	// -------------------------

	/*
	 * setupGameBoard
	 * 
	 * sets up game board
	 * 
	 */
	private void setupGameBoard(GridPane gameboardGridPane) {
		game_buttons = new ArrayList<GameButton>();
		r0c0 = new GameButton();
		r0c1 = new GameButton();
		r0c2 = new GameButton();
		r0c3 = new GameButton();
		r0c4 = new GameButton();
		r0c5 = new GameButton();
		r0c6 = new GameButton();
		r1c0 = new GameButton();
		r1c1 = new GameButton();
		r1c2 = new GameButton();
		r1c3 = new GameButton();
		r1c4 = new GameButton();
		r1c5 = new GameButton();
		r1c6 = new GameButton();
		r2c0 = new GameButton();
		r2c1 = new GameButton();
		r2c2 = new GameButton();
		r2c3 = new GameButton();
		r2c4 = new GameButton();
		r2c5 = new GameButton();
		r2c6 = new GameButton();
		r3c0 = new GameButton();
		r3c1 = new GameButton();
		r3c2 = new GameButton();
		r3c3 = new GameButton();
		r3c4 = new GameButton();
		r3c5 = new GameButton();
		r3c6 = new GameButton();
		r4c0 = new GameButton();
		r4c1 = new GameButton();
		r4c2 = new GameButton();
		r4c3 = new GameButton();
		r4c4 = new GameButton();
		r4c5 = new GameButton();
		r4c6 = new GameButton();
		r5c0 = new GameButton();
		r5c1 = new GameButton();
		r5c2 = new GameButton();
		r5c3 = new GameButton();
		r5c4 = new GameButton();
		r5c5 = new GameButton();
		r5c6 = new GameButton();

		// row 0
		gameboardGridPane.add(r0c0, 0, 0, 1, 1);
		r0c0.setPosition(0, 0);
		game_buttons.add(r0c0);
		gameboardGridPane.add(r0c1, 1, 0, 1, 1);
		r0c1.setPosition(0, 1);
		game_buttons.add(r0c1);
		gameboardGridPane.add(r0c2, 2, 0, 1, 1);
		r0c2.setPosition(0, 2);
		game_buttons.add(r0c2);
		gameboardGridPane.add(r0c3, 3, 0, 1, 1);
		r0c3.setPosition(0, 3);
		game_buttons.add(r0c3);
		gameboardGridPane.add(r0c4, 4, 0, 1, 1);
		r0c4.setPosition(0, 4);
		game_buttons.add(r0c4);
		gameboardGridPane.add(r0c5, 5, 0, 1, 1);
		r0c5.setPosition(0, 5);
		game_buttons.add(r0c5);
		gameboardGridPane.add(r0c6, 6, 0, 1, 1);
		r0c6.setPosition(0, 6);
		game_buttons.add(r0c6);

		// row 1
		gameboardGridPane.add(r1c0, 0, 1, 1, 1);
		r1c0.setPosition(1, 0);
		game_buttons.add(r1c0);
		gameboardGridPane.add(r1c1, 1, 1, 1, 1);
		r1c1.setPosition(1, 1);
		game_buttons.add(r1c1);
		gameboardGridPane.add(r1c2, 2, 1, 1, 1);
		r1c2.setPosition(1, 2);
		game_buttons.add(r1c2);
		gameboardGridPane.add(r1c3, 3, 1, 1, 1);
		r1c3.setPosition(1, 3);
		game_buttons.add(r1c3);
		gameboardGridPane.add(r1c4, 4, 1, 1, 1);
		r1c4.setPosition(1, 4);
		game_buttons.add(r1c4);
		gameboardGridPane.add(r1c5, 5, 1, 1, 1);
		r1c5.setPosition(1, 5);
		game_buttons.add(r1c5);
		gameboardGridPane.add(r1c6, 6, 1, 1, 1);
		r1c6.setPosition(1, 6);
		game_buttons.add(r1c6);

		// row 2
		gameboardGridPane.add(r2c0, 0, 2, 1, 1);
		r2c0.setPosition(2, 0);
		game_buttons.add(r2c0);
		gameboardGridPane.add(r2c1, 1, 2, 1, 1);
		r2c1.setPosition(2, 1);
		game_buttons.add(r2c1);
		gameboardGridPane.add(r2c2, 2, 2, 1, 1);
		r2c2.setPosition(2, 2);
		game_buttons.add(r2c2);
		gameboardGridPane.add(r2c3, 3, 2, 1, 1);
		r2c3.setPosition(2, 3);
		game_buttons.add(r2c3);
		gameboardGridPane.add(r2c4, 4, 2, 1, 1);
		r2c4.setPosition(2, 4);
		game_buttons.add(r2c4);
		gameboardGridPane.add(r2c5, 5, 2, 1, 1);
		r2c5.setPosition(2, 5);
		game_buttons.add(r2c5);
		gameboardGridPane.add(r2c6, 6, 2, 1, 1);
		r2c6.setPosition(2, 6);
		game_buttons.add(r2c6);

		// row 3
		gameboardGridPane.add(r3c0, 0, 3, 1, 1);
		r3c0.setPosition(3, 0);
		game_buttons.add(r3c0);
		gameboardGridPane.add(r3c1, 1, 3, 1, 1);
		r3c1.setPosition(3, 1);
		game_buttons.add(r3c1);
		gameboardGridPane.add(r3c2, 2, 3, 1, 1);
		r3c2.setPosition(3, 2);
		game_buttons.add(r3c2);
		gameboardGridPane.add(r3c3, 3, 3, 1, 1);
		r3c3.setPosition(3, 3);
		game_buttons.add(r3c3);
		gameboardGridPane.add(r3c4, 4, 3, 1, 1);
		r3c4.setPosition(3, 4);
		game_buttons.add(r3c4);
		gameboardGridPane.add(r3c5, 5, 3, 1, 1);
		r3c5.setPosition(3, 5);
		game_buttons.add(r3c5);
		gameboardGridPane.add(r3c6, 6, 3, 1, 1);
		r3c6.setPosition(3, 6);
		game_buttons.add(r3c6);

		// row 4
		gameboardGridPane.add(r4c0, 0, 4, 1, 1);
		r4c0.setPosition(4, 0);
		game_buttons.add(r4c0);
		gameboardGridPane.add(r4c1, 1, 4, 1, 1);
		r4c1.setPosition(4, 1);
		game_buttons.add(r4c1);
		gameboardGridPane.add(r4c2, 2, 4, 1, 1);
		r4c2.setPosition(4, 2);
		game_buttons.add(r4c2);
		gameboardGridPane.add(r4c3, 3, 4, 1, 1);
		r4c3.setPosition(4, 3);
		game_buttons.add(r4c3);
		gameboardGridPane.add(r4c4, 4, 4, 1, 1);
		r4c4.setPosition(4, 4);
		game_buttons.add(r4c4);
		gameboardGridPane.add(r4c5, 5, 4, 1, 1);
		r4c5.setPosition(4, 5);
		game_buttons.add(r4c5);
		gameboardGridPane.add(r4c6, 6, 4, 1, 1);
		r4c6.setPosition(4, 6);
		game_buttons.add(r4c6);

		// row 5
		gameboardGridPane.add(r5c0, 0, 5, 1, 1);
		r5c0.setPosition(5, 0);
		game_buttons.add(r5c0);
		gameboardGridPane.add(r5c1, 1, 5, 1, 1);
		r5c1.setPosition(5, 1);
		game_buttons.add(r5c1);
		gameboardGridPane.add(r5c2, 2, 5, 1, 1);
		r5c2.setPosition(5, 2);
		game_buttons.add(r5c2);
		gameboardGridPane.add(r5c3, 3, 5, 1, 1);
		r5c3.setPosition(5, 3);
		game_buttons.add(r5c3);
		gameboardGridPane.add(r5c4, 4, 5, 1, 1);
		r5c4.setPosition(5, 4);
		game_buttons.add(r5c4);
		gameboardGridPane.add(r5c5, 5, 5, 1, 1);
		r5c5.setPosition(5, 5);
		game_buttons.add(r5c5);
		gameboardGridPane.add(r5c6, 6, 5, 1, 1);
		r5c6.setPosition(5, 6);
		game_buttons.add(r5c6);

		gameboardGridPane.setAlignment(Pos.CENTER);
		gameboardGridPane.setHgap(4);
		gameboardGridPane.setVgap(4);

		// Disable game buttons on launch
		for (GameButton button : game_buttons) {
			button.setDisable(true);
		}
	}

	// -------------------------

	public static void main(String[] args) {
		launch(args);
	}

	// -------------------------

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Connect 4 - Client - Welcome!");

		// Welcome scene components
		welcomeLabel = new Label("Welcome to Connect 4!");
		welcomeLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bolder; -fx-text-fill: white;");
		try {
			logoImage = new Image("Logo.png");
			logoImageView = new ImageView(logoImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		welcomeImageAndMsgVbox = new VBox(welcomeLabel, logoImageView);
		welcomeImageAndMsgVbox.setAlignment(Pos.CENTER);

		infoLabel = new Label("Please enter the IP address and port number of the server you wish to connect to:");
		infoLabel.setStyle(
				"-fx-font-size: 18; -fx-underline: true; -fx-font-weight: bolder; -fx-text-fill: white; -fx-alignment: center;");
		ipAddressTextField = new TextField("127.0.0.1");
		ipAddressTextField.setPromptText("IP Address");
		ipAddressTextField.setMaxWidth(250);
		portTextField = new TextField("5555");
		portTextField.setPromptText("Port number");
		portTextField.setMaxWidth(250);
		connectToServerButton = new Button("Connect to server");
		connectToServerButton.setMinWidth(200);
		connectToServerButton.setStyle("-fx-background-color: green; -fx-font-weight: bolder; -fx-font-size: 20;");
		welcomeVbox = new VBox(infoLabel, ipAddressTextField, portTextField, connectToServerButton);
		welcomeVbox.setAlignment(Pos.CENTER);
		welcomeVbox.setSpacing(6);

		BorderPane root = new BorderPane(welcomeVbox);
		try {
			Image img = new Image("welcome_and_results_screen_background.jpg");
			BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true,
					true);
			BackgroundImage bckGImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER, bSize);
			Background background = new Background(bckGImg);
			root.setBackground(background);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Show Welcome scene
		root.setTop(welcomeImageAndMsgVbox);
		Scene scene = new Scene(root, 700, 700);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Attempt to connect to server
		connectToServerButton.setOnAction(e -> {
			if (ipAddressTextField.getText() == "" || portTextField.getText() == "") {
				Alert missingValues = new Alert(Alert.AlertType.ERROR);
				missingValues.setContentText("You must include a value for both the IP address and port number!");
				missingValues.show();
				return;
			} else {
				Alert confirmConnection = new Alert(Alert.AlertType.CONFIRMATION);
				confirmConnection.setContentText("You wish to connect to the server with the IP Address: "
						+ ipAddressTextField.getText() + " and port number: " + portTextField.getText() + "?");
				Optional<ButtonType> result = confirmConnection.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.CANCEL) {
					return;
				}

				// Connect to server
				clientConnection = new Client(ipAddressTextField.getText(), Integer.parseInt(portTextField.getText()),
						data -> {
							Platform.runLater(() -> {

								// -------------------------

								// Receive CFour
								CFourInfo new_data = (CFourInfo) data;
								player = new_data.getCurrentPlayer();
								playerIdentifierLocalLabel.setText("You are player " + player);

								if (!new_data.getMessage().isEmpty())
									gameInfoListView.getItems().add(new_data.getMessage().toString());

								// -------------------------

								// Active Game
								if (new_data.enoughPlayersToStart()) {
									for (GameButton button : game_buttons) {
										if (new_data.currentGrid[button.getRow()][button.getCol()] != 0) {
											color_buttons(button);
										} else {
											button.setDisable(false);
										}
									}

									game_button_handler(this.game_buttons, new_data);
								} else {
									for (GameButton button : game_buttons) {
										button.setDisable(true);
									}
								}

								// -------------------------

								// Win/Tie
								if (new_data.getWinCheck()) {
									process_win(new_data);
								}

								// -------------------------

								// Client leaves
								if (new_data.getMoveCounter() == 0 && new_data.getMessage().length() == 65) {
									for (GameButton button : game_buttons) {
										button.setStyle("-fx-background-color: lightgray");
										button.setDisable(true);
									}
								}

								// -------------------------
							});
						});

				// Start connection
				clientConnection.start();
			}

			// GameBoard Scene Components
			playerIdentifierLocalLabel = new Label("You are player " + player);
			playerIdentifierLocalLabel.setStyle("-fx-font-size: 20; -fx-text-fill: white;");
			customizeButton = new Button("Customize look");
			customizeButton.setStyle("-fx-font-size: 18; -fx-background-color: white;");
			customizeButton.setMinWidth(200);
			exitGameButton = new Button("Exit game");
			exitGameButton.setStyle("-fx-font-size: 18; -fx-background-color: red;");
			exitGameButton.setMinWidth(200);
			gameInfoListView = new ListView<String>();
			gameInfoListView.setMaxWidth(650);

			// Create game board w/ disabled buttons
			setupGameBoard(gameboardGridPane);

			gameplayMasterVbox = new VBox(playerIdentifierLocalLabel, customizeButton, exitGameButton, gameInfoListView,
					gameboardGridPane);
			gameplayMasterVbox.setAlignment(Pos.CENTER);
			gameplayMasterVbox.setSpacing(6);
			gameplayMasterVbox.setStyle("-fx-padding: 10");
			BorderPane root3 = new BorderPane(gameplayMasterVbox);
			setGameBoardBackground(root3);
			gameplayStage = new Stage();

			// Show GameBoard
			primaryStage.setTitle("Connect 4 - Client - Gameplay");
			primaryStage.setScene(new Scene(root3, 750, 750));
			primaryStage.show();

			// Close Stage
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});

			// Customize event handler
			customizeButton.setOnAction(ee -> {
				changeBackgroundButton = new Button("Change Background");
				changeBackgroundButton.setStyle("-fx-font-size: 20");
				changeBackgroundButton.setMinWidth(200);
				changeBackgroundButton.setMinHeight(40);
				exitCustomizeButton = new Button("Exit Customize Menu");
				exitCustomizeButton.setStyle("-fx-font-size: 20; -fx-background-color: red;");
				exitCustomizeButton.setMinWidth(200);
				exitCustomizeButton.setMinHeight(40);

				customizeVbox = new VBox(changeBackgroundButton, exitCustomizeButton);
				customizeVbox.setAlignment(Pos.CENTER);
				customizeVbox.setSpacing(6);
				BorderPane root2 = new BorderPane(customizeVbox);
				root2.setStyle("-fx-background-color: gray");
				Stage customizeStage = new Stage();
				customizeStage.setTitle("Connect 4 - Client - Customize");
				customizeStage.setScene(new Scene(root2, 300, 300));
				customizeStage.show();
				changeBackgroundButton.setOnAction(eee -> {
					if (counterForBackgroundSelection == 3) {
						counterForBackgroundSelection = 1;
					} else {
						counterForBackgroundSelection++;
					}
					setGameBoardBackground(root3);
				});
				exitCustomizeButton.setOnAction(eee -> {
					customizeStage.close();
				});
			});

			// Exit event handler
			exitGameButton.setOnAction(ee -> {
				Platform.exit();
				System.exit(0);
			});
		});
	}

	// -------------------------
}

// --------------------------------------------------