package Application.Helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * Class of the Quiz functionality. To be extended by the
 * varying levels of the quiz.
 *
 */
public class Quiz {
	private final String DIR = "./Creation_Directory/";
	private int _score = 0;
	private int _total;
	private int _currentQuestionNumber = 0;
	private String _difficulty;
	private List<Question> _listOfQuestions = new ArrayList<>();

	public Quiz() {
		_total = new File(DIR).listFiles(File::isDirectory).length;
	}
	
	public int getScore() {
		return _score;
	}
	
	public int getTotal() {
		return _total;
	}

	public void setScore(int score) {
		this._score = score;
	}

	public void setTotal(int total) {
		this._total = total;
	}

	public int getCurrentQuestionNumber() {
		return _currentQuestionNumber;
	}

	public void incrementCurrentQuestionNumber(){
		_currentQuestionNumber++;
	}

	public void incrementScore(){
		_score++;
		_listOfQuestions.get(_currentQuestionNumber - 1).setCorrectness(true);
	}

	public void setDifficulty(String difficulty) {
		this._difficulty = difficulty;
	}

	public void addUserAnswer(String userAnswer) {
		_listOfQuestions.get(_currentQuestionNumber).setUserAnswer(userAnswer);
	}

	public MediaView createQuizScreen(){
		Question question = new Question();
		question.setQuestionNumber(String.valueOf(_currentQuestionNumber + 1));
		File[] listOfFiles = new File(DIR).listFiles(File::isDirectory);
		File currentCreation = listOfFiles[_currentQuestionNumber];
		int firstPatternIndex = currentCreation.getName().indexOf("_-_");
		int secondPatternIndex = currentCreation.getName().indexOf("_-_", firstPatternIndex + 3);
		question.setCorrectAnswer(currentCreation.getName().substring(firstPatternIndex + 3, secondPatternIndex));
		Media video;
		if (_difficulty.equals("Easy")) {
			video = new Media(currentCreation.toURI().toString() + System.getProperty("file.separator") + "video.mp4");
		}
		else if (_difficulty.equals("Medium")){
			video = new Media(currentCreation.toURI().toString() + System.getProperty("file.separator") + "slideshow.mp4");
		}
		else{
			video = new Media(currentCreation.toURI().toString() + System.getProperty("file.separator") + "slideshow.mp4");
		}
		MediaPlayer player = new MediaPlayer(video);
		player.setAutoPlay(true);
		MediaView mediaView = new MediaView();
		mediaView.setMediaPlayer(player);
		mediaView.setFitHeight(400);
		mediaView.setFitWidth(500);

		_listOfQuestions.add(question);
		return mediaView;
	}

	public String getCorrectAnswer() {
		return _listOfQuestions.get(_currentQuestionNumber - 1).getCorrectAnswer();
	}

	public List<Question> getListOfQuestions() {
		return _listOfQuestions;
	}

}
