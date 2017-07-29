package application.color;

import application.MiniSpriter;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 *  Container for the color palette.
 * @author Christoffer
 *
 */
public class ColorPaletteContainer {

	private int columnCount;
	private int rowCount;
	private int size;
	
	private Canvas canvas;
	
	/**
	 *  Draws all of the colors on a canvas and lets the user select colors to be drawn.
	 */
	public ColorPaletteContainer() {
		columnCount = 3 * 2 * 3 * 2 * 3;
		rowCount = 2;
		size = 30;
		
		canvas = new Canvas(columnCount * size, rowCount * size);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		int i = 0;
		for (int x = 0; x < columnCount; x++) {
			for (int y = 0; y < rowCount; y++) {
				gc.setFill(ColorUtilities.getMinicraftColors()[i]);
				gc.fillRect(x * size, y * size, size, size);
				i++;
			}
		}
		
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouse) {
				int x = (int) (mouse.getX() / size);
				int y = (int) (mouse.getY() / size);
				
				int colorIndex = x * rowCount + y;
				ColorUtilities.setColorMask(ColorUtilities.getCurrentColorIndex(), ColorUtilities.getMinicraftColors()[colorIndex]);
				MiniSpriter.update();
			}
		});
	}
	
	/**
	 *  Canvas containing the color palette.
	 * @return The canvas.
	 */
	public Canvas getCanvas() {
		return canvas;
	}
	
	public int getColumnCount() {
		return columnCount;
	}
	
	public int getRowCount() {
		return rowCount;
	}
	
	public int getSize() {
		return size;
	}

}
