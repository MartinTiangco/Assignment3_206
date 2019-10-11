package Application.Controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Application.Helpers.Creation;
import Application.Helpers.Quiz;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Quiz_Start_ScreenController extends Controller implements Initializable {
	@FXML private AnchorPane _pane;
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
	
	public void handleStart() {
		_quiz = new Quiz();
		_quiz.setDifficulty(_difficulty.getValue().toString());
		Controller controller = loadScreen("Quiz", "/Application/fxml/Quiz_Screen.fxml","");
		((Quiz_ScreenController)(controller)).Start();
		Stage stage = (Stage)_startButton.getScene().getWindow();
		stage.close();
	}
	
	public void handleBack() {
		Stage stage = (Stage) _mainMenuButton.getScene().getWindow();
        stage.close();
	}

	public Quiz getQuiz(){
		return _quiz;
	}
}
