import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class GUI extends JFrame {
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

	final AsciiPanel asciiPanel = new AsciiPanel(this);
	private final CameraPanel cameraPanel = new CameraPanel(this);
	private final JPanel masterPanel = new JPanel();
	private final JPanel mainPanel = new JPanel();
	private final ControlPanel ctrlPanel = new ControlPanel(this);
	private Mat imageToConvert = null;
	private static final String frameTitle = "ASCII Camera";

	GUI() {
		super(frameTitle);

		this.frameWidth = 600;
		this.frameHeight = 350;
		buildGuiElements();
		setInitialSizes();
	}

	/**
	 * Create the general structure of the differen GUI-elements
	 */
	private void buildGuiElements() {
		// Master panel
		masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.PAGE_AXIS));
		this.add(masterPanel);

		// * Main panel
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		masterPanel.add(mainPanel);

		// ** Camera panel
		mainPanel.add(cameraPanel);

		// ** ASCII Panel
		mainPanel.add(asciiPanel);

		// * Control Panel
		masterPanel.add(ctrlPanel);
	}

	/**
	 * Initialize the sizes of the different components in the GUI
	 */
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

	/**
	 * Resize the components in the GUI-layout based on new capturesource dimensions
	 * 
	 * @param newCamWidth  width of the capturesource
	 * @param newCamHeight height of the capturesource
	 */
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

	/**
	 * Insert a captured image from a capturesource into storage. This for later
	 * retrieval by an {@link AsciiWorker} to convert.
	 * 
	 * @param imageMat image to insert
	 * @throws InterruptedException
	 */
	void insertImgToConvert(Mat imageMat) throws InterruptedException {
		imgLock.lock();
		try {
			imageToConvert = imageMat;
			imgReady.signalAll();
		} finally {
			imgLock.unlock();
		}
	}

	/**
	 * Grab an image in order to convert it to ascii-art
	 * 
	 * @return a {@code Mat}-object representing the image
	 * @throws InterruptedException
	 */
	Mat getImgToConvert() throws InterruptedException {
		imgLock.lock();
		try {
			Mat output = imageToConvert;
			imageToConvert = null;
			return output;
		} finally {
			imgLock.unlock();
		}
	}

	/**
	 * Wait for the running {@link AsciiWorker} to publish a new image which needs
	 * to be converted to ascii-art
	 * 
	 * @throws InterruptedException
	 */
	void waitForImage() throws InterruptedException {
		imgLock.lock();
		try {
			while (imageToConvert == null && asciiPanel.captureMode) {
				imgReady.await();
			}

		} finally {
			imgLock.unlock();
		}
	}

	private final ExecutorService exec = Executors.newFixedThreadPool(2);

	void startCapture() {
		// Start a CameraWorker
		CameraWorker cameraWorker = cameraPanel.createCameraWorker();
		cameraWorker.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("nativeResolution".equals(evt.getPropertyName())) {
					Dimension nativeRes = (Dimension) evt.getNewValue();
					setCameraSize(nativeRes.width, nativeRes.height);

				}
			}
		});
		exec.execute(cameraWorker);

		// Start a AsciiWorker
		AsciiWorker asciiWorker = asciiPanel.createAsciiWorker();
		exec.execute(asciiWorker);

		// Start capturing
		cameraPanel.startCapture();
		asciiPanel.startCapture();
	}

	void stopCapture() {
		cameraPanel.stopCapture();
		asciiPanel.stopCapture();
	}

	/*
	 * Resize components. Usually called after setting updating size-attributes.
	 */
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
		// comp.setSize(newSize);
		comp.setPreferredSize(newSize);
	}

	void run() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		GUI myGUI = new GUI();
		myGUI.run();
	}
}
