package Application.Controllers;

import Application.Helpers.Question;
import Application.Helpers.Quiz;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the 'Quiz Score Screen'
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Quiz_Score_ScreenController extends Controller implements Initializable {

    @FXML private Button _mainMenuButton;
    @FXML private Button _tryAgainButton;
    @FXML private ImageView _medal;
    @FXML private Label _percentageScore;
    @FXML private TableColumn<Question, String> _questionNumber;
    @FXML private TableColumn<Question, String> _userAnswer;
    @FXML private TableColumn<Question, String> _correctAnswer;
    @FXML private TableView _analysisTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        _questionNumber.setCellValueFactory(new PropertyValueFactory<>("questionNumber"));
        _userAnswer.setCellValueFactory(new PropertyValueFactory<>("userAnswer"));
        _correctAnswer.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

        for (Object tableColumn : _analysisTable.getColumns())
            ((TableColumn<Question, String>)tableColumn).setCellFactory(column -> {
            return new TableCell<Question, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {

                        setText(item);
                        Question question = getTableView().getItems().get(getIndex());

                        if (question.getCorrectness()) {
                            setTextFill(Color.WHITE);
                            setStyle("-fx-background-color: green");
                        } else {
                            setTextFill(Color.WHITE); //The text in red
                            setStyle("-fx-background-color: red");
                        }
                    }
                }
            };
        });

    }

    public void evaluate() {
        Quiz quiz = ((Quiz_ScreenController)getParentController()).getQuiz();

        int score = quiz.getScore();
        int total = quiz.getTotal();
        int percentage = (int)(((score + 0.0)/(total + 0.0)*100));
        
        showResults(percentage);

        _percentageScore.setText(percentage + " %");

        _analysisTable.getItems().addAll(quiz.getListOfQuestions());
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
	
	public void showResults(int score) {
		// show a medal icon and message to user depending on results
		String assetPath = "/Application/assets/";
		String icon = "";
		if (score == 100) {
			icon = "trophy.png";
        } else if (score < 100 && score > 89) {
        	icon = "gold_medal.png";
        } else if (score < 90 && score > 79) {
        	icon = "silver_medal.png";
        } else if (score < 80 && score > 69) {
        	icon = "bronze_medal.png";
        } 
		
		if (!icon.isEmpty()) {
	        Image image = new Image(assetPath + icon);
	        _medal.setImage(image);
		}
		
		// add a fade in transition to the medal image
		FadeTransition fadeInMedal = new FadeTransition(Duration.millis(2000));
		fadeInMedal.setNode(_medal);
		fadeInMedal.setFromValue(0.0);
		fadeInMedal.setToValue(1.0);
		fadeInMedal.setCycleCount(1);
		fadeInMedal.setAutoReverse(false);
		fadeInMedal.playFromStart();
		
		// add a fade in transition to the percentage score
		FadeTransition fadeInPercentageScore = new FadeTransition(Duration.millis(2000));
		fadeInPercentageScore.setNode(_percentageScore);
		fadeInPercentageScore.setFromValue(0.0);
		fadeInPercentageScore.setToValue(1.0);
		fadeInPercentageScore.setCycleCount(1);
		fadeInPercentageScore.setAutoReverse(false);
		fadeInPercentageScore.playFromStart();
	}
}
