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

	@FXML private AnchorPane _entireScreenPane;
	@FXML private Button _backButton;
	@FXML private Button _createButton;
	@FXML private Button _helpButton;
	@FXML private Button _generateButton;
	@FXML private CheckBox _selectAll;
	@FXML private ImageView _imageView = new ImageView();
	@FXML private Label _invalidInput;
	@FXML private ListView<ImageView> _selectedImages;
	@FXML private ListView<Picture> _listOfImages;
	@FXML private ProgressIndicator _progressIndicator;
	@FXML private TextField _nameInput;
	@FXML private TextField _input;

	private Add_Audio_ScreenController _controller;
	private ExecutorService _executor = Executors.newSingleThreadExecutor();
	private String _term;

	public void initialize() {
		_generateButton.setDisable(true);

		_input.requestFocus();

		_input.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
								String newValue) {
				// only allow digits to be typed
				if (!newValue.matches("\\d+")) {
					_input.setText(newValue.replaceAll("[^\\d]", ""));
				}
				// only allow up to 2 digits to be typed
				if (newValue.length() > 2) {
					_input.setText(newValue.substring(0,2));
				}
				// enables or disables button if number is a valid input
				if (!_input.getText().isEmpty()) {
					if (_input.getText().matches("\\d+")) {
						if (isValidNumber()) {
							_invalidInput.setVisible(false);
							_generateButton.setDisable(false);
						} else {
							_invalidInput.setVisible(true);
							_generateButton.setDisable(true);
						}
					}
				} else {
					// if textField is empty, disable the button.
					_generateButton.setDisable(true);
				}
			}
		});

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
	
	public void handleHelp() {
		
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

	// this is needed for the check box functionality

	public void handleGenerate() {
		if (!isValidNumber()) {
			return;
		}

		// progress indicator
		_entireScreenPane.setDisable(true);
		_progressIndicator.setProgress(-1);
		_progressIndicator.setVisible(true);
		
		_term = ((Add_Audio_ScreenController)(this.getParentController().getParentController())).getSearchInput();
		int numPics = Integer.parseInt(_input.getText());

		// retrieves images from Flickr
		ImageGenerator imgGen = new ImageGenerator(_term, numPics, this);
		_executor.submit(imgGen);
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

	public boolean isValidNumber() {
		if (Integer.parseInt(_input.getText()) > 0 && Integer.parseInt(_input.getText()) < 11) {
			return true;
		}
		return false;
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

	public AnchorPane getEntireScreenPane() {
		return _entireScreenPane;
	}

	public ProgressIndicator getProgressIndicator() {
		return _progressIndicator;
	}

	public ListView<Picture> getListOfImages() {
		return _listOfImages;
	}

	public CheckBox getSelectAll() {
		return _selectAll;
	}
}

