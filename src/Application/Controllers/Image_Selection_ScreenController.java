package Application.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Image_Selection_ScreenController extends Controller {
	@FXML Button _createButton;
	@FXML TextField _input;
	
	public void handleCreate() {
		System.out.println("You created");
	}
	
	public void handleInput() {
		System.out.println("You input");
	}
	
}
