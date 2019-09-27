package Application.Helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;


public class AlertMessage implements Runnable {
	
	private Alert alert;
	private String status;
	
	/**
	 * there are final strings to compare the command statuses to
	 */
	private final String VOICE_CANNOT_SPEAK = "voice_cannot_speak";
	private final String CREATE_AUDIO_UNSUCCESSFUL = "create_audio_failed";
	
	public AlertMessage(String status) {
		this.status = status;
	}

	/**
	 * Shows an alert depending on the status.
	 */
	@Override
	public void run() {
		switch(status) {
			case VOICE_CANNOT_SPEAK:
				showAlert("We are sorry, but the chosen voice can not speak the line(s). Please choose another voice.");
				break;
			case CREATE_AUDIO_UNSUCCESSFUL:
				showAlert("We are sorry, but the audio cannot be created. Please try again.");
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
	}
}
