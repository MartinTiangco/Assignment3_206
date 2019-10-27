package Application.Helpers;

import Application.Controllers.Image_Selection_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * 
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class ImageViewer extends Task<Long> {

    private Image_Selection_ScreenController _controller;

    public ImageViewer(Image_Selection_ScreenController controller) {
        _controller = controller;
    }
    @Override
    protected Long call() {
        Runnable viewImage = () -> _controller.viewImage();
        Platform.runLater(viewImage);

        return null;
    }
}
