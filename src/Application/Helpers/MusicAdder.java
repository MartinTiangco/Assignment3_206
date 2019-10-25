package Application.Helpers;

import Application.Controllers.Background_Music_ScreenController;
import Application.Helpers.AlertMessage;
import Application.Helpers.Track;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * Class to add the background music into the output audio file
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class MusicAdder extends Task<Long> {
	private final String MUSIC_DIR = ".Music_Directory" + System.getProperty("file.separator");
	private final String OUTPUT_DIR = ".Output_Directory" + System.getProperty("file.separator");

	private Background_Music_ScreenController _controller;
	private String _spokenFile; // the file that is sent through text-to-speech
	private Track _chosenTrack; // the music track that the user selected
	
	public MusicAdder(Track chosenTrack, String spokenFile, Background_Music_ScreenController controller) {
		_chosenTrack = chosenTrack;
		_spokenFile = spokenFile;
		_controller = controller;
	}

	@Override
	protected Long call() throws Exception {
		// combines spoken audio with background music
		String spokenAudio = OUTPUT_DIR + _spokenFile;
		String output = OUTPUT_DIR + "music_" + _spokenFile;
		
		String cmd = "";	
		if (_chosenTrack.getTrackName().equals(_chosenTrack.getNoMusicString())) {
			// no background music is chosen
			cmd = "ffmpeg -y -i " + spokenAudio + " " + output;
		} else {
			String bgMusic = MUSIC_DIR + _chosenTrack.getTrackFile();
			cmd = "ffmpeg -i " + spokenAudio + " -i " + bgMusic + " -filter_complex amerge=inputs=2 -ac 2 -y " + output;
		}
		
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			Process process = builder.start();
			int exitStatus = process.waitFor();
			if (exitStatus == 0) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						// loads the Image Selection Screen
						_controller.loadScreen("Image Selection Screen", "/Application/fxml/Image_Selection_Screen.fxml", "");

						// closes the 'Background Music Screen'
						Stage stage = (Stage) _controller.getNextButton().getScene().getWindow();
						stage.hide();
					}          		
				};
				Platform.runLater(runnable);
			} else {
				// if the background music fails to combine with the audio
				AlertMessage alert = new AlertMessage("bgm_failed");
				Platform.runLater(alert);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
