package application.canvas;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import application.MiniSpriter;
import application.color.ColorUtilities;
import application.spritesheet.SpriteSheet;
import application.spritesheet.SpriteSheetManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

/**
 *  Methods for loading and saving a sprite (MiniSpriterCanvas) to a file or a spritesheet.
 * @author Christoffer
 *
 */
public class FileCanvasHandler {
	
	/**
	 *  Saves the canvas to a user selected file.
	 */
	public static void saveCanvasToSingleFile(boolean saveColors) {
		FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
       
        File file = fileChooser.showSaveDialog(MiniSpriter.primaryStage);
         
        if(file != null){
            try {
            	
            	MiniSpriterCanvas canvas = MiniSpriter.getMiniSpriterCanvas();
            	
            	int w = (int)canvas.getWidth();
            	int h = (int)canvas.getHeight();  	
                WritableImage writableImage = new WritableImage(w, h);
                
                Canvas saveCanvas = new Canvas(w, h);
    			
    			PixelWriter gc = saveCanvas.getGraphicsContext2D().getPixelWriter();
    			for (int x = 0; x < w; x++) {
    				for (int y = 0; y < h; y++) {
    					if (!saveColors) {
    						Color grayScaleColor = ColorUtilities.getGrayScaleColors()[canvas.getPixel(x, y)];
    						gc.setColor(x, y, grayScaleColor);
    					} else {
    						Color color = ColorUtilities.getColorMasks()[canvas.getPixel(x, y)];
    						gc.setColor(x, y, color);
    					}
    				}
    			}
    			
                saveCanvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                
                ImageIO.write(renderedImage, "png", file);
                
                Alert info = new Alert(AlertType.INFORMATION);
                info.setTitle("Reminder...");
                if (!saveColors) {
                	info.setHeaderText("Remember to check View->Color Info before you close the sprite.");
                	info.setContentText("Color information is only stored in this program. Minicraft uses grayscale sprites so you have to copy the information from this program into your code. The last line can be copied directly into your code.");
                } else {
                	info.setHeaderText("This sprite is not compatible with minicraft.");
                	info.setContentText("This file is saved WITH colors and can't be used with minicraft.");
                }
                info.showAndWait();
                
            } catch (IOException ex) {
                
            }
        }
	}
	
	/**
	 *  Loads a sprite from a user selected file to the canvas.
	 */
	public static void loadCanvasFromSingleFile() {
		FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
		
        File file = fileChooser.showOpenDialog(MiniSpriter.primaryStage);
	                
        if(file != null){
        	try {
        		BufferedImage img = ImageIO.read(file);
        		
        		int width = img.getWidth();
        		int height = img.getHeight();
        		
        		int[][] pixels = new int[width][height];
        		for (int x = 0; x < width; x++) {
        			for (int y = 0; y < height; y++) {
                		int rgb = img.getRGB(x, y);
                		
                		int colorIndex = redValueToColorIndex(rgb);
                		if (colorIndex == -1) {
                			Alert alert = new Alert(AlertType.ERROR);
                			alert.setHeaderText("This file is in the wrong format. Loading aborted.");
                			return;
                		}
                		
                		pixels[x][y] = colorIndex;
                	
        			}
        		}
        		
        		MiniSpriter.getMiniSpriterCanvas().generateNewCanvas(width, height, pixels);
        		MiniSpriter.update();
        		
        	} catch (IOException ex) {
        		
        	}
        }
	}
	

	/**
	 * Saves the canvas to a spritesheet. The user is asked to select spritesheet and position.
	 */
	public static void saveCanvasToSpritesheet() {
		SpriteSheet spritesheet = SpriteSheetManager.getSpriteSheetFromUser();
		File file = spritesheet.getFile();
		if (file == null)
			return;
		
       try {
    		BufferedImage imgSpritesheet = ImageIO.read(file);
    		int w = imgSpritesheet.getWidth();
    		int h = imgSpritesheet.getHeight();	
        	
            WritableImage writableImage = new WritableImage(w, h);
            
            Canvas saveCanvas = new Canvas(w, h);
			MiniSpriterCanvas canvas = MiniSpriter.getMiniSpriterCanvas();
            
            
			int[][] pPixel = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
	        		int rgb = imgSpritesheet.getRGB(x, y);
            		int colorIndex = redValueToColorIndex(rgb);
            		if (colorIndex == -1) {
            			Alert alert = new Alert(AlertType.ERROR);
            			alert.setHeaderText("This file is in the wrong format. Loading aborted.");
            			return;
            		}
            		
            		pPixel[x][y] = colorIndex;
				}
			}
				
