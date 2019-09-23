package Application;

public class Creation {

    private String _name = null;
    private String _termSearched = null;
    private String _dateModified = null;
    private long _videoLength;


    public Creation(String name, String termSearched, String dateModified, long videoLength) {
        _name = name;
        _termSearched = termSearched;
        _dateModified = dateModified;
        _videoLength = videoLength;
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

    public long getVideoLength() {
        return _videoLength;
    }

    public void setVideoLength(long videoLength) {
        _videoLength = videoLength;
    }

}
