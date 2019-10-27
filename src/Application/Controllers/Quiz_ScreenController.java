package Application.Controllers;

import Application.Helpers.Quiz;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the 'Quiz Screen', where the user answers the quiz
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Quiz_ScreenController extends Controller implements Initializable {

	@FXML private Button _nextButton;
	@FXML private Pane _quizScreen;
	@FXML private TextField _guess;

	private Quiz _quiz;
	private MediaView _mediaView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/**
	 * Saving the user answer to the question and proceed to the next question
	 * loading the video or text for the next question
	 *
	 * If the current question is the last question, then proceed to the Quiz Score Screen
	 */
	public void handleNextCreation() {
		_quiz.addUserAnswer(_guess.getText());
		_quiz.incrementCurrentQuestionNumber();
		if (_guess.getText().matches("(?i)" + _quiz.getCorrectAnswer())){
			_quiz.incrementScore();
		}
		if (_mediaView != null) {
			_mediaView.getMediaPlayer().dispose();
		}
		// Check if the current question is the last question
		if (_quiz.getCurrentQuestionNumber() >= _quiz.getTotal()) {
			// load the Quiz Score Screen
			Controller controller = loadScreen("Quiz", "/Application/fxml/Quiz_Score.fxml", "/Application/css/Quiz_Score.css");
			((Quiz_Score_ScreenController)controller).evaluate();
			Stage stage = (Stage) _nextButton.getScene().getWindow();
			stage.close();
		} else {
			// load the same screen but with a different video or text
			_quizScreen.getChildren().clear();
			_guess.clear();
			if (_quiz.getDifficulty().equals("Hard")){
				hardQuiz();
			}
			else {
				_mediaView = (MediaView) _quiz.createQuizScreen().getChildren().get(0);
				fitToParent();
				_quizScreen.getChildren().add(_mediaView);
			}
		}
	}

	/**
	 * loading the video or text fo the first question
	 */
	public void Start() {
		_quiz = ((Quiz_Start_ScreenController)(getParentController())).getQuiz();
		if (_quiz.getDifficulty().equals("Hard")){
			hardQuiz();
		}
		else {
			_mediaView = (MediaView) _quiz.createQuizScreen().getChildren().get(0);
			fitToParent();
			_quizScreen.getChildren().add(_mediaView);

			Stage stage = (Stage)_nextButton.getScene().getWindow();
			stage.setOnCloseRequest(t -> _mediaView.getMediaPlayer().dispose()
			);
		}
	}

	/**
	 * modifying the text area for hard quiz for better user experience
	 */
	private void hardQuiz() {
		TextArea textArea = (TextArea) _quiz.createQuizTextArea().getChildren().get(0);
		Label label = new Label("What goes in the blanks?");
		_quizScreen.getChildren().add(new VBox(label, textArea));
		textArea.prefWidthProperty().bind(_quizScreen.widthProperty());
		textArea.prefHeightProperty().bind(_quizScreen.heightProperty());
	}

	public Quiz getQuiz() {
		return _quiz;
	}

	private void fitToParent() {
		// set the mediaview to fit the pane parent
		_mediaView.fitWidthProperty().bind(_quizScreen.widthProperty());
		_mediaView.fitHeightProperty().bind(_quizScreen.heightProperty());
	}
}
