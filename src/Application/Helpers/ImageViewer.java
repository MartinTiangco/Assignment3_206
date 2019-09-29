package Application.Helpers;

import Application.Controllers.Image_Selection_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class ImageViewer extends Task<Long> {

    private Image_Selection_ScreenController _controller;

    public ImageViewer(Image_Selection_ScreenController controller) {
        _controller = controller;
    }
    @Override
    protected Long call() throws Exception {
        Runnable viewImage = new Runnable() {
            @Override
            public void run() {
                _controller.viewImage();
            }
        };
        Platform.runLater(viewImage);

        return null;
    }
}
