package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *  Main entrypoint of the program.
 * @author Christoffer
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = MiniSpriter.getMiniSpriterScene(primaryStage);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
