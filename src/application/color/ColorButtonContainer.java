package application.color;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 *  Container for all of the color selection buttons.
 * @author Christoffer
 *
 */
public class ColorButtonContainer {

	private ArrayList<Button> selectButtons;
	private ArrayList<Canvas> buttonCanvases;
	private ArrayList<VBox> vBoxes;
	
	private VBox container;
	
	/**
	 *  Makes all the buttons and the canvases which are displayed on the left side of the borderpane.
	 */
	public ColorButtonContainer() {
		selectButtons = new ArrayList<Button>();
		buttonCanvases = new ArrayList<Canvas>();
		vBoxes = new ArrayList<VBox>();
		
		for (int i = 0; i < 4; i++) {
			Button selectButton = new Button("Select");
			Canvas canvas = new Canvas(45, 35);
			VBox vBox = new VBox(canvas, selectButton);
			vBox.setAlignment(Pos.CENTER);
			
			selectButtons.add(selectButton);
			buttonCanvases.add(canvas);
			vBoxes.add(vBox);
		}
		
		selectButtons.get(0).setOnAction((e) -> ColorUtilities.setCurrentColorIndex(0));
		selectButtons.get(1).setOnAction((e) -> ColorUtilities.setCurrentColorIndex(1));
		selectButtons.get(2).setOnAction((e) -> ColorUtilities.setCurrentColorIndex(2));
		selectButtons.get(3).setOnAction((e) -> ColorUtilities.setCurrentColorIndex(3));		
		
		container = new VBox();
		container.getChildren().addAll(vBoxes);
		container.setAlignment(Pos.CENTER);
		Insets padding = new Insets(0, 5f, 0, 5f);
		container.setPadding(padding);
		container.setSpacing(10f);
	}

	/**
	 *  Returns the node containing all the buttons and canvases as a VBox.
	 * @return The VBox containing everything.
	 */
	public VBox getContainer() {
		return container;
	}
	
	/**
	 *  Filles the button canvases with the correct (user-selected) color or gray scale.
	 */
	public void updateCanvases() {
		GraphicsContext gc;
		for (int i = 0; i < buttonCanvases.size(); i++) {
			gc = buttonCanvases.get(i).getGraphicsContext2D();
			gc.setFill(ColorUtilities.getColor(i));
			gc.fillRect(0, 0, 45, 35);
		}
	}
}
