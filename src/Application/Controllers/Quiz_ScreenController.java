package Application.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import Application.Helpers.Quiz;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
//		_guess.setOnAction(event -> handleNextCreation());
	}


	@FXML
	public void enter(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER))
		{
			handleNextCreation();
		}
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
			// changes button text to Finish! when all creations have played
			// at the moment we are just going to the score screen
		Stage stage = (Stage) _nextButton.getScene().getWindow();
		stage.close();
		Controller controller = loadScreen("Quiz", "/Application/fxml/Quiz_Score.fxml","");
		System.out.println("reached");
		((Quiz_Score_ScreenController)controller).evaluate();
		} else {
			// load the same screen but with a different video
        System.out.println(_guess.getText());
        System.out.println(_quiz.getCorrectAnswer());
		_quizScreen.getChildren().clear();
		_guess.clear();
        _mediaView = _quiz.createQuizScreen();
		_quizScreen.getChildren().add(_mediaView);
		}
	}


	public void Start(){
	    _quiz = ((Quiz_Start_ScreenController)(getParentController())).getQuiz();
	    _mediaView = _quiz.createQuizScreen();
	    _quizScreen.getChildren().add(_mediaView);
    }

    public Quiz getQuiz() {
        return _quiz;
    }

    //	public void extractQuizContent() {
//			try {
//				String quiz = System.getProperty("user.dir") + System.getProperty("file.separator") + "quiz.txt";
//				File q = new File(quiz);
//				BufferedReader file = new BufferedReader(new FileReader(q));
//
//				String line;
//				while ((line = file.readLine()) != null) {
//					System.out.println(line);
//	                if (line.trim().startsWith("Number of creations")) {
//	                    _numOfCreations = Integer.parseInt(line.substring(line.indexOf("=") + 1));
//	                    System.out.println(_numOfCreations);
//	                }
//	                if (line.trim().startsWith("Current quiz number")) {
//	                	_currentQuizNumber = Integer.parseInt(line.substring(line.indexOf("=") + 1));
//	                	System.out.println(_currentQuizNumber);
//	                }
//	            }
//	            file.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//	}
	
//	public void reset() {
//		try {
//            String quiz = System.getProperty("user.dir") + System.getProperty("file.separator") + "quiz.txt";
//            File q = new File(quiz);
//            BufferedReader file = new BufferedReader(new FileReader(q));
//            StringBuilder inputBuffer = new StringBuilder();
//
//            String line;
//            while ((line = file.readLine()) != null) {
//                if (line.trim().startsWith("Number of creations")) {
//                    line = "Number of creations=" + _numOfCreations;
//                }
//                if (line.trim().startsWith("Current quiz number")) {
//                	line = "Current quiz number=" + 1;
//                }
//                inputBuffer.append(line);
//                inputBuffer.append('\n');
//            }
//            file.close();
//
//            // write the new string with the replaced line OVER the same file
//            FileOutputStream fileOut = new FileOutputStream("quiz.txt");
//            fileOut.write(inputBuffer.toString().getBytes());
//            fileOut.close();
//
//        } catch (Exception e) {
//            System.out.println("Problem reading file.");
//        }
//	}

//	public void writeIntoNextCreation() {
//		try {
//            String quiz = System.getProperty("user.dir") + System.getProperty("file.separator") + "quiz.txt";
//
//            File q = new File(quiz);
//            BufferedReader file = new BufferedReader(new FileReader(q));
//            StringBuilder inputBuffer = new StringBuilder();
//
//            String line;
//            while ((line = file.readLine()) != null) {
//                if (line.trim().startsWith("Current quiz number")) {
//                	_currentQuizNumber++;
//                	line = "Current quiz number=" + (_currentQuizNumber);
//                }
//                inputBuffer.append(line);
//                inputBuffer.append('\n');
//            }
//            file.close();
//
//            // write the new string with the replaced line OVER the same file
//            FileOutputStream fileOut = new FileOutputStream("quiz.txt");
//            fileOut.write(inputBuffer.toString().getBytes());
//            fileOut.close();
//
//        } catch (Exception e) {
//            System.out.println("Problem reading file.");
//        }
//	}
}
