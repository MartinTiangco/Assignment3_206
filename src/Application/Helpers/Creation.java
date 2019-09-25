package Application.Helpers;

public class Creation {

    private String name = null;
    private String termSearched = null;
    private String dateModified;
    private String videoLength;


    public Creation(String name, String termSearched, String dateModified, String videoLength) {
        this.name = name;
        this.termSearched = termSearched;
        this.dateModified = dateModified;
        this.videoLength = videoLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public String getTermSearched() {
        return termSearched;
    }

    public void setTermSearched(String termSearched) {
        termSearched = termSearched;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        dateModified = dateModified;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        videoLength = videoLength;
    }

}
