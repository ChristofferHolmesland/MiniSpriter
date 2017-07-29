package application.color;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 *  Utility class containing everything about colors.
 * @author Christoffer
 *
 */
public class ColorUtilities {

	/**
	 *  Index of the color the user is currently using to draw with.
	 */
	public static int currentColorIndex = 0;
	
	/**
	 *  Determines what color to be used when drawing on a canvas.
	 *  GRAYSCALE - Only gray scale colors should be used.
	 *  COLOR - All colors can be used.
	 * @author Christoffer
	 *
	 */
	public static enum ColorMode {GRAYSCALE, COLOR};
	/**
	 *  The ColorMode which is currently being used to display colors.
	 */
	private static ColorMode currentColorMode = ColorMode.COLOR;
	
	/**
	 *  The gray scale colors used in the Minicraft spritesheet.
	 */
	private final static Color[] grayScales = {
			new Color(0, 0, 0, 1),
			new Color(81f / 255f, 81f / 255f, 81f / 255f, 1),
			new Color(173f / 255f, 173f / 255f, 173f / 255f, 1),
			new Color(1, 1, 1, 1)
	};
	
	/**
	 *  Color used to mask the gray scale color. Starting colors are the
	 *  players colors -1, 100, 220, 532.
	 */
	private final static Color[] colorMasks = {
			new Color(0, 0, 0, 1),
			new Color(51f / 255f, 0, 0, 1),
			new Color(102f / 255f, 102f / 255f, 0, 1),
			new Color(1, 153f / 255, 102f / 255f, 1)
	};
	
	/**
	 *  All the colors which can be displayed in Minicraft.
	 */
	private static Color[] validMinicraftColors = new Color[216];
	
	/**
	 *  Generates all the colors which can be displayed in Minicraft. Has to be called at least once before using the method getMinicraftColors().
	 */
	public static void generateValidMinicraftColors() {
		int i = 0;

		for (int r = 0; r <= 255; r += 51) {
			for (int g = 0; g <= 255; g += 51) {
				for (int b = 0; b <= 255; b += 51) {
					validMinicraftColors[i] = new Color(r / 255f, g / 255f, b / 255f, 1);
					i++;
				}
			}
		}
	}
	
	/**
	 *  Displays all the color information in the Minicraft color format so they can be copied into the source code.
	 */
	private static String colorInfo2;
	public static void displayColorInfoDialog() {
		Dialog<Object> dialog = new Dialog<Object>();
		dialog.setTitle("Color info:");
		
		String colorInfo = "";
		for (int i = 0; i < 4; i++) {
			String minicraftColor = RGBToMinicraftColor(colorMasks[i]);
			colorInfo += "Color" + (i+1) + ": " + minicraftColor + "\n";
		}
		dialog.setHeaderText(colorInfo);
		
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		ButtonType copyButton = new ButtonType("Copy", ButtonData.OTHER);
		dialog.getDialogPane().getButtonTypes().addAll(okButton, copyButton);
		
		colorInfo2 = "Color.get(";
		for (int i = 0; i < 4; i++) {
			String minicraftColor = RGBToMinicraftColor(colorMasks[i]);
			colorInfo2 += minicraftColor;
			if (i != 3)
				colorInfo2 += ",";
			else
				colorInfo2 += ");";
		}
		
		dialog.getDialogPane().setContent(new Label(colorInfo2));
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == copyButton) {
	    	    StringSelection selection = new StringSelection(colorInfo2);
	    	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    	    clipboard.setContents(selection, selection);
		    } else if (dialogButton == okButton) {
		    	dialog.close();
		    	// Dialog will only close if something is returned.
		    	return new Object();
		    }
		    return null;
		});
		
		dialog.showAndWait();
	}
	
	/**
	 *  Converts a color in the RGB format to a color in the Minicraft format.
	 * @param color The RGB color.
	 * @return The string containing the RGB Minicraft values.
	 */
	public static String RGBToMinicraftColor(Color color) {
		double red = convertRGBValueToMinicraftValue(color.getRed());
		double green = convertRGBValueToMinicraftValue(color.getGreen());
		double blue = convertRGBValueToMinicraftValue(color.getBlue());
		
		return (int) red + "" + (int) green + "" + (int) blue;
	}
	
	/**
	 *  Converts a color value (red, green or blue) to the corresponding value used in Minicraft.
	 * @param value The 0-255 RGB value.
	 * @return The 0-5 Minicraft value.
	 */
	private static double convertRGBValueToMinicraftValue(double value) {
		return (value * 255) / 51f;
	}
	
	
	/**
	 *  Returns the right color from index index, based on the current ColorMode.
	 * @param index Index of the color.
	 * @return The color corresponding to the current ColorMode.
	 */
	public static Color getColor(int index) {
		if (currentColorMode == ColorMode.COLOR) {
			return colorMasks[index];
		} else if (currentColorMode == ColorMode.GRAYSCALE) {
			return grayScales[index];
		}
		
		return null;
	}

	/**
	 *  Returns the color the user is currently using. Respects ColorMode.
	 * @return
	 */
	public static Color getCurrentColor() {
		if (currentColorMode == ColorMode.COLOR) {
			return colorMasks[currentColorIndex];
		} else if (currentColorMode == ColorMode.GRAYSCALE) {
			return grayScales[currentColorIndex];
		}
		
		return null;
	}
	
	/**
	 *  The gray scale colors used in the Minicraft spritesheet.
	 */
	public static Color[] getGrayScaleColors() {
		return grayScales;
	}
	
	/**
	 *  Color used to mask the gray scale color.
	 */
	public static Color[] getColorMasks() {
		return colorMasks;
	}
	
	/**
	 *  Changes a color mask to a new color.
	 * @param index Index of mask to change.
	 * @param newColor The new color to be used as mask.
	 */
	public static void setColorMask(int index, Color newColor) {
		colorMasks[index] = newColor;
	}
	
	/**
	 *  All the colors which can be displayed in Minicraft.
	 */
	public static Color[] getMinicraftColors() {
		return validMinicraftColors;
	}
	
	/**
	 * 	Returns the currently used ColorMode.
	 * @return The ColorMode.
	 */
	public static ColorMode getColorMode() {
		return currentColorMode;
	}
	
	/**
	 *  Changes what ColorMode is currently being used to display the colors.
	 * @param newColorMode The new ColorMode.
	 */
	public static void setColorMode(ColorMode newColorMode) {
		currentColorMode = newColorMode;
	}
	
	/**
	 *  Changes what color the user is currently using.
	 * @param newIndex Index of the new color to be used.
	 */
	public static void setCurrentColorIndex(int newIndex) {
		currentColorIndex = newIndex;
	}
	
	/**
	 *  Returns the index of the color the user is currently using.
	 * @return Index of the color.
	 */
	public static int getCurrentColorIndex() {
		return currentColorIndex;
	}
}
