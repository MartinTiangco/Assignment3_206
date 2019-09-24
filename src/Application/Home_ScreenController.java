package Application;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Home_ScreenController extends Controller implements Initializable {


    @FXML private Button _playButton;
    @FXML private Button _addButton;
    @FXML private Button _deleteButton;
    @FXML private Button _settingsButton;
    @FXML private TableView _creationTable;
    @FXML private TableColumn _nameColumn;
    @FXML private TableColumn _termSearchedColumn;
    @FXML private TableColumn _dateModifiedColumn;
    @FXML private TableColumn _videoLengthColumn;
    private UpdateHelper _updateHelper;
    private ArrayList<Creation> _creations = new ArrayList<Creation>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        _termSearchedColumn.setCellValueFactory(new PropertyValueFactory<>("termSearched"));
        _dateModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("dateModified"));
        _videoLengthColumn.setCellValueFactory(new PropertyValueFactory<>("videoLength"));
        Update();
    }

    @FXML
    public void handlePlay() {
        System.out.println("You pressed play");
    }

    @FXML
    public void handleAdd() {
    	Stage addAudioStage = new Stage();
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("Add_Audio_Screen.fxml"));
            Parent root = loader.load();
            Add_Audio_ScreenController Add_Audio_ScreenController = loader.getController();
           	Add_Audio_ScreenController.setCurrentController(Add_Audio_ScreenController);
            Scene scene = new Scene(root, 858, 692);
            
            //once we have the css file for Add_Audio_Screen
            //scene.getStylesheets().addAll(this.getClass().getResource("css/Add_Audio_Screen.css").toExternalForm());
            
            addAudioStage.setScene(scene);
            addAudioStage.show();	
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        Update();
    }

    @FXML
    public void handleDelete() {

        System.out.println("You pressed delete");
        Update();
    }

    @FXML
    public void handleSettings() {
        System.out.println("You pressed settings");
    }

    public ArrayList<Creation> getCreations() {
        return _creations;
    }

    public TableView getCreationTable(){
        return _creationTable;
    }

    public void Update() {
        _updateHelper = new UpdateHelper(this);
        _updateHelper.run();
    }
}
