package Application.Helpers;

import java.io.File;

import Application.Controllers.Background_Music_ScreenController;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The class for playing a background music track in the 'Background Music Screen'
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class TrackPlayer extends Task<Long> {
	private final String MUSIC_DIR = ".Music_Directory" + System.getProperty("file.separator");
	
    private Background_Music_ScreenController _controller;
    private Process _process;
    private Track _track;

    public TrackPlayer(Track track, Background_Music_ScreenController controller) {
        _track = track;
        _controller = controller;
    }

    @Override
    protected Long call() throws Exception {
        if (_track.getTrackName() != null) {
            playTrack();
        }
        return null;
    }

    /**
     * Plays the background music
     */
    public void playTrack() {
        File fileUrl = new File(MUSIC_DIR + _track.getTrackName());
        Media audio = new Media(fileUrl.toURI().toString());
        MediaPlayer player = new MediaPlayer(audio);
        player.setAutoPlay(true);
        _controller.getMediaView().setMediaPlayer(player);
    }

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
