package Application.Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * A centralized class for Audio objects.
 */
public class Audio {
    private int pitch = 50;
    private int speed = 175;
    private String audioLength = null;
    private String filename = null;
    private String numberOfLines = null;
    private String termSearched = null;
    private String voice = "-ven-gb+m5";
    private String voiceDisplay = "English-USA-male"; // Voice Display is shown on the TableView under the corresponding 'Voice' column
    private List<String> content = new ArrayList<String>();

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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        if (voice == "English-USA-male") {
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
            this.voice = "-ven-gb+m5";
        }
        voiceDisplay = voice;
    }
    
    public String getVoiceDisplay() {
    	return voiceDisplay;
    }
    
    public void setVoiceDisplay(String voiceDisplay) {
    	this.voiceDisplay = voiceDisplay;
    }
}
