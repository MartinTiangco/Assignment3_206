package Application.Helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Application.Controllers.Add_Audio_ScreenController;
import Application.Controllers.Image_Selection_ScreenController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AudioCombiner extends Task<Long> {
	private final String AUDIO_DIR = ".Audio_Directory" + System.getProperty("file.separator");
	private final String OUTPUT_DIR = ".Output_Directory" + System.getProperty("file.separator");
	
	private List<Audio> _audioList;
	private Add_Audio_ScreenController _controller;
	
	public AudioCombiner(ObservableList<Audio> audioList, Add_Audio_ScreenController controller) {
		_audioList = audioList;
		_controller = controller;
	}
	
	@Override
	protected Long call() throws Exception {
		// retrieves a list of filenames from the List of Audio objects as the input to the sox command
		List<String> filenames = getFilenames();
		String input = "";
		for (String filename : filenames) {
			input = input + " " + AUDIO_DIR + filename;
		}
		
		int id = new File(OUTPUT_DIR).listFiles().length;
		_controller.setAudioFileId(id);
		
		// combines multiple audio files for the creation
		String cmd = "sox" + input + " " + OUTPUT_DIR + "output" + id + ".wav";
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
		
        try {
            process = builder.start();
            int exitStatus = process.waitFor();
            if (exitStatus == 0) {
            	Runnable runnable = new Runnable() {
					@Override
					public void run() {
						// loads the Image Selection Screen
						_controller.loadScreen("Background Music Selection", "/Application/fxml/Background_Music_Screen.fxml", "");
		        		
		        		// closes the Add Audio screen
		        		Stage stage = (Stage) _controller.getAudioList().getScene().getWindow();
		                stage.close();
					}          		
            	};
            	Platform.runLater(runnable);
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
			filenames.add(a.getFilename());
		}
		return filenames;
	}

}
