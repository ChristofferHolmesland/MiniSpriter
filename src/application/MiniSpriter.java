package application;

import application.canvas.MiniSpriterCanvas;
import application.color.ColorButtonContainer;
import application.color.ColorPaletteContainer;
import application.color.ColorUtilities;
import application.menubar.MiniSpriterMenuBar;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *  The main class for the MiniSpriter application.
 * @author Christoffer
 *
 */
public class MiniSpriter {

	public static Stage primaryStage;
	public static BorderPane root;
	public static ColorButtonContainer colorButtonContainer;
	public static ColorPaletteContainer colorPaletteContainer;
	
	/**
	 *  Sets up the layout and starts the application. Main entrypoint from the JavaFX main method to the MiniSpriter application.
	 * @param primaryStage The applications primary stage.
	 * @return The application in a Scene object.
	 */
	public static Scene getMiniSpriterScene(Stage primaryStage) {
		MiniSpriter.primaryStage = primaryStage;
		primaryStage.setTitle("MiniSpriter: The sprite editor designed for Minicraft");
		ColorUtilities.generateValidMinicraftColors();
		
		root = new BorderPane();
		Scene scene = new Scene(root);
		
		root.setCenter(new Group(new MiniSpriterCanvas(8, 8)));
		root.setTop(new MiniSpriterMenuBar());
		
		colorButtonContainer = new ColorButtonContainer();
		root.setLeft(colorButtonContainer.getContainer());
		
		colorPaletteContainer = new ColorPaletteContainer();
		ScrollPane colsScrollPane = new ScrollPane(colorPaletteContainer.getCanvas());
		colsScrollPane.setPrefHeight(colorPaletteContainer.getSize() * colorPaletteContainer.getRowCount() + 15);
		colsScrollPane.setPrefWidth(200);
		root.setBottom(colsScrollPane);
		
		update();
		return scene;
	}
	
	/**
	 *  Updates the canvas and the color button canvases.
	 */
	public static void update() {
		MiniSpriterCanvas canvas = getMiniSpriterCanvas();

		if (canvas != null)
			canvas.update();
		
		if (colorButtonContainer != null)
			colorButtonContainer.updateCanvases();
	}
	
	/**
	 * Adds all the keybindings on the root BorderPane.
	 */
	public static void addKeyBinds() {
		// Ctrl+z keybind to undo
		root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getCode() == KeyCode.Z && arg0.isControlDown())
					getMiniSpriterCanvas().undo();
			}
		});
	}
	
	/**
	 * Returns the MiniSpriterCanvas inside the BorderPanes center position.
	 * @return The MiniSpriterCanvas object.
	 */
	public static MiniSpriterCanvas getMiniSpriterCanvas() {
		Group canvasGroup = (Group)root.getCenter();
		if (canvasGroup == null)
			return null;
		
		return (MiniSpriterCanvas)canvasGroup.getChildren().get(0);
	}
}