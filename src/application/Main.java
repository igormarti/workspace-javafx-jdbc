package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static Scene sceneMain;
	
	public static Scene getSceneMain() {
		return sceneMain;
	}
	
	@Override
	public void start(Stage stage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scPane = loader.load();
			
			scPane.setFitToHeight(true);
			scPane.setFitToWidth(true);
			
			sceneMain = new Scene(scPane);
			stage.setScene(sceneMain);
			
			stage.setTitle("Main View");
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
