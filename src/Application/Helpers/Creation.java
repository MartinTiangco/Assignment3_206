package Application.Helpers;

public class Creation {

    private String name = null;
    private String termSearched = null;
    private String dateModified = null;
    private String videoLength = null;
    private String fileName = null;


    public Creation(String name, String termSearched, String dateModified, String videoLength, String fileName) {
        this.name = name;
        this.termSearched = termSearched;
        this.dateModified = dateModified;
        this.videoLength = videoLength;
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTermSearched() {
        return termSearched;
    }

    public void setTermSearched(String termSearched) {
        this.termSearched = termSearched;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
