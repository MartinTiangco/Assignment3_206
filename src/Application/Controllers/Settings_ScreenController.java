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

    private final static String BOOTSTRAP3 = "/Application/css/bootstrap3.css";
    private final static String SKY = "/Application/css/jfx9be/sky.css";
    private final static String RED = "/Application/css/jfx9be/flatred.css";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listOfStyles.getItems().addAll("Default", "BootStrap3", "Sky", "Red Devil");
    }

    public void handleSave(){
        String style = listOfStyles.getSelectionModel().getSelectedItem();
        switch(style) {
            case "Default":
                setStyleSheet("");
                break;
            case "BootStrap3":
                setStyleSheet(BOOTSTRAP3);
                break;
            case "Sky":
                setStyleSheet(SKY);
                break;
            case "Red Devil":
                setStyleSheet(RED);
                break;
        }

        loadScreen("Home", "/Application/fxml/Home_Screen.fxml", "/Application/css/Home_Screen.css");

        Stage stage = (Stage) ((Home_ScreenController)(getParentController())).getCreationTable().getScene().getWindow();
        stage.close();
        stage = (Stage) save.getScene().getWindow();
        stage.close();

    }

    public void selectDefault() {
        switch (this.getStyleSheet()) {
            case "":
                listOfStyles.getSelectionModel().select(0);
                break;
            case BOOTSTRAP3:
                listOfStyles.getSelectionModel().select(1);
                break;
            case SKY:
                listOfStyles.getSelectionModel().select(2);
                break;
            case RED:
                listOfStyles.getSelectionModel().select(3);
                break;
        }
    }

}
