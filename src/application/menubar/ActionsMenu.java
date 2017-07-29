package application.menubar;

import application.MiniSpriter;
import application.canvas.FileCanvasHandler;
import application.canvas.MiniSpriterCanvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * The "Actions" menu.
 * @author Christoffer
 *
 */
public class ActionsMenu extends Menu {
	
	/**
	 * The "Actions" menu.
	 */
	public ActionsMenu() {
		super("Actions");
		
		this.getItems().addAll(
				getNewMenu(),
				getSaveMenu(),
				getLoadMenu(),
				getUndoMenuItem()
		);
	}
	
	/**
	 * Creates and returns the submenu "New".
	 * @return The "New" menu.
	 */
	private Menu getNewMenu() {
		Menu newMenu = new Menu("New");
		MenuItem newBlank = new MenuItem("Blank");
		MenuItem newFromTemplate = new MenuItem("From template");
		newMenu.getItems().addAll(newBlank, newFromTemplate);
		
		MiniSpriterCanvas canvas = MiniSpriter.getMiniSpriterCanvas();
		newBlank.setOnAction((e) -> canvas.generateNewCanvasWithDialog());
		newFromTemplate.setOnAction((e) -> canvas.generateNewCanvasFromTemplate());
		
		return newMenu;
	}
	
	/**
	 * Creates and returns the submenu "Save".
	 * @return The "Save" menu.
	 */
	private Menu getSaveMenu() {
		Menu saveMenu = new Menu("Save");
		MenuItem saveToFile = new MenuItem("File");
		MenuItem saveToSpritesheet = new MenuItem("Spritesheet");
		MenuItem saveToColorFile = new MenuItem("Save with colors");
		saveMenu.getItems().addAll(saveToFile, saveToSpritesheet, saveToColorFile);
		
		saveToFile.setOnAction((e) -> FileCanvasHandler.saveCanvasToSingleFile(false));
		saveToSpritesheet.setOnAction((e) -> FileCanvasHandler.saveCanvasToSpritesheet());
		saveToColorFile.setOnAction((e) -> FileCanvasHandler.saveCanvasToSingleFile(true));
		
		return saveMenu;
	}
	
	/**
	 * Creates and returns the submenu "Load".
	 * @return The "Load" menu.
	 */
	private Menu getLoadMenu() {
		Menu loadMenu = new Menu("Load");
		MenuItem loadFromFile = new MenuItem("File");
		MenuItem loadFromSpritesheet = new MenuItem("Spritesheet");
		loadMenu.getItems().addAll(loadFromFile, loadFromSpritesheet);
		
		loadFromFile.setOnAction((e) -> FileCanvasHandler.loadCanvasFromSingleFile());
		loadFromSpritesheet.setOnAction((e) -> FileCanvasHandler.loadCanvasFromSpritesheet());
		
		return loadMenu;
	}
	
	/**
	 * Creates and returns the menuitem "Undo".
	 * @return The "Undo" menuitem.
	 */
	private MenuItem getUndoMenuItem() {
		MenuItem undoMenuItem = new MenuItem("Undo");

		undoMenuItem.setOnAction((e) -> MiniSpriter.getMiniSpriterCanvas().undo());
		
		return undoMenuItem;
	}
}
