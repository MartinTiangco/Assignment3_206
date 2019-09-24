package Application.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Add_Audio_ScreenController extends Controller {
	@FXML private Button _mainMenuButton;
	
	@FXML
	public void handleBackToMainMenu() {
		System.out.println("You pressed back to main menu button.");
	}
}
