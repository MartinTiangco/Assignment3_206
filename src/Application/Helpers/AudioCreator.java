package Application.Helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Application.Controllers.Add_Audio_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class AudioCreator extends Task<Long> {
	private final String DIR = ".Audio_Directory" + System.getProperty("file.separator");
	private List<String> _content;
	private Add_Audio_ScreenController _controller;
	private Audio _audio;
	
	public AudioCreator(int audioIndex, Audio audio, Add_Audio_ScreenController controller) {
		audio.setFilename("temp" + String.valueOf(audioIndex) + ".wav");
		_content = audio.getContent();
		_controller = controller;
	}
	
	@Override
	protected Long call() throws Exception {

		try {
			Path file = Paths.get(DIR + "temp.txt");
			System.out.println(_content);
			Files.write(file, _content);
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		String cmd = "text2wave -o " + DIR + _audio.getFilename() + DIR + "temp.txt" + " -eval .Audio_Directory/speech.scm";
		System.out.println(cmd);
		
		// ProcessBuilder to generate audio 
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		try {
			Process process = builder.start();
			int exitStatus = process.waitFor();
			
			if (exitStatus == 0) {
				System.out.println("success");
			} else {
				System.out.println("fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//Append onto ListView
				_controller.getAudioList().getItems().add("untitled");
			}
		});
		
		return null;
	}
}
