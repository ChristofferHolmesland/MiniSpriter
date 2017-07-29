package application.menubar;

import application.spritesheet.SpriteSheetManager;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * The "Spritesheet" menu.
 * @author Christoffer
 *
 */
public class SpriteSheetMenu extends Menu {

	/**
	 * The "Spritesheet" menu.
	 */
	public SpriteSheetMenu() {
		super("Spritesheet");
		
		this.getItems().addAll(
				getLoadMenuItem(),
				getManageMenuItem()
		);
		
	}
	
	/**
	 * Creates and returns the "Load spritesheet" menuitem.
	 * @return The "Load spritesheet" menuitem.
	 */
	private MenuItem getLoadMenuItem() {
		MenuItem loadSpriteSheetMenuItem = new MenuItem("Load spritesheet");
		
		loadSpriteSheetMenuItem.setOnAction((e) -> SpriteSheetManager.loadSpriteSheet());
		
		return loadSpriteSheetMenuItem;
	}
	
	/**
	 * Creates and returns the disabled "Manage spritesheets" menuitem.
	 * @return The disabled "Manage spritesheets" menuitem.
	 */
	private MenuItem getManageMenuItem() {
		MenuItem manageSpriteSheetMenuItem = new MenuItem("Manage spritesheets");
		
		manageSpriteSheetMenuItem.setOnAction((e) -> SpriteSheetManager.manageSpriteSheets());
		manageSpriteSheetMenuItem.setDisable(true);
		
		return manageSpriteSheetMenuItem;
	}

}
