package Application.Helpers;

import Application.Controllers.Controller;

class ProgressRunnable implements Runnable {
	private Controller _controller;
	
	public ProgressRunnable(Controller controller) {
		_controller = controller;
	}
	
	@Override
	public void run() {
		_controller.getEntireScreenPane().setDisable(false);
		_controller.getProgressIndicator().setVisible(false);
	}
}
