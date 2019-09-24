package Application.Controllers;

import Application.Helpers.AudioPlayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Add_Audio_ScreenController extends Controller  implements Initializable {
	@FXML private Button _mainMenuButton;
	@FXML private Button _playTextButton;
	@FXML private ListView _textDescription;
	private AudioPlayer _audioPlayer;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_audioPlayer = new AudioPlayer();
		_textDescription.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		_textDescription.setCellFactory(TextFieldListCell.forListView());
		_textDescription.getItems().addAll("line 1", "line 2", "line 3");
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

		_audioPlayer.playText(description);
	}


}
