import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class CameraPanel extends JPanel {
	private int panelWidth;
	private int panelHeight;
	private JLabel cameraScreen = new JLabel("Capture not started");
	private Boolean captureMode = false;
	VideoCapture capture = new VideoCapture(0);
	GUI parentGUI;

	CameraPanel() {
		this(null);
	}

	CameraPanel(GUI parentGUI) {
		super();
		this.parentGUI = parentGUI;
		setBorder(BorderFactory.createLineBorder(Color.blue));
		add(cameraScreen);

	}

	void updateCameraScreen(Icon icon) {
		// TODO: Locks?
		cameraScreen.setText(null);
		cameraScreen.setIcon(icon);
	}

//	void startCapture() {
//		if (!captureMode) {
//			captureMode = true;
//			cameraScreen.setText(null);
//			System.out.println("creating CameraTask");
//			CameraTask task = new CameraTask(this);
//			task.addPropertyChangeListener(new PropertyChangeListener() {
//				public void propertyChange(PropertyChangeEvent evt) {
//					if ("nativeResolution".equals(evt.getPropertyName())) {
//						Dimension nativeRes = (Dimension) evt.getNewValue();
////						System.out.println("Native res: " + nativeRes.width + "/" + nativeRes.height);
//						if (parentGUI != null) {
//							parentGUI.setCameraSize(nativeRes.width, nativeRes.height);
//						}
//
//					}
//				}
//			});
////			task.addPropertyChangeListener(new PropertyChangeListener() {
////				public void propertyChange(PropertyChangeEvent evt) {
////					if ("image".equals(evt.getPropertyName())) {
////						cameraScreen.setIcon((ImageIcon) evt.getNewValue());
////					}
////				}
////			});
//			task.execute();
//		}
//	}
//
//	void stopCapture() {
//		captureMode = false;
//	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CameraPanel cameraPanel = new CameraPanel();
//		CameraPanel cameraPanel = new CameraPanel(400, 300);
		f.add(cameraPanel);
		f.setLocationRelativeTo(null);

		f.pack();
		f.setVisible(true);

		try {
//			Thread.sleep(2000);
			System.out.println("+++++++++++++++START");
//			cameraPanel.startCapture();
//			Thread.sleep(4000);
//			System.out.println("------------------------------------END");
//			cameraPanel.stopCapture();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void setPanelSize(int width, int height) {
		panelWidth = width;
		panelHeight = height;
		Dimension size = new Dimension(panelWidth, panelHeight);
		this.setSize(size);
		this.setPreferredSize(size);
	}

	void setPanelSize(Dimension size) {
		setPanelSize(size.width, size.height);
	}

	Dimension getNativeResolution() {
		double nativeHeight = capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
		double nativeWidth = capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
		capture.release();
		return new Dimension((int) nativeWidth, (int) nativeHeight);
	}

	

}
