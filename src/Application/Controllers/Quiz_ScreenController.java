package Application.Controllers;

import java.util.List;

import Application.Helpers.Creation;
import Application.Helpers.EasyQuiz;
import Application.Helpers.Quiz;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Quiz_ScreenController extends Controller {
	@FXML private AnchorPane _pane;
	@FXML private Button _mainMenuButton;
	@FXML private Button _nextButton;
	@FXML private Button _startButton;
	@FXML private Button _tryAgainButton;
	@FXML private ComboBox _difficulty;
	@FXML private String[] _difficultyLevels = {"Easy", "Medium", "Hard"};
	
	private Quiz quiz;
	private int quizNumber = 0;
	private int _numOfCreations;
	
	public void initialize() {
		if (_pane.getChildren().contains(_startButton)) {
			_difficulty.getItems().removeAll(_difficulty.getItems());
			_difficulty.getItems().addAll(_difficultyLevels);
			_difficulty.getSelectionModel().select(_difficultyLevels[0]);
		}
		
		if (_pane.getChildren().contains(_nextButton)) {
			System.out.println("Contains next button");
			//quiz.play();
		}
	}
	
	public void handleStart() {
		String difficulty = _difficulty.getValue().toString();
		
		// get a list of the creations
		List<Creation> listOfCreations = ((Home_ScreenController)this.getParentController()).getCreationTable().getItems();
		
		// get the total number of creations
		int numOfCreations = ((Home_ScreenController)this.getParentController()).getCreationTable().getItems().size();
		_numOfCreations = numOfCreations;
		System.out.println("number of creations is " + numOfCreations);
		System.out.println("quiz number is " + quizNumber);
		
		if (difficulty.equals("Easy")) {
			// loads easy quiz
			loadScreen("Quiz", "/Application/fxml/Quiz_Easy.fxml","");
			
			quiz = new EasyQuiz(numOfCreations, listOfCreations);
			
			// then we play each creation one by one
		} else if (difficulty.equals("Medium")) {
			// loads medium quiz
			System.out.println("You selected medium");
		} else {
			// loads hard quiz
			System.out.println("You selected hard");
		}
		
		// closes
		Stage stage = (Stage) _startButton.getScene().getWindow();
        stage.close();
	}
	
	public void handleNextCreation() {
		// PROBLEM - we are making a NEW controller and hence we can't pass on the quiz number / number of creations
		System.out.println("quiz number is " + quizNumber);
		System.out.println("number of creations is " + _numOfCreations);
		System.out.println(quizNumber == _numOfCreations);
		if (quizNumber == _numOfCreations) {
			System.out.println("quiz is finished");
			// changes button text to Finish! when all creations have played
			// at the moment we are just going to the score screen
			loadScreen("Quiz", "/Application/fxml/Quiz_Score.fxml","");
		} else {
			// load the same screen but with a different video
			System.out.println("Current quiz number is " + quizNumber);
			quizNumber++;
			loadScreen("Quiz", "/Application/fxml/Quiz_Easy.fxml","");
		}
		
		Stage stage = (Stage) _nextButton.getScene().getWindow();
        stage.close();
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
	
}
