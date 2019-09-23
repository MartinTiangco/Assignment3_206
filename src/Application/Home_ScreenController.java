package Application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Home_ScreenController extends Controller implements Initializable {


    @FXML
    private Button _playButton;
    @FXML private Button _addButton;
    @FXML private Button _deleteButton;
    @FXML private Button _settingsButton;

    @FXML
    public void handlePlay() {
        System.out.println("You pressed play");
    }

    @FXML
    public void handleAdd() {
        System.out.println("You pressed add");
    }

    @FXML
    public void handleDelete() {
        System.out.println("You pressed delete");
    }

    public void handleSettings() {
        System.out.println("You pressed settings");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
