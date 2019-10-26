package Application.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.lang.invoke.SwitchPoint;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the 'Settings screen' accessible in the Home Menu.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Settings_ScreenController extends Controller implements Initializable {

    private final static String BOOTSTRAP3 = "/Application/css/bootstrap3.css";
    private final static String SKY = "/Application/css/jfx9be/sky.css";
    private final static String RED = "/Application/css/jfx9be/flatred.css";

    @FXML private Button _save;
    @FXML private ComboBox<String> _listOfStyles;
    @FXML private Label _titleLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _listOfStyles.getItems().addAll("Default", "Bootstrap", "Sky", "Red Devil");
        
        // sets the theme icon for the label
        Image themeImage = new Image(getClass().getResourceAsStream("/Application/assets/theme.png"));
        _titleLabel.setGraphic(new ImageView(themeImage));
    }

    /**
     * Submits the stylesheet to use.
     */
    public void handleSave(){
        String style = _listOfStyles.getSelectionModel().getSelectedItem();
        switch(style) {
            case "Default":
                setStyleSheet("");
                break;
            case "Bootstrap":
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
        stage = (Stage) _save.getScene().getWindow();
        stage.close();

    }

    /**
     * Sets the currently selected option (default setting) on the ComboBox when you open the 'Settings Screen'.
     */
    public void selectDefault() {
        switch (this.getStyleSheet()) {
            case "":
                _listOfStyles.getSelectionModel().select(0);
                break;
            case BOOTSTRAP3:
                _listOfStyles.getSelectionModel().select(1);
                break;
            case SKY:
                _listOfStyles.getSelectionModel().select(2);
                break;
            case RED:
                _listOfStyles.getSelectionModel().select(3);
                break;
        }
    }

}
