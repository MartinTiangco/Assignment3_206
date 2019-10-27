package Application.Controllers;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Application.Helpers.MusicAdder;
import Application.Helpers.Track;
import Application.Helpers.TrackPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 * Controller for the 'Background Music Screen'.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Background_Music_ScreenController extends Controller {

	private final String AUDIO_DIR = ".Audio_Directory" + System.getProperty("file.separator");
	private final String MUSIC_DIR = ".Music_Directory" + System.getProperty("file.separator");
	
	private final String NO_MUSIC = "- None -";
	
	private final List<Track> TRACK = new ArrayList<Track>();

	@FXML private Button _backButton;
	@FXML private Button _helpButton;
	@FXML private Button _nextButton;
	@FXML private Button _playButton;
	@FXML private ComboBox _musicComboBox;
	@FXML private ImageView _profilePic;
	@FXML private ImageView _tag;
	@FXML private Label _featuring;
	@FXML private Label _nameOfTrack;
	@FXML private Label _license;
	@FXML private Label _author;
	@FXML private MediaView _mediaView;
	@FXML private StackPane _creditsPane;
	@FXML private StackPane _helpImagePane;
	@FXML private StackPane _noMusicPane;
	@FXML private TextField _nameInput;
	
	private String _url;
	private TrackPlayer _trackPlayer;
	private ExecutorService _playerExecutor = Executors.newSingleThreadExecutor();
	private ExecutorService _backgroundExecutor = Executors.newFixedThreadPool(5);
	
	public void initialize() {
		// initializes the help image and the attribution pane to be invisible 
        _creditsPane.setVisible(false);
        _helpImagePane.setVisible(false);
        _noMusicPane.setVisible(true);
        
		// populate the combo box with the key-value pair being emotions-filename
		TRACK.add(new Track("- None -", ""));
		TRACK.add(new Track("Relaxed", "airtone_-_commonGround.mp3"));
		TRACK.add(new Track("Excited", "cyba_-_yellow.mp3"));
		TRACK.add(new Track("Happy", "JeffSpeed68_-_Little_reindeer.mp3"));
		TRACK.add(new Track("Dramatic", "septahelix_-_Triptych_of_Snippets.mp3"));
		
		_musicComboBox.getItems().clear();
		for (Track trackEmotion : TRACK) {
			_musicComboBox.getItems().addAll(trackEmotion);
		}
		// selects the default (No music)
		_musicComboBox.getSelectionModel().select(TRACK.get(0));
		_playButton.setDisable(true);
	}
	
	/**
	 * shows the help screen when the '?' button is clicked
	 */
	public void showHelp() {
        _helpImagePane.setVisible(true);
	}
	
	/**
	 * hides the help screen when the window is clicked
	 */
	public void hideHelp() {
        _helpImagePane.setVisible(false);
	}
	
	/**
	 * Opens up the browser and takes the user to the music web page
	 */
	public void handleLink() {
		// get the link to the author profile page for the selected track
		Runtime rt = Runtime.getRuntime();
		String[] browsers = { "epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx" };
		StringBuffer cmd = new StringBuffer();
		for (int i = 0; i < browsers.length; i++) {
			if (i == 0) {
				cmd.append(String.format("%s \"%s\"", browsers[i], _url));
			}
			else {
				cmd.append(String.format(" || %s \"%s\"", browsers[i], _url));
			}
		}
		try {
			rt.exec(new String[] { "sh", "-c", cmd.toString() });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Display the attribution for the author when you select their background music
	 * track and sets the properties in the Track object.
	 */
	public void handleSelect() {
		// get the selected item
		Track track = (Track)_musicComboBox.getValue();
		
		if (track.getTrackName() != NO_MUSIC) {
			_playButton.setDisable(false);
	        _noMusicPane.setVisible(false);
			_creditsPane.setVisible(true);
			
			// display the author and trackName
			String trackFullName = track.getTrackFile();

			//gets the occurrence of the file separator pattern
			int patternIndex = trackFullName.indexOf("_-_");
			int lengthPattern = 3;
			
			// get the occurrence of the extension .mp3
			int extPatternIndex = trackFullName.indexOf(".mp3");
			
			// sets the labels, profile picture, music tag and URL to dig.ccmixter.org
			_nameOfTrack.setText(trackFullName.substring(patternIndex + lengthPattern, extPatternIndex));
			_author.setText(track.getAuthor());
			_featuring.setText(track.getFeaturing());
			_profilePic.setImage(track.getProfilePic());
			_tag.setImage(track.getTag());
			_url = track.getURL();
			System.out.println("set url: "+_url);
		} else {
			_playButton.setDisable(true);
			_creditsPane.setVisible(false);
	        _noMusicPane.setVisible(true);
		}
	}
	
	/**
	 * Combines the text-to-speech of the wikit content with the backgorund music tracks and loads the
	 * 'Image Selection Screen'.
	 */
	public void handleNext() {
		// stop all preview/audio player
		terminatePlayers();
		
		// get the spoken audio file output
		String spokenAudio = "output" 
				+ ((Add_Audio_ScreenController)this.getParentController()).getAudioFileId()
				+ ".wav";

		// combine the audio and the background music scene and load the Image Selection Screen
		Track track = (Track) _musicComboBox.getValue();
		MusicAdder bgmAdder = new MusicAdder(track, spokenAudio, this);
		_backgroundExecutor.submit(bgmAdder);
		_nextButton.setDisable(true);
	}
	
	/**
	 * goes back to the Add Audio Screen with all saved audio kept in memory
	 */
	public void handleBack() {
		((Stage)((Add_Audio_ScreenController)this.getParentController()).getAudioList().getScene().getWindow()).show();
		((Add_Audio_ScreenController)this.getParentController()).getNextButton().setDisable(false);
		((Stage)(_backButton.getScene().getWindow())).close();
	}
	
	/**
	 * Handles the preview functionality for the background music track.
	 */
	public void handlePlay() {
		
		// allow you to play audio without waiting for the first to finish
		if (_musicComboBox.getValue().toString() != NO_MUSIC) {
			
			
			if (_mediaView.getMediaPlayer() != null) {
				_mediaView.getMediaPlayer().dispose();
			}
			
			_trackPlayer = new TrackPlayer((Track)_musicComboBox.getValue(), this);
			_playerExecutor = Executors.newSingleThreadExecutor();
			_playerExecutor.submit(_trackPlayer);
		}
	}
	
	/**
	 * Terminates the media players so they do not overlap
	 */
	private void terminatePlayers() {
		if (_mediaView.getMediaPlayer() != null) {
			_mediaView.getMediaPlayer().dispose();
		}
	}
	
	/**
	 * extracts the background music tracks from the background music directory
	 */
	private List<String> extractFromDirectory() {
		List<String> listOfFilenames = new ArrayList<>();
		File[] listOfFiles = new File(MUSIC_DIR).listFiles();

		for (File file : listOfFiles) {
			listOfFilenames.add(file.getName());
		}
		return listOfFilenames;
	}
	
	public Button getNextButton() {
		return _nextButton;
	}

	public MediaView getMediaView() {
		return _mediaView;
	}
}
