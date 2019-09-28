package Application.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Image_Selection_ScreenController extends Controller {
	@FXML Button _createButton;
	@FXML TextField _input;
	
	public void initialize() {
		_input.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		            _input.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
	}
	
	public void handleCreate() {
		System.out.println("You created");
	}
	
	public void handleInput() {
		if (!validateNumInput()) {
			return;
		}
		
		System.out.println("You input a valid number");
	}

	public boolean validateNumInput() {
		// allow only numbers to be typed
		
		// allow only numbers from 0 to 10 to be typed
		if (Integer.parseInt(_input.getText()) >= 0 && Integer.parseInt(_input.getText()) <= 10) {
			return true;
		}
		return false;
	}
}
