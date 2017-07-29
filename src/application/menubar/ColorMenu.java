package application.menubar;

import application.MiniSpriter;
import application.color.ColorUtilities;
import application.color.ColorUtilities.ColorMode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * The "Color" menu.
 * @author Christoffer
 *
 */
public class ColorMenu extends Menu {

	/**
	 * The "Color" menu.
	 */
	public ColorMenu() {
		super("Color");
		
		this.getItems().addAll(
				getModeMenuItem(),
				getInfoMenuItem()
		);
	}
	
	/**
	 * Creates and returns the "Grayscale" checkbox.
	 * @return The "Grayscale" checkbox.
	 */
	private CheckMenuItem getModeMenuItem() {
		CheckMenuItem colorModeMenuItem = new CheckMenuItem("Grayscale");
		
		colorModeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (colorModeMenuItem.isSelected())
					ColorUtilities.setColorMode(ColorMode.GRAYSCALE);
				else
					ColorUtilities.setColorMode(ColorMode.COLOR);
				
				MiniSpriter.update();	
			}
		});
		
		return colorModeMenuItem;
	}
	
	/**
	 * Creates and returns the "Color info" menuitem.
	 * @return The "Color info" menuitem.
	 */
	private MenuItem getInfoMenuItem() {
		MenuItem colorInfoMenuItem = new MenuItem("Color info");
		
		colorInfoMenuItem.setOnAction((e) -> ColorUtilities.displayColorInfoDialog());
		
		return colorInfoMenuItem;
	}
}
