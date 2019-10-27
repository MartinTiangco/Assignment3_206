package Application.Helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import Application.Controllers.Add_Audio_ScreenController;
import Application.Controllers.Home_ScreenController;
import Application.Controllers.Image_Selection_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

/**
 * Generates the video by combining the slideshow and the audio files to make a Creation.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class VideoGenerator extends Task<Long> {
	private final String OUTPUT_DIR = ".Output_Directory" + System.getProperty("file.separator");
	private final String CREATION_DIR = "Creation_Directory" + System.getProperty("file.separator");
	private final String WIKIT_FILE = ".Wikit_Directory" + System.getProperty("file.separator") + "raw.txt";
	private String IMAGES = "";

	private Image_Selection_ScreenController _controller;
	private int _numPics;
	private File _folder;
	private String _audioFile;
	private String _creationName;
	private String _length;
	private String _term;

	public VideoGenerator(String term, Image_Selection_ScreenController controller) {
		_term = term;
		_controller = controller;
		_audioFile = OUTPUT_DIR + "music_output" 
				+ ((Add_Audio_ScreenController)_controller.getParentController().getParentController()).getAudioFileId() 
				+ ".wav";
	}

	@Override
	protected Long call() throws Exception {
		// close the 'Image Selection Screen', start progress indicator and label at the Home Screen
		Runnable closeWindow = new Runnable() {
			@Override
			public void run() {
				Stage stage = (Stage) _controller.getCreateButton().getScene().getWindow();
				stage.close();

				// get the home screen controller and set the progress indicator and progress label visible
				Home_ScreenController homeController = (Home_ScreenController) _controller.getParentController().getParentController().getParentController();
				ProgressIndicator progress = homeController.getProgressIndicator();
				progress.setProgress(-1);
				progress.setVisible(true);

				Label progressMsg = homeController.getProgressMsg();
				progressMsg.setText("Generating the creation \"" + _creationName + "\".");
				progressMsg.setVisible(true);
			}	
		};
		Platform.runLater(closeWindow);

		// retrieve the audio length
		retrieveAudioLength();

		// audio length in double format
		float lengthDouble = Float.parseFloat(_length.substring(0, _length.indexOf(".") + 3));

		// make the creation folder
		String length =  _length.substring(0, _length.indexOf("."));
		_folder = new File(CREATION_DIR + 
				_creationName + 
				"_-_" + _term + 
				"_-_" + length + 
				System.getProperty("file.separator"));

		if (_folder.isDirectory()) {
			// the folder already exists (though this means that the name, termSearched and the audio length are the same)
			AlertMessage msg = new AlertMessage("creation_exist");
			Platform.runLater(msg);
		}

		_folder.mkdir();

		// move the wikit content to the folder to track it for later use (in the Active Learning Component, the 'Quiz')
		Files.move(
				Paths.get(WIKIT_FILE),
				Paths.get(_folder + System.getProperty("file.separator") + "wikit.txt")); 

		// calculate the length for an image to show in the video
		float imgLength = lengthDouble/_numPics;

		// generate a slideshow
		generateSlideshow(imgLength);

		// generate video with audio
		generateVideo(imgLength);

		// generate subtitle
		generateSubtitle();

		// creation at this point has been created
		AlertMessage alert = new AlertMessage("creation_successful", _creationName, _controller);
		Platform.runLater(alert);

		// once finished, make the progress indicator and message invisible again
		Runnable finished = new Runnable() {
			@Override
			public void run() {
				// get the home screen controller and set the progress indicator and progress label invisible
				Home_ScreenController homeController = (Home_ScreenController) _controller.getParentController().getParentController().getParentController();
				homeController.getProgressIndicator().setVisible(false);
				homeController.getProgressMsg().setVisible(false);
			}	
		};
		Platform.runLater(finished);

		return null;
	}

	/**
	 * Retrieve the audio length from the Audio output
	 */
	private void retrieveAudioLength() {
		String cmd = "soxi -D " + _audioFile;
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
		try {
			process = builder.start();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			int exitStatus = process.waitFor();
			if (exitStatus == 0) {
				_length = stdout.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Make a slideshow out of the images selected in the 'Image Selection Screen'. Each image
	 * shows in equal amount of time.
	 */
	public void generateSlideshow(float imgLength) {
		String cmd = "cat " + IMAGES + " | ffmpeg -f image2pipe -framerate " + (1/imgLength) 
				+ " -i - -vf \"scale=w=1920:h=1080:force_original_aspect_ratio=1,pad=1920:1080:(ow-iw)/2:(oh-ih)/2\""
				+ " -r 25 -max_muxing_queue_size 1024 -y " 
				+ _folder + System.getProperty("file.separator") + "slideshow.mp4";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Makes the video by combining the audio file and the slideshow
	 * @param imgLength - the time length a single image will show in the video
	 */
	public void generateVideo(float imgLength) {
		String cmd = "ffmpeg -i " + _audioFile + 
				" -i " + _folder + System.getProperty("file.separator") + "slideshow.mp4"
				+ " -y " + _folder + System.getProperty("file.separator") + "video.mp4";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a subtitle of the search term in the middle of the screen
	 */
	public void generateSubtitle() {
		String cmd = "ffmpeg -i " + _folder + System.getProperty("file.separator") + "video.mp4 -vf drawtext=\"text='" 
				+ _term + "': fontcolor=white: fontsize=72: box=1: boxcolor=black@0.5:boxborderw=5: x=(w-text_w)/2: y=h-(h-text_h)/3\" -codec:a copy -y " 
				+ _folder + System.getProperty("file.separator") + "creation.mp4";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// removes the audio and output directory contents (all files are temporary)
		Cleaner cleaner = new Cleaner();
		cleaner.cleanAudio();
		cleaner.cleanOutput();
	}

	public void addImage(String image) {
		IMAGES = IMAGES + " " + image;
	}

	public void setCreationName(String creationName) {
		_creationName = creationName;
	}

	public void setNumPics(int numPics) {
		_numPics = numPics;
	}
}
