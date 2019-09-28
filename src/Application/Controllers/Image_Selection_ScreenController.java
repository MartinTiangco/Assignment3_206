package Application.Controllers;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Application.Helpers.ImageGenerator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Image_Selection_ScreenController extends Controller {
	@FXML private Button _createButton;
	@FXML private TextField _input;
	@FXML private Label _invalidInput;
	@FXML private Button _generateButton;
	
	private Add_Audio_ScreenController _controller;
	private ExecutorService _executor = Executors.newSingleThreadExecutor();
	
	public void initialize() {
		_generateButton.setDisable(true);
		_input.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	// only allow digits to be typed
		        if (!newValue.matches("\\d*")) {
		            _input.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		        // only allow up to 2 digits to be typed
		        if (newValue.length() > 2) {
		        	_input.setText(newValue.substring(0,2));
		        }
		        // enables or disables button if number is a valid input
		        if (!_input.getText().isEmpty()) {
		        	if (Integer.parseInt(newValue) > 10) {
			        	_invalidInput.setVisible(true);
			        } else {
			        	_invalidInput.setVisible(false);
			        }
		        	
			        if (isValidNumber()) {
			        	_generateButton.setDisable(false);
			        } else {
			        	_generateButton.setDisable(true);
			        }
		        } else {
		        	// if textField is empty, disable the button.
		        	_generateButton.setDisable(true);
		        }
		    }
		});
	}
	
	public void handleGenerate() {
		if (!isValidNumber()) {
			return;
		}
		
		System.out.println("The number is valid");
		
		String term = ((Add_Audio_ScreenController)(this.getParentController())).getSearchInput();
		int numPics = Integer.parseInt(_input.getText());
		ImageGenerator imgGen = new ImageGenerator(term, numPics, this);
		_executor.submit(imgGen);
		
		// retrieve the length of the audio file
		
	}
	
	public void handleCreate() {
		System.out.println("You created");
	}

	public boolean isValidNumber() {
		if (Integer.parseInt(_input.getText()) >= 0 && Integer.parseInt(_input.getText()) < 11) {
			return true;
		}
		return false;
	}
}













//package test;
//
//import javafx.application.Application;
//import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//import javafx.beans.value.ObservableValue;
//import javafx.scene.Scene;
//import javafx.scene.control.ListView;
//import javafx.scene.control.cell.CheckBoxListCell;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//import javafx.util.Callback;
//
//public class Main extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        ListView<Item> listView = new ListView<>();
//        for (int i=1; i<=20; i++) {
//            Item item = new Item("Item "+i, false);
//
//            // observe item's on property and display message if it changes:
//            item.onProperty().addListener((obs, wasOn, isNowOn) -> {
//                System.out.println(item.getName() + " changed on state from "+wasOn+" to "+isNowOn);
//            });
//
//            listView.getItems().add(item);
//        }
//
//        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<Item, ObservableValue<Boolean>>() {
//            @Override
//            public ObservableValue<Boolean> call(Item item) {
//                return item.onProperty();
//            }
//        }));
//
//        BorderPane root = new BorderPane(listView);
//        Scene scene = new Scene(root, 250, 400);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static class Item {
//        private final StringProperty name = new SimpleStringProperty();
//        private final BooleanProperty on = new SimpleBooleanProperty();
//
//        public Item(String name, boolean on) {
//            setName(name);
//            setOn(on);
//        }
//
//        public final StringProperty nameProperty() {
//            return this.name;
//        }
//
//        public final String getName() {
//            return this.nameProperty().get();
//        }
//
//        public final void setName(final String name) {
//            this.nameProperty().set(name);
//        }
//
//        public final BooleanProperty onProperty() {
//            return this.on;
//        }
//
//        public final boolean isOn() {
//            return this.onProperty().get();
//        }
//
//        public final void setOn(final boolean on) {
//            this.onProperty().set(on);
//        }
//
//        @Override
//        public String toString() {
//            return getName();
//        }
//
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

