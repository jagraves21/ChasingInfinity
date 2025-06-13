package gui;

import geometric.AbstractGeometricFractal;
import geometric.GeometricFractalRegistry;

import renderer.FractalPanel;

import java.util.ArrayList;
import java.util.List;

import java.util.function.Supplier;

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
		JComboBox<Supplier<AbstractGeometricFractal<?>>> comboBox
	) {
		// reference to the currently instantiated fractal
		// this is needed for the resetCheck functionality
		final AbstractGeometricFractal<?>[] currentFractal = new AbstractGeometricFractal<?>[1];

		JCheckBox resetCheck = new JCheckBox("Reset on Completion");
		resetCheck.setSelected(true);
		resetCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (currentFractal[0] != null) {
					currentFractal[0].setReset(resetCheck.isSelected());
				}
			}
		});

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(comboBox, BorderLayout.CENTER);
		topPanel.add(resetCheck, BorderLayout.EAST);

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				Supplier<AbstractGeometricFractal<?>> selectedSupplier =
					(Supplier<AbstractGeometricFractal<?>>) comboBox.getSelectedItem();
				if (selectedSupplier == null) return;

				comboBox.setEnabled(false);

				new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						fractalPanel.stop();
						return null;
					}

					protected void done() {
						AbstractGeometricFractal<?> fractal = selectedSupplier.get();
						currentFractal[0] = fractal;

						fractal.reset();
						fractal.setReset( resetCheck.isSelected() );

						fractalPanel.clearDrawables();
						fractalPanel.addDrawable(fractal);
						fractalPanel.setFPS(getFPS(fractal));
						fractalPanel.setUPS(getUPS(fractal));
						fractalPanel.moveViewTo(0, 0);
						fractalPanel.setZoom(1.0);

						new SwingWorker<Void, Void>() {
							protected Void doInBackground() {
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

	public static String getSupplierName(Supplier<?> supplier) {
		try {
			return supplier.get().toString();
		} catch (Exception e) {
			return "Unknown";
		}
	}

	protected static JComboBox<Supplier<AbstractGeometricFractal<?>>> getComboBox(
		List<Supplier<AbstractGeometricFractal<?>>> fractalSuppliers
	) {
		JComboBox<Supplier<AbstractGeometricFractal<?>>> comboBox = new JComboBox<>();
		for (Supplier<AbstractGeometricFractal<?>> supplier : fractalSuppliers) {
			final String supplierName = getSupplierName(supplier);
			comboBox.addItem(new Supplier<AbstractGeometricFractal<?>>() {
				public AbstractGeometricFractal<?> get() {
					return supplier.get();
				}

				public String toString() {
					return supplierName;
				}
			});
		}

		return comboBox;
	}

	protected static void createAndShowGUI(List<Supplier<AbstractGeometricFractal<?>>> fractalSuppliers) {
		FractalPanel fractalPanel = new FractalPanel();
		JComboBox<Supplier<AbstractGeometricFractal<?>>> comboBox = getComboBox(fractalSuppliers);

		JFrame frame = new JFrame("Geometric Fractals");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
		List<Supplier<AbstractGeometricFractal<?>>> fractalSuppliers = new ArrayList<>();
		GeometricFractalRegistry.loadAllFractals();
		fractalSuppliers.addAll(GeometricFractalRegistry.getAll().values());

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(fractalSuppliers);
			}
		});
	}
}

