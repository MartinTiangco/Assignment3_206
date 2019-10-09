package Application.Helpers;

import Application.Controllers.Home_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Updates the Home Menu Screen
 */
public class UpdateHelper extends Task<Long> {
	private final String DIR = "./Creation_Directory/";
	
	//SEPARATOR_LENGTH is the sequence of characters "_-_" used to extract data from a file
	private final int SEPARATOR_LENGTH = 3;

	private Home_ScreenController _homeScreenController;
	private List<Creation> _creations = new ArrayList<Creation>();

	public UpdateHelper(Home_ScreenController homeScreenController) {
		_homeScreenController = homeScreenController;
	}

	@Override
	public Long call() {
			List<String> listOfFilenames = extractFromDirectory();
			createCreations(listOfFilenames);
			Platform.runLater(new Update());
		return null;
	}

	class Update implements Runnable {
		@Override
		public void run() {
			_homeScreenController.getCreationTable().getItems().clear();
			_homeScreenController.getCreationTable().getItems().addAll(_creations);
		}
	}

	private List<String> extractFromDirectory() {
		List<String> listOfFilenames = new ArrayList<>();
		File dir = new File(DIR);
		File[] listOfFiles = new File(DIR).listFiles(File::isDirectory);

		for (int i = 0; i < listOfFiles.length; i++) {
			listOfFilenames.add(listOfFiles[i].getName());
		}
		return listOfFilenames;
	}
	
	/*
	 * Extract details from the filenames of Creations and display on the TableView
	 */
	private void createCreations(List<String> listOfFilenames) {
		for (String file : listOfFilenames) {
			if (new File(DIR + file + System.getProperty("file.separator") + "creation.mp4").exists()) {
				//gets the first occurrence of the file separator pattern
				int firstPatternIndex = file.indexOf("_-_");
				
				//gets the second occurrence of the file separator pattern
				int secondPatternIndex = file.indexOf("_-_", firstPatternIndex + SEPARATOR_LENGTH);
				
				Creation creation = new Creation(extractName(file, firstPatternIndex), 
						extractTerm(file, firstPatternIndex, secondPatternIndex), 
						extractDateModified(file),
						extractLength(file, secondPatternIndex), file + System.getProperty("file.separator") + "creation.mp4");
				
				_creations.add(creation);
			}
		}
	}
	
	private String extractName(String filename, int firstPatternIndex) {
		return filename.substring(0, firstPatternIndex);
	}
	
	private String extractTerm(String filename, int firstPatternIndex, int secondPatternIndex) {
		return filename.substring(firstPatternIndex + SEPARATOR_LENGTH, secondPatternIndex);
	}
	
	private String extractDateModified(String filename) {
		return new SimpleDateFormat("yyyy/MM/dd h:mm a").format(new Date(new File(DIR + filename).lastModified()));
	}
	
	/**
	 * Extracts length in mm:ss format
	 */
	private String extractLength(String filename, int secondPatternIndex) {
		int seconds = Integer.parseInt(filename.substring(secondPatternIndex + SEPARATOR_LENGTH));
		int sec = seconds % 60;
		int min = seconds / 60;
		String timeMin = "" + min;
		String timeSec = "" + sec;
		
		// format the time; add a leading 0 if sec / min is less than 0 seconds
		if (sec < 10) {
			timeSec = "0" + sec;
		} 
		if (min < 10) {
			timeMin = "0" + min;
		}
		return timeMin + ":" + timeSec;
	}
}
