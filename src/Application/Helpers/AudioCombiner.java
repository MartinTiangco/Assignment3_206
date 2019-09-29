package Application.Helpers;

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
		// combines multiple audio files for the creation
		String cmd = "sox" + input + " " + OUTPUT_DIR + "output.wav";
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
		
        try {
            process = builder.start();
            int exitStatus = process.waitFor();
            if (exitStatus == 0) {
            	Runnable runnable = new Runnable() {
					@Override
					public void run() {
						// removes the audio directory contents (all files are temporary)
						Cleaner cleaner = new Cleaner();
						cleaner.cleanAudio();
						cleaner.cleanWikit();
						
						// loads the Image Selection Screen
						Stage imageScreen = new Stage();
		        		try {
		        			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/fxml/Image_Selection_Screen.fxml"));
		        	        Parent root = loader.load();
		        	        Image_Selection_ScreenController Image_Selection_ScreenController = loader.getController();
		        	        Image_Selection_ScreenController.setCurrentController(Image_Selection_ScreenController);
		        	        Image_Selection_ScreenController.setParentController(_controller);
		        	        imageScreen.setTitle("VARpedia - Image Selection Screen");
		        	        Scene scene = new Scene(root, 631, 500);
		        	        imageScreen.setScene(scene);
		        	        imageScreen.show();
		        		} catch (Exception e) {
		        			e.printStackTrace();
		        		}
		        		
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
