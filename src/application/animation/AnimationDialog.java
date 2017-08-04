package application.animation;

import application.MiniSpriter;
import application.canvas.MiniSpriterCanvas;
import application.color.ColorUtilities;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class AnimationDialog {	
	private Timeline timer;
	private int zoom;
	private GraphicsContext gc;
	private int frameSize;
	private int frameCount;
	
	private int[][][] frames;
	
	public void showAnimationDialog() {		
		Dialog<Object> dialog = new Dialog<Object>();
		dialog.setTitle("Animation");
		dialog.setHeaderText("Choose speed (fps):");
		
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(okButton);
		
		BorderPane borderPane = new BorderPane();
		
		MiniSpriterCanvas spriteCanvas = MiniSpriter.getMiniSpriterCanvas();
		zoom = spriteCanvas.getZoom();
		frameSize = (int) (spriteCanvas.getHeight() / zoom);
		frameCount = (int) (spriteCanvas.getWidth() / frameSize / zoom);
		
		Canvas animationCanvas = new Canvas(frameSize * zoom, frameSize * zoom);
		
		int[][] rawData = spriteCanvas.getRawPixelData();
		
		frames = new int[frameCount][][];
		for (int i = 0; i < frameCount; i++) {
			frames[i] = new int[frameSize * zoom][frameSize * zoom];
		}

		for (int x = 0; x < rawData.length; x++) {
			for (int y = 0; y < rawData[x].length; y++) {
				int frame = x / frameSize;
				try {
					// x goes from 0 to rawData.length, but we want it between 0 to frameSize
					frames[frame][x - (frame * frameSize)][y] = rawData[x][y];
				} catch (ArrayIndexOutOfBoundsException e) {
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setHeaderText("The animation tool only supports square frames.");
					errorAlert.showAndWait();
					return;
				}
			}
		}
		
		gc = animationCanvas.getGraphicsContext2D();
		
		setAnimationFrameRate(Duration.seconds(1));
		
		HBox borderPaneTop = new HBox(5);
		TextField fpsField = new TextField();
		Button changeButton = new Button("Change");
		changeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					setAnimationFrameRate(Duration.seconds(1.0d / Double.parseDouble(fpsField.getText())));
				} catch (Exception e) {
					// Not a valid number in the textfield.
					return;
				}
			}
		});
		borderPaneTop.getChildren().addAll(fpsField, changeButton);
		
		borderPane.setTop(borderPaneTop);
		borderPane.setCenter(new Group(animationCanvas));
		BorderPane.setMargin(borderPane.getCenter(), new Insets(10, 0, 0, 5));
		dialog.getDialogPane().setContent(borderPane);
		
		// This dialog doesn't return anything
		dialog.setResultConverter(dialogButton -> {
			return new Object();
		});
		
		dialog.show();
	}
	
	private void setAnimationFrameRate(Duration seconds) {
		if (timer != null)
			timer.stop();
		
		timer = new Timeline(new KeyFrame(seconds, new EventHandler<ActionEvent>() {
		    private int currentFrame = 0;
			
			@Override
		    public void handle(ActionEvent event) {			
				for (int x = 0; x < frameSize; x++) {
					for (int y = 0; y < frameSize; y++) {
						gc.setFill(ColorUtilities.getColor(frames[currentFrame][x][y]));
						gc.fillRect(x * zoom, y * zoom, zoom, zoom);
					}
				}
				
				currentFrame++;		
				if (currentFrame >= frameCount)
					currentFrame = 0;
		    }
		}));
		
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}
}
