package Application.Helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * Class of the Quiz functionality.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Quiz {
	private final String DIR = "Creation_Directory" + System.getProperty("file.separator");

	private int _currentQuestionNumber = 0;
	private int _score = 0;
	private int _total;
	private List<Question> _listOfQuestions = new ArrayList<>();
	private String _difficulty;

	public Quiz() {
		// retrieves the number of creations in the directory
		_total = new File(DIR).listFiles(File::isDirectory).length;
	}

	public int getScore() {
		return _score;
	}

	public int getTotal() {
		return _total;
	}

	public void setScore(int score) {
		_score = score;
	}

	public void setTotal(int total) {
		_total = total;
	}

	public int getCurrentQuestionNumber() {
		return _currentQuestionNumber;
	}

	public void incrementCurrentQuestionNumber() {
		_currentQuestionNumber++;
	}

	public void incrementScore() {
		_score++;
		_listOfQuestions.get(_currentQuestionNumber - 1).setCorrectness(true);
	}

	public void setDifficulty(String difficulty) {
		_difficulty = difficulty;
	}

	public void addUserAnswer(String userAnswer) {
		_listOfQuestions.get(_currentQuestionNumber).setUserAnswer(userAnswer);
	}

	public Pane createQuizScreen(){
		Question question = setUpQuestion();

		Media video;
		if (_difficulty.equals("Easy")) {
			video = new Media(question.getCreationTested().toURI().toString() + System.getProperty("file.separator") + "video.mp4");
		}
		else {
			video = new Media(question.getCreationTested().toURI().toString() + System.getProperty("file.separator") + "slideshow.mp4");
		}
		MediaPlayer player = new MediaPlayer(video);
		player.setAutoPlay(true);
		MediaView mediaView = new MediaView();
		mediaView.setMediaPlayer(player);
		mediaView.setFitHeight(400);
		mediaView.setFitWidth(500);
		
		_listOfQuestions.add(question);
		return new Pane(mediaView);
	}

	public Pane createQuizTextArea(){
		Question question = setUpQuestion();
		String text = question.getCreationTested().toURI().toString() + System.getProperty("file.separator")
				+ "wikit.txt";
		
		File c = new File(text.substring(5));
		BufferedReader file = null;
		String line;
		String newline = null;
		try {
			file = new BufferedReader(new FileReader(c));
			while ((line = file.readLine()) != null) {
				newline = line.replace(" " + question.getCorrectAnswer() + " ", "________");
				newline = newline.replace(question.getCorrectAnswer().toUpperCase().charAt(0) + question.getCorrectAnswer().substring(1), "________");
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		TextArea textArea = new TextArea(newline);
		textArea.setWrapText(true);
		textArea.setEditable(false);
		
		_listOfQuestions.add(question);
		return new Pane(textArea);
	}

	public Question setUpQuestion(){
		Question question = new Question();
		question.setQuestionNumber(String.valueOf(_currentQuestionNumber + 1));
		
		File[] listOfFiles = new File(DIR).listFiles(File::isDirectory);
		question.setCreationTested(listOfFiles[_currentQuestionNumber]);
		int firstPatternIndex = question.getCreationTested().getName().indexOf("_-_");
		int secondPatternIndex = question.getCreationTested().getName().indexOf("_-_", firstPatternIndex + 3);
		question.setCorrectAnswer(question.getCreationTested().getName().substring(firstPatternIndex + 3, secondPatternIndex));
		return question;
	}

	public String getCorrectAnswer() {
		return _listOfQuestions.get(_currentQuestionNumber - 1).getCorrectAnswer();
	}

	public List<Question> getListOfQuestions() {
		return _listOfQuestions;
	}

	public String getDifficulty() {
		return _difficulty;
	}
}
