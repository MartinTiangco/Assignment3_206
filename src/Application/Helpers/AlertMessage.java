package Application.Helpers;

import Application.Controllers.Home_ScreenController;
import Application.Controllers.Image_Selection_ScreenController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class AlertMessage implements Runnable {
	
	private Alert alert;
	private String status;
	private String term;
	private Image_Selection_ScreenController controller;
	
	/**
	 * there are final strings to compare the command statuses to
	 */
	private final String VOICE_CANNOT_SPEAK = "voice_cannot_speak";
	private final String AUDIO_COMBINING_FAILED = "audio_combining_failed";
	private final String CREATE_AUDIO_UNSUCCESSFUL = "create_audio_failed";
	private final String CREATION_SUCCESSFUL = "creation_successful";
	private final String TOO_MANY_LINES = "Please select 5 lines or less";
	
	public AlertMessage(String status) {
		this.status = status;
	}
	
	public AlertMessage(String status, String term, Image_Selection_ScreenController controller) {
		this.status = status;
		this.term = term;
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
			  showSuccess("Creation " + term + " was successfully generated!");
			  break;
		  case TOO_MANY_LINES:
			  showAlert("Please select 5 lines or less");
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
	 * Shared method for generating successes
	 * @param msg
	 */
	private void showSuccess(String msg) {
		alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.show();
		((Home_ScreenController)(controller.getParentController().getParentController())).Update();
		Stage stage = (Stage)controller.getCreateButton().getScene().getWindow();
		stage.close();
	}
}
