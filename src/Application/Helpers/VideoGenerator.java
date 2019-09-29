package Application.Helpers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

import Application.Controllers.Image_Selection_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;


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
;			
	public VideoGenerator(String term, Image_Selection_ScreenController controller) {
		_term = term;
		_controller = controller;
	}

	@Override
	protected Long call() throws Exception {
		
		// retrieve the audio length
		String length = retrieveAudioLength();
		System.out.println(length);
		double lengthDouble = Double.parseDouble(length.substring(0, length.indexOf(".") + 3));
		
		// calculate the length for an image to show in the video
		System.out.println("Calculating length...");
		double imgLength = lengthDouble/_numPics;
		System.out.println("each image shows for " + imgLength);
		
		// generate a slideshow
		generateVideo(imgLength);
		
		// generate subtitle
        generateSubtitle();
        
		
		// delete output.wav now that we don't need it anymore
//		File outFile = new File(OUTPUT_DIR);
//		deleteDirContents(outFile);
		
		return null;
	}
	
	private String retrieveAudioLength() {
		String length = "";
		String cmd = "soxi -D " + OUTPUT_DIR + "output.wav";
		System.out.println(cmd);
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
        try {
            process = builder.start();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            int exitStatus = process.waitFor();
            System.out.println("Exit Status for soxi is " + exitStatus);
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
		System.out.println(cmd);
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		Process process;
        try {
            process = builder.start();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            int exitStatus = process.waitFor();
            String line = "";
            while (((line = stdout.readLine()) != null)) {
            	System.out.println(line);
            }
            System.out.println("Exit Status for ffmpeg is " + exitStatus);
            if (exitStatus == 0) {
            	System.out.println("success");
            } else {
            	System.out.println("fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void generateSubtitle() {
		String cmd = "ffmpeg -i " + OUTPUT_DIR + "slideshow.mp4 -vf drawtext=\"text='" + _term + "': fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5:boxborderw=5: x=(w-text_w)/2: y=h-(h-text_h)/3\" -codec:a copy -y " + OUTPUT_DIR + "tempCreation.mp4";
		System.out.println(cmd);
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		System.out.println("builder");
		Process process;
		System.out.println("process");
        try {
            process = builder.start();
            System.out.println("process started");
            int exitStatus = process.waitFor();
            System.out.println("Exit Status for ffmpeg is " + exitStatus);
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
				if (!success) {
					System.out.println("rename failed");
				}
            	System.out.println("success");
            } else {
            	System.out.println("fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public void addImage(String image) {
		this.IMAGES = this.IMAGES + " " + image;
	}

	public static void deleteDirContents(File dir) {
		File[] files = dir.listFiles();
		if(files != null) {
			for (File f: files) {
				if (f.isDirectory()) {
					deleteDirContents(f);
				} else {
					f.delete();
				}
			}
		}
	}

	public void set_numPics(int _numPics) {
		this._numPics = _numPics;
	}
}
