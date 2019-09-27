package Application.Helpers;

import java.util.List;

public class Audio {

    private String filename = null;
    private String termSearched = null;
    private List<String> content = null;
    private String numberOfLines = null;
    private String audioLength = null;
    private String pitch = "120";
    private String speed = "1";
    private String voice = "kal_diphone";


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

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}
