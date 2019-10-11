package Application.Controllers;

import java.util.List;

import Application.Helpers.Creation;
import Application.Helpers.Quiz;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Quiz_Start_ScreenController extends Controller {
	@FXML private AnchorPane _pane;
	@FXML private Button _mainMenuButton;
	@FXML private Button _startButton;
	@FXML private ComboBox _difficulty;
	@FXML private String[] _difficultyLevels = {"Easy", "Medium", "Hard"};
	
	private Quiz _quiz;
	private int _currentQuestionNumber;
	private int _numOfCreations;
	
	public void initialize() {
		_difficulty.getItems().removeAll(_difficulty.getItems());
		_difficulty.getItems().addAll(_difficultyLevels);
		_difficulty.getSelectionModel().select(_difficultyLevels[0]);
	}
	
	public void handleStart() {
		loadScreen("Quiz", "/Application/fxml/Quiz_Easy.fxml","");
	}
	
	public void handleBack() {
		Stage stage = (Stage) _mainMenuButton.getScene().getWindow();
        stage.close();
	}
}
