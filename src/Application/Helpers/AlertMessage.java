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
 * An Alert class for generating alerts
 */
public class AlertMessage implements Runnable {
	
	private Alert alert;
	private Image_Selection_ScreenController controller;
	private String status;
	private String creationName;
	
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
	
	public AlertMessage(String status) {
		this.status = status;
	}
	
	public AlertMessage(String status, String creationName, Image_Selection_ScreenController controller) {
		this.status = status;
		this.creationName = creationName;
		this.controller = controller;
	}

	/**
	 * Shows an alert depending on the status.
	 */
	@Override
	public void run() {
		switch(status) {
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
			  showSuccess("Creation \"" + creationName + "\" was successfully generated!");
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
		}
	}
		
	/**
	 * Shared method for generating alerts
	 * @param msg
	 */
	public void showAlert(String msg) {
		alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.show();
	}
	
	/**
	 * Method for generating successes
	 * @param msg
	 */
	private void showSuccess(String msg) {
		alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.show();
		((Home_ScreenController)(controller.getParentController().getParentController()).getParentController()).Update();
	}

	private void waitForConfirmation(String msg){
		alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		Optional<ButtonType> result = alert.showAndWait();
		if (!result.isPresent()){
		}
		else if (result.get() == ButtonType.OK){
			((Home_ScreenController)(controller.getParentController().getParentController())).Update();
		}
	}
}
