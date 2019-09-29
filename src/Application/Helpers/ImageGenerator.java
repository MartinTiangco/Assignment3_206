package Application.Helpers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.*;
import com.flickr4java.flickr.photos.*;

import Application.Controllers.Image_Selection_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class ImageGenerator extends Task<Long> {
	private final String OUTPUT_DIR = ".Output_Directory" + System.getProperty("file.separator");
	private final String IMAGE_DIR = ".Image_Directory" + System.getProperty("file.separator");
	private final String IMAGES = IMAGE_DIR + "*.jpg";
	private final String AUDIO = OUTPUT_DIR + "output.wav";
	
	private String _term;
	private int _numPics;
	private Image_Selection_ScreenController _controller;
	
	public ImageGenerator(String term, int numPics, Image_Selection_ScreenController controller) {
		_term = term;
		_numPics = numPics;
		_controller = controller;
	}

	@Override
	protected Long call() throws Exception {
		// delete all images first
		Cleaner cleaner = new Cleaner();
		cleaner.cleanImage();
		
		// retrieves from Flickr
		retrievePhotos();	

		// fills up progress bar
        updateProgress(1,1);
		return null;
	}
	
	/*
	 *  Uses flickr's API to retrieve photos
	 */
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
			
			int numId = 0;
			for (Photo photo : results) {
				try {
					BufferedImage image = photos.getImage(photo, Size.LARGE);
					String filename = query.trim().replace(' ', '-') + "-00" + numId + ".jpg";
					File outputfile = new File(".Image_Directory", filename);
					ImageIO.write(image, "jpg", outputfile);
					numId++;
				} catch (FlickrException fe) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Platform.runLater(() -> _controller.listImages());
	}
	
	/*
	 * Retrieves the APIKey from the file "flickr-api-keys.txt"
	 */
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
