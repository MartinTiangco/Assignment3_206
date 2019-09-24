package Application;

import javafx.application.Platform;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateHelper extends Thread {
	private final String DIR = "./Creation_Directory/";
	
	private Home_ScreenController _homeScreenController;
	private List<Creation> _creations = new ArrayList<Creation>();
	
	public UpdateHelper(Home_ScreenController homeScreenController) {
		_homeScreenController = homeScreenController;
	}
	
	@Override
	public void run() {
		List<String> listOfFilenames = extractFromDirectory();
		createCreations(listOfFilenames);

		Update update = new Update(_creations, _homeScreenController);
		Platform.runLater(update);
	}

	class Update implements Runnable {

		private Home_ScreenController _homeScreenController;
		private List<Creation> _creations;

		public Update(List<Creation> creations, Home_ScreenController homeScreenController) {
			_homeScreenController = homeScreenController;
			_creations = creations;

		}


		@Override
		public void run() {
			_homeScreenController.getCreationTable().getItems().addAll(_creations);
		}
	}

	private List<String> extractFromDirectory() {
		List<String> listOfFilenames = new ArrayList<>();
		File dir = new File(DIR);
		File[] listOfFiles = dir.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			listOfFilenames.add(listOfFiles[i].getName());
		}
		return listOfFilenames;
	}
	
	private void createCreations(List<String> listOfFilenames) {
		
		for (String file : listOfFilenames) {
			//gets the first occurrence of the file separator pattern
			int firstPatternIndex = file.indexOf("_-_");
			
			//gets the second occurrence of the file separator pattern
			int secondPatternIndex = file.indexOf("_-_", firstPatternIndex + 3);
			
			Creation creation = new Creation(extractName(file, firstPatternIndex), 
					extractTerm(file, firstPatternIndex, secondPatternIndex), 
					extractDateModified(file),
					extractLength(file, secondPatternIndex));
			
			_creations.add(creation);
		}

	}
	
	private String extractName(String filename, int firstPatternIndex) {
		return filename.substring(0, firstPatternIndex);
	}
	
	private String extractTerm(String filename, int firstPatternIndex, int secondPatternIndex) {
		return filename.substring(firstPatternIndex + 3, secondPatternIndex);
	}
	
	private String extractDateModified(String filename) {
		return new SimpleDateFormat("dd/MM/yyyy h:mm a").format(new Date(new File(DIR + filename).lastModified()));

	}
	
	private String extractLength(String filename, int secondPatternIndex) {
		//gets the filename extension index i.e. ".mp4"
		int ext = filename.indexOf(".mp4");
		return filename.substring(secondPatternIndex + 3, ext);
	}
}
