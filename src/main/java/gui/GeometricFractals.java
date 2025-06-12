package gui;

import geometric.fractals.*;

import geometric.AbstractGeometricFractal;

import renderer.FractalPanel;

import java.util.ArrayList;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JCheckBox;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class GeometricFractals {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private static int getFPS(AbstractGeometricFractal<?> fractal) {
		try {
			return (int) fractal.getClass().getMethod("getSuggestedFPS").invoke(null);
		} catch (Exception e) {
			return 30;
		}
	}

	private static int getUPS(AbstractGeometricFractal<?> fractal) {
		try {
			return (int) fractal.getClass().getMethod("getSuggestedUPS").invoke(null);
		} catch (Exception e) {
			return 1;
		}
	}

	protected static JComponent getContentPane(
		FractalPanel fractalPanel,
		JComboBox<AbstractGeometricFractal<?>> comboBox
	) {

		JCheckBox resetCheck = new JCheckBox("Reset on Completion");
		resetCheck.setSelected(true);
		resetCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				AbstractGeometricFractal<?> selectedFractal = (AbstractGeometricFractal<?>) comboBox.getSelectedItem();
				if (selectedFractal == null) return;
				selectedFractal.setReset( resetCheck.isSelected() );
			}
		});

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(comboBox, BorderLayout.CENTER);
		topPanel.add(resetCheck, BorderLayout.EAST);

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbstractGeometricFractal<?> selectedFractal = (AbstractGeometricFractal<?>) comboBox.getSelectedItem();
				if (selectedFractal == null) return;

				comboBox.setEnabled(false);

				new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						fractalPanel.stop();
						return null;
					}

					protected void done() {
						selectedFractal.reset();
						selectedFractal.setReset( resetCheck.isSelected() );
						fractalPanel.clearDrawables();
						fractalPanel.addDrawable(selectedFractal);
						fractalPanel.setFPS( getFPS(selectedFractal) );
						fractalPanel.setUPS( getUPS(selectedFractal) );
						fractalPanel.moveViewTo(0, 0);
						fractalPanel.setZoom(1.0);

						new SwingWorker<Void, Void>() {
							protected Void doInBackground() throws Exception {
								fractalPanel.start();
								return null;
							}

							protected void done() {
								comboBox.setEnabled(true);
							}
						}.execute();
					}
				}.execute();
			}
		});

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(fractalPanel, BorderLayout.CENTER);
		return contentPane;
	}

	protected static void createAndShowGUI(List<AbstractGeometricFractal<?>> fractals) {
		JFrame frame = new JFrame("Geometric Fractals");
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		FractalPanel fractalPanel = new FractalPanel();
		JComboBox<AbstractGeometricFractal<?>> comboBox = new JComboBox<>(
			fractals.toArray(new AbstractGeometricFractal<?>[0])
		);
		frame.add( getContentPane(fractalPanel, comboBox) );
		frame.pack();
		frame.setLocationRelativeTo(null);

		frame.addWindowListener(
			new WindowAdapter() {
				public void windowOpened(WindowEvent e) {
					comboBox.setSelectedIndex(0);
				}

				public void windowClosed(WindowEvent e) {
					new SwingWorker<Void, Void>() {
						protected Void doInBackground() throws Exception {
							fractalPanel.stop();
							return null;
						}
					}.execute();
				}
			}
		);

		String os = System.getProperty("os.name").toLowerCase();
		String keyStroke = os.contains("mac") ? "meta W" : "control W";

		JRootPane rootPane = frame.getRootPane();
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = rootPane.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(keyStroke), "close");
		actionMap.put(
			"close",
			new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}
			}
		);

		frame.setVisible(true);
	}
    
	public static void main(String[] args) {
		List<AbstractGeometricFractal<?>> fractals = new ArrayList<>();

		fractals.add(new BinarySierpinskiTriangle());
		fractals.add(new BinarySierpinskiTriangleFilled());
		fractals.add(new BoxFractal());
		fractals.add(new CesaroCurves());
		fractals.add(new CesaroFractal());
		fractals.add(new CrossFractal());
		fractals.add(new CrossKochCurve());
		fractals.add(new HTree());
		fractals.add(new InwardCircles());
		fractals.add(new KochAntiSnowflake());
		fractals.add(new KochLine());
		fractals.add(new KochSnowflake());
		fractals.add(new LevyCurve());
		fractals.add(new LevyTapestryInside());
		fractals.add(new LevyTapestryOutside());
		fractals.add(new NumberLineCircles());
		fractals.add(new OuterSierpinskiTriangle());
		fractals.add(new OutwardCircles());
		fractals.add(new PythagorasShrub());
		fractals.add(new PythagorasTree());
		fractals.add(new SierpinskiArrowhead());
		fractals.add(new SierpinskiCarpet());
		fractals.add(new SierpinskiCircle());
		fractals.add(new SierpinskiCurve());
		fractals.add(new SierpinskiCurve1());
		fractals.add(new SierpinskiCurve2());
		fractals.add(new SierpinskiCurve3());
		fractals.add(new SierpinskiHexagon());
		fractals.add(new SierpinskiRelatives());
		fractals.add(new SierpinskiRelatives1());
		fractals.add(new SierpinskiRelatives2());
		fractals.add(new SierpinskiRelatives3());
		fractals.add(new SierpinskiRelatives4());
		fractals.add(new SierpinskiRelatives5());
		fractals.add(new SierpinskiRelatives6());
		fractals.add(new SierpinskiSnowflake());
		fractals.add(new SierpinskiTriangle());
		fractals.add(new Thorn());
		fractals.add(new TornSquare());
		fractals.add(new TriCircle());
		fractals.add(new VicsekTriangle());

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(fractals);
			}
		});
	}
}

