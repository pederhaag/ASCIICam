import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * The {@code ControlPanel} class is an extension of a JPanel. It is used as a
 * container for user based controls in the GUI.
 * 
 * It contains a combobox for selecting different mapping, a start button and an
 * exit button.
 *
 */
class ControlPanel extends JPanel {
	private static final long serialVersionUID = 8036698022990148234L;
	private final JPanel leftPanel = new JPanel();
	private final JPanel rightPanel = new JPanel();
	private final ExitButton exitBtn = new ExitButton();
	private final StartCaptureBtn captureBtn = new StartCaptureBtn();
	private final String[] mapNames = buildMapNames();
	private final JComboBox<String> mappingSelector = new JComboBox<String>(mapNames);
	private final JLabel configDesc = new JLabel("Select mapping:");
	private final GUI GUIParent;

	ControlPanel(GUI GUIParent) {
		this.GUIParent = GUIParent;
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		/**
		 * Left panel - containing a constant text-label and a combobox for selecting
		 * ascii-mappings
		 */
		leftPanel.add(configDesc);
		leftPanel.add(mappingSelector);
		mappingSelector.setSelectedItem(AsciiMappings.getDefault());
		mappingSelector.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String mapName = (String) mappingSelector.getSelectedItem();
				GUIParent.asciiPanel.setCurrentMapping(mapName);
			}

		});
		add(leftPanel);

		/**
		 * Right panel - containing a capture button and exit button
		 */
		rightPanel.add(captureBtn);
		rightPanel.add(exitBtn);
		add(rightPanel);

	}

	/**
	 * Build a {@code String} array with mappingnames. Used for the combobox
	 * @return Array of mapping names
	 */
	private static String[] buildMapNames() {
		Set<String> names = AsciiPanel.getAvailMappings();
		return names.toArray(new String[names.size()]);
	}

	/**
	 * Resize all of the components ensuring the designed layout
	 */
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

	/**
	 * 
	 * A simple exit-button
	 *
	 */
	private class ExitButton extends JButton {
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

	/**
	 * 
	 * A simple start-stop button for capturing
	 *
	 */
	private class StartCaptureBtn extends JButton {
		private static final long serialVersionUID = 1L;
		private static final String unpressedText = "Start capture";
		private static final String pressedText = "Stop capture";
		private Boolean pressed = false;

		StartCaptureBtn() {
			super(unpressedText);
			this.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (pressed) {
						setText(unpressedText);
						pressed = false;
						GUIParent.stopCapture();
					} else {
						setText(pressedText);
						pressed = true;
						GUIParent.startCapture();

					}
				}

			});
		}
	}
}