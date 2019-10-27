package Application.Controllers;

import Application.Helpers.ImageViewer;
import Application.Helpers.Picture;
import Application.Helpers.VideoGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	@FXML private CheckBox _selectAll;
	@FXML private ImageView _imageView = new ImageView();
	@FXML private ListView<ImageView> _selectedImages;
	@FXML private ListView<Picture> _listOfImages;
	@FXML private StackPane _helpImagePane;
	@FXML private TextField _nameInput;
	@FXML private Label _imageScreenTitle;
	@FXML private Label _selectedImagesLabel;

	private ExecutorService _executor = Executors.newSingleThreadExecutor();
	private String _term;

	public void initialize() {
		// adds the graphic icons to the labels
        _imageScreenTitle.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Application/assets/image.png"))));
        _selectedImagesLabel.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Application/assets/selected.png"))));

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

	/**
	 * Handles functionality to go back to selecting background music
	 */
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

	/**
	 * Create the creation and return to the Home Screen
	 */
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

	/**
	 * displays the images onto the 'Image Selection Screen'.
	 */
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

		// Selecting the first image automatically
		_listOfImages.getSelectionModel().select(0);
		selectImage();
		_selectAll.setVisible(true);
		_imageScreenTitle.setText("Images of \"" + _term + "\"");
	}

	/**
	 * Selecting a image to be included the final creation
	 */
	private void selectImage() {
		ImageViewer imageViewer = new ImageViewer(this);
		_executor.submit(imageViewer);
	}

	/**
	 * Selecting all images to be included the final creation
	 */
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

	/**
	 * Updating the list of selected images to be included int eh final creation
	 */
	private void updateSelectedImages() {
		for (Picture image : _listOfImages.getItems()) {
			if (image.getSelected() && !_selectedImages.getItems().contains(image.getImageView())) {
				_selectedImages.getItems().add(image.getImageView());
			}
			else if(!image.getSelected()){
				_selectedImages.getItems().remove(image.getImageView());
			}

		}
	}

	/**
	 * Check if the filename for the new creation is valid
	 * @return true if input name is valid otherwise false
	 */
	private boolean isNameValid() {
		// Disallows input of spaces or an empty string
		if (_nameInput.getText().trim().isEmpty()) {
			return false;
		}

		//prevents any special characters apart from "-", "_" and (space)
		return _nameInput.getText().matches("[a-zA-Z0-9 ]*");
	}

	public Button getCreateButton() {
		return _createButton;
	}

	public void setTerm(String term) {
		_term = term;
	}
}

