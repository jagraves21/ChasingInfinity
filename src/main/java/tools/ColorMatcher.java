package tools;

import utils.color.NamedColors;
import utils.color.ColorUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class ColorMatcher {
	protected static Color getContrastingColor(Color color) {
		int yiq = ((color.getRed() * 299) + (color.getGreen() * 587) + (color.getBlue() * 114)) / 1000;
		return yiq >= 128 ? Color.BLACK : Color.WHITE;
	}

	protected static void updatedPreferedSize(JPanel panel) {
		Dimension originalSize = panel.getPreferredSize();
		int newWidth = originalSize.width + 50;
		int newHeight = originalSize.height + 50;
		Dimension newSize = new Dimension(newWidth, newHeight);
		panel.setPreferredSize(newSize);
	}

	protected static JComponent getContentPane(Color queryColor, Map<String, Color> matchingColors) {
		JPanel outerPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 10, 10));
		outerPanel.setBackground(queryColor);

		List<Map.Entry<String, Color>> sortedEntries = matchingColors.entrySet().stream()
			.sorted(new Comparator<Map.Entry<String, Color>>() {
				public int compare(Map.Entry<String, Color> e1, Map.Entry<String, Color> e2) {
					double d1 = ColorUtils.labColorDistance(queryColor, e1.getValue());
					double d2 = ColorUtils.labColorDistance(queryColor, e2.getValue());
					return Double.compare(d1, d2);
				}
			})
			.collect(Collectors.toList());

		for (Map.Entry<String, Color> entry : sortedEntries) {
			String name = entry.getKey();
			Color color = entry.getValue();
			Color textColor = getContrastingColor(color);
			String textLabel = String.format(
				"<html>%s<br>%.2f<br>%.2f</html>",
				name,
				ColorUtils.rgbColorDistance(queryColor, color),
				ColorUtils.labColorDistance(queryColor, color)
			);

			JLabel label = new JLabel(textLabel, SwingConstants.CENTER);
			label.setForeground(textColor);
			label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					StringSelection selection = new StringSelection(name);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, null);
					System.out.println("Copied to clipboard: " + name);
				}
			});

			JPanel colorPanel = new JPanel(new BorderLayout());
			colorPanel.setBackground(color);
			colorPanel.add(label, BorderLayout.CENTER);
			colorPanel.setToolTipText(name);
			updatedPreferedSize(colorPanel);

			outerPanel.add(colorPanel);
		}

		return outerPanel;
	}

	protected static void createAndShowGUI(Color queryColor, Map<String, Color> matchingColors) {
		final JFrame frame = new JFrame("Color Matcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(getContentPane(queryColor, matchingColors));
		frame.setPreferredSize(new Dimension(800,600));
		frame.pack();
		frame.setLocationRelativeTo(null);

		String os = System.getProperty("os.name").toLowerCase();
		String keyStroke = os.contains("mac") ? "meta W" : "control W";

		JRootPane rootPane = frame.getRootPane();
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = rootPane.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(keyStroke), "close");
		actionMap.put("close", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});

		frame.setVisible(true);
	}

	private static int parseComponent(String value, String name) {
		try {
			int val = Integer.parseInt(value);
			if (val < 0 || val > 255) {
				throw new IllegalArgumentException(name + " component must be between 0 and 255");
			}
			return val;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(name + " component must be an integer");
		}
	}

	private static Color parseHexColor(String hex) {
		if (hex.startsWith("#")) {
			hex = hex.substring(1);
		}
		if (hex.length() != 6 && hex.length() != 8) {
			throw new IllegalArgumentException("Hex color must be 6 or 8 characters long");
		}
		try {
			int rgb = (int) Long.parseLong(hex, 16); // avoid overflow
			if (hex.length() == 6) {
				return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
			} else {
				return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, (rgb >> 24) & 0xFF);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid hex digits: " + hex);
		}
	}

	public static Color parseColorFromArgs(String[] args) {
		if (args == null || (args.length != 1 && args.length != 3)) {
			throw new IllegalArgumentException("Usage: <r> <g> <b>  or  <hex>");
		}

		if (args.length == 1) {
			return parseHexColor(args[0]);
		} else {
			int r = parseComponent(args[0], "red");
			int g = parseComponent(args[1], "green");
			int b = parseComponent(args[2], "blue");
			return new Color(r, g, b);
		}
	}

	public static void main(String[] args) {
		try {
			Color queryColor = parseColorFromArgs(args);
			List<String> colorNames = ColorUtils.getClosestNamedColors(queryColor, 20);
			Map<String,Color> matchingColors = new HashMap<>();
			for (String colorName : colorNames) {
				matchingColors.put(colorName, NamedColors.getColorByName(colorName));
			}
			SwingUtilities.invokeLater(() -> {
				createAndShowGUI(queryColor, matchingColors);
			});
		} catch (IllegalArgumentException e) {
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
}

