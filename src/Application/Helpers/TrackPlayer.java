package Application.Helpers;

import java.io.File;

import Application.Controllers.Background_Music_ScreenController;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class TrackPlayer extends Task<Long> {

	private final String MUSIC_DIR = ".Music_Directory" + System.getProperty("file.separator");
    private Background_Music_ScreenController _controller;
    private Track _track;
    private Process _process;

    public TrackPlayer(Track track, Background_Music_ScreenController controller) {
        _track = track;
        _controller = controller;
    }

    @Override
    protected Long call() throws Exception {
    	// previews either the content text from Wikipedia or the saved audio
        if (_track.getTrackName() != null) {
            playAudio();
        }
        return null;
    }

    public void playAudio() {
        File fileUrl = new File(MUSIC_DIR + _track.getTrackName());
        Media audio = new Media(fileUrl.toURI().toString());
        MediaPlayer player = new MediaPlayer(audio);
        player.setAutoPlay(true);
        _controller.getMediaView().setMediaPlayer(player);
    }

    public void StartProcess(ProcessBuilder builder) {
        try {
            _process = builder.start();
            int exitStatus = _process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Process getProcess() {
        return _process;
    }
}
