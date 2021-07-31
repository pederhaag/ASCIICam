import javax.swing.*;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

class AsciiPanel extends JPanel {
	private int panelHeight;
	private int panelWidth;
	private double lineHeight;
	private final Font font = new Font("monospaced", Font.PLAIN, 500);
	private String[] asciiImg = { "" };
	private AffineTransform scaleTransformation = new AffineTransform();
	private boolean transformGenerated = false;
	private static HashMap<String, Set<Entry<Integer, Character>>> asciiMappings = AsciiMappings.mappings;
	private Set<Entry<Integer, Character>> currentMapping = asciiMappings.get(AsciiMappings.getDefault());
	GUI parentGUI;

	AsciiPanel() {
		this(null);
	}

	AsciiPanel(GUI parentGUI) {
		super();
		this.setFont(font);

		setBorder(BorderFactory.createLineBorder(Color.red));
	}

	private void setTransformation(Graphics2D g, String string) {
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

//	static String[] getMapNames() {
//		Set<String> names = asciiMappings.keySet();
//		String[] mapNames = new String[names.size()];
//		Iterator<String> iter = names.iterator();
//		for (int i = 0; i < mapNames.length; i++) {
//			mapNames[i] = iter.next();
//		}
//		return mapNames;
//	}

//	private void setPanelSize(int width, int height) {
//		panelWidth = width;
//		panelHeight = height;
//		Dimension size = new Dimension(panelWidth, panelHeight);
//		this.setSize(size);
//		this.setPreferredSize(size);
//	}

	private void generateTransformation(Graphics2D g) {

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

	private void drawString(Graphics2D g, int x, int y) {
		for (String line : asciiImg) {
			g.drawString(line, x, y += lineHeight);
		}

	}

	private Rectangle getStringBounds(Graphics2D g2, String str, float x, float y) {
		FontRenderContext frc = g2.getFontRenderContext();
		GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
		return gv.getPixelBounds(null, x, y);
	}

	void updateImage(String[] asciiImg) {
		this.asciiImg = asciiImg;
		transformGenerated = false;
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

		Graphics2D g2d = (Graphics2D) g.create();
		setTransformation(g2d, asciiImg[0]);
		drawString(g2d, 0, 0);

		g2d.dispose();
	}

	public static void main(String s[]) {
		String[] asciiImg = { "2V@", "SS@", "@.R" };
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AsciiPanel asciiPanel;
		try {

//			asciiPanel = new AsciiPanel(asciiImg);
			asciiPanel = new AsciiPanel();
			asciiPanel.updateImage(asciiImg);
//			asciiPanel = new AsciiPanel(400, 300);
			f.add(asciiPanel);

//			f.setSize(300, 300);
			f.pack();
			f.setVisible(true);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String[] newAsciiImg = { "@.O", "X2!", "..R" };
			asciiPanel.updateImage(newAsciiImg);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}