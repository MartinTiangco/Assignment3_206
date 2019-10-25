package Application.Helpers;

import java.util.HashMap;

/**
 * Class containing the properties of the Background Music Tracks
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Track {
	// If no music track is selected in the ComboBox in the 'Background Music Screen'
	private final String NO_MUSIC = "- None -";
	
	private String _author;
	private String _trackName;
	private String _trackFile;
	
	public Track(String trackName, String trackFile) {
		_trackFile = trackFile;
		_trackName = trackName;
		if (trackName != NO_MUSIC) {
			_author = extractAuthor();
		}
	}
	
	public String getAuthor() {
		return _author;
	}
	
	public String getTrackName() {
		return _trackName;
	}
	
	public String getTrackFile() {
		return _trackFile;
	}
	
	public String toString() {
		return _trackName;
	}
	
	public String extractAuthor() {
		System.out.println(_trackFile);
		// gets the occurrence of the file separator pattern and the length
		int patternIndex = _trackFile.indexOf("_-_");
		return _trackFile.substring(0, patternIndex);
	}
	
	public String getNoMusicString() {
		return NO_MUSIC;
	}
}
