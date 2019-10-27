package Application.Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * A centralized class for Audio objects with the appropriate properties.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Audio {
    private int _pitch = 50;
    private int _speed = 175;
    private String _audioLength = null;
    private String _filename = null;
    private String _numberOfLines = null;
    private String _termSearched = null;
    private String _voice = "-ven-gb+m5"; // this value is the espeak voice value
    private String _voiceDisplay = "English-USA-male"; // Voice Display is shown on the TableView under the corresponding 'Voice' column
    private List<String> _content = new ArrayList<String>();

    public String getFilename() {
        return _filename;
    }

    public void setFilename(String filename) {
        _filename = filename;
    }

    public String getTermSearched() {
        return _termSearched;
    }

    public void setTermSearched(String termSearched) {
        _termSearched = termSearched;
    }

    public List<String> getContent() {
        return _content;
    }

    public void setContent(List<String> content) {
        _content = content;
    }

    public String getNumberOfLines() {
        return _numberOfLines;
    }

    public void setNumberOfLines(String numberOfLines) {
        _numberOfLines = numberOfLines;
    }

    public int getPitch() {
        return _pitch;
    }

    public void setPitch(int pitch) {
        _pitch = pitch;
    }

    public int getSpeed() {
        return _speed;
    }

    public void setSpeed(int speed) {
        _speed = speed;
    }

    public String getVoice() {
        return _voice;
    }

    /**
     * Sets the corresponding espeak voice (the variable _voice) according to the ComboBox voice options (the variable voice)
     * @param voice - this voice is the description on the ComboBox
     */
    public void setVoice(String voice) {
        switch (voice) {
            case "English-USA-male":
                _voice = "-ven-us+m5";
                break;
            case "English-USA-female":
                _voice = "-ven-us+f2";
                break;
            case "English-UK-female":
                _voice = "-ven-gb+f2";
                break;
            default:
                _voice = "-ven-gb+m5";
                break;
        }
        _voiceDisplay = voice;
    }
    
    public String getVoiceDisplay() {
    	return _voiceDisplay;
    }

}
