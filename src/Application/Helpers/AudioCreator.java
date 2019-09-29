package Application.Helpers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import Application.Controllers.Add_Audio_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * AudioCreator takes a list of text and the speech settings and generates
 * an audio file.
 */
public class AudioCreator extends Task<Long> {
	private final String DIR = ".Audio_Directory" + System.getProperty("file.separator");

	private Add_Audio_ScreenController _controller;
	private Audio _audio;
	private List<String> _content;
	
	public AudioCreator(int audioIndex, Audio audio, Add_Audio_ScreenController controller) {
		audio.setFilename("temp" + String.valueOf(audioIndex) + ".wav");
		_audio = audio;
		_content = audio.getContent();
		_controller = controller;
	}
	
	@Override
	protected Long call() throws Exception {
		try {
			Path file = Paths.get(DIR + "temp.txt");
			Files.write(file, _content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String texts = "";
        for (int i = 0; i < _audio.getContent().size(); i++) {
            texts = texts + _audio.getContent().get(i);
        }
		
		String cmd = "espeak -p " + (_audio.getPitch())
				 		+ " -s " + (_audio.getSpeed()) + " "
				 		+ _audio.getVoice() 
				 		+ " -w " + DIR + _audio.getFilename() + " \"" + texts + "\"";
		
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
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//Append onto ListView
				_controller.getAudioList().getItems().add(_audio);
				_controller.enableBottomHalf();
			}
		});
		return null;
	}
}
