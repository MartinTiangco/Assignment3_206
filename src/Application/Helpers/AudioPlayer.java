package Application.Helpers;

import Application.Controllers.Add_Audio_ScreenController;
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
    private String _texts = "";
    private String _audioFileName = null;
    private String _voice = "voice_kal_diphone";

    public AudioPlayer(Add_Audio_ScreenController controller) {
        _controller = controller;
    }

    @Override
    protected Long call() throws Exception {
        if (_audioFileName != null) {
            playAudio();
            _audioFileName = null;
        }
        else if (_texts != "") {
            playText();
            _texts = "";
        }
        else {
            throw new IllegalArgumentException();
        }
        return null;
    }

    public void playText() {
        List<String> instruction = new ArrayList<>();
        instruction.add("(" +_voice + ")");
        instruction.add("(SayText \"" + _texts +"\")");
        try {
            Path file = Paths.get(".Audio_Directory/speech.scm");
            Files.write(file, instruction);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProcessBuilder builder = new ProcessBuilder("bash", "-c", "festival -b .Audio_Directory/speech.scm");
        Process process;
        try {
            process = builder.start();

            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            int exitStatus = process.waitFor();
            System.out.println(exitStatus);
            if (exitStatus == 0) {
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                String errorline = "";
                String line;
                while ((line = stderr.readLine()) != null) {
                    errorline = errorline + "\n\t" + line;
                }
                error.setTitle("Error ecountered");
                error.setContentText(errorline);
                error.show();

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }



    }

    public void playAudio() {

        File fileUrl = new File("../../../.Audio_Directory/" + _audioFileName + ".mp4");
        Media video = new Media(fileUrl.toURI().toString());
        MediaPlayer player = new MediaPlayer(video);
        player.setAutoPlay(true);

        _controller.getMediaView().setMediaPlayer(player);
    }

    public void setTexts(List<String> texts) {
        for (int i = 0; i < texts.size(); i++) {
            _texts = _texts + texts.get(i);
        }
    }

    public void setAudioFileName(String audioFileName) {
        _audioFileName = audioFileName;
    }

    public void setVoice(String voice) {
        if (voice == "Martin Tiangco") {
            _voice = "voice_kal_diphone";
        }
        if (voice == "A") {
            _voice = "voice_kal_diphone";
        }
        if (voice == "B") {
            _voice = "voice_kal_diphone";
        }
        else {
            _voice = "voice_kal_diphone";
        }
    }
}
