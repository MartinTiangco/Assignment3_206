package Application.Helpers;

import Application.Controllers.Background_Music_ScreenController;
import Application.Helpers.AlertMessage;
import Application.Helpers.Track;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class MusicAdder extends Task<Long> {
	
	private final String AUDIO_DIR = ".Audio_Directory" + System.getProperty("file.separator");
	private final String MUSIC_DIR = ".Music_Directory" + System.getProperty("file.separator");
	private final String OUTPUT_DIR = ".Output_Directory" + System.getProperty("file.separator");
	
	private Track _chosenTrack;
	private String _spokenFile;
	private Background_Music_ScreenController _controller;
	
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
			cmd = "ffmpeg -i " + spokenAudio + " " + output;
		} else {
			String bgMusic = MUSIC_DIR + _chosenTrack.getTrackName();
			cmd = "ffmpeg -i " + spokenAudio + " -i " + bgMusic + " -filter_complex amerge=inputs=2 -ac 2 " + output;
		}
		
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		System.out.println(cmd);
		Process process;
		try {
			process = builder.start();
			int exitStatus = process.waitFor();
			if (exitStatus == 0) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						// loads the Image Selection Screen
						_controller.loadScreen("Image Selection Screen", "/Application/fxml/Image_Selection_Screen.fxml", "");

						// closes the Add Audio screen
						Stage stage = (Stage) _controller.getNextButton().getScene().getWindow();
						stage.close();
					}          		
				};
				Platform.runLater(runnable);
			} else {
				AlertMessage alert = new AlertMessage("bgm_failed");
				Platform.runLater(alert);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
