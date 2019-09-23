package Application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateHelper extends Thread {
	private final String DIR = "./Creation_Directory";
	
	private Controller _homeScreenController;
	
	public UpdateHelper(Controller homeScreenController) {
		_homeScreenController = homeScreenController;
	}
	
	@Override
	public void run() {
		List<String> listOfFilenames = extractFromDirectory();
		List<Creation> createCreations = createCreations();
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
		List<Creation> creations = new ArrayList<Creation>();
		
		for (String file : listOfFilenames) {
			//gets the first occurrence of the file separator pattern
			int firstPatternIndex = file.indexOf("_-_");
			
			//gets the second occurrence of the file separator pattern
			int secondPatternIndex = file.indexOf("_-_", firstPatternIndex);
			
			Creation creation = new Creation(extractName(file, firstPatternIndex), 
					extractTerm(file, firstPatternIndex, secondPatternIndex), 
					extractDateModified(file), 
					extractLength(file, secondPatternIndex));
			
			creations.add(creation);
		}
		// we need to add to existing list of creations in Runnable runLater class.
	}
	
	private String extractName(String filename, int firstPatternIndex) {
		return filename.substring(0, firstPatternIndex);
	}
	
	private String extractTerm(String filename, int firstPatternIndex, int secondPatternIndex) {
		return filename.substring(firstPatternIndex, secondPatternIndex);
	}
	
	private long extractDateModified(String filename) {
		return new File(DIR + filename).lastModified();
	}
	
	private int extractLength(String filename, int secondPatternIndex) {
		//gets the filename extension index i.e. ".mp4"
		int ext = filename.indexOf(".mp4");
		return Integer.parseInt(filename.substring(secondPatternIndex, ext));
	}
}
