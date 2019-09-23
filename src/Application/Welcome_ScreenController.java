package Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Welcome_ScreenController  extends Controller implements Initializable {

    @FXML
    Button _getStarted;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void getStarted(){
        Stage homeStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home_Screen.fxml"));
            Parent root = loader.load();
            Controller Home_ScreenController = loader.getController();
            Home_ScreenController.setCurrentController(Home_ScreenController);
            Home_ScreenController.setParentController(_currentController);
            //root.setId("homeScreen");
            homeStage.setTitle("VARpedia");
            Scene scene = new Scene(root, 700, 600);
            //scene.getStylesheets().addAll(this.getClass().getResource("Welcome_Screen.css").toExternalForm());
            homeStage.setScene(scene);
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
