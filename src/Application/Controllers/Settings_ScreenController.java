package Application.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.lang.invoke.SwitchPoint;
import java.net.URL;
import java.util.ResourceBundle;

public class Settings_ScreenController extends Controller implements Initializable {

    @FXML private ComboBox<String> listOfStyles;
    @FXML private Button save;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listOfStyles.getItems().addAll("BootStrap3", "Sky", "Red Devil");

        listOfStyles.getSelectionModel().select(0);
    }

    public void handleSave(){
        String style = listOfStyles.getSelectionModel().getSelectedItem();
        switch(style) {
            case "BootStrap3":
                setStyleSheet("/Application/css/bootstrap3.css");
                break;
            case "Sky":
                setStyleSheet("/Application/css/jfx9be/sky.css");
                break;
            case "Red Devil":
                setStyleSheet("/Application/css/jfx9be/flatred.css");
                break;
        }

        loadScreen("Home", "/Application/fxml/Home_Screen.fxml", "/Application/css/Home_Screen.css");

        Stage stage = (Stage) ((Home_ScreenController)(getParentController())).getCreationTable().getScene().getWindow();
        stage.close();
        stage = (Stage) save.getScene().getWindow();
        stage.close();

    }

}
