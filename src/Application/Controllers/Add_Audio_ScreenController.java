package Application.Controllers;

import Application.Helpers.AudioPlayer;
import Application.Helpers.WikitWorker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Add_Audio_ScreenController extends Controller  implements Initializable {
	@FXML private Button _mainMenuButton;
	@FXML private Button _playTextButton;
	@FXML private Button _searchButton;
	@FXML private ListView _textDescription;
	@FXML private MediaView _mediaView;
	
	@FXML private TextField _searchTextField;
	@FXML private TextArea _contentTextArea;
	
	//directory for wiki text files
	private File wikitDir = new File(".Wikit_Directory");
	private File wikitRaw = new File(wikitDir + System.getProperty("file.separator") + "raw.txt"); //raw content - where content is not separated to lines
	private File wikitTemp = new File(wikitDir + System.getProperty("file.separator") + "temp.txt"); //temp content - where content is separated
	private AudioPlayer _audioPlayer;
	private ExecutorService _executor = Executors.newSingleThreadExecutor();


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_textDescription.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		_textDescription.setCellFactory(TextFieldListCell.forListView());
		_textDescription.getItems().addAll("line 1", "line 2", "line 3");
		_audioPlayer = new AudioPlayer(this);
	}

	@FXML
	public void handlePlayText() {
		System.out.println("You pressed back to play text button.");
		List<String> description = _textDescription.getSelectionModel().getSelectedItems();
		System.out.println(description.toString());
		_audioPlayer.cancel();
		_audioPlayer = new AudioPlayer(this);
		_audioPlayer.setTexts(description);
		_executor.submit(_audioPlayer);

	}

	public MediaView getMediaView() {
		return _mediaView;
	}
	
	public void handleBackToMainMenu() {
		Stage stage = (Stage) _mainMenuButton.getScene().getWindow();
        stage.close();
	}
	
	public void handleSearch() {
		System.out.println("Searching");
		String searchInput = _searchTextField.getText();
		
		if (!validateSearch(searchInput)) {
			System.out.println("Search term is invalid");
			return;
		}

		try {
			//First clears the TextArea and hides the line count
			//lineCount.setVisible(false);
			_textDescription.getItems().clear();
			
			wikitSearch(searchInput);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void wikitSearch(String searchInput) {
		try {
			//create raw.txt for raw wikit content (has not been separated)
			Writer rawFileWriter = new FileWriter(wikitRaw, false);
			
			String cmd = "wikit " + searchInput;
			
			// Runs the wikit command on a worker thread
			WikitWorker wikitWorker = new WikitWorker(cmd, searchInput, rawFileWriter, wikitRaw, wikitTemp, this);
			wikitWorker.run();
		} catch (Exception e) {
			e.printStackTrace();
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
}
