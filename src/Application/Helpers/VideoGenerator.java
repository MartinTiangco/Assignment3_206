package Application.Helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import Application.Controllers.Image_Selection_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Generates the video by combining the slideshow and the audio files to make a Creation.
 */
public class VideoGenerator extends Task<Long> {
	private String _term;
	private String _creationName;
	private String _length;
	private int _numPics;
	private Image_Selection_ScreenController _controller;

	private final String OUTPUT_DIR = ".Output_Directory" + System.getProperty("file.separator");
	private final String CREATION_DIR = "Creation_Directory" + System.getProperty("file.separator");
	private File _folder;
	private String IMAGES = "";
	private final String AUDIO = OUTPUT_DIR + "output.wav";
	
	public VideoGenerator(String term, Image_Selection_ScreenController controller) {
		_term = term;
		_controller = controller;
	}

	@Override
	protected Long call() throws Exception {
		
		// retrieve the audio length
		retrieveAudioLength();
		float lengthDouble = Float.parseFloat(_length.substring(0, _length.indexOf(".") + 3));
		
		// make the creation folder
		String length =  _length.substring(0, _length.indexOf("."));
		_folder = new File(CREATION_DIR + 
				_creationName + 
				"_-_" + _term + 
				"_-_" + length + 
				System.getProperty("file.separator"));
		
		Boolean success = false;
		if (_folder.isDirectory()) {
			AlertMessage msg = new AlertMessage("creation_exist");
			Platform.runLater(msg);
        }

        _folder.mkdir();
		
		// calculate the length for an image to show in the video
		float imgLength = lengthDouble/_numPics;
		
		// generate a slideshow
		generateSlideshow(imgLength);
		
		// generate video with audio
		generateVideo(imgLength);

		// generate subtitle
        generateSubtitle();

        ProgressRunnable progressRunnable = new ProgressRunnable(_controller);
        Platform.runLater(progressRunnable);
        
        AlertMessage alert = new AlertMessage("creation_successful", _creationName, _controller);
        Platform.runLater(alert);
		
		// delete output.wav now that we don't need it anymore
//		Cleaner cleaner = new Cleaner();
//		cleaner.cleanAll();
		return null;
	}
	
	private void retrieveAudioLength() {
		String length = "";
		String cmd = "soxi -D " + OUTPUT_DIR + "output.wav";
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

	public void generateSlideshow(float imgLength) {
		String cmd = "cat " + IMAGES + " | ffmpeg -f image2pipe -framerate " + (1/imgLength) 
				+ " -i - -vf \"scale=w=1920:h=1080:force_original_aspect_ratio=1,pad=1920:1080:(ow-iw)/2:(oh-ih)/2\""
				+ " -r 25 -max_muxing_queue_size 1024 -y " 
				+ _folder + System.getProperty("file.separator") + "slideshow.mp4";
		System.out.println(cmd);
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
        try {
            process = builder.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void generateVideo(float imgLength) {
		System.out.println(1/imgLength);
		String cmd = "ffmpeg -i " + AUDIO + 
				" -i " + _folder + System.getProperty("file.separator") + "slideshow.mp4"
				+ " -y " + _folder + System.getProperty("file.separator") + "video.mp4";
		System.out.println(cmd);
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
        try {
            process = builder.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void generateSubtitle() {
		String cmd = "ffmpeg -i " + _folder + System.getProperty("file.separator") + "video.mp4 -vf drawtext=\"text='" 
					+ _term + "': fontcolor=white: fontsize=72: box=1: boxcolor=black@0.5:boxborderw=5: x=(w-text_w)/2: y=h-(h-text_h)/3\" -codec:a copy -y " 
					+ _folder + System.getProperty("file.separator") + "creation.mp4";
		System.out.println(cmd);
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
        try {
            process = builder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public void addImage(String image) {
		this.IMAGES = this.IMAGES + " " + image;
	}

	public void setCreationName(String creationName) {
		this._creationName = creationName;
	}

	public void setNumPics(int numPics) {
		this._numPics = numPics;
	}
}
