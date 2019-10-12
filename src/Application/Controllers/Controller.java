package Application.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * This class is extended by all controllers and is used to track the controllers
 * even from another screen.
 *
 */
public class Controller {
	private Parent _entireScreenPane;
	private ProgressIndicator _progressIndicator;
    protected Controller _currentController;
    protected Controller _parentController;
    protected String _styleSheet = "";

    public void setCurrentController(Controller currentController){
        _currentController = currentController;
    }

    public void setParentController(Controller parentController){
        _parentController = parentController;
    }

    public Controller getCurrentController(){
        return _currentController;
    }

    public Controller getParentController(){
        return _parentController;
    }
    
    public Parent getEntireScreenPane() {
    	return _entireScreenPane;
    }
    
    public ProgressIndicator getProgressIndicator() {
    	return _progressIndicator;
    }

    public String getStyleSheet() {
        return _styleSheet;
    }

    public void setStyleSheet(String styleSheet) {
        this._styleSheet = styleSheet;

        try {
            // input the (modified) file content to the StringBuffer "input"
            String config = System.getProperty("user.dir") + System.getProperty("file.separator") + ".config";

            File c = new File(config);
            BufferedReader file = new BufferedReader(new FileReader(c));
            StringBuilder inputBuffer = new StringBuilder();
            String line;

            while ((line = file.readLine()) != null) {
                if (line.trim().startsWith("Stylesheet")) {
                    line = "Stylesheet = " + styleSheet;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(".config");
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }


    }

    public Controller loadScreen(String stageName, String fxmlFile, String cssFile) {
        Stage stage = new Stage();
        Controller controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            controller = loader.getController();
            controller.setCurrentController(controller);
            controller.setParentController(_currentController);
            controller.setStyleSheet(_styleSheet);
            stage.setTitle("VARpedia - " + stageName);
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(this.getClass().getResource(cssFile).toExternalForm(), this.getClass().getResource(_styleSheet).toExternalForm());
            stage.setScene(scene);
            if (stageName.equals("Home")) {
                stage.setOnCloseRequest(t -> {
                    Platform.exit();
                    System.exit(0);
                });
            }
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return controller;
    }
}
