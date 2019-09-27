package Application.Helpers;

import java.util.List;

public class Audio {

    private String filename = null;
    private String termSearched = null;
    private List<String> content = null;
    private String numberOfLines = null;
    private String audioLength = null;


    public Audio(String termSearched, List<String> content, String numberOfLines) {
        this.termSearched = termSearched;
        this.content = content;
        this.numberOfLines = numberOfLines;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTermSearched() {
        return termSearched;
    }

    public void setTermSearched(String termSearched) {
        this.termSearched = termSearched;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(String numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public String getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(String audioLength) {
        this.audioLength = audioLength;
    }

}
