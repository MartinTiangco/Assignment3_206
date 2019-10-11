package Application.Controllers;

import Application.Helpers.Quiz;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Quiz_Score_ScreenController extends Controller implements Initializable {


    @FXML private Label _percentageScore;
    @FXML private Label _rawScore;
    @FXML private AnchorPane _pane;
    @FXML private Button _mainMenuButton;
    @FXML private Button _nextButton;
    @FXML private Button _startButton;
    @FXML private Button _tryAgainButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void evaluate() {
        Quiz quiz = ((Quiz_ScreenController)getParentController()).getQuiz();

        int score = quiz.getScore();
        int total = quiz.getTotal();
        int percentage = (int)(((score + 0.0)/(total + 0.0)*100));

        _rawScore.setText(score + " out of " + total + " creations");
        _percentageScore.setText(percentage + " %");

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
