import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.videoio.VideoCapture;

class CameraPanel extends JPanel {
	private static final long serialVersionUID = 8197194390711639914L;
	private JLabel cameraScreen = new JLabel("Capture not started");
	VideoCapture capture = new VideoCapture(0);
	GUI parentGUI; // REMOVE?
	Boolean captureMode = false;

	CameraPanel(GUI parentGUI) {
		super();
		this.parentGUI = parentGUI;
		add(cameraScreen);

	}

	void updateCameraScreen(Icon icon) {
		cameraScreen.setText(null);
		cameraScreen.setIcon(icon);
	}

	CameraWorker createCameraWorker() {
		return new CameraWorker(this);
	}

	void startCapture() {
		captureMode = true;
	}

	void stopCapture() {
		captureMode = false;
	}

	public void setCaptureMode(Boolean captureMode) {
		this.captureMode = captureMode;
	}

}
