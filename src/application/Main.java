package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene cenaPrincipal;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/JanelaPrincipal.fxml"));
			ScrollPane scrollPane = loader.load();
			
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			cenaPrincipal = new Scene(scrollPane);
			primaryStage.setScene(cenaPrincipal);
			primaryStage.setTitle("Sample JavaFX application");
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Scene getCenaPrincipal() {
		return cenaPrincipal;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
