package Application.Controllers;

import Application.Helpers.Quiz;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Quiz_Score_ScreenController extends Controller implements Initializable {


    @FXML private Label _percentageScore;
    @FXML private Label _rawScore;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void evaluate() {
        Quiz quiz = ((Quiz_ScreenController)getParentController()).getQuiz();

        int score = quiz.getScore();
        int total = quiz.getTotal();
        int percentage = (int)((score + 0.0)/(total + 0.0));

        _rawScore.setText(score + " out of " + total + " creations");
        _percentageScore.setText(percentage + " %");

    }
}
