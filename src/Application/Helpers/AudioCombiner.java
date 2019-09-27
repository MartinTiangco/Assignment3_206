package Application.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class AudioCombiner extends Task<Long> {
	private List<Audio> _audioList;
	private final String AUDIO_DIR = ".Audio_Directory" + System.getProperty("file.separator");
	
	public AudioCombiner(ObservableList<Audio> audioList) {
		_audioList = audioList;
	}
	
	@Override
	protected Long call() throws Exception {
		// retrieves a list of filenames from the List of Audio objects as the input to the sox command
		List<String> filenames = getFilenames();
		String input = "";
		for (String filename : filenames) {
			input = input + " " + AUDIO_DIR + filename;
		}
		String cmd = "sox" + input + " " + AUDIO_DIR + "output.wav";
		System.out.println(cmd);
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
		
        try {
            process = builder.start();
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            int exitStatus = process.waitFor();
            System.out.println("Exit Status for sox is " + exitStatus);
            if (exitStatus == 0) {
            } else {
                AlertMessage alert = new AlertMessage("audio_combining_failed");
                Platform.runLater(alert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return null;
	}
	
	private List<String> getFilenames() {
		List<String> filenames = new ArrayList<String>();
		for (Audio a : _audioList) {
			System.out.println("Adding " + a.getFilename());
			filenames.add(a.getFilename());
		}	
		System.out.println(filenames);
		return filenames;
	}

}
