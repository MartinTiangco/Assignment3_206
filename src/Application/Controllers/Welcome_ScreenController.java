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

/**
 * The opening screen to the application, or the 'Welcome Screen'
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Welcome_ScreenController extends Controller {

    @FXML private Button _getStarted;

    public void getStarted() {
        loadScreen("Home", "/Application/fxml/Home_Screen.fxml","/Application/css/Home_Screen.css");

        Stage stage = (Stage) _getStarted.getScene().getWindow();
        stage.close();
    }
}
