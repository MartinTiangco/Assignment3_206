package Application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateHelper extends Thread {
	private final String DIR = "./Creation_Directory";
	
	private Controller _homeScreenController;
	private List<Creation> _listOfCreations;
	private Creation _creation;
	private String _filename;
	private String _name;
	private String _termSearched;
	private long _dateModified;
	private int _length;
	
	public UpdateHelper(Controller homeScreenController) {
		_homeScreenController = homeScreenController;
	}
	
	@Override
	public void run() {
		extractDetails(extractFromDirectory());
	}
	
	private List<String> extractFromDirectory() {
		List<String> listOfFilenames = new ArrayList<>();
		File dir = new File(DIR);
		File[] listOfFiles = dir.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			listOfFilenames.add(listOfFiles[i].getName());
		}
	}
	
	//NEED TO CONVERT LIST<STRING> AND INDIVIDUALLY PROCESS
	private void extractDetails(List<String> filenames) {
		//gets the first occurrence of the file separator pattern
		int firstPatternIndex = _filename.indexOf("_-_");
		
		//gets the second occurrence of the file separator pattern
		int secondPatternIndex = _filename.indexOf("_-_", firstPatternIndex);
	
		_name = extractName(firstPatternIndex);
		_termSearched = extractTerm(firstPatternIndex, secondPatternIndex);
		_dateModified = extractDateModified();
		_length = extractLength(secondPatternIndex);
	}
	
	private String extractName(int firstPatternIndex) {
		return _filename.substring(0, firstPatternIndex);
	}
	
	private String extractTerm(int firstPatternIndex, int secondPatternIndex) {
		return _filename.substring(firstPatternIndex, secondPatternIndex);
	}
	
	private long extractDateModified() {
		return new File(DIR + _filename).lastModified();
	}
	
	private int extractLength(int secondPatternIndex) {
		//gets the filename extension index i.e. ".mp4"
		int ext = _filename.indexOf(".mp4");
		return Integer.parseInt(_filename.substring(secondPatternIndex, ext));
	}
}
