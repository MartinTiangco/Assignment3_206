package Application.Controllers;

import Application.Helpers.AudioPlayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.media.MediaView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Add_Audio_ScreenController extends Controller  implements Initializable {
	@FXML private Button _mainMenuButton;
	@FXML private Button _playTextButton;
	@FXML private ListView _textDescription;
	@FXML private MediaView _mediaView;
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
	public void handleBackToMainMenu() {
		System.out.println("You pressed back to main menu button.");
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

}
