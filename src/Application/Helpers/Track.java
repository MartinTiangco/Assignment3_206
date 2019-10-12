package Application.Helpers;

public class Track {
	private String _author;
	private String _trackName;
	
	public Track(String trackName) {
		_trackName = trackName;
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
}
