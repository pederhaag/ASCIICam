import javax.swing.*;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 
 * The {@code AsciiPanel} extends the JPanel and is used for handling the
 * rendering of ascii-art in the GUI
 * 
 * <p>
 * An image is retrieved by a {@link AsciiWorker} and inserted via the
 * {@link UpdateImage} method. When painting the {@code AsciiPanel} the
 * {@code Graphics2D.drawString} method is called for each row.
 * 
 * The {@code Graphics2D} generated is being given an scaling-transformation in
 * order to make sure that the image being drawn gets the same resolution as the
 * input image.
 * 
 * A TODO(?) feature would be to allow for the ascii-art displayed to have a
 * smaller resolution than the captured image
 *
 */
class AsciiPanel extends JPanel {
	private static final long serialVersionUID = -4514449244589624311L;
	private int panelHeight;
	private int panelWidth;
	private double lineHeight;
	private final Font font = new Font("monospaced", Font.PLAIN, 500);
	private String[] asciiImg = { "" };
	private AffineTransform scaleTransformation = new AffineTransform();
	private boolean transformGenerated = false;
	private static HashMap<String, Set<Entry<Integer, Character>>> asciiMappings = AsciiMappings.getMappings();
	private Set<Entry<Integer, Character>> currentMapping = asciiMappings.get(AsciiMappings.getDefault());
	GUI parentGUI;
	Boolean captureMode = false;

	AsciiPanel(GUI parentGUI) {
		super();
		this.setFont(font);
		this.parentGUI = parentGUI;
	}

	AsciiWorker createAsciiWorker() {
		return new AsciiWorker(this);
	}

	void startCapture() {
		captureMode = true;
	}

	void stopCapture() {
		captureMode = false;
	}

	/**
	 * Apply the correct scaling-transformation to the {@code Graphics2D} object
	 * 
	 * @param g The graphics which should get the transformation
	 */
	private void setTransformation(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (!transformGenerated) {
			generateTransformation(g);
			transformGenerated = true;
		}
		g.transform(scaleTransformation);
	}

	static Set<String> getAvailMappings() {
		return asciiMappings.keySet();
	}

	/**
	 * Generate the correct {@code AffineTransformation} and give it to the
	 * {@code ScaleTransformation} class variable
	 * 
	 * @param g the graphics for which the generation of the transformation should
	 *          be based on
	 */
	private void generateTransformation(Graphics2D g) {

		// Only generate a new transformation if the panel-size has changed, else it's
		// not needed
		if (panelHeight != this.getHeight() || panelWidth != this.getWidth()) {
			panelHeight = this.getHeight();
			panelWidth = this.getWidth();

			scaleTransformation = new AffineTransform();
			Rectangle r = getStringBounds(g, asciiImg[0], 0, 0);
			lineHeight = r.getHeight();
			double linewidth = r.getWidth();

			double targetLineHeight = ((double) panelHeight) / ((double) asciiImg.length);
			double targetLinewidth = panelWidth;

			double heightScale = targetLineHeight / lineHeight;
			double widthScale = targetLinewidth / linewidth;

			scaleTransformation.scale(widthScale, heightScale);

		}

	}

	/*
	 * Draw the ascii-strings
	 */
	private void drawString(Graphics2D g, int x, int y) {
		for (String line : asciiImg) {
			g.drawString(line, x, y += lineHeight);
		}

	}

	/**
	 * A small helpful method for retrieving the bounds of a string in the context
	 * of a {@code Graphics2D} object. This is needed in order to calculate the
	 * scaling factors of the transformation
	 */
	private Rectangle getStringBounds(Graphics2D g2, String str, float x, float y) {
		FontRenderContext frc = g2.getFontRenderContext();
		GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
		return gv.getPixelBounds(null, x, y);
	}

	/**
	 * Update the panel with a new ascii-image
	 * @param asciiImg
	 */
	void updateImage(String[] asciiImg) {
		this.asciiImg = asciiImg;
		transformGenerated = false; // Why is this needed?
		this.revalidate();
		this.repaint();
	}

	Set<Entry<Integer, Character>> getCurrentMapping() {
		return currentMapping;
	}

	Set<Entry<Integer, Character>> setCurrentMapping(String mapID) {
		this.currentMapping = asciiMappings.get(mapID);
		return getCurrentMapping();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw the ascii-image
		Graphics2D g2d = (Graphics2D) g.create();
		setTransformation(g2d);
		drawString(g2d, 0, 0);

		g2d.dispose();
	}

}