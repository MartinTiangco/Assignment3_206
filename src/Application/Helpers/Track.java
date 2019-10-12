package Application.Helpers;

public class Track {
	private String _author;
	private String _trackName;
	private final String NO_MUSIC = "No music";
	
	public Track(String trackName) {
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
	
	public String toString() {
		return _trackName;
	}
	
	public String extractAuthor() {
		// gets the occurrence of the file separator pattern and the length
		int patternIndex = _trackName.indexOf("_-_");
		return _trackName.substring(0, patternIndex);
	}
	
	public String getNoMusicString() {
		return NO_MUSIC;
	}
}
