package Application.Controllers;

import Application.Helpers.AlertMessage;
import Application.Helpers.Audio;
import Application.Helpers.AudioCombiner;
import Application.Helpers.AudioCreator;
import Application.Helpers.AudioPlayer;
import Application.Helpers.WikitWorker;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Add_Audio_ScreenController extends Controller  implements Initializable {
	@FXML private Button _mainMenuButton;
	@FXML private MediaView _mediaView;

	// elements in the top half of the screen
	@FXML private TextField _searchTextField;
	@FXML private ProgressBar _wikitProgress;
	@FXML private ComboBox _voiceBox;
	@FXML private Slider _speedSlider;
	@FXML private Slider _pitchSlider;
	@FXML private Button _playTextButton;
	@FXML private Button _searchButton;
	@FXML private Button _createAudioButton;
	@FXML private ListView _textDescription;
	@FXML private ProgressBar _pb;
	
	// elements in the bottom half
	@FXML private AnchorPane _bottomHalf;
	@FXML private Button _playAudioButton;
	@FXML private Button _deleteAudioButton;
	@FXML private TableView _savedAudio;
	@FXML private TableColumn _termSearched;
	@FXML private TableColumn _numberOfLines;
	@FXML private TableColumn _voice;
	@FXML private TableColumn _speed;
	@FXML private TableColumn _pitch;
	

	//directory for wiki text files
	private File wikitDir = new File(".Wikit_Directory");
	private File wikitRaw = new File(wikitDir + System.getProperty("file.separator") + "raw.txt"); //raw content - where content is not separated to lines
	private File wikitTemp = new File(wikitDir + System.getProperty("file.separator") + "temp.txt"); //temp content - where content is separated
	
	private AudioPlayer _audioPlayer;
	private ExecutorService _playerExecutor = Executors.newSingleThreadExecutor();
	private ExecutorService _backgroundExecutor = Executors.newFixedThreadPool(5);
	private int _numberOfAudiosCreated = 0;
	private String _searchInput;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//disables all except the search functionality
		disableCustomization();
		disableBottomHalf();
		
		_textDescription.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		//_savedAudio.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		_textDescription.setCellFactory(TextFieldListCell.forListView());
		
		_searchTextField.requestFocus();
		
		_termSearched.setCellValueFactory(new PropertyValueFactory<>("termSearched"));
		_numberOfLines.setCellValueFactory(new PropertyValueFactory<>("numberOfLines"));
		_voice.setCellValueFactory(new PropertyValueFactory<>("voiceDisplay"));
		_speed.setCellValueFactory(new PropertyValueFactory<>("speed"));
		_pitch.setCellValueFactory(new PropertyValueFactory<>("pitch"));
		
//		_voiceBox.getItems().add("Default");
//		_voiceBox.getItems().add("Dumb Voice");
		_voiceBox.getItems().add("English-USA-male");
		_voiceBox.getItems().add("English-USA-female");
		_voiceBox.getItems().add("English-UK-male");
		_voiceBox.getItems().add("English-UK-female");

		_voiceBox.getSelectionModel().select(0);
		_textDescription.getItems().add("No content found.");
		_textDescription.setDisable(true);
	}

	@FXML
	public void handlePlayText() {
		if (!_textDescription.getSelectionModel().getSelectedItems().isEmpty()) {
			_playAudioButton.setDisable(true);
			Audio audio = new Audio();
			setUpAudio(audio);
			_audioPlayer = new AudioPlayer(audio, this);
			_playerExecutor.submit(_audioPlayer);
		}

	}

	@FXML
	public void handlePlayAudio() {
		if (_savedAudio.getSelectionModel().getSelectedItem() != null) {
			_playTextButton.setDisable(true);
		}
		_audioPlayer = new AudioPlayer((Audio)_savedAudio.getSelectionModel().getSelectedItem(),this);
		_playerExecutor.submit(_audioPlayer);
	}

	public void handleSearch() {
		
		_pb.progressProperty().unbind();
		_pb.setProgress(0);
		
		_searchInput = _searchTextField.getText();

		if (!validateSearch(_searchInput)) {
			System.out.println("Search term is invalid");
			return;
		}

		try {
			//First clears the TextArea and hides the line count
			//lineCount.setVisible(false);
			_textDescription.getItems().clear();

			wikitSearch(_searchInput);
		} catch (Exception e) {
			e.printStackTrace();
		}
		_textDescription.getSelectionModel().select(0);
		_textDescription.setDisable(false);
	}

	public void handleCreateAudio() {
		if (_textDescription.getSelectionModel().getSelectedItems().size() > 6) {
			AlertMessage alert = new AlertMessage("Please select 5 lines or less");
			Platform.runLater(alert);
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setHeaderText(null);
//			alert.setContentText("Please select 20 lines or less.");
//			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//			alert.show();
			return;
		}

		_searchTextField.setDisable(true);
		if (!_textDescription.getSelectionModel().getSelectedItems().isEmpty()) {
			_numberOfAudiosCreated++;
			Audio audio = new Audio();
			audio.setTermSearched(_searchInput);
			setUpAudio(audio);
			audio.setNumberOfLines(String.valueOf(_textDescription.getSelectionModel().getSelectedItems().size()));
			AudioCreator audioCreator = new AudioCreator(_numberOfAudiosCreated, audio, this);
			_backgroundExecutor.submit(audioCreator);
		}
		if (_savedAudio.getItems().isEmpty()) {
			_searchTextField.setDisable(false);
		}
	}

	public void handleDeleteAudio() {
		//get the item or items selected and remove from list
		ObservableList<Audio> allAudio = _savedAudio.getItems();
		ObservableList<Audio> selectedAudio = _savedAudio.getSelectionModel().getSelectedItems();
		allAudio.removeAll(selectedAudio);


		if (_savedAudio.getItems().isEmpty()) {
			_searchTextField.setDisable(false);
			disableBottomHalf();
		}
	}

	public void handleNext() {
		// combine the audios
		ObservableList<Audio> allAudio = _savedAudio.getItems();
		AudioCombiner combiner = new AudioCombiner(allAudio, this);
		_backgroundExecutor.submit(combiner);
	}


	public void handleBackToMainMenu() {
		// removes the audio directory contents (all files are temporary)
		File dir = new File(".Audio_Directory");
		deleteDirContents(dir);
			
		// closes audio screen
		Stage stage = (Stage) _mainMenuButton.getScene().getWindow();
		stage.close();
	}

	private void wikitSearch(String searchInput) {
		try {
			//create raw.txt for raw wikit content (has not been separated)
			Writer rawFileWriter = new FileWriter(wikitRaw, false);

			String cmd = "wikit " + searchInput;

			// Runs the wikit command on a worker thread
			WikitWorker wikitWorker = new WikitWorker(cmd, searchInput, rawFileWriter, wikitRaw, wikitTemp, this);
			_backgroundExecutor.submit(wikitWorker);
			_pb.progressProperty().bind(wikitWorker.progressProperty());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteLines(KeyEvent key) {
		if (key.getCode().equals(KeyCode.DELETE)) {

			final int selectedIdx = _textDescription.getSelectionModel().getSelectedIndex();
			if (selectedIdx != -1) {

				final int newSelectedIdx =
						(selectedIdx == _textDescription.getItems().size() - 1)
								? selectedIdx - 1
								: selectedIdx;

				_textDescription.getItems().remove(selectedIdx);
				_textDescription.getSelectionModel().select(newSelectedIdx);
			}
			for (String line : (List<String>)_textDescription.getItems()) {
				if (!line.equals("")) {
					return;
				}
			}
			
			//disable top half except for search functionality when the content list is empty
			disableCustomization();

			//TODO
            /*
			ObservableList<Integer> selectedIdx = _textDescription.getSelectionModel().getSelectedIndices();
			List<String> temp = _textDescription.getItems();
			System.out.println(selectedIdx.toString());
			if (!selectedIdx.contains(-1)) {
			    for (int i = selectedIdx.size()-1; i >= 0; i--) {
			        System.out.println("deleting" + i);
                    temp.remove(selectedIdx.get(i));
                }
                //_textDescription.getItems().clear();
			    _textDescription.getItems().addAll(temp);
                _textDescription.getSelectionModel().select(selectedIdx.get(0));
            }
			if (_textDescription.getSelectionModel().getSelectedItems().size() < 5) {
				_textDescription.getItems().add("");
			}

            */
		}
	}

	public static void deleteDirContents(File dir) {
		File[] files = dir.listFiles();
		if(files != null) {
			for (File f: files) {
				if (f.isDirectory()) {
					deleteDirContents(f);
				} else {
					f.delete();
				}
			}
		}
	}

	private boolean validateSearch(String searchInput) {
		// checks for textfield being an empty string or only spaces
		if (searchInput.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public void disableCustomization() {
		_voiceBox.setDisable(true);
		_speedSlider.setDisable(true);
		_pitchSlider.setDisable(true);
//		_createAudioButton.setDisable(true);
//		_playTextButton.setDisable(true);
		//_searchButton.setDisable(true);
	}
	
	public void enableCustomization() {
		_voiceBox.setDisable(false);
		_speedSlider.setDisable(false);
		_pitchSlider.setDisable(false);
//		_createAudioButton.setDisable(false);
//		_playTextButton.setDisable(false);
		//_searchButton.setDisable(false);
	}
	
	public void disablePlayCreateText() {
		_createAudioButton.setDisable(true);
		_playTextButton.setDisable(true);
	}
	
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

//	public void checkIfListSelected() {
//		System.out.println("calling checkIfListSelected");
//		if (_textDescription.getSelectionModel().getSelectedItems().isEmpty()) {
//			_createAudioButton.setDisable(true);
//			_playTextButton.setDisable(true);
//		} else {
//			_createAudioButton.setDisable(false);
//			_playTextButton.setDisable(false);
//		}
//	}

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
}
