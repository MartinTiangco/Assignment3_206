package Application.Controllers;

import Application.Helpers.Audio;
import Application.Helpers.AudioCreator;
import Application.Helpers.AudioPlayer;
import Application.Helpers.WikitWorker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Add_Audio_ScreenController extends Controller  implements Initializable {
	@FXML private Button _mainMenuButton;
	@FXML private Button _playTextButton;
	@FXML private Button _searchButton;
	@FXML private Button _createAudioButton;
	@FXML private ListView _textDescription;
	@FXML private MediaView _mediaView;
	
	@FXML private TableView _savedAudio;
	@FXML private TableColumn _termSearched;
	@FXML private TableColumn _numberOfLines;
	@FXML private TableColumn _audioLength;
	
	@FXML private TextField _searchTextField;
	@FXML private ComboBox _voiceBox;
	@FXML private Slider _speedSlider;
	@FXML private Slider _pitchSlider;
	
	//directory for wiki text files
	private File wikitDir = new File(".Wikit_Directory");
	private File wikitRaw = new File(wikitDir + System.getProperty("file.separator") + "raw.txt"); //raw content - where content is not separated to lines
	private File wikitTemp = new File(wikitDir + System.getProperty("file.separator") + "temp.txt"); //temp content - where content is separated
	private AudioPlayer _audioPlayer;
	private ExecutorService _executor = Executors.newSingleThreadExecutor();
	private int _numberOfAudiosCreated = 0;
	private String _searchInput;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_textDescription.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		_textDescription.setCellFactory(TextFieldListCell.forListView());
		_termSearched.setCellValueFactory(new PropertyValueFactory<>("termSearched"));
		_numberOfLines.setCellValueFactory(new PropertyValueFactory<>("numberOfLines"));
		_audioLength.setCellValueFactory(new PropertyValueFactory<>("audioLength"));
		_voiceBox.getItems().add("Default");
		_voiceBox.getItems().add("Dumb Voice");
		_voiceBox.getSelectionModel().select(0);
		_textDescription.getItems().addAll("line 1", "line 2", "line 3");
	}

	@FXML
	public void handlePlayText() {
		Audio audio = new Audio();
		audio.setContent(_textDescription.getSelectionModel().getSelectedItems());
		audio.setVoice((String)(_voiceBox.getSelectionModel().getSelectedItem()));
		audio.setSpeed(String.valueOf((int)(_speedSlider.getValue())));
		audio.setPitch(String.valueOf((int)(_pitchSlider.getValue())));
		_audioPlayer = new AudioPlayer(audio,this);
		_executor.submit(_audioPlayer);

	}

	@FXML
	public void handlePlayAudio() {

		_audioPlayer = new AudioPlayer((Audio)_savedAudio.getSelectionModel().getSelectedItems(),this);
		_executor.submit(_audioPlayer);

	}
	
	public void handleSearch() {
		System.out.println("Searching");
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
		
	}
	
	public void handleCreateAudio() {
		System.out.println("Now creating audio");
		// Creates the selected lines of content and generates an audio file. It will then show on the TableView on the
		// bottom of the screen
		_numberOfAudiosCreated++;
		List<String> description = _textDescription.getSelectionModel().getSelectedItems();
		//System.out.println(description.toString());
		Audio audio = new Audio();
		audio.setTermSearched(_searchInput);
		audio.setContent(description);
		audio.setNumberOfLines(String.valueOf(_textDescription.getSelectionModel().getSelectedItems().size()));
		AudioCreator audioCreator = new AudioCreator(_numberOfAudiosCreated, audio, this);
		_executor.submit(audioCreator);
		
	}
	
	public void handleDeleteAudio() {
		System.out.println("You deleted the audio");
		//get the item selected and remove from list
		List<String> description = _textDescription.getSelectionModel().getSelectedItems();
		
		
	}
	
	public void handleNext() {
		System.out.println("You went to next");
	}

	public MediaView getMediaView() {
		return _mediaView;
	}
	
	public void handleBackToMainMenu() {
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
			_executor.submit(wikitWorker);
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
			if (_textDescription.getSelectionModel().getSelectedItems().size() < 5) {
				_textDescription.getItems().add("");
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

	public ListView getContent() {
		return _textDescription;
	}
	
	public TableView getAudioList() {
		return _savedAudio;
	}
}
