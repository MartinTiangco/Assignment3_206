package Application.Helpers;

/**
 * The Creation class containing the properties needed to populate the TableView in the 'Home Screen'.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Creation {

    private String _name = null;
    private String _termSearched = null;
    private String _dateModified = null;
    private String _videoLength = null;
    private String _fileName = null;
    private String _folderName = null;

    /**
     * The constructor for the Creation class
     * @param name - the given name of the creation
     * @param termSearched - the term used to search wikit
     * @param dateModified
     * @param videoLength
     * @param fileName
     */
    public Creation(String name, String termSearched, String dateModified, String videoLength, String fileName) {
        _name = name;
        _termSearched = termSearched;
        _dateModified = dateModified;
        _videoLength = videoLength;
        _fileName = fileName;
        _folderName = fileName.substring(0, fileName.indexOf(System.getProperty("file.separator")));
        
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getTermSearched() {
        return _termSearched;
    }

    public void setTermSearched(String termSearched) {
        _termSearched = termSearched;
    }

    public String getDateModified() {
        return _dateModified;
    }

    public void setDateModified(String dateModified) {
        _dateModified = dateModified;
    }

    public String getVideoLength() {
        return _videoLength;
    }

    public void setVideoLength(String videoLength) {
        _videoLength = videoLength;
    }

    public String getFileName() {
        return _fileName;
    }

    public void setFileName(String fileName) {
        _fileName = fileName;
    }
    
    public String getFolderName() {
    	return _folderName;
    }
}
