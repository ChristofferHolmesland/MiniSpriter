package application.canvas;

import java.util.ArrayList;
import java.util.Optional;

import application.MiniSpriter;
import application.color.ColorUtilities;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 *  Canvas based on a 2D array of integers, which also supports zooming.
 * @author Christoffer
 *
 */
public class MiniSpriterCanvas extends Canvas {

	private int zoom = 10;
	private int minZoom = 1;
	private int maxZoom = 90;
	
	private int[][] rawPixelData;
	private ArrayList<int[][]> undoLog = new ArrayList<int[][]>();
	
	/**
	 * Initializes a blank canvas of size width * height.
	 * @param width Width.
	 * @param height Height.
	 */
	public MiniSpriterCanvas(int width, int height) {
		generateBlankCanvas(width, height);
	}
	
	/**
	 *  Resizes the canvas according to zoom level and redraws all the pixels.
	 *  Should only be called by the MiniSpriter class.
	 */
	public void update() {
		this.setWidth(rawPixelData.length * zoom);
		this.setHeight(rawPixelData[0].length * zoom);
		
		drawPixelDataOnCanvas();
	}

	/**
	 * Draws all the pixels from "rawPixelData" as colored rectangles on the canvas.
	 */
	private void drawPixelDataOnCanvas() {
		GraphicsContext gc = this.getGraphicsContext2D();
		
		for (int x = 0; x < rawPixelData.length; x++) {
			for (int y = 0; y < rawPixelData[0].length; y++) {
				gc.setFill(ColorUtilities.getColor(rawPixelData[x][y]));
				gc.fillRect(x * zoom, y * zoom, zoom, zoom);
			}
		}
	}
	
	/**
	 * Changes rawPixelData to its last saved state. 
	 */
	public void undo() {
		if (undoLog.size() == 0)
			return;
		
		rawPixelData = undoLog.get(undoLog.size() - 1);
		undoLog.remove(undoLog.size() - 1);
		MiniSpriter.update();
	}
	
	/**
	 * Saves the current rawPixelData state to a list.
	 */
	private void addToUndoLog() {
		if (undoLog.size() >= 50) {
			for (int i = 0; i < 5; i++)
				undoLog.remove(0);
		}
		
		int[][] a = new int[rawPixelData.length][rawPixelData[0].length];
		for (int i = 0; i < rawPixelData.length; i++) {
			for (int j = 0; j < rawPixelData[i].length; j++) {
				a[i][j] = rawPixelData[i][j];
			}
		}
		
		undoLog.add(a);
	}
	
	/**
	 *  Generates a blank (index=0) canvas of size width * heigt.
	 * @param width Canvas width.
	 * @param height Canvas height.
	 */
	public void generateBlankCanvas(int width, int height) {
		int[][] pixels = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixels[x][y] = 0;
			}
		}
		
		generateNewCanvas(width, height, pixels);
	}
	
	/**
	 *  Generates a new canvas of size width * height with the colors from pixelData.
	 *  The canvas is scaled according to the zoom level.
	 *  
	 * @param newWidth Canvas width.
	 * @param newHeight Canvas height.
	 * @param pixelData Array of all the color indexes which determine the canvas colors.
	 */
	public void generateNewCanvas(int width, int height, int[][] pixelData) {				
		int maxDim = Math.max(width, height);
		if (maxDim == 8) zoom = 45;
		else if (maxDim == 16) zoom = 22;
		else zoom = 15;

		rawPixelData = pixelData;
		this.setWidth(width * zoom);
		this.setHeight(height * zoom);
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouse) {
				int x = (int) (mouse.getX() / zoom);
				int y = (int) (mouse.getY() / zoom);			
				addToUndoLog();
				rawPixelData[x][y] = ColorUtilities.getCurrentColorIndex();
				MiniSpriter.update();
			}
		});
		
		drawPixelDataOnCanvas();
		MiniSpriter.update();
	}

	/**
	 *  Asks the user for a canvas size and then generates a blank canvas of wanted size.
	 */
	public void generateNewCanvasWithDialog() {
		Dialog<Pair<Integer, Integer>> dialog = new Dialog<Pair<Integer, Integer>>();
		dialog.setTitle("New Sprite");
		dialog.setHeaderText("Choose size:");
		
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
		
		GridPane gp = new GridPane();
		
		TextField widthField = new TextField("0");
		TextField heightField = new TextField("0");
		
		gp.add(new Label("Width:"), 0, 0);
		gp.add(widthField, 1, 0);
		gp.add(new Label("Height"), 0, 1);
		gp.add(heightField, 1, 1);
		
		dialog.getDialogPane().setContent(gp);
	
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == okButton) {
		    	int width = Integer.parseInt(widthField.getText());
		    	int height = Integer.parseInt(heightField.getText());
		    	
		        return new Pair<>(width, height);
		    }
		    return null;
		});
		
		Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
		
		if (result.isPresent())
			generateBlankCanvas(result.get().getKey(), result.get().getValue());	
	}
	
	/**
	 * Prompts the user to select a template for their new sprite.
	 */
	public void generateNewCanvasFromTemplate() {
		Dialog<String> dialog = new Dialog<String>();
		dialog.setTitle("Template select");
		dialog.setHeaderText("Select template:");
		
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
		
		ComboBox<String> cbTemplates = new ComboBox<String>();
		cbTemplates.getItems().add("Mob");
		
		dialog.getDialogPane().setContent(cbTemplates);
	
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == okButton) {
		    	return cbTemplates.getValue();
		    }
		    return null;
		});
		
		Optional<String> result = dialog.showAndWait();
		
		String template = "";
		if (result.isPresent())
			template = result.get();
		
		if (template.equals("Mob")) {
			generateBlankCanvas(8 * 8, 4 * 8);
		}
	}
	
	/**
	 *  Returns the color index value of a pixel.
	 * @param x X-coordinate of pixel.
	 * @param y Y-coordinate of pixel.
	 * @return The color index value of the pixel.
	 */
	public int getPixel(int x, int y) {
		return rawPixelData[x][y];
	}
	
	/**
	 * Returns the current zoom level.
	 * @return Zoom level.
	 */
	public int getZoom() {
		return zoom;
	}
	
	/**
	 *  Sets the zoom level to a new level between minZoom and maxZoom.
	 * @param newZoom Wanted zoom level (bounded by minZoom and maxZoom).
	 */
	public void setZoom(int newZoom) {
		zoom = newZoom;
		
		if (zoom > maxZoom) 
			zoom = maxZoom;
		else if (zoom < minZoom) 
			zoom = minZoom;
	}
	
	/**
	 *  Returns the minimum zoom level.
	 * @return Minimum zoom level.
	 */
	public int getMinZoom() {
		return minZoom;
	}
	
	/**
	 *  Returns the maximum zoom level.
	 * @return Maximum zoom level.
	 */
	public int getMaxZoom() {
		return maxZoom;
	}
}
