package tools;

import utils.color.NamedColors;
import utils.color.ColorUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class ColorViewer {

	public static final Map<String, Color> BASE_COLORS;
	static {
		Map<String, Color> map = new HashMap<>();
		map.put("RED", NamedColors.getColorByName("RED"));
		map.put("ORANGE", NamedColors.getColorByName("ORANGE"));
		map.put("YELLOW", NamedColors.getColorByName("YELLOW"));
		map.put("GREEN", NamedColors.getColorByName("GREEN"));
		map.put("BLUE", NamedColors.getColorByName("BLUE"));
		map.put("PURPLE", NamedColors.getColorByName("PURPLE"));
		map.put("BROWN", NamedColors.getColorByName("BROWN"));
		map.put("BLACK", NamedColors.getColorByName("BLACK"));
		map.put("WHITE", NamedColors.getColorByName("WHITE"));
		map.put("GRAY", NamedColors.getColorByName("GRAY"));
		BASE_COLORS = Collections.unmodifiableMap(map);
	}
	
	public static String findClosestBaseColorName(Color target, Map<String, Color> baseColors) {
		return baseColors.entrySet().stream()
			.min(Comparator.comparingDouble(e -> ColorUtils.labColorDistance(target, e.getValue())))
			.map(Map.Entry::getKey)
			.orElseThrow(() -> new IllegalStateException("No base colors available"));
	}

	public static Map<String, Map<String, Color>> groupColors(Map<String, Color> baseColors, Map<String, Color> colorsToAssign) {
		return colorsToAssign.entrySet().stream()
			.collect(Collectors.groupingBy(
				entry -> findClosestBaseColorName(entry.getValue(), baseColors),
				Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
			)
		);
	}

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

	protected static JComponent getColorGridPanel(Color baseColor, Map<String, Color> namedColors) {
		JPanel outerPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 10, 10));

		List<Map.Entry<String, Color>> sortedEntries = namedColors.entrySet().stream()
			.sorted(new Comparator<Map.Entry<String, Color>>() {
				public int compare(Map.Entry<String, Color> e1, Map.Entry<String, Color> e2) {
					double d1 = ColorUtils.labColorDistance(baseColor, e1.getValue());
					double d2 = ColorUtils.labColorDistance(baseColor, e2.getValue());
					return Double.compare(d1, d2);
				}
			})
			.collect(Collectors.toList());

		for (Map.Entry<String, Color> entry : sortedEntries) {
			String name = entry.getKey();
			Color color = entry.getValue();

			JLabel label = new JLabel(name, SwingConstants.CENTER);
			label.setForeground(getContrastingColor(color));
			label.setToolTipText(name);

			JPanel colorPanel = new JPanel(new BorderLayout());
			colorPanel.setBackground(color);
			colorPanel.add(label, BorderLayout.CENTER);
			colorPanel.setToolTipText(name);
			updatedPreferedSize(colorPanel);

			outerPanel.add(colorPanel);
		}

		JScrollPane scrollPane = new JScrollPane(outerPanel);
		scrollPane.setPreferredSize(new Dimension(600, 400));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		return scrollPane;
	}

	protected static JComponent getContentPane(Map<String, Map<String, Color>> colorGroups) {
		JTabbedPane tabbedPane = new JTabbedPane();
		for (Map.Entry<String,Map<String,Color>> groupEntry : colorGroups.entrySet()) {
			String groupName = groupEntry.getKey();
			Map<String,Color> colorGroup = groupEntry.getValue();
			Color tabColor = NamedColors.getColorByName(groupName);

			JComponent tabContent = getColorGridPanel(tabColor, colorGroup);
			tabbedPane.addTab(groupName, tabContent);
			tabbedPane.setBackgroundAt(
				tabbedPane.getTabCount() - 1, tabColor
			);
		}

		tabbedPane.setPreferredSize(new Dimension(800, 600));
		return tabbedPane;
	}
	
	protected static void createAndShowGUI(Map<String, Map<String, Color>> colorGroups) {
		final JFrame frame = new JFrame("Color Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(getContentPane(colorGroups));
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
	
	public static void main(String[] args) {
		Map<String, Map<String, Color>> colorGroups = 
			groupColors(BASE_COLORS, NamedColors.getNameToColorMap());

		SwingUtilities.invokeLater(() -> {
			createAndShowGUI(colorGroups);
		});
	}
}

