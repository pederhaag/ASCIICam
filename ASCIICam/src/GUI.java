import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.Videoio;

public class GUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Some sizes
	private int frameWidth;
	private int frameHeight;
	private int mainWidth;
	private int mainHeight;
	private int camWidth;
	private int camHeight;
	private int asciiWidth;
	private int asciiHeight;
	private int ctrlWidth;
	private static final int ctrlHeight = 50;

	AsciiPanel asciiPanel = new AsciiPanel();
	private CameraPanel cameraPanel = new CameraPanel(this);
	private JPanel masterPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private ControlPanel ctrlPanel = new ControlPanel(this);
//	private JPanel ctrlPanel = new JPanel();
//	private ExitButton exitBtn = new ExitButton();
//	private StartCaptureBtn captureBtn = new StartCaptureBtn();
	private Mat imageToConvert = null;
	Boolean captureMode = false;
	private static final String frameTitle = "ASCII Camera";

	GUI() {
		super(frameTitle);

		this.frameWidth = 600;
		this.frameHeight = 350;
		buildGuiElements();
		setInitialSizes();
	}

	private void buildGuiElements() {
		// Master panel
		masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.PAGE_AXIS));
		masterPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(masterPanel);

		// * Main panel
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		masterPanel.add(mainPanel);

		// ** Camera panel
		mainPanel.add(cameraPanel);

		// ** ASCII Panel
		mainPanel.add(asciiPanel);

		// * Control Panel
		masterPanel.add(ctrlPanel);
	}

	private void setInitialSizes() {
		mainHeight = frameHeight - ctrlHeight;
		mainWidth = frameWidth;

		camHeight = mainHeight;
		camWidth = mainWidth / 2;

		asciiHeight = mainHeight;
		asciiWidth = mainWidth - camWidth;

		ctrlWidth = frameWidth;

		resizeComponents();
		setLocationRelativeTo(null);
	}

	void setCameraSize(int newCamWidth, int newCamHeight) {
		camHeight = newCamHeight;
		camWidth = newCamWidth;

		asciiHeight = camHeight;
		asciiWidth = camWidth;

		mainHeight = camHeight;
		mainWidth = camWidth + asciiWidth;

		frameHeight = mainHeight + ctrlHeight;
		frameWidth = mainWidth;

		resizeComponents();
	}

	private Lock imgLock = new ReentrantLock();
	private Condition imgReady = imgLock.newCondition();

	void insertImgToConvert(Mat imageMat) throws InterruptedException {
		imgLock.lock();
		try {
//			System.out.println(Thread.currentThread().getName() + " has acquired lock");
//			while (imageToConvert != null) {
//				System.out.print(Thread.currentThread().getName() + " is waiting...");
//				System.out.println("imageToConvert = " + imageToConvert);
//				needImg.await();
//			}
			imageToConvert = imageMat;
			imgReady.signalAll();
//			Boolean check = imageToConvert == null;
//			System.out.println("imgReady signalled! nullcheck should be false: " + check);
		} finally {
			imgLock.unlock();
		}
	}

	Mat getImgToConvert() throws InterruptedException {
		imgLock.lock();
		try {
//			System.out.println(Thread.currentThread().getName() + " has acquired lock");
			while (imageToConvert == null) {
//				System.out.println(Thread.currentThread().getName() + " is waiting...");
				imgReady.await();
			}
			Mat output = imageToConvert;
			imageToConvert = null;
			return output;
		} finally {
			imgLock.unlock();
		}
	}

	void waitForImage() {
		imgLock.lock();
		try {
//			System.out.println(Thread.currentThread().getName() + " is waiting for image");
			while (imageToConvert == null) {
				imgReady.await();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			imgLock.unlock();
		}
	}

	private final ExecutorService exec = Executors.newFixedThreadPool(2);

	void startCapture() {
		captureMode = true;
		CameraTask cameraWorker = new CameraTask(this);
		cameraWorker.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("nativeResolution".equals(evt.getPropertyName())) {
					Dimension nativeRes = (Dimension) evt.getNewValue();
					setCameraSize(nativeRes.width, nativeRes.height);

				}
			}
		});
		exec.execute(cameraWorker);

		AsciiWorker asciiWorker = new AsciiWorker(this);
		exec.execute(asciiWorker);
	}

	private class CameraTask extends SwingWorker<Boolean, Object> {
		private CameraPanel camPanel;
		private GUI GUI;

		CameraTask(GUI GUIobj) {
			this.GUI = GUIobj;
			this.camPanel = GUIobj.cameraPanel;
		}

		@Override
		protected Boolean doInBackground() throws Exception {
			// Signal native resolution of camera
			double nativeHeight = camPanel.capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
			double nativeWidth = camPanel.capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
			signalNativeResolution((int) nativeWidth, (int) nativeHeight);

			Mat imageMat = new Mat();
			byte[] imageData;

			Boolean moreWork = GUI.captureMode;
			while (moreWork) {
				// Read image to matrix
				camPanel.capture.read(imageMat);
				// convert to byte
				final MatOfByte buf = new MatOfByte();
				Imgcodecs.imencode(".jpg", imageMat, buf);
				imageData = buf.toArray();

				// Store icon
				camPanel.updateCameraScreen(new ImageIcon(imageData));

				if (GUI != null) {
					GUI.insertImgToConvert(imageMat);
				}

				moreWork = GUI.captureMode;
			}

			return true;
		}

		private void signalNativeResolution(int width, int height) {
			this.firePropertyChange("nativeResolution", null, new Dimension(width, height));
		}

	}

	private class AsciiWorker extends SwingWorker<Boolean, Object> {
		private GUI parent;
		private AsciiPanel asciiPanel;

		AsciiWorker(GUI parentGUI) {
			this.parent = parentGUI;
			this.asciiPanel = parentGUI.asciiPanel;
		}

		@Override
		protected Boolean doInBackground() throws Exception {
			Mat img;
			Mat greyImage;
			String[] asciiImg;
			while (parent.captureMode) {
				parent.waitForImage();
				img = parent.getImgToConvert();
//				System.out.println(img);
				greyImage = ImageProcessing.grayify(img);
				asciiImg = ImageProcessing.toAscii(greyImage, asciiPanel.getCurrentMapping());
				asciiPanel.updateImage(asciiImg);
			}
			return true;
		}

	}

	void stopCapture() {
		captureMode = false;
	}

	private void resizeComponents() {

		GUI.setCompSize(masterPanel, mainWidth, frameHeight);
		GUI.setCompSize(mainPanel, mainWidth, mainHeight);
		GUI.setCompSize(cameraPanel, camWidth, camHeight);
		GUI.setCompSize(asciiPanel, asciiWidth, asciiHeight);
		GUI.setCompSize(ctrlPanel, ctrlWidth, ctrlHeight);

		ctrlPanel.resizeComponents();

		revalidate();

		pack();
	}

	static void setCompSize(JComponent comp, int width, int height) {
		Dimension newSize = new Dimension(width, height);
		comp.setSize(newSize);
		comp.setPreferredSize(newSize);
	}

	void run() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}

	void updateImage(String[] asciiImg) {
		asciiPanel.updateImage(asciiImg);
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		GUI myGUI = new GUI();
		myGUI.run();
	}
}
