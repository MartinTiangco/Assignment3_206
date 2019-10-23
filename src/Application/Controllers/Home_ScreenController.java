package Application.Controllers;

import Application.Helpers.AlertMessage;
import Application.Helpers.Cleaner;
import Application.Helpers.Creation;
import Application.Helpers.MediaBar;
import Application.Helpers.Quiz;
import Application.Helpers.UpdateHelper;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controller for the Main Menu / Home Screen
 */
public class Home_ScreenController extends Controller implements Initializable {

	// The elements of the application
    @FXML private Button _playButton;
    @FXML private Button _addButton;
    @FXML private Button _deleteButton;
    @FXML private Button _quizButton;
    @FXML private Button _settingsButton;
    @FXML private Label _progressMsg;
    @FXML private ProgressIndicator _progressIndicator;
    @FXML private Tab _creationTab;
    @FXML private TableColumn _nameColumn;
    @FXML private TableColumn _termSearchedColumn;
    @FXML private TableColumn _dateModifiedColumn;
    @FXML private TableColumn _videoLengthColumn;
    @FXML private TableView _creationTable;
    @FXML private TabPane _videoTabs;

    private UpdateHelper _updateHelper;
    private ArrayList<Creation> _creations = new ArrayList<Creation>();   // DONT NEED THIS
    private ExecutorService _executor = Executors.newSingleThreadExecutor();
    private List<MediaPlayer> _listOfMediaPlayer = new ArrayList<>();

    /**
     * Initializes the TableView and updates the table to have any existing creations
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	// allows for multiple selection
        _creationTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        _nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        _termSearchedColumn.setCellValueFactory(new PropertyValueFactory<>("termSearched"));
        _dateModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("dateModified"));
        _videoLengthColumn.setCellValueFactory(new PropertyValueFactory<>("videoLength"));
        Update();
    }

    /**
     * Plays a selected creation
     */
    public void handlePlay() {
        List<Creation> listOfCreations = _creationTable.getSelectionModel().getSelectedItems();
        if (listOfCreations != null) {
            for (Creation creation : listOfCreations) {
                Tab tab = new Tab();
                tab.setClosable(true);
                tab.setText(creation.getName());
                
                File fileUrl = new File("Creation_Directory/" + creation.getFileName());
                Media video = new Media(fileUrl.toURI().toString());
                MediaPlayer player = new MediaPlayer(video);
                player.setAutoPlay(true);
                tab.setOnClosed(new EventHandler<Event>()
                {
                    @Override
                    public void handle(Event arg0)
                    {
                        Update();
                        player.dispose();
                    }
                });

                MediaView mediaView = new MediaView();
                mediaView.setMediaPlayer(player);
                mediaView.setFitHeight(400);
                mediaView.setFitWidth(500);
                MediaBar bar = new MediaBar(player);
                VBox vbox = new VBox(mediaView, bar);
                vbox.setPadding(new Insets(25, 50, 25, 50));
                vbox.setSpacing(25);
                tab.setContent(new AnchorPane(vbox));
                _videoTabs.getTabs().add(tab);
                _videoTabs.getSelectionModel().select(_videoTabs.getTabs().size() - 1);

                for (MediaPlayer pause : _listOfMediaPlayer) {
                    pause.pause();
                }
                _listOfMediaPlayer.add(player);
            }
        }
        Update();
    }

    /**
     * Opens the 'Add Audio Screen' when the Add button is clicked
     */
    public void handleAdd() {
        loadScreen("Add Audio", "/Application/fxml/Add_Audio_Screen.fxml", "/Application/css/Add_Audio_Screen.css");
        Update();
    }

    /**
     * Deletes the selected creation from the TableView
     */
    public void handleDelete() {
    	Cleaner cleaner = new Cleaner();
        List<Creation> listOfCreations = _creationTable.getSelectionModel().getSelectedItems();
        if (listOfCreations != null) {
            List<Tab> listOfTabToBeRemoved = new ArrayList<>();
            for (Creation creation : listOfCreations) {
            	System.out.println("Creation_Directory/" + creation.getFolderName());
                File filePath = new File("Creation_Directory/" + creation.getFolderName());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to delete \"" + creation.getName() + "\"?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        for (Tab tab : _videoTabs.getTabs()) {
                            if (tab.getText().equals(creation.getName())) {
                            	// when deleting a playing video, stops it from playing and removes the tab
                                for (MediaPlayer player : _listOfMediaPlayer) {
                                    if (player.getMedia().getSource().equals("file:" + filePath.getAbsolutePath())) {
                                        player.dispose();
                                    }
                                }
                                listOfTabToBeRemoved.add(tab);
                            }
                        }
                        cleaner.cleanCreation(creation.getFolderName());
                        filePath.delete();
                        _videoTabs.getTabs().removeAll(listOfTabToBeRemoved);
                    }
                });
            }
        }
        Update();
    }
    
    /**
     * Opens the quiz page when the quiz button is clicked
     */
    public void handleQuiz() {
    	if (_creationTable.getItems().size() == 0) {
    		AlertMessage alert = new AlertMessage("no_creations_found");
    		Platform.runLater(alert);
    	} else {
        	loadScreen("Quiz", "/Application/fxml/Quiz_Start.fxml", "/Application/css/Quiz_Start.css");
    	}
    }

    /**
     * Opens the settings page when the settings button is clicked
     */
    public void handleSettings() {
        Controller controller = loadScreen("Settings", "/Application/fxml/Settings_Screen.fxml", "");
        ((Settings_ScreenController)controller).selectDefault();
    }

    public ArrayList<Creation> getCreations() {
        return _creations;
    }

    public TableView getCreationTable() {
        return _creationTable;
    }

    /**
     * Updates the TableView Creation items
     */
    public void Update() {
        _updateHelper = new UpdateHelper(this);
        _executor.submit(_updateHelper);
        disable();
    }

    /**
     * Disables the play and delete button when the user is playing a video and is not on the Creation Tab
     */
    public void disable() {
        if (_videoTabs.getSelectionModel().getSelectedItem() != _creationTab) {
            _deleteButton.setDisable(true);
            _playButton.setDisable(true);
        }
        else {
            _deleteButton.setDisable(false);
            _playButton.setDisable(false);
        }
    }
    
    public Label getProgressMsg() {
    	return _progressMsg;
    }
    
    public ProgressIndicator getProgressIndicator() {
    	return _progressIndicator;
    }
}
