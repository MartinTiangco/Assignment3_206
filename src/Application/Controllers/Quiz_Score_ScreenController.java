package Application.Controllers;

import Application.Helpers.Question;
import Application.Helpers.Quiz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class Quiz_Score_ScreenController extends Controller implements Initializable {


    @FXML private Label _percentageScore;
    @FXML private Label _rawScore;
    @FXML private Pane _graphicScore;
    @FXML private Button _mainMenuButton;
    @FXML private Button _tryAgainButton;
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
        
        PieChart.Data correct = new PieChart.Data("Correct", score);
        PieChart.Data incorrect = new PieChart.Data("Incorrect", total - score);

        _rawScore.setText(score + " out of " + total + " creations");
        _percentageScore.setText(percentage + " %");
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(correct, incorrect);
        _graphicScore.getChildren().clear();
        PieChart piechart = new PieChart(data);
        piechart.setMaxHeight(400);
        piechart.setMaxWidth(400);
        _graphicScore.getChildren().add(piechart);
        piechart.setLegendVisible(false);
        
        correct.getNode().setStyle("-fx-pie-color: #008000;");
        incorrect.getNode().setStyle("-fx-pie-color: #FF0000;");
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
}
