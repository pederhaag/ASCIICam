package main;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.Videoio;

/**
 * 
 * A {@code SwingWorker} extension for continuously reading images from the
 * capturesource of the {@code CameraPanel}. Read images will be stored in the
 * {@code CameraPanel} and the corresponding {@code GUI}-object. The latter for
 * the {@link AsciiWorker} to later grab.
 *
 */
class CameraWorker extends SwingWorker<Void, Void> {
	private final CameraPanel cameraPanel;
	private final GUI GUI;

	CameraWorker(CameraPanel cameraPanel) {
		this.cameraPanel = cameraPanel;
		this.GUI = cameraPanel.parentGUI;
	}

	@Override
	protected Void doInBackground() throws Exception {
		// Signal native resolution of camera
		double nativeHeight = this.cameraPanel.capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
		double nativeWidth = this.cameraPanel.capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
		signalNativeResolution((int) nativeWidth, (int) nativeHeight);

		Mat imageMat = new Mat();
		byte[] imageData;

		while (cameraPanel.captureMode) {
			// Read image to matrix
			cameraPanel.capture.read(imageMat);
			// convert to byte
			final MatOfByte buf = new MatOfByte();
			Imgcodecs.imencode(".jpg", imageMat, buf);
			imageData = buf.toArray();

			// Store icon
			cameraPanel.updateCameraScreen(new ImageIcon(imageData));

			// Give to GUI-storage
			GUI.insertImgToConvert(imageMat);
		}

		return null;
	}

	private void signalNativeResolution(int width, int height) {
		this.firePropertyChange("nativeResolution", null, new Dimension(width, height));
	}

}