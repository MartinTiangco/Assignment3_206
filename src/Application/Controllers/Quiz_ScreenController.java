package Application.Controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Application.Helpers.Creation;
import Application.Helpers.Quiz;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Quiz_ScreenController extends Controller implements Initializable {
	@FXML private AnchorPane _pane;
	@FXML private Button _mainMenuButton;
	@FXML private Button _nextButton;
	@FXML private Button _startButton;
	@FXML private Button _tryAgainButton;
	@FXML private Pane _quizScreen;
	@FXML private TextField _guess;
	@FXML private ComboBox _difficulty;
	@FXML private String[] _difficultyLevels = {"Easy", "Medium", "Hard"};
	
	private Quiz _quiz;
	private MediaView _mediaView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {


	}

	
	public void handleNextCreation() {
		_quiz.incrementCurrentQuestionNumber();
		if (_mediaView != null) {
            _mediaView.getMediaPlayer().dispose();
        }
		System.out.println("Question" + _quiz.getCurrentQuestionNumber());
		System.out.println("Total" + _quiz.getTotal());
	if (_quiz.getCurrentQuestionNumber() > _quiz.getTotal()) {
			// changes button text to Finish! when all creations have played
			// at the moment we are just going to the score screen
		Stage stage = (Stage) _nextButton.getScene().getWindow();
		stage.close();
			loadScreen("Quiz", "/Application/fxml/Quiz_Score.fxml","");
		} else {
			// load the same screen but with a different video
		_quizScreen.getChildren().clear();
		_guess.clear();
        _mediaView = _quiz.createQuizScreen();
		_quizScreen.getChildren().add(_mediaView);
		}
	}
	
	public void handleBack() {
		Stage stage = (Stage) _mainMenuButton.getScene().getWindow();
        stage.close();
	}
	
	public void handleTryAgain() {
		loadScreen("home", "/Application/fxml/Quiz_Start.fxml","");
		
		Stage stage = (Stage) _tryAgainButton.getScene().getWindow();
        stage.close();
	}

	public void Start(){
	    _quiz = ((Quiz_Start_ScreenController)(getParentController())).getQuiz();
	    _mediaView = _quiz.createQuizScreen();
	    _quizScreen.getChildren().add(_mediaView);
    }
}
