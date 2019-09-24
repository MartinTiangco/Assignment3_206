package Application;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

        System.out.println("You pressed add");
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
