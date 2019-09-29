package Application.Controllers;

/**
 * This class is extended by all controllers and is used to track the controllers
 * even from another screen.
 *
 */
public class Controller {

    protected Controller _currentController;
    protected Controller _parentController;

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
}
