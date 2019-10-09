package Application;

import java.io.File;

import Application.Controllers.Welcome_ScreenController;
import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Application.setUserAgentStylesheet(null);
//        StyleManager.getInstance().addUserAgentStylesheet("css/jfx9be/sky.css");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Welcome_Screen.fxml"));
        Parent root = loader.load();
        Welcome_ScreenController Welcome_ScreenController = loader.getController();
        Welcome_ScreenController.setCurrentController(Welcome_ScreenController);
        root.setId("background");
        primaryStage.setTitle("Welcome to VARpedia");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().addAll(this.getClass().getResource("css/Welcome_Screen.css").toExternalForm(), this.getClass().getResource("css/jfx9be/sky.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    public static void main(String[] args) {

        //make the directories
        File creationDir = new File("Creation_Directory");
        File audioDir = new File(".Audio_Directory");
        File videoDir = new File(".Video_Directory");
        File wikitDir = new File(".Wikit_Directory");
        File imageDir = new File(".Image_Directory");
        File outputDir = new File(".Output_Directory");

        if (!creationDir.isDirectory()) {
            creationDir.mkdir();
        }

        if (!audioDir.isDirectory()) {
            audioDir.mkdir();
        }

        if (!videoDir.isDirectory()) {
            videoDir.mkdir();
        }

        if (!wikitDir.isDirectory()) {
            wikitDir.mkdir();
        }

        if (!imageDir.isDirectory()) {
            imageDir.mkdir();
        }
        
        if (!outputDir.isDirectory()) {
        	outputDir.mkdir();
        }

        launch(args);
    }
}
