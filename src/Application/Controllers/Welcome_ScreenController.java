 package Application.Controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Welcome_ScreenController extends Controller implements Initializable {

    @FXML
    Button _getStarted;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void getStarted(){
        loadScreen("home", "/Application/fxml/Home_Screen.fxml","/Application/css/Home_Screen.css");
//        Stage homeStage = new Stage();
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/fxml/Home_Screen.fxml"));
//            Parent root = loader.load();
//            Controller Home_ScreenController = loader.getController();
//            Home_ScreenController.setCurrentController(Home_ScreenController);
//            Home_ScreenController.setParentController(_currentController);
//            homeStage.setTitle("VARpedia - Home");
//            Scene scene = new Scene(root, 700, 500);
//            scene.getStylesheets().addAll(this.getClass().getResource("/Application/fxml/Home_Screen.fxml").toExternalForm(), this.getClass().getResource("/Application/css/jfx9be/flatred.css").toExternalForm());
//            homeStage.setScene(scene);
//            homeStage.setOnCloseRequest(t -> {
//                Platform.exit();
//                System.exit(0);
//            });
//            homeStage.show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Stage stage = (Stage) _getStarted.getScene().getWindow();
        stage.close();
    }
}
