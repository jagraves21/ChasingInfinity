package tools;

import utils.color.NamedPalettes;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.LinearGradientPaint;
import java.awt.Paint;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class PaletteViewer {
	protected static JComponent getColorPalettePanel(Color[] colors) {
		return new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				int width = getWidth();
				int height = getHeight();
				int topHeight = height / 2;
				
				int[] xPositions = new int[colors.length + 1];
				for (int ii = 0; ii <= colors.length; ii++) {
					xPositions[ii] = Math.round(ii * (width / (float) colors.length));
				}

				float[] fractions = new float[colors.length];
				for (int ii = 0; ii < colors.length; ii++) {
					int x = xPositions[ii];
					int w = xPositions[ii + 1] - x;

					g2d.setColor(colors[ii]);
					g2d.fillRect(x, 0, w, topHeight);

					fractions[ii] = (x + w / 2f) / width;
				}


				Paint paint = new LinearGradientPaint(
					0, 0,
					width, 0,
					fractions,
					colors
				);
				g2d.setPaint(paint);
				g2d.fillRect(0, topHeight, width, height);
				g2d.dispose();
			}
		};
	}

	protected static JComponent getContentPane(Map<String, Color[]> colorPalettes) {
		JTabbedPane tabbedPane = new JTabbedPane();
		for (Map.Entry<String,Color[]> paletteEntry : colorPalettes.entrySet()) {
			String paletteName = paletteEntry.getKey();
			Color[] paletteColors = paletteEntry.getValue();

			JComponent tabContent = getColorPalettePanel(paletteColors);
			tabbedPane.addTab(paletteName, tabContent);
		}

		tabbedPane.setPreferredSize(new Dimension(800, 600));
		return tabbedPane;
	}
	
	protected static void createAndShowGUI(Map<String, Color[]> colorPalettes) {
		final JFrame frame = new JFrame("Color Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(getContentPane(colorPalettes));
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
		Map<String, Color[]> colorPalettes = new TreeMap<>(
			NamedPalettes.getNameToPaletteMap()
		);

		SwingUtilities.invokeLater(() -> {
			createAndShowGUI(colorPalettes);
		});
	}
}

