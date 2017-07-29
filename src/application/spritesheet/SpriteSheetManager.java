package application.spritesheet;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class SpriteSheetManager {

	private static ArrayList<SpriteSheet> spriteSheets = new ArrayList<SpriteSheet>();
	
	/**
	 *  Loads a user selected spritesheet and lets the user give it a name.
	 *  Spritesheet is added to the ArrayList spriteSheets.
	 */
	public static void loadSpriteSheet() {
		FileChooser fc = new FileChooser();
		File spriteSheetFile = fc.showOpenDialog(null);
		
		if (spriteSheetFile == null)
			return;
		
		Dialog<String> dialog = new Dialog<String>();
		dialog.setTitle("Select name");
		dialog.setHeaderText("Choose desired name for spritesheet.");
		
		ButtonType okButton = new ButtonType("Load", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
		
		TextField nameField = new TextField();
		dialog.getDialogPane().setContent(nameField);
		
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButton) {
				String name = nameField.getText();
				if (name == null || name.equals("") || name.equals(" "))
					return null;
				
				return name;
			}
			
			return null;
		});
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent())
			spriteSheets.add(new SpriteSheet(spriteSheetFile, result.get()));
		
	}
	
	public static void manageSpriteSheets() {
		
	}
	
	/**
	 *  Shows a dialog where the user can select a spritesheet from the loaded sheets.
	 * @return The spritesheet the user selects. Or null if none are selected.
	 */
	public static SpriteSheet getSpriteSheetFromUser() {
		SpriteSheet sheet = null;
		
		Dialog<SpriteSheet> dialog = new Dialog<SpriteSheet>();
		dialog.setTitle("Spritesheet select");
		dialog.setHeaderText("Select spritesheet:");
		
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
		
		ComboBox<SpriteSheet> cbSpriteSheets = new ComboBox<SpriteSheet>();
		for (SpriteSheet ss : spriteSheets)
			cbSpriteSheets.getItems().add(ss);
		
		dialog.getDialogPane().setContent(cbSpriteSheets);
	
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == okButton) {
		    	return cbSpriteSheets.getValue();
		    }
		    return null;
		});
		
		Optional<SpriteSheet> result = dialog.showAndWait();
		
		if (result.isPresent())
			sheet = result.get();
		
		return sheet;
	}
	
	/**
	 *  Adds a spritesheet to the list of spritesheets.
	 * @param spriteSheet The spritesheet to add.
	 */
	public static void addSpriteSheet(SpriteSheet spriteSheet) {
		spriteSheets.add(spriteSheet);
	}
	
	/**
	 *  Returns all the spritesheets.
	 * @return ArrayList containing the spritesheets.
	 */
	public static ArrayList<SpriteSheet> getSpriteSheets() {
		return spriteSheets;
	}
}
