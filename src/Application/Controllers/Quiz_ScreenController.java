package Application.Controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Application.Helpers.AlertMessage;
import Application.Helpers.Quiz;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Quiz_ScreenController extends Controller implements Initializable {

	@FXML private Button _nextButton;
	@FXML private Pane _quizScreen;
	@FXML private TextField _guess;
	
	private Quiz _quiz;
	private MediaView _mediaView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void handleNextCreation() {
		if (_guess.getText().matches("(?i)" + _quiz.getCorrectAnswer())){
			_quiz.incrementScore();
		}
		_quiz.incrementCurrentQuestionNumber();
		if (_mediaView != null) {
            _mediaView.getMediaPlayer().dispose();
        }
		System.out.println("Question" + _quiz.getCurrentQuestionNumber());
		System.out.println("Total" + _quiz.getTotal());
		if (_quiz.getCurrentQuestionNumber() >= _quiz.getTotal()) {
			Controller controller = loadScreen("Quiz", "/Application/fxml/Quiz_Score.fxml", "/Application/css/Quiz_Score.css");
			System.out.println("reached");
			((Quiz_Score_ScreenController)controller).evaluate();
			Stage stage = (Stage) _nextButton.getScene().getWindow();
			stage.close();

		} else {
			// load the same screen but with a different video
			System.out.println(_guess.getText());
			System.out.println(_quiz.getCorrectAnswer());
			_quizScreen.getChildren().clear();
			_guess.clear();
			_mediaView = _quiz.createQuizScreen();
			fitToParent();
			_quizScreen.getChildren().add(_mediaView);
		}
	}


	public void Start(){
	    _quiz = ((Quiz_Start_ScreenController)(getParentController())).getQuiz();
	    _mediaView = _quiz.createQuizScreen();
	    fitToParent();
	    _quizScreen.getChildren().add(_mediaView);
    }

    public Quiz getQuiz() {
        return _quiz;
    }
    
    public void fitToParent() {
    	// set the mediaview to fit the pane parent
    	_mediaView.fitWidthProperty().bind(_quizScreen.widthProperty()); 
    	_mediaView.fitHeightProperty().bind(_quizScreen.heightProperty());
    }
}
