package Application.Helpers;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 * This class enables the user to set the volume or use the slider to play the creation at any point
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class MediaBar extends HBox {

    private final static String PLAY_SYMBOL = "▶";
    private final static String PAUSE_SYMBOL = "||";
    private final static String MUTE_SYMBOL = "\uD83D\uDD07";
    private final static String LOW_VOLUME_SYMBOL = "\uD83D\uDD08";
    private final static String MEDIUM_VOLUME_SYMBOL = "\uD83D\uDD09";
    private final static String HIGH_VOLUME_SYMBOL = "\uD83D\uDD0A";

    // introducing Sliders
    private Slider _time = new Slider(); // Slider for time
    private Slider _vol = new Slider(); // Slider for volume
    private Button _playButton = new Button("||"); // For pausing the player
    private Label _volume = new Label(HIGH_VOLUME_SYMBOL);
    private MediaPlayer _player;

    /**
     * Constructor for MediaBar. Adds all the elements and functionality needed.
     * @param play
     */
    public MediaBar(MediaPlayer play) {
        _player = play;

        setAlignment(Pos.CENTER); // setting the HBox to center
        setPadding(new Insets(5, 10, 5, 10));
        // Setting the preference for volume bar
        _vol.setPrefWidth(70);
        _vol.setMinWidth(30);
        _vol.setValue(100);
        HBox.setHgrow(_time, Priority.ALWAYS);
        _playButton.setPrefWidth(30);

        // Adding the components to the bottom
        HBox hBox = new HBox(_playButton, _time);
        hBox.setPrefSize(hBox.getWidth(), hBox.getHeight());
        getChildren().add(hBox); // Play button, time slider
        getChildren().add(new HBox(_volume, _vol)); // volume slider

        // Adding Functionality to play the media player
        _playButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Status status = _player.getStatus();
                
                if (status == status.PLAYING) {
                    // If the status is Video playing
                    if (_player.getCurrentTime().greaterThanOrEqualTo(_player.getTotalDuration())) {

                        // If the player is at the end of video
                        _player.seek(_player.getStartTime()); // Restart the video
                        _player.play();
                    } else {
                        // Pausing the player
                        _player.pause();

                        _playButton.setText(PLAY_SYMBOL);
                    }
                } if (status == Status.HALTED || status == Status.STOPPED || status == Status.PAUSED) {
                	// If the video is stopped, halted or paused 
                    _player.play(); // Start the video
                    _playButton.setText("||");
                }
            }
        });

        // Providing functionality to time slider
        _player.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                updatesValues();
            }
        });

        // In order to jump to the certain part of video
        _time.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (_time.isPressed()) { 
                	// It would set the time as specified by user by pressing
                    _player.seek(_player.getMedia().getDuration().multiply(_time.getValue() / 100));
                }
            }
        });

        // providing functionality to volume slider
        _vol.valueProperty().addListener(ov -> {
            if (_vol.isPressed()) {
                _player.setVolume(_vol.getValue() / 100);
                if (_vol.getValue() == 0){
                    _volume.setText(MUTE_SYMBOL);
                }
                else if (_vol.getValue() < 33) {
                    _volume.setText(LOW_VOLUME_SYMBOL);
                }
                else if (_vol.getValue() < 66) {
                    _volume.setText(MEDIUM_VOLUME_SYMBOL);
                }
                else {
                    _volume.setText(HIGH_VOLUME_SYMBOL);
                }
            }
        });
    }

    /**
     * Move the slider while playing the video
     */
    private void updatesValues() {
        Platform.runLater(() -> {
            // Updating to the new time value
            // This will move the slider while running your video
            _time.setValue(_player.getCurrentTime().toMillis()/_player.getTotalDuration().toMillis() * 100);
        });
    }
}
