package application.menubar;

import javafx.scene.control.MenuBar;

/**
 * Menubar containing all the menus.
 * @author Christoffer
 *
 */
public class MiniSpriterMenuBar extends MenuBar {
	
	/**
	 * Menubar containing all the menus.
	 */
	public MiniSpriterMenuBar() {
		super(
			new ActionsMenu(),
			new ViewMenu(),
			new SpriteSheetMenu(),
			new ColorMenu(),
			new AnimateMenu()
		);
	}
}
