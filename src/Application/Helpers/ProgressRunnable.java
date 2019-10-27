package Application.Helpers;

import Application.Controllers.Add_Audio_ScreenController;
import Application.Controllers.Controller;

/**
 * The Runnable class for showing the progress indicator
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
class ProgressRunnable implements Runnable {
	private Controller _controller;
	
	public ProgressRunnable(Controller controller) {
		_controller = controller;
	}
	
	@Override
	public void run() {
		// this stops the loading animation
		((Add_Audio_ScreenController)_controller).getEntireScreenPane().setDisable(false);
		((Add_Audio_ScreenController)_controller).getProgressIndicator().setVisible(false);
		((Add_Audio_ScreenController)_controller).getCancelButton().setVisible(false);
	}
}
