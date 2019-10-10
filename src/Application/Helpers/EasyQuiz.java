package Application.Helpers;

import java.io.File;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class EasyQuiz extends Quiz {

	public EasyQuiz(int total, List<Creation> listOfCreations) {
		super(total, listOfCreations);
	}
	
	@Override
	public void play(int i) {
			Creation creation = _listOfCreations.get(i);
			System.out.println("Creation_Directory" + System.getProperty("file.separator") + creation.getFileName());
			File fileUrl = new File("Creation_Directory" + System.getProperty("file.separator") + creation.getFileName());
			Media video = new Media(fileUrl.toURI().toString());
			MediaPlayer player = new MediaPlayer(video);
			player.setAutoPlay(true);

			MediaView mediaView = new MediaView();
			mediaView.setMediaPlayer(player);
			mediaView.setFitHeight(400);
			mediaView.setFitWidth(500);
			MediaBar bar = new MediaBar(player);
			VBox vbox = new VBox(mediaView, bar);
			vbox.setPadding(new Insets(25, 50, 25, 50));
			vbox.setSpacing(25);
	}
}
