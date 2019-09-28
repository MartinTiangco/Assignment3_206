package Application.Helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Application.Controllers.Add_Audio_ScreenController;
import Application.Controllers.Controller;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AudioCombiner extends Task<Long> {
	private List<Audio> _audioList;
	private Add_Audio_ScreenController _controller;
	private final String AUDIO_DIR = ".Audio_Directory" + System.getProperty("file.separator");
	
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
            	Runnable runnable = new Runnable() {

					@Override
					public void run() {
						// removes the audio directory contents (all files are temporary)
						File dir = new File(".Audio_Directory");
						_controller.deleteDirContents(dir);
						
						Stage imageScreen = new Stage();
		        		try {
		        			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Image_Selection_Screen.fxml"));
		        	        Parent root = loader.load();
		        	        Controller Image_Selection_ScreenController = loader.getController();
		        	        Image_Selection_ScreenController.setCurrentController(Image_Selection_ScreenController);
		        	        Image_Selection_ScreenController.setParentController(_controller);
		        	        imageScreen.setTitle("VARpedia - Image Selection Screen");
		        	        Scene scene = new Scene(root, 700, 600);
		        	        //scene.getStylesheets().addAll(this.getClass().getResource("css/Home_Screen.css").toExternalForm());
		        	        imageScreen.setScene(scene);
		        	        imageScreen.show();
		        		} catch (Exception e) {
		        			e.printStackTrace();
		        		}
		        		
		        		// closes the audio screen
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
			System.out.println("Adding " + a.getFilename());
			filenames.add(a.getFilename());
		}	
		System.out.println(filenames);
		return filenames;
	}

}
