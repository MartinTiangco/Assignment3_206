 package Application.Controllers;

 import javafx.fxml.FXML;
 import javafx.fxml.Initializable;
 import javafx.scene.control.Button;
 import javafx.stage.Stage;
 import java.net.URL;
 import java.util.ResourceBundle;

/**
 * The opening screen to the application, or the 'Welcome Screen'
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Welcome_ScreenController extends Controller implements Initializable{

    @FXML private Button _getStarted;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void getStarted() {
        loadScreen("Home", "/Application/fxml/Home_Screen.fxml","/Application/css/Home_Screen.css");

        Stage stage = (Stage) _getStarted.getScene().getWindow();
        stage.close();
    }
}
