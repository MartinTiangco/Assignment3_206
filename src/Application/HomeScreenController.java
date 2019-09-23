package Application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeScreenController {
	@FXML private Button _playButton;
	@FXML private Button _addButton;
	@FXML private Button _deleteButton;
	@FXML private Button _settingsButton;
	
	@FXML
	public void handlePlay() {
		System.out.println("You pressed play");
	}
	
	@FXML
	public void handleAdd() {
		System.out.println("You pressed add");
	}
	
	@FXML
	public void handleDelete() {
		System.out.println("You pressed delete");
	}
	
	@FXML
	public void handleSettings() {
		System.out.println("You pressed settings");
	}
}
