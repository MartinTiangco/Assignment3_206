package Application.Helpers;

import Application.Controllers.Home_ScreenController;
import Application.Controllers.Image_Selection_ScreenController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * An Alert class for generating alerts.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class AlertMessage implements Runnable {
	
	private Alert _alert;
	private Image_Selection_ScreenController _controller;
	private String _status;
	private String _creationName;
	
	/**
	 * there are final strings to compare the command statuses to
	 */
	private final String VOICE_CANNOT_SPEAK = "voice_cannot_speak";
	private final String AUDIO_COMBINING_FAILED = "audio_combining_failed";
	private final String CREATE_AUDIO_UNSUCCESSFUL = "create_audio_failed";
	private final String CREATION_SUCCESSFUL = "creation_successful";
	private final String TOO_MANY_LINES = "Please select 5 lines or less";
	private final String NO_CREATIONS_FOUND = "no_creations_found";
	private final String BGM_FAILED = "bgm_failed";
	private final String NOT_FOUND = "not_found";
	
	/**
	 * The standard alert constructor.
	 * @param status - status of the error that will appear
	 */
	public AlertMessage(String status) {
		_status = status;
	}
	
	/**
	 * The alert message constructor showing when a creation generation is successful in the
	 * 'Image Selection Screen'. 
	 * @param status
	 * @param creationName
	 * @param controller
	 */
	public AlertMessage(String status, String creationName, Image_Selection_ScreenController controller) {
		_status = status;
		_creationName = creationName;
		_controller = controller;
	}

	/**
	 * Shows an alert depending on the status.
	 */
	@Override
	public void run() {
		switch(_status) {
		  case VOICE_CANNOT_SPEAK:
			  showAlert("We are sorry, but the chosen voice can not speak the line(s). Please choose another.");
			  break;
		  case AUDIO_COMBINING_FAILED:
			  showAlert("We are sorry, but the audio combining has failed for some reason. Please try combining another set of words.");
			  break;
		  case CREATE_AUDIO_UNSUCCESSFUL:
			  showAlert("We are sorry, but the audio cannot be created. Please try again.");
			  break;
		  case CREATION_SUCCESSFUL:
			  showSuccess("Creation \"" + _creationName + "\" was successfully generated!");
			  break;
		  case TOO_MANY_LINES:
			  showAlert("Please select 5 lines or less.");
			  break;
		  case NO_CREATIONS_FOUND:
			  showAlert("Please make a creation first!");
			  break;
		  case BGM_FAILED:
			  showAlert("We are sorry, but the background music can not be added.");
			  break;
		  case NOT_FOUND:
			  showAlert("We are sorry, but the search term can not be found. Please try again.");
			  break;
		}
	}
		
	/**
	 * Shared method for generating alerts
	 * @param msg
	 */
	public void showAlert(String msg) {
		_alert = new Alert(AlertType.ERROR);
		_alert.setHeaderText(null);
		_alert.setContentText(msg);
		_alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		_alert.show();
	}
	
	/**
	 * Method for generating a successful creation on the 'Image Selection Screen'
	 * @param msg
	 */
	private void showSuccess(String msg) {
		_alert = new Alert(AlertType.INFORMATION);
		_alert.setHeaderText(null);
		_alert.setContentText(msg);
		_alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		_alert.show();
		((Home_ScreenController)(_controller.getParentController().getParentController()).getParentController()).Update();
	}
}
