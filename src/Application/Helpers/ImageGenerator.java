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

/**
 * Class that retrieves the images from Flickr using an adapted version of Nasser's example.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class ImageGenerator extends Task<Long> {
	private Image_Selection_ScreenController _controller;
	private int _numPics;
	private String _term;
	
	public ImageGenerator(String term, int numPics, Image_Selection_ScreenController controller) {
		_controller = controller;
		_numPics = numPics;
		_term = term;
	}

	@Override
	protected Long call() throws Exception {
		// delete all images first from the images directory
		Cleaner cleaner = new Cleaner();
		cleaner.cleanImage();
		
		// retrieves from Flickr
		retrievePhotos();	

		// enables screen and sets progress indicator to invisible
		ProgressRunnable progressRunnable = new ProgressRunnable(_controller);
        Platform.runLater(progressRunnable);
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
		
		/**
		 * displays the images onto the 'Image Selection Screen'.
		 */
		Platform.runLater(() -> {
			_controller.listImages();
			_controller.getListOfImages().getSelectionModel().select(0);
			_controller.selectImage();
			_controller.getSelectAll().setVisible(true);
		});
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
