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
import java.util.List;

public class AudioPlayer extends Task<Long> {

    private Add_Audio_ScreenController _controller;
    private String _texts = null;
    private String _audioFileName = null;

    public AudioPlayer(List<String> texts, Add_Audio_ScreenController controller) {
        for (int i = 0; i < texts.size(); i++) {
            _texts = _texts + texts.get(i);
        }
        _controller = controller;
    }

    public AudioPlayer(String audioFileName, Add_Audio_ScreenController controller) {
        _audioFileName = audioFileName;
        _controller = controller;
    }

    @Override
    protected Long call() throws Exception {
        if (_audioFileName != null) {
            playAudio(_audioFileName);
        }
        else if (_texts != null) {
            playText(_texts);
        }
        else {
            throw new IllegalArgumentException();
        }

        return null;
    }

    public void playText(List<String> texts) {
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", "$echo " + _texts + " | festival --tts");
        Process process;
        try {
            process = builder.start();

            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            int exitStatus = process.waitFor();
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

    public void playAudio(String audioFileName) {

        File fileUrl = new File("../../../.Audio_Directory/" + audioFileName + ".mp4");
        Media video = new Media(fileUrl.toURI().toString());
        MediaPlayer player = new MediaPlayer(video);
        player.setAutoPlay(true);

        _controller.getMediaView().setMediaPlayer(player);
    }
}
