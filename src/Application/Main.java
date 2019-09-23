package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Welcome_Screen.fxml"));
        root.setId("background");
        primaryStage.setTitle("Welcome to VARpedia");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().addAll(this.getClass().getResource("Welcome_Screen.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
