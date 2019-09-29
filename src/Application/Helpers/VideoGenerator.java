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
	private String IMAGES = "";
	private final String AUDIO = OUTPUT_DIR + "output.wav";
	
	public VideoGenerator(String term, Image_Selection_ScreenController controller) {
		_term = term;
		_controller = controller;
	}

	@Override
	protected Long call() throws Exception {
		
		// retrieve the audio length
		String length = retrieveAudioLength();
		double lengthDouble = Double.parseDouble(length.substring(0, length.indexOf(".") + 3));
		
		// calculate the length for an image to show in the video
		double imgLength = lengthDouble/_numPics;
		
		// generate a slideshow
		generateVideo(imgLength);
		
		// generate subtitle
        generateSubtitle();
        
        // fills progress bar to show task has finished
        updateProgress(1,1);
        
        AlertMessage alert = new AlertMessage("creation_successful", _term, _controller);
        Platform.runLater(alert);
		
		// delete output.wav now that we don't need it anymore
		Cleaner cleaner = new Cleaner();
		cleaner.cleanAll();
		return null;
	}
	
	private String retrieveAudioLength() {
		String length = "";
		String cmd = "soxi -D " + OUTPUT_DIR + "output.wav";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
        try {
            process = builder.start();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int exitStatus = process.waitFor();
            if (exitStatus == 0) {
            	length = stdout.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        _length = length.substring(0, length.indexOf("."));
        return length;
	}
	
	public void generateVideo(double imgLength) {
		String cmd = "cat " + IMAGES + " | ffmpeg -f image2pipe -framerate " + (1/imgLength) + 
				" -i - -i " + AUDIO + " -vcodec libx264 -pix_fmt yuv420p -vf \"scale=w=1920:h=1080:force_original_aspect_ratio=1,pad=1920:1080:(ow-iw)/2:(oh-ih)/2\""
						+ " -r 25 -max_muxing_queue_size 1024 -y " + OUTPUT_DIR + "slideshow.mp4";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
        try {
            process = builder.start();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            int exitStatus = process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void generateSubtitle() {
		String cmd = "ffmpeg -i " + OUTPUT_DIR + "slideshow.mp4 -vf drawtext=\"text='" + _term + "': fontcolor=white: fontsize=72: box=1: boxcolor=black@0.5:boxborderw=5: x=(w-text_w)/2: y=h-(h-text_h)/3\" -codec:a copy -y " + OUTPUT_DIR + "tempCreation.mp4";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
        try {
            process = builder.start();
            int exitStatus = process.waitFor();
            if (exitStatus == 0) {
				// File (or directory) with old name
				File file = new File(OUTPUT_DIR + "tempCreation.mp4");
				File file2 = new File(CREATION_DIR + _creationName + "_-_" + _term + "_-_" + _length + ".mp4");

				Boolean success = false;
				if (file2.exists()) {
					AlertMessage msg = new AlertMessage("creation_exist");
					Platform.runLater(msg);
				}
				else {
					success = file.renameTo(file2);
				}
            }
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
