package Application.Controllers;

import Application.Helpers.Quiz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Quiz_Score_ScreenController extends Controller implements Initializable {

	@FXML private Label _message;
    @FXML private Label _percentageScore;
    @FXML private Label _rawScore;
    @FXML private Pane _graphicScore;
    @FXML private AnchorPane _pane;
    @FXML private Button _mainMenuButton;
    @FXML private Button _nextButton;
    @FXML private Button _startButton;
    @FXML private Button _tryAgainButton;
    @FXML private ImageView _medal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void evaluate() {
        Quiz quiz = ((Quiz_ScreenController)getParentController()).getQuiz();

        int score = quiz.getScore();
        int total = quiz.getTotal();
        int percentage = (int)(((score + 0.0)/(total + 0.0)*100));
        
        showResults(percentage);
        
        PieChart.Data correct = new PieChart.Data("Correct", score);
        PieChart.Data incorrect = new PieChart.Data("Incorrect", total - score);

        _rawScore.setText(score + " out of " + total + " creations correct.");
        _percentageScore.setText(percentage + " %");
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(correct, incorrect);
        _graphicScore.getChildren().clear();
        PieChart piechart = new PieChart(data);
        _graphicScore.getChildren().add(piechart);
        fitToParent(piechart);
        piechart.setLegendVisible(false);
        
        correct.getNode().setStyle("-fx-pie-color: #008000;");
        incorrect.getNode().setStyle("-fx-pie-color: #FF0000;");
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
	
	public void fitToParent(PieChart piechart) {
    	// set the piechart to fit the pane parent
		piechart.setPrefSize(_graphicScore.getHeight(), _graphicScore.getWidth());
    }
	
	public void showResults(int score) {
		// show a medal icon and message to user depending on results
		String assetPath = "/Application/assets/";
		String icon = "";
		if (score == 100) {
			icon = "trophy.png";
        	_message.setText("Congratulations, 100%!");
        } else if (score < 100 && score > 89) {
        	icon = "gold_medal.png";
        	_message.setText("Awesome! Go for perfect!");
        } else if (score < 90 && score > 79) {
        	icon = "silver_medal.png";
        	_message.setText("Great job! Strive for gold!");
        } else if (score < 80 && score > 69) {
        	icon = "bronze_medal.png";
        	_message.setText("Good work, but you \ncan do better!");
        } else {
        	// no medal
        	_message.setText("Keep trying!");
        }
		
		if (!icon.isEmpty()) {
	        Image image = new Image(assetPath + icon);
	        _medal.setImage(image);
		}
	}
}
