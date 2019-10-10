package Application.Helpers;

import java.io.File;
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
 * Abstract class of the Quiz functionality. To be extended by the
 * varying levels of the quiz.
 *
 */
public abstract class Quiz {
	protected int _score = 0;
	protected int _total;
	protected List<Creation> _listOfCreations;
	
	public Quiz(int total, List<Creation> listOfCreations) {
		_total = total;
		_listOfCreations = listOfCreations;
	}
	
	public int getScore() {
		return _score;
	}
	
	public int getTotal() {
		return _total;
	}
	
	public List<Creation> getListOfCreations() {
		return _listOfCreations;
	}

	public void play(int i) {
	}
	
}
