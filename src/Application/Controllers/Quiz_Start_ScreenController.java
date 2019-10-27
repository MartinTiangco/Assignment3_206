package Application.Controllers;

import Application.Helpers.Quiz;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the 'Quiz Start Screen', where the user selects the difficulty of the quiz.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Quiz_Start_ScreenController extends Controller implements Initializable {
	@FXML private Button _mainMenuButton;
	@FXML private Button _startButton;
	@FXML private ComboBox _difficulty;
	@FXML private String[] _difficultyLevels = {"Easy", "Medium", "Hard"};
	
	private Quiz _quiz;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_difficulty.getItems().removeAll(_difficulty.getItems());
		_difficulty.getItems().addAll(_difficultyLevels);
		_difficulty.getSelectionModel().select(_difficultyLevels[0]);
	}

	/**
	 * Creating a new Quiz object and setting its difficulty based on the user's choice
	 * Load the Quiz Screen
	 */
	public void handleStart() {
		_quiz = new Quiz();
		_quiz.setDifficulty(_difficulty.getValue().toString());
		Controller controller = loadScreen("Quiz", "/Application/fxml/Quiz_Screen.fxml","");
		((Quiz_ScreenController)(controller)).Start();
		Stage stage = (Stage)_startButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Handles functionality to go back to main menu
	 */
	public void handleBack() {
		Stage stage = (Stage) _mainMenuButton.getScene().getWindow();
        stage.close();
	}

	public Quiz getQuiz() {
		return _quiz;
	}
}
