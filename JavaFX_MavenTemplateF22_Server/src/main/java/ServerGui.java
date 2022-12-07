// --------------------------------------------------

/*
 * Author: Zaid Awaidah
 * 		   Alex Castillo
 * 
 * UIC, Fall 2022
 * CS 342
 *
 * Project 3 - Server GUI
 * 
*/

// --------------------------------------------------

import java.util.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

// --------------------------------------------------

public class ServerGui extends Application {
	// -------------------------

	/* Locals */
	private ImageView view;
	private BorderPane starting_pane;
	private TextField Port;
	private Button Start;
	private Scene starting_scene;
	private HashMap<String, Scene> scenes;
	private ListView<String> listItems;

	// -------------------------

	/*
	 * welcome_scene
	 * 
	 * Builds server welcome scene
	 */
	private Scene welcome_scene() {
		this.starting_pane = new BorderPane();

		// Starting label
		try {
			Image img = new Image("/images/Logo.png");
			this.view = new ImageView(img);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.view.setFitWidth(500);
		this.view.setFitHeight(300);
		HBox label_component = new HBox(this.view);
		HBox.setMargin(this.view, new Insets(50, 0, 0, 0));

		// Port TextField
		this.Port = new TextField();
		this.Port.setPromptText("Enter Port #");
		this.Port.setMaxWidth(300);
		this.Port.setMaxHeight(100);

		// Start button
		this.Start = new Button("Start");
		this.Start.setMaxWidth(100);
		this.Start.setMaxHeight(100);

		VBox components = new VBox(this.Port, this.Start);
		components.setSpacing(10);
		this.Port.setAlignment(Pos.CENTER);
		this.Start.setAlignment(Pos.CENTER);

		// set component positions
		this.starting_pane.setTop(label_component);
		this.starting_pane.setCenter(components);
		components.setAlignment(Pos.CENTER);
		label_component.setAlignment(Pos.TOP_CENTER);

		// set background
		try {
			Image img = new Image("/images/background_2.jpg");
			BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true,
					true);
			BackgroundImage bckGImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER, bSize);
			Background background = new Background(bckGImg);
			this.starting_pane.setBackground(background);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Welcome scene
		this.starting_scene = new Scene(this.starting_pane, 1000, 800);

		return this.starting_scene;
	}

	// -------------------------

	/*
	 * createServerGui
	 * 
	 * Builds server main component
	 */
	public Scene createServerGui() {
		BorderPane pane = new BorderPane();
		this.listItems = new ListView<String>();

		// set background
		try {
			Image img = new Image("/images/background_2.jpg");
			BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true,
					true);
			BackgroundImage bckGImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER, bSize);
			Background background = new Background(bckGImg);
			pane.setBackground(background);
		} catch (Exception e) {
			e.printStackTrace();
		}

		pane.setPadding(new Insets(70));
		pane.setCenter(listItems);

		return new Scene(pane, 500, 400);
	}

	// -------------------------

	/*
	 * start_eventHandler
	 */
	private void start_eventHandler(Stage primaryStage) {
		this.Start.setOnAction(a -> {
			if (this.Port.getText().length() == 0) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("You must include port # before continuing");
				alert.show();
				return;
			} else {
				this.scenes.put("server", createServerGui());
				primaryStage.setScene(this.scenes.get("server"));
				primaryStage.setTitle("Connect4 - Server");

				@SuppressWarnings("unused")
				Server server_connection = new Server(Integer.parseInt(this.Port.getText()), data -> {
					Platform.runLater(() -> {
						listItems.getItems().add(data.toString());
					});
				});
				listItems.getItems().add("Server is running and waiting for clients.");

			}

		});
	}

	// -------------------------

	/* main */
	public static void main(String[] args) {
		launch(args);
	}

	// -------------------------

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Connect 4 - Server - Welcome!");

		// Application scenes
		this.scenes = new HashMap<String, Scene>();

		// store scene
		this.scenes.put("welcome", welcome_scene());

		// Start Event Handler
		start_eventHandler(primaryStage);

		// Close Stage
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		/* Show scene */
		if (primaryStage.getScene() == null) {
			primaryStage.setScene(this.scenes.get("welcome"));
		}
		primaryStage.setResizable(true);
		primaryStage.show();
	}

	// -------------------------
}

// --------------------------------------------------
