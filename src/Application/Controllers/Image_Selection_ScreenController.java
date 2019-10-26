package Application.Controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Application.Helpers.ImageGenerator;
import Application.Helpers.ImageViewer;
import Application.Helpers.Picture;
import Application.Helpers.VideoGenerator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * The controller for the 'Image Selection Screen', where the user selects what images to include
 * in the slideshow for the creation.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Image_Selection_ScreenController extends Controller {

	private static final String IMAGE_DIR = ".Image_Directory" + System.getProperty("file.separator");

	@FXML private Button _backButton;
	@FXML private Button _createButton;
	@FXML private Button _helpButton;
	@FXML private CheckBox _selectAll;
	@FXML private ImageView _imageView = new ImageView();
	@FXML private ListView<ImageView> _selectedImages;
	@FXML private ListView<Picture> _listOfImages;
	@FXML private StackPane _helpImagePane;
	@FXML private TextField _nameInput;
	@FXML private Label _imageScreenTitle;

	private Add_Audio_ScreenController _controller;
	private ExecutorService _executor = Executors.newSingleThreadExecutor();
	private String _term;

	public void initialize() {
		// initializes the help image to be invisible
        _helpImagePane.setVisible(false);
        
		// handles the checkboxes next to the images
		_listOfImages.setCellFactory(CheckBoxListCell.forListView(image -> {
			image.onProperty().addListener((obs, wasOn, isNowOn) -> {
				image.setSelected(isNowOn);
				updateSelectedImages();
				if (!isNowOn) {
					_selectAll.setSelected(false);
				}
			});
			return image.onProperty();
		}));
	}

	public void handleBack() {
		((Stage)(((Background_Music_ScreenController)this.getParentController()).getNextButton().getScene().getWindow())).show();
		((Background_Music_ScreenController)this.getParentController()).getNextButton().setDisable(false);
		((Stage)(_backButton.getScene().getWindow())).close();
	}
	
	public void showHelp() {
        _helpImagePane.setVisible(true);
	}
	
	public void hideHelp() {
        _helpImagePane.setVisible(false);
	}

	public void handleCreate() {
		if (!isNameValid()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please enter a valid name for the creation.");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
			return;
		}
		// disallows user to click Create again
		_createButton.setDisable(false);

		// creates the creation
		VideoGenerator videoGen = new VideoGenerator(_term, this);
		videoGen.setCreationName(_nameInput.getText());
		int numPics = 0;
		for (ImageView imageView : _selectedImages.getItems()) {
			videoGen.addImage(IMAGE_DIR + ((Picture)imageView.getImage()).getFileName());
			numPics++;
		}
		if (numPics == 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please select at least one image.");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
			return;
		}
		videoGen.setNumPics(numPics);
		_executor.submit(videoGen);
	}

	public void listImages() {
		_listOfImages.getItems().removeAll(_listOfImages.getItems());
		File dir = new File(".Image_Directory");
		File[] listOfFiles = dir.listFiles();

		if (listOfFiles != null) {
			for (int i = 0; i < listOfFiles.length; i++) {
				Picture image = new Picture("file:" + listOfFiles[i].getAbsolutePath(),_term + " " + (i+1), false, listOfFiles[i].getName());
				_listOfImages.getItems().add(image);
			}
		}

		/**
		 * displays the images onto the 'Image Selection Screen'.
		 */
		_listOfImages.getSelectionModel().select(0);
		selectImage();
		_selectAll.setVisible(true);
		_imageScreenTitle.setText("Images of " + _term);
		_imageScreenTitle.setFont(Font.font(30));
	}

	public void selectImage() {
		ImageViewer imageViewer = new ImageViewer(this);
		_executor.submit(imageViewer);
	}

	public void selectAll() {
		if (_selectAll.isSelected()) {
			for (Picture image : _listOfImages.getItems()) {
				image.setSelected(true);
				image.setOn(true);
			}
		}
		else {
			for (Picture image : _listOfImages.getItems()) {
				image.setSelected(false);
				image.setOn(false);
			}
		}
		updateSelectedImages();
	}

	public void viewImage(){
		if (_listOfImages.getSelectionModel().getSelectedItem() != null) {
			_imageView.setImage(_listOfImages.getSelectionModel().getSelectedItem());
		}
	}

	public void updateSelectedImages() {
		for (Picture image : _listOfImages.getItems()) {
			if (image.getSelected() && !_selectedImages.getItems().contains(image.getImageView())) {
				_selectedImages.getItems().add(image.getImageView());
			}
			else if(!image.getSelected() && _selectedImages.getItems().contains(image.getImageView())){
				_selectedImages.getItems().remove(image.getImageView());
			}

		}
	}


	private boolean isNameValid() {
		// Disallows input of spaces or an empty string
		if (_nameInput.getText().trim().isEmpty()) {
			return false;
		}

		//prevents any special characters apart from "-", "_" and (space)
		if (_nameInput.getText().matches("[a-zA-Z0-9 ]*")) {
			return true;
		} else {
			return false;
		}
	}

	public Button getCreateButton() {
		return _createButton;
	}

	public ListView<Picture> getListOfImages() {
		return _listOfImages;
	}

	public CheckBox getSelectAll() {
		return _selectAll;
	}

	public void setTerm(String term) {
		_term = term;
	}
}

