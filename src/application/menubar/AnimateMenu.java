package application.menubar;

import application.animation.AnimationDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class AnimateMenu extends Menu {

	public AnimateMenu() {
		super("Animate");
		
		this.getItems().addAll(
				getShowMenuItem(),
				getConfigureMenuItem()
		);
	}

	public MenuItem getShowMenuItem() {
		MenuItem showMenuItem = new MenuItem("Show");
		
		showMenuItem.setOnAction((e) -> new AnimationDialog().showAnimationDialog());
		
		return showMenuItem;
	}
	
	public MenuItem getConfigureMenuItem() {
		MenuItem configureMenuItem = new MenuItem("Configure");
		
		configureMenuItem.setDisable(true);
		
		return configureMenuItem;
	}
}
