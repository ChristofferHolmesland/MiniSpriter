package application.menubar;

import application.MiniSpriter;
import application.canvas.MiniSpriterCanvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;

/**
 * The "View" menu.
 * @author Christoffer
 *
 */
public class ViewMenu extends Menu {

	/**
	 * The "View" menu.
	 */
	public ViewMenu() {
		super("View");
		
		this.getItems().addAll(
				getZoomMenu()
		);
	}
	
	/**
	 * Creates and returns the "Zoom" slider menuitem.
	 * @return The "Zoom" slider menuitem.
	 */
	private Menu getZoomMenu() {
		Menu zoomMenu = new Menu("Zoom");
		MiniSpriterCanvas canvas = MiniSpriter.getMiniSpriterCanvas();
		
		MenuItem zoomSliderMenuItem = new MenuItem("");
		Slider zoomSlider = new Slider(canvas.getMinZoom(), canvas.getMaxZoom(), canvas.getZoom());
		zoomSliderMenuItem.setGraphic(zoomSlider);
		zoomMenu.getItems().add(zoomSliderMenuItem);
		
		zoomMenu.setOnShowing((e) -> zoomSlider.setValue(canvas.getZoom()));
		zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			canvas.setZoom(newValue.intValue());
		    MiniSpriter.update();
		});
		
		return zoomMenu;
	}
}
