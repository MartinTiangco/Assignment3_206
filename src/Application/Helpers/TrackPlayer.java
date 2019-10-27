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
    private Track _track;

    public TrackPlayer(Track track, Background_Music_ScreenController controller) {
        _track = track;
        _controller = controller;
    }

    @Override
    protected Long call() throws Exception {
        if (_track.getTrackFile() != null) {
            playTrack(_track.getTrackFile());
        }
        return null;
    }

    /**
     * Plays the background music
     */
    public void playTrack(String trackFile) {
        File fileUrl = new File(MUSIC_DIR + trackFile);
        Media audio = new Media(fileUrl.toURI().toString());
        MediaPlayer player = new MediaPlayer(audio);
        player.setAutoPlay(true);
        _controller.getMediaView().setMediaPlayer(player);
    }
}
