package main;


import javax.swing.SwingWorker;

import org.opencv.core.Mat;

/**
 * 
 * {@code AsciiWorker} is a simple {@code SwingWorker} extension which is used
 * for grabbing images from GUI (previously stored by {@link CameraWorker}) and
 * convert it to ascii-art and publish is to the {@link AsciiPanel} for viewing.
 *
 */
class AsciiWorker extends SwingWorker<Void, Void> {
	private final AsciiPanel asciiPanel;
	private final GUI GUI;

	AsciiWorker(AsciiPanel asciiPanel) {
		this.asciiPanel = asciiPanel;
		this.GUI = asciiPanel.parentGUI;
	}

	@Override
	protected Void doInBackground() throws Exception {
		Mat img;
		Mat greyImage;
		String[] asciiImg;
		while (asciiPanel.captureMode) {
			GUI.waitForImage();
			img = GUI.getImgToConvert();
			greyImage = ImageProcessing.grayify(img);
			asciiImg = ImageProcessing.toAscii(greyImage, asciiPanel.getCurrentMapping());
			asciiPanel.updateImage(asciiImg);
		}
		return null;
	}

}