package Application.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * This class is extended by all controllers and is used to track the controllers
 * even from another screen.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Controller {
    private Controller _currentController;
    private Controller _parentController;
    private String _styleSheet = "";

    public void setCurrentController(Controller currentController){
        _currentController = currentController;
    }

    private void setParentController(Controller parentController){
        _parentController = parentController;
    }

    public Controller getParentController(){
        return _parentController;
    }

    public String getStyleSheet() {
        return _styleSheet;
    }

    /**
     * Sets the style sheet to the theme you have chosen in the Settings Screen.
     * @param styleSheet
     */
    public void setStyleSheet(String styleSheet) {
        _styleSheet = styleSheet;

        try {
        	// set the stylesheet from the config file
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

        } catch (Exception ignored) {
        }
    }

    /**
     *Shared method that loads the FXML screen
     * @param stageName
     * @param fxmlFile
     * @param cssFile
     */
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
            Font.loadFont(getClass().getResource("/Application/css/Roboto-Medium.ttf").toExternalForm(), 24);
            scene.getStylesheets().addAll(this.getClass().getResource(cssFile).toExternalForm(), this.getClass().getResource("/Application/css/Global.css").toExternalForm(), this.getClass().getResource(_styleSheet).toExternalForm());
            stage.setScene(scene);
            
            // Closes the program if the Home Screen is closed (by pressing the top right 'X')
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
