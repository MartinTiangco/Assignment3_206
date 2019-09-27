package Application.Helpers;

import java.util.List;

public class Audio {

    private String filename = null;
    private String termSearched = null;
    private List<String> content = null;
    private String numberOfLines = null;
    private String audioLength = null;
    private int pitch = 120;
    private double speed = 1.0;
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

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        if (voice == "Dumb Voice") {
            this.voice = "akl_nz_jdt_diphone";
        }
        else if (voice == "English-USA-male") {
            this.voice = "-ven-us+m5";
        }
        else if (voice == "English-USA-female") {
            this.voice = "-ven-us+f2";
        }
        else if (voice == "English-UK-male") {
            this.voice = "-ven-gb+m5";
        }
        else if (voice == "English-UK-female") {
            this.voice = "-ven-gb+f2";
        }
        else {
            this.voice = "kal_diphone";
        }
    }
}
