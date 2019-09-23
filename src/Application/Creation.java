package Application;

public class Creation {

    private String _name = null;
    private String _termSearched = null;
    private long _dateModified;
    private String _videoLength;


    public Creation(String name, String termSearched, long dateModified, String videoLength) {
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

    public long getDateModified() {
        return _dateModified;
    }

    public void setDateModified(long dateModified) {
        _dateModified = dateModified;
    }

    public String getVideoLength() {
        return _videoLength;
    }

    public void setVideoLength(String videoLength) {
        _videoLength = videoLength;
    }

}
