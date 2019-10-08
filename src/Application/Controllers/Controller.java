package Application.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is extended by all controllers and is used to track the controllers
 * even from another screen.
 *
 */
public class Controller {

    protected Controller _currentController;
    protected Controller _parentController;
    protected String _style = "/Application/css/bootstrap3.css";

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

    public void loadScreen(String stageName, String fxmlFile, String cssFile) {
        Stage stage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.setCurrentController(controller);
            controller.setParentController(_currentController);
            stage.setTitle("VARpedia - " + stageName);
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(this.getClass().getResource(cssFile).toExternalForm(), this.getClass().getResource(_style).toExternalForm());
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
    }
}
