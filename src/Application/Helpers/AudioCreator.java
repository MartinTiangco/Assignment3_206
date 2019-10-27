package Application.Helpers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import Application.Controllers.Add_Audio_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * AudioCreator takes a list of text and the speech settings the user chose
 * and generates an audio file.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class AudioCreator extends Task<Long> {
	// the audio directory
	private final String AUDIO_DIR = ".Audio_Directory" + System.getProperty("file.separator");

	private Add_Audio_ScreenController _controller;
	private Audio _audio;
	private List<String> _content;
	
	/**
	 * The constructor for the Audio Creator
	 * @param audioIndex - used for making the id of the temporary audio files
	 * @param audio - the Audio object containing all the properties that were set by the user
	 * @param controller
	 */
	public AudioCreator(int audioIndex, Audio audio, Add_Audio_ScreenController controller) {
		audio.setFilename("temp" + String.valueOf(audioIndex) + ".wav");
		_audio = audio;
		_content = audio.getContent();
		_controller = controller;
	}
	
	@Override
	protected Long call() {
		// writes the content of the wikit search into a temporary text file 
		try {
			Path file = Paths.get(AUDIO_DIR + "temp.txt");
			Files.write(file, _content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// converting the List<String> to String for use in the espeak command below
		String texts = "";
        for (int i = 0; i < _audio.getContent().size(); i++) {
            texts = texts + _audio.getContent().get(i);
        }
		
        // the command for espeak, setting the pitch, speed, voice and wikit content chosen by the user
		String cmd = "espeak -p " + (_audio.getPitch())
				 		+ " -s " + (_audio.getSpeed()) + " "
				 		+ _audio.getVoice() 
				 		+ " -w " + AUDIO_DIR + _audio.getFilename() + " \"" + texts + "\"";
		
		// ProcessBuilder to generate audio
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		try {
			Process process = builder.start();
			int exitStatus = process.waitFor();
			if (exitStatus == 0) {
			} else {
				AlertMessage alert = new AlertMessage("combine_audio_failed");
				Platform.runLater(alert);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		Platform.runLater(() -> {
			_controller.getSearchTextField().setDisable(true);
			//Append the new Audio object onto the List of Audio TableView
			_controller.getAudioList().getItems().add(_audio);
			_controller.enableBottomHalf();
		});
		return null;
	}
}
