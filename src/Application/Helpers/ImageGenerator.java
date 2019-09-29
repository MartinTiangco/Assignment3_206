package Application.Helpers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.*;
import com.flickr4java.flickr.photos.*;

import Application.Controllers.Controller;
import Application.Controllers.Image_Selection_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ImageGenerator extends Task<Long> {
	private String _term;
	private int _numPics;
	private Image_Selection_ScreenController _controller;

	private final String OUTPUT_DIR = ".Output_Directory" + System.getProperty("file.separator");
	private final String IMAGE_DIR = ".Image_Directory" + System.getProperty("file.separator");
	private final String IMAGES = IMAGE_DIR + "*.jpg";
	private final String AUDIO = OUTPUT_DIR + "output.wav";
;			
	public ImageGenerator(String term, int numPics, Image_Selection_ScreenController controller) {
		_term = term;
		_numPics = numPics;
		_controller = controller;
	}

	@Override
	protected Long call() throws Exception {
		// delete all images first
		File file = new File(IMAGE_DIR);
		deleteDirContents(file);
		
		// retrieves from Flickr
		retrievePhotos();
		
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
	
	private void retrievePhotos() {
		try {
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");
			
			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
			
			String query = _term;
			int resultsPerPage = _numPics;
			int page = 0;
			
			PhotosInterface photos = flickr.getPhotosInterface();
			SearchParameters params = new SearchParameters();
			params.setSort(SearchParameters.RELEVANCE);
			params.setMedia("photos");
			params.setText(query);
			
			PhotoList<Photo> results = photos.search(params, resultsPerPage, page);
			System.out.println("Retrieving " + results.size() + " results");
			
			int numId = 0;
			for (Photo photo : results) {
				try {
					BufferedImage image = photos.getImage(photo, Size.LARGE);
					String filename = query.trim().replace(' ', '-') + "-00" + numId + ".jpg";
					File outputfile = new File(".Image_Directory", filename);
					ImageIO.write(image, "jpg", outputfile);
					System.out.println("Downloaded " + filename);
					numId++;
				} catch (FlickrException fe) {
					System.out.println("Ignoring image " + photo.getId() + ": " + fe.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		_controller.listImages();
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
        return length;
	}
	
	public void generateVideo(double imgLength) {
		String cmd = "cat " + IMAGES + " | ffmpeg -f image2pipe -framerate " + (1/imgLength) + 
				" -i - -i " + AUDIO + " -vcodec libx264 -pix_fmt yuv420p -vf \"scale=w=1920:h=1080:force_original_aspect_ratio=1,pad=1920:1080:(ow-iw)/2:(oh-ih)/2\""
						+ " -r 25 -max_muxing_queue_size 1024 -y " + OUTPUT_DIR + "videoout.mp4";
		
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		System.out.println("builder");
		Process process;
		System.out.println("process");
        try {
            process = builder.start();
            System.out.println("process started");
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
		String cmd = "ffmpeg -i " + OUTPUT_DIR + "videoout.mp4 -vf drawtext=\"text='" + _term + "': fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5:boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy " + OUTPUT_DIR + "tempCreation.mp4";
		System.out.println(cmd);
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		System.out.println("builder");
		Process process;
		System.out.println("process");
        try {
            process = builder.start();
            System.out.println("process started");
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
	
	public static String getAPIKey(String key) throws Exception {
		String config = System.getProperty("user.dir") + System.getProperty("file.separator") + "flickr-api-keys.txt";
		
		File file = new File(config);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line;
		while ((line = br.readLine()) != null) {
			if ((line.trim().startsWith(key))) {
				br.close();
				return line.substring(line.indexOf("=")+1).trim();
			}
		}
		br.close();
		throw new RuntimeException("Couldn't find " + key + " in config file " + file.getName());
	}
}
