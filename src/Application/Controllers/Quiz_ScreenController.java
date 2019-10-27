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
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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

	public void handleNextCreation() {
		_quiz.addUserAnswer(_guess.getText());
		_quiz.incrementCurrentQuestionNumber();
		if (_guess.getText().matches("(?i)" + _quiz.getCorrectAnswer())){
			_quiz.incrementScore();
		}
		if (_mediaView != null) {
			_mediaView.getMediaPlayer().dispose();
		}
		if (_quiz.getCurrentQuestionNumber() >= _quiz.getTotal()) {
			Controller controller = loadScreen("Quiz", "/Application/fxml/Quiz_Score.fxml", "/Application/css/Quiz_Score.css");
			((Quiz_Score_ScreenController)controller).evaluate();
			Stage stage = (Stage) _nextButton.getScene().getWindow();
			stage.close();

		} else {
			// load the same screen but with a different video
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
			stage.setOnCloseRequest(t -> {
						_mediaView.getMediaPlayer().dispose();
					}
			);
		}
	}

	public void hardQuiz() {
		TextArea textArea = (TextArea) _quiz.createQuizTextArea().getChildren().get(0);
		Label label = new Label("What goes in the Blanks?");
		_quizScreen.getChildren().add(new VBox(label, textArea));
		textArea.prefWidthProperty().bind(_quizScreen.widthProperty());
		textArea.prefHeightProperty().bind(_quizScreen.heightProperty());
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
