package Application.Controllers;

import Application.Helpers.Quiz;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Quiz_Score_ScreenController extends Controller {
	@FXML private AnchorPane _pane;
	@FXML private Button _mainMenuButton;
	@FXML private Button _nextButton;
	@FXML private Button _startButton;
	@FXML private Button _tryAgainButton;
	
	private Quiz _quiz;
	
	public void initialize() {
	}
	
	public void handleBack() {
		Stage stage = (Stage) _mainMenuButton.getScene().getWindow();
        stage.close();
	}
	
	public void handleTryAgain() {
		loadScreen("Quiz", "/Application/fxml/Quiz_Start.fxml","");
		
		Stage stage = (Stage) _tryAgainButton.getScene().getWindow();
        stage.close();
	}
}
