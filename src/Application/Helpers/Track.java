package Application.Helpers;

import javafx.scene.image.Image;

/**
 * Class containing the properties of the Background Music Tracks
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Track {
	// If no music track is selected in the ComboBox in the 'Background Music Screen'
	private final String NO_MUSIC = "- None -";
	
	// labels that say the other authors
	private final String FEATURING_EXCITED = "featuring tigabeatz"; // track named "yellow" by cyba
	private final String FEATURING_DRAMATIC = "featuring gummerstreet, reiswerk, moscardo"; // track named "Triptych of Snippets" by septahelix
	private final String FEATURING_RELAXED = "featuring simonlittlefield"; // track named "commonGround" by airtone
	private final String FEATURING_HAPPY = "featuring Martin Cee (softmartin)"; // track named "little reindeer" by JeffSpeed68
	
	// URL to the tracks
	private final String URL_EXCITED = "http://dig.ccmixter.org/files/cyba/60166";
	private final String URL_DRAMATIC = "http://dig.ccmixter.org/files/septahelix/60171";
	private final String URL_RELAXED = "http://dig.ccmixter.org/files/airtone/58703";
	private final String URL_HAPPY = "http://dig.ccmixter.org/files/JeffSpeed68/58885";

	// track properties
	private Image _profilePic;
	private Image _tag;
	private String _author;
	private String _trackName;
	private String _trackFile;
	private String _featured;
	private String _url;
	
	public Track(String trackName, String trackFile) {
		_trackFile = trackFile;
		_trackName = trackName;
		// extracts the authors from the filenames of the tracks
		if (!trackName.equals(NO_MUSIC)) {
			_author = extractAuthor();
		}
		
		// sets the profile pictures, tags, URL and the featuring labels depending on the track
		switch (trackName) {
			case "Relaxed":
				_featured = FEATURING_RELAXED;
				_profilePic = new Image(getClass().getResourceAsStream("/Application/assets/musicAuthors/commonGround-airtone.jpeg"));
				_tag = new Image(getClass().getResourceAsStream("/Application/assets/tags/jazzTag.png"));
				_url = URL_RELAXED;
				break;
			case "Excited":
				_featured = FEATURING_EXCITED;
				_profilePic = new Image(getClass().getResourceAsStream("/Application/assets/musicAuthors/yellow-cyba.jpg"));
				_tag = new Image(getClass().getResourceAsStream("/Application/assets/tags/technoTag.png"));
				_url = URL_EXCITED;
				break;
			case "Dramatic":
				_featured = FEATURING_DRAMATIC;
				_profilePic = new Image(getClass().getResourceAsStream("/Application/assets/musicAuthors/triptychOfSnippets-septahelix.jpg"));
				_tag = new Image(getClass().getResourceAsStream("/Application/assets/tags/electronicTag.png"));
				_url = URL_DRAMATIC;
				break;
			case "Happy":
				_featured = FEATURING_HAPPY;
				_profilePic = new Image(getClass().getResourceAsStream("/Application/assets/musicAuthors/littlereindeer-StefanKartenBerg.jpg"));
				_tag = new Image(getClass().getResourceAsStream("/Application/assets/tags/guitarTag.png"));
				_url = URL_HAPPY;
				break;
		}
	}

	/**
	 * Extracts the author from the track filename
	 */
	private String extractAuthor() {
		// gets the occurrence of the file separator pattern and the length
		int patternIndex = _trackFile.indexOf("_-_");
		return _trackFile.substring(0, patternIndex);
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
	
	public String getNoMusicString() {
		return NO_MUSIC;
	}
	
	public String getFeaturing() {
		return _featured;
	}
	
	public Image getProfilePic() {
		return _profilePic;
	}
	
	public String getURL() {
		return _url;
	}
	
	public Image getTag() {
		return _tag;
	}
}
