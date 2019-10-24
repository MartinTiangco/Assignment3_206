package Application.Helpers;

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
		_controller.getEntireScreenPane().setDisable(false);
		_controller.getProgressIndicator().setVisible(false);
	}
}
