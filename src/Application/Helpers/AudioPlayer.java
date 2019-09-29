package Application.Helpers;

import Application.Controllers.Add_Audio_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AudioPlayer extends Task<Long> {

    private Add_Audio_ScreenController _controller;
    private Audio _audio;
    private String _texts;
    private Process _process;

    public AudioPlayer(Audio audio, Add_Audio_ScreenController controller) {
        _audio = audio;
        _controller = controller;
    }

    @Override
    protected Long call() throws Exception {
        if (_audio.getFilename() != null) {
        	_controller.getPlayTextButton().setDisable(true);
            playAudio();
        }
        else {
            playText();
        }
        return null;
    }

    public void playText() {
        setTexts();
        String cmd = "espeak -p " + String.valueOf(_audio.getPitch()) +
                		" -s " + String.valueOf((int)(_audio.getSpeed())) + " -a 50" +
                		" " + _audio.getVoice() + " \"" + _texts + "\"";
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
        System.out.println("The command for playing text is: " + cmd);
        StartProcess(builder);
        return;
    }

    public void playAudio() {
        File fileUrl = new File(".Audio_Directory" + System.getProperty("file.separator") + _audio.getFilename());
        Media audio = new Media(fileUrl.toURI().toString());
        MediaPlayer player = new MediaPlayer(audio);
        player.setAutoPlay(true);
        _controller.getMediaView().setMediaPlayer(player);

        player.setOnEndOfMedia(() -> {
            _controller.getPlayTextButton().setDisable(false);
        });
    }

    public void setTexts() {
    	_texts = "";
        for (int i = 0; i < _audio.getContent().size(); i++) {
        	System.out.println("Adding to text: " + _texts);
            _texts = _texts + _audio.getContent().get(i);
        }
        System.out.println(_texts);
    }

    public void StartProcess(ProcessBuilder builder) {
        try {
            _process = builder.start();
            BufferedReader stderr = new BufferedReader(new InputStreamReader(_process.getErrorStream()));
            int exitStatus = _process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Process getProcess() {
        return _process;
    }
}