			PixelWriter gc = saveCanvas.getGraphicsContext2D().getPixelWriter();
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					gc.setColor(x, y, ColorUtilities.getGrayScaleColors()[pPixel[x][y]]);
				}
			}
			
			
			Rectangle2D position = selectSpriteFromSpritesheet(file);
			for (int x = 0; x < position.getWidth(); x++) {
				for (int y = 0; y < position.getHeight(); y++) {
					Color grayScaleColor = ColorUtilities.getGrayScaleColors()[canvas.getPixel(x, y)];
					gc.setColor(x + (int)position.getMinX(), y + (int)position.getMinY(), grayScaleColor);
				}
			}
			
            saveCanvas.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            
            ImageIO.write(renderedImage, "png", file);
            
            Alert info = new Alert(AlertType.INFORMATION);
            info.setTitle("Reminder...");
            info.setHeaderText("Remember to check View->Color Info before you close the sprite.");
            info.setContentText("Color information is only stored in this program. Minicraft uses grayscale sprites so you have to copy the information from this program into your code. The last line can be copied directly into your code.");
            info.showAndWait();
            
        } catch (IOException ex) {
            
        }
	}
	
	/**
	 *  Loads a sprite from a user selected spritesheet.
	 */
	public static void loadCanvasFromSpritesheet() {
		SpriteSheet spritesheet = SpriteSheetManager.getSpriteSheetFromUser();
		File file = spritesheet.getFile();
		if (file == null)
			return;

    	Rectangle2D position = selectSpriteFromSpritesheet(file);
    	if (position == null) return;
    	
    	try {
			BufferedImage spriteSheet = ImageIO.read(file);
			BufferedImage sprite = spriteSheet.getSubimage((int)position.getMinX(), (int)position.getMinY(),
					(int)position.getWidth(), (int)position.getHeight());
			
    		int width = sprite.getWidth();
    		int height = sprite.getHeight();
    		
    		int[][] pixels = new int[width][height];
    		for (int x = 0; x < width; x++) {
    			for (int y = 0; y < height; y++) {
            		int rgb = sprite.getRGB(x, y);
            		int colorIndex = redValueToColorIndex(rgb);
            		if (colorIndex == -1) {
            			Alert alert = new Alert(AlertType.ERROR);
            			alert.setHeaderText("This file is in the wrong format. Loading aborted.");
            			return;
            		}
            		
            		pixels[x][y] = colorIndex;
    			}
    		}
    		
    		MiniSpriter.getMiniSpriterCanvas().generateNewCanvas(width, height, pixels);
    		MiniSpriter.update();
			
		} catch (IOException e) {
			
		}
	}
	
	/**
	 *  Lets the user select a sprite from a spritesheet.
	 * @return Rectangle2D object with user selected position and dimension.
	 */
	public static Rectangle2D selectSpriteFromSpritesheet(File spritesheet) {
		Dialog<Rectangle2D> dialog = new Dialog<Rectangle2D>();
		dialog.setTitle("Sprite information");
		dialog.setHeaderText("Where, and what size is the sprite? Position is given by x- and y-coordinates stating from (0,0) in the top-left corner.");
		
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
		
		GridPane gp = new GridPane();
		
		TextField widthField = new TextField("0");
		TextField heightField = new TextField("0");
		TextField xPosition = new TextField("0");
		TextField yPosition = new TextField("0");
		
		gp.add(new Label("Width: "), 0, 0);
		gp.add(widthField, 1, 0);
		gp.add(new Label("Height: "), 0, 1);
		gp.add(heightField, 1, 1);
		gp.add(new Label("Position (x, y):"), 0, 2);
		gp.add(xPosition, 1, 2);
		gp.add(yPosition, 2, 2);
		
		dialog.getDialogPane().setContent(gp);
		
		BufferedImage spriteSheet;
		try {
			int scaling = 2;
			spriteSheet = ImageIO.read(spritesheet);
			int pW = spriteSheet.getWidth();
			int pH = spriteSheet.getHeight();
			Canvas spriteSheetPreview = new Canvas(pW * scaling, pH * scaling);
			
			int[][] pPixel = new int[pW][pH];
			for (int x = 0; x < pW; x++) {
				for (int y = 0; y < pH; y++) {
	        		int rgb = spriteSheet.getRGB(x, y);
            		int colorIndex = redValueToColorIndex(rgb);
            		if (colorIndex == -1) {
            			Alert alert = new Alert(AlertType.ERROR);
            			alert.setHeaderText("This file is in the wrong format. Loading aborted.");
            			return null;
            		}
            		
            		pPixel[x][y] = colorIndex;
				}
			}
			
			GraphicsContext gc = spriteSheetPreview.getGraphicsContext2D();
			for (int x = 0; x < pW; x++) {
				for (int y = 0; y < pH; y++) {
					gc.setFill(ColorUtilities.getGrayScaleColors()[pPixel[x][y]]);
					gc.fillRect(x * scaling, y * scaling, scaling, scaling);
				}
			}
			
			ScrollPane spriteSheetPreviewScroll = new ScrollPane(spriteSheetPreview);
			spriteSheetPreviewScroll.prefHeight(50);
			spriteSheetPreviewScroll.prefWidth(50);
			gp.add(spriteSheetPreviewScroll, 0, 3, 4, 2);
			
			class Picker implements ChangeListener<Object> {

				@Override
				public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
			    	int width;
			    	int height;
					int x;
			    	int y;
			    	
			    	try {
			    		x = Integer.parseInt(xPosition.getText()) * 8;
			    		y = Integer.parseInt(yPosition.getText()) * 8;
			    		width = Integer.parseInt(widthField.getText());
			    		height = Integer.parseInt(heightField.getText());
			    	} catch (Exception ex) {
			    		return;
			    	}
			    	
					for (int x2 = 0; x2 < pW; x2++) {
						for (int y2 = 0; y2 < pH; y2++) {
							gc.setFill(ColorUtilities.getGrayScaleColors()[pPixel[x2][y2]]);
							gc.fillRect(x2 * scaling, y2 * scaling, scaling, scaling);
						}
					}
					
			    	gc.setStroke(Color.RED);
			    	gc.setLineWidth(4);
			    	gc.strokeRect(x * scaling, y * scaling, width * scaling, height * scaling);
				}
				
			}
			widthField.textProperty().addListener(new Picker());
			heightField.textProperty().addListener(new Picker());
			xPosition.textProperty().addListener(new Picker());
			yPosition.textProperty().addListener(new Picker());
			
		} catch (IOException e) {
			
		}
	
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == okButton) {
		    	int width = Integer.parseInt(widthField.getText());
		    	int height = Integer.parseInt(heightField.getText());
		    	int x = Integer.parseInt(xPosition.getText()) * 8;
		    	int y = Integer.parseInt(yPosition.getText()) * 8;
		    	
		        return new Rectangle2D(x, y, width, height);
		    }
		    return null;
		});
		
		Optional<Rectangle2D> result = dialog.showAndWait();
		
		if (result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}
	
	/**
	 * This is used to determine what grayscale a pixel is.
	 *  Converts a pixel value of the TYPE_INT_ARGB format to a color index value.
	 * @param rgb The color in TYPE_INT_ARGB format.
	 * @return rgb's color index, or -1 if it doesn't exist.
	 */
	private static int redValueToColorIndex(int rgb) {
		int red = (rgb >> 16) & 0xFF;
		
		if (red == 0) 
			return 0;
		else if (red == 81) 
			return 1;
		else if (red == 173) 
			return 2;
		else if (red == 255) 
			return 3;
		else if (red == 107 || red == 214)
			return 0;
		else
			return -1;
	}
	
}
