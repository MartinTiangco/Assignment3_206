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

    public AudioPlayer(Audio audio, Add_Audio_ScreenController controller) {
        _audio = audio;
        _controller = controller;
    }

    @Override
    protected Long call() throws Exception {
        if (_audio.getFilename() != null) {
            playAudio();
        }
        else {
            playText();
        }
        return null;
    }

    public void playText() {
        setTexts();
        if (_audio.getVoice() == "kal_diphone" || _audio.getVoice() == "akl_nz_jdt_diphone" ) {
            CreateScmFile();
        }
        else{
            String cmd = "espeak -p " + String.valueOf(_audio.getPitch() - 70) +
                    " -s " + String.valueOf((int)(_audio.getSpeed()*175.0)) + " " + _audio.getVoice() + " \"" + _texts + "\"";
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
            System.out.println(cmd);
            StartProcess(builder);
            return;
        }
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", "festival -b .Audio_Directory/speech.scm");
        StartProcess(builder);
    }

    public void playAudio() {

        File fileUrl = new File(".Audio_Directory" + System.getProperty("file.separator") + _audio.getFilename() + ".wav");
        Media video = new Media(fileUrl.toURI().toString());
        MediaPlayer player = new MediaPlayer(video);
        player.setAutoPlay(true);

        _controller.getMediaView().setMediaPlayer(player);
    }

    public void setTexts () {
        for (int i = 0; i < _audio.getContent().size(); i++) {
            _texts = _texts + _audio.getContent().get(i);
        }
    }

    public void StartProcess(ProcessBuilder builder) {
        Process process;
        try {
            process = builder.start();
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            int exitStatus = process.waitFor();
            System.out.println(exitStatus);
            if (exitStatus == 0) {
            } else {
                AlertMessage alert = new AlertMessage("voice_cannot_speak");
                Platform.runLater(alert);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void CreateScmFile(){
        List<String> instruction = new ArrayList<>();
        instruction.add("(voice_" +_audio.getVoice() + ")");
        System.out.println(_audio.getVoice());
        instruction.add("(Parameter.set 'Duration_Stretch " + String.format("%1f", _audio.getSpeed()) + ")");
        instruction.add("(set! duffint_params '((start " + String.valueOf(_audio.getPitch()) + ") (end " + String.valueOf(_audio.getPitch()) + ")))");
        instruction.add("(Parameter.set 'Int_Method 'DuffInt)");
        instruction.add("(Parameter.set 'Int_Target_Method Int_Targets_Default)");
        instruction.add("(SayText \"" + _texts +"\")");
        try {
            Path file = Paths.get(".Audio_Directory/speech.scm");
            Files.write(file, instruction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
