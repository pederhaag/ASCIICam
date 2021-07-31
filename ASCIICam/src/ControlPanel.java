import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ControlPanel extends JPanel {
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private ExitButton exitBtn = new ExitButton();
	private StartCaptureBtn captureBtn = new StartCaptureBtn();
	private final String[] mapNames = buildMapNames();
	private JComboBox<String> mappingSelector = new JComboBox<String>(mapNames);
	private JLabel configDesc = new JLabel("Select mapping:");
	private GUI GUIParent;

	ControlPanel(GUI GUIParent) {
		this.GUIParent = GUIParent;
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setBorder(BorderFactory.createLineBorder(Color.BLUE));

		leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		leftPanel.add(configDesc);
		leftPanel.add(mappingSelector);
		mappingSelector.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String mapName = (String) mappingSelector.getSelectedItem();
				GUIParent.asciiPanel.setCurrentMapping(mapName);
			}

		});
		add(leftPanel);

		rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		rightPanel.add(exitBtn);
		rightPanel.add(captureBtn);
		add(rightPanel);

	}

	private static String[] buildMapNames() {
		Set<String> names = AsciiPanel.getAvailMappings();
		return names.toArray(new String[names.size()]);
	}

	void resizeComponents() {
		int totalHeight = this.getHeight();
		int totalWidth = this.getWidth();

		int leftHeight = totalHeight;
		int leftWidth = totalWidth / 2;
		int rightHeight = totalHeight;
		int rightWidth = totalWidth - leftWidth;

		GUI.setCompSize(leftPanel, leftWidth, leftHeight);
		GUI.setCompSize(rightPanel, rightWidth, rightHeight);
	}

	private class ExitButton extends JButton {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final String displayText = "Exit";

		ExitButton() {
			super(displayText);
			this.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}

			});
		}
	}

	private class StartCaptureBtn extends JButton {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final String unpressedText = "Start capture";
		private static final String pressedText = "Stop capture";
		private Boolean pressed = false;
//		private final Border pressedBorder = BorderFactory.createBevelBorder(2);
//		private final Border unpressedBorder = BorderFactory.createBevelBorder(2);

		StartCaptureBtn() {
			super(unpressedText);
//			setBorder(unpressedBorder);
			this.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (pressed) {
						setText(unpressedText);
						pressed = false;
						GUIParent.stopCapture();
//						setBorder(unpressedBorder);
					} else {
						setText(pressedText);
						pressed = true;
						GUIParent.startCapture();
//						setBorder(pressedBorder);

					}
				}

			});
		}
	}
}