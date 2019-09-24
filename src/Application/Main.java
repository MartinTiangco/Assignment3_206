package Application;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Welcome_Screen.fxml"));
        Parent root = loader.load();
        Welcome_ScreenController Welcome_ScreenController = loader.getController();
        Welcome_ScreenController.setCurrentController(Welcome_ScreenController);
        root.setId("background");
        primaryStage.setTitle("Welcome to VARpedia");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().addAll(this.getClass().getResource("css/Welcome_Screen.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
    	
	//make the directories
			File creationDir = new File("Creation_Directory");
			File audioDir = new File(".Audio_Directory");
			File videoDir = new File(".Video_Directory");
			
			if (!creationDir.isDirectory()) {
				creationDir.mkdir();
			}
			
			if (!audioDir.isDirectory()) {
				audioDir.mkdir();
			}
			
			if (!videoDir.isDirectory()) {
				videoDir.mkdir();
			}	
    			
        launch(args);
    }
}
