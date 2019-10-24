package Application.Helpers;

import Application.Controllers.Add_Audio_ScreenController;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Plays the 'Saved Audio' and previews the selected text
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class AudioPlayer extends Task<Long> {

    private Add_Audio_ScreenController _controller;
    private Audio _audio;
    private Process _process;
    private String _texts;

    public AudioPlayer(Audio audio, Add_Audio_ScreenController controller) {
        _audio = audio;
        _controller = controller;
    }

    @Override
    protected Long call() throws Exception {
    	// previews either the content text from Wikipedia or the saved audio
        if (_audio.getFilename() != null) {
            playAudio();
        }
        else {
            playText();
        }
        return null;
    }

    /*
     * Plays the text from the Wikipedia content
     */
    public void playText() {
        setTexts();
        String cmd = "espeak -p " + String.valueOf(_audio.getPitch()) +
                		" -s " + String.valueOf((int)(_audio.getSpeed())) + " -a 50" +
                		" " + _audio.getVoice() + " \"" + _texts + "\"";
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
        startProcess(builder);
        return;
    }

    /**
     * Plays the audio from the saved audio table
     */
    public void playAudio() {
        File fileUrl = new File(".Audio_Directory" + System.getProperty("file.separator") + _audio.getFilename());
        Media audio = new Media(fileUrl.toURI().toString());
        MediaPlayer player = new MediaPlayer(audio);
        player.setAutoPlay(true);
        _controller.getMediaView().setMediaPlayer(player);
    }

    public void setTexts() {
    	_texts = "";
        for (int i = 0; i < _audio.getContent().size(); i++) {
            _texts = _texts + _audio.getContent().get(i);
        }
    }

    /**
     * Starts the preview text process so we could keep track of it
     */
    public void startProcess(ProcessBuilder builder) {
        try {
            _process = builder.start();
            _process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Process getProcess() {
        return _process;
    }
}
