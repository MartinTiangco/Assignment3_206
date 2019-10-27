package Application.Controllers;

import Application.Helpers.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * The Controller for the 'Add Audio Screen' where the user selects which text will be spoken
 * by text-to-speech.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Add_Audio_ScreenController extends Controller implements Initializable {
	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("datatype");
	
	@FXML private Button _mainMenuButton;
	@FXML private MediaView _mediaView;
	@FXML private SplitPane _entireScreenPane;

	@FXML private Button _playTextButton;
	@FXML private Button _createAudioButton;
	@FXML private ComboBox _voiceBox;
	@FXML private ListView _textDescription;
	@FXML private Slider _speedSlider;
	@FXML private Slider _pitchSlider;
	@FXML private TextField _searchTextField;
	@FXML private ProgressIndicator _progressIndicator;
	@FXML private Button _cancelButton;
	@FXML private StackPane _helpBottomHalf;
	
	// elements in the bottom half
	@FXML private AnchorPane _bottomHalf;
	@FXML private Button _playAudioButton;
	@FXML private Button _nextButton;
	@FXML private TableColumn _termSearched;
	@FXML private TableColumn _numberOfLines;
	@FXML private TableColumn _voice;
	@FXML private TableColumn _speed;
	@FXML private TableColumn _pitch;
	@FXML private TableView _savedAudio;
	@FXML private StackPane _helpTopHalf;

	private File audioDir = new File(".Audio_Directory");

	private AudioPlayer _audioPlayer;
	private ExecutorService _playerExecutor = Executors.newSingleThreadExecutor();
	private ExecutorService _backgroundExecutor = Executors.newFixedThreadPool(5);
	private int _audioFileId;
	private int _numberOfAudiosCreated = 0;
	private String _searchInput;
	private WikitWorker _wikitWorker;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// initializes the help images to be invisible
        _helpTopHalf.setVisible(false);
        _helpBottomHalf.setVisible(false);
        
		//disables all except the search functionality
		disableCustomization();
		disableBottomHalf();

		_textDescription.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		_textDescription.setCellFactory(TextFieldListCell.forListView());

		_searchTextField.requestFocus();

		// prepares the TableView to be populated with Audio objects
		_termSearched.setCellValueFactory(new PropertyValueFactory<>("termSearched"));
		_numberOfLines.setCellValueFactory(new PropertyValueFactory<>("numberOfLines"));
		_voice.setCellValueFactory(new PropertyValueFactory<>("voiceDisplay"));
		_speed.setCellValueFactory(new PropertyValueFactory<>("speed"));
		_pitch.setCellValueFactory(new PropertyValueFactory<>("pitch"));

		// list of voices
		_voiceBox.getItems().add("English-USA-male");
		_voiceBox.getItems().add("English-USA-female");
		_voiceBox.getItems().add("English-UK-male");
		_voiceBox.getItems().add("English-UK-female");

		_voiceBox.getSelectionModel().select(0);
		_textDescription.getItems().add("No content found.");
		_textDescription.setDisable(true);


		_savedAudio.setRowFactory(tv -> {
			TableRow<Audio> row = new TableRow<>();

			row.setOnDragDetected(event -> {
				if (! row.isEmpty()) {
					Integer index = row.getIndex();
					Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
					db.setDragView(row.snapshot(null, null));
					ClipboardContent cc = new ClipboardContent();
					cc.put(SERIALIZED_MIME_TYPE, index);
					db.setContent(cc);
					event.consume();
				}
			});

			row.setOnDragOver(event -> {
				Dragboard db = event.getDragboard();
				if (db.hasContent(SERIALIZED_MIME_TYPE)) {
					if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
						event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
						event.consume();
					}
				}
			});

			row.setOnDragDropped(event -> {
				Dragboard db = event.getDragboard();
				if (db.hasContent(SERIALIZED_MIME_TYPE)) {
					int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
					Audio draggedPerson = (Audio) _savedAudio.getItems().remove(draggedIndex);

					int dropIndex ;

					if (row.isEmpty()) {
						dropIndex = _savedAudio.getItems().size() ;
					} else {
						dropIndex = row.getIndex();
					}

					_savedAudio.getItems().add(dropIndex, draggedPerson);

					event.setDropCompleted(true);
					_savedAudio.getSelectionModel().select(dropIndex);
					event.consume();
				}
			});
			return row ;
		});
	}
	
	public void showHelpTopHalf(){
		_helpTopHalf.setVisible(true);
	}

	public void showHelpBottomHalf(){
		_helpBottomHalf.setVisible(true);
	}
	
	public void hideHelp(){
		_helpTopHalf.setVisible(false);
		_helpBottomHalf.setVisible(false);
	}
	
	/**
	 * Previews the text using the current voice settings
	 */
	public void handlePlayText() {
		if (!_textDescription.getSelectionModel().getSelectedItems().isEmpty()) {
			// allows you to preview text without waiting for the first one to finish
			terminatePlayers();
			
			Audio audio = new Audio();
			setUpAudio(audio);
			_audioPlayer = new AudioPlayer(audio, this);
			_playerExecutor.submit(_audioPlayer);
		}

	}

	/**
	 * Previews an audio file that is saved onto the 'List of Saved Audio'
	 */
	public void handlePlayAudio() {
		// allow you to play audio without waiting for the first to finish
		if (_savedAudio.getSelectionModel().getSelectedItem() != null) {
			terminatePlayers();
			_audioPlayer = new AudioPlayer((Audio) _savedAudio.getSelectionModel().getSelectedItem(), this);
			_playerExecutor = Executors.newSingleThreadExecutor();
			_playerExecutor.submit(_audioPlayer);
		}
	}

	/**
	 * Enables searching Wikipedia using wikit
	 */
	public void handleSearch() {
		// progress indicator enables
		_entireScreenPane.setDisable(true);
		_progressIndicator.setProgress(-1);
		_progressIndicator.setVisible(true);
		_cancelButton.setVisible(true);
		
		_searchInput = _searchTextField.getText();

		// if the search is empty then we reset the progress indicator and returns
		if (!validateSearch(_searchInput)) {
			_entireScreenPane.setDisable(false);
			_progressIndicator.setProgress(1);
			_progressIndicator.setVisible(false);
			_cancelButton.setVisible(false);
			return;
		}

		try {
			// clear the ListView
			_textDescription.getItems().clear();

			// use wikit to search the term
			wikitSearch(_searchInput);
		} catch (Exception e) {
			e.printStackTrace();
		}
		_textDescription.getSelectionModel().select(0);
		_textDescription.setDisable(false);

		// retrieves images from Flickr
		ImageGenerator imgGen = new ImageGenerator(_searchInput, 10);
		_backgroundExecutor.submit(imgGen);
	}

	/**
	 * Handles functionality of creating an audio file when wikit content is selected and the 'Save' button is clicked
	 */
	public void handleCreateAudio() {
		// disallow the user selecting more than 5 lines
		if (_textDescription.getSelectionModel().getSelectedItems().size() > 5) {
			AlertMessage alert = new AlertMessage("Please select 5 lines or less");
			Platform.runLater(alert);
			return;
		}
		
		// if the text is a single or multiple punctuation marks ONLY, then we display an error
		if (!validateText()) {
			AlertMessage alert = new AlertMessage("audio_combining_failed");
			Platform.runLater(alert);
			return;
		}

		// add an Audio object to the TableView
		if (!_textDescription.getSelectionModel().getSelectedItems().isEmpty()) {
			_numberOfAudiosCreated++;
			Audio audio = new Audio();
			audio.setTermSearched(_searchInput);
			setUpAudio(audio);
			audio.setNumberOfLines(String.valueOf(_textDescription.getSelectionModel().getSelectedItems().size()));
			AudioCreator audioCreator = new AudioCreator(_numberOfAudiosCreated, audio, this);
			_backgroundExecutor.submit(audioCreator);
		}
		
		// disallow searching for another term to avoid saving audio with different terms
		if (_savedAudio.getItems().isEmpty()) {
			_searchTextField.setDisable(false);
		}
	}

	/**
	 * Handles deleting of the selected audio item when the 'Delete' button is clicked
	 */
	public void handleDeleteAudio() {
		//get the item or items selected and remove from list
		if ((_mediaView.getMediaPlayer() != null)&&(_mediaView.getMediaPlayer().getMedia().getSource().equals("file:" + audioDir.getAbsolutePath() + System.getProperty("file.separator") + ((Audio)_savedAudio.getSelectionModel().getSelectedItem()).getFilename()))) {
			_mediaView.getMediaPlayer().dispose();
		}
		_savedAudio.getItems().remove(_savedAudio.getSelectionModel().getSelectedItem());
		if (_savedAudio.getItems().isEmpty()) {
			_searchTextField.setDisable(false);
			disableBottomHalf();
		}
	}

	/**
	 * Combines all audio in the 'List of Saved Audio' and then goes into the 'Background Music Screen'
	 */
	public void handleNext() {
		// stop all preview/audio player
		terminatePlayers();
		// combine the audios and load the Image Selection Screen
		ObservableList<Audio> allAudio = _savedAudio.getItems();
		AudioCombiner combiner = new AudioCombiner(allAudio, this);
		_backgroundExecutor.submit(combiner);
		_nextButton.setDisable(true);

	}

	/**
	 * Handles functionality to go back to main menu
	 */
	public void handleBackToMainMenu() {
		// stop all preview/audio player
		terminatePlayers();

		// removes the audio directory contents (all files are temporary)
		Cleaner cleaner = new Cleaner();
		cleaner.cleanAudio();

		// closes audio screen
		Stage stage = (Stage) _mainMenuButton.getScene().getWindow();
		stage.close();
	}

	public void handleCancel(){
		_wikitWorker.getProcess().destroy();
		_entireScreenPane.setDisable(false);
		_progressIndicator.setProgress(1);
		_progressIndicator.setVisible(false);
		_cancelButton.setVisible(false);

	}

	/**
	 * Creates a wikit thread that searches Wikipedia in the background
	 * @param searchInput
	 */
	private void wikitSearch(String searchInput) {
		String cmd = "wikit " + searchInput;

		// Runs the wikit command on a worker thread
		_wikitWorker = new WikitWorker(cmd, this);
		_backgroundExecutor.submit(_wikitWorker);
	}

	/**
	 * Allows the user to delete lines in the wikit content ListView
	 * @param key
	 */
	public void deleteLines(KeyEvent key) {
		// allows the deletion of lines in the TextArea
		if (key.getCode().equals(KeyCode.DELETE)) {
			ObservableList<Object>  linesSelected, lines;
			lines = _textDescription.getItems();
			linesSelected = _textDescription.getSelectionModel().getSelectedItems();
			lines.removeAll(linesSelected);

			//disable top half except for search functionality when the content list is empty
			if (_textDescription.getItems().isEmpty()) {
				disableCustomization();
			}
		}
	}

	/**
	 * Checks if the user has input an empty string. Return false if so, as it is not valid.
	 * @param searchInput
	 */
	private boolean validateSearch(String searchInput) {
		// checks for textfield being an empty string or only spaces
		if (searchInput.trim().isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Validates the wikit content. Disallow the user to create audio consisting of only punctuation marks
	 * as that will create problems with the audio.
	 */
	private boolean validateText() {
		// checks if the selected item is just a punctuation mark and disallows it
		String listString = (String.join("", _textDescription.getSelectionModel().getSelectedItems())).trim();
		if (Pattern.matches("\\p{Punct}+", listString)) {
		    return false;
		}
		
		// this fixes a bug where multiple punctuation marks are selected, then attempt to save, then select one line to edit,
		// then save again
		if (Pattern.matches("null", listString)) {
			return false;
		}
		return true;
	}

	/**
	 * Stops all media players from playing to prevent them from overlapping
	 */
	private void terminatePlayers() {
		if (_mediaView.getMediaPlayer() != null) {
			_mediaView.getMediaPlayer().dispose();
		}
		if (_audioPlayer != null) {
			Process process = _audioPlayer.getProcess();
			if (process != null){
				process.destroy();
			}
		}
	}

	/**
	 * disables the voice customization
	 */
	public void disableCustomization() {
		_voiceBox.setDisable(true);
		_speedSlider.setDisable(true);
		_pitchSlider.setDisable(true);
	}

	/**
	 * enables the voice customization
	 */
	public void enableCustomization() {
		_voiceBox.setDisable(false);
		_speedSlider.setDisable(false);
		_pitchSlider.setDisable(false);
	}

	/**
	 * disables the preview / create buttons in the top-half
	 */
	public void disablePlayCreateText() {
		_createAudioButton.setDisable(true);
		_playTextButton.setDisable(true);
	}

	/**
	 * enables the preview / create buttons in the top-half
	 */
	public void enablePlayCreateText() {
		_createAudioButton.setDisable(false);
		_playTextButton.setDisable(false);
	}

	public void disableBottomHalf() {
		_bottomHalf.setDisable(true);
	}

	public void enableBottomHalf() {
		_bottomHalf.setDisable(false);
	}

	public void setUpAudio(Audio audio) {
		audio.setContent(_textDescription.getSelectionModel().getSelectedItems());
		audio.setVoice((String) (_voiceBox.getSelectionModel().getSelectedItem()));
		audio.setSpeed((int)_speedSlider.getValue());
		audio.setPitch((int) (_pitchSlider.getValue()));
	}

	public ListView getContent() {
		return _textDescription;
	}

	public TableView getAudioList() {
		return _savedAudio;
	}

	public MediaView getMediaView() {
		return _mediaView;
	}

	public Button getPlayTextButton() {
		return _playTextButton;
	}

	public Button getPlayAudioButton() {
		return _playAudioButton;
	}

	public String getSearchInput() {
		return _searchInput;
	}

	public TextField getSearchTextField() {
		return _searchTextField;
	}
	
	public ProgressIndicator getProgressIndicator() {
		return _progressIndicator;
	}

	public Button getCancelButton() {
		return _cancelButton;
	}

	public SplitPane getEntireScreenPane() {
		return _entireScreenPane;
	}
	
	public int getAudioFileId() {
		return _audioFileId;
	}
	
	public void setAudioFileId(int id) {
		_audioFileId = id;
	}

	public Button getNextButton() {
		return _nextButton;
	}
}
