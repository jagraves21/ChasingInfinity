package gui;

import renderer.AbstractFractal;
import renderer.FractalPanel;

import java.util.ArrayList;
import java.util.List;

import java.util.function.Supplier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class FractalViewerApp {
	public static final String DEFAULT_TITLE = "Fractal Viewer";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	/*public static void setMacDockIcon(Image image) {
		try {
			if (System.getProperty("os.name").toLowerCase().contains("mac")) {
				Class<?> appClass = Class.forName("com.apple.eawt.Application");
				Method getApp = appClass.getMethod("getApplication");
				Object app = getApp.invoke(null);
				Method setIcon = appClass.getMethod("setDockIconImage", java.awt.Image.class);
				setIcon.invoke(app, image);
			}
		} catch (Exception e) {
			System.err.println("Failed to set macOS Dock icon: " + e.getMessage());
		}
	}*/

	protected static int getFPS(AbstractFractal fractal) {
		try {
			return (int) fractal.getClass().getMethod("getSuggestedFPS").invoke(null);
		} catch (Exception e) {
			return 30;
		}
	}

	protected static int getUPS(AbstractFractal fractal) {
		try {
			return (int) fractal.getClass().getMethod("getSuggestedUPS").invoke(null);
		} catch (Exception e) {
			return 1;
		}
	}

	protected static JComponent getContentPane(
		FractalPanel fractalPanel,
		JComboBox<Supplier<AbstractFractal>> comboBox
	) {
		// reference to the currently instantiated fractal
		// this is needed for the resetCheck functionality
		final AbstractFractal[] currentFractal = new AbstractFractal[1];

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(comboBox, BorderLayout.CENTER);

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				Supplier<AbstractFractal> selectedSupplier =
					(Supplier<AbstractFractal>) comboBox.getSelectedItem();
				if (selectedSupplier == null) return;

				comboBox.setEnabled(false);

				new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						fractalPanel.stop();
						return null;
					}

					protected void done() {
						AbstractFractal fractal = selectedSupplier.get();
						currentFractal[0] = fractal;

						fractal.reset();
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

	protected static JComboBox<Supplier<AbstractFractal>> getComboBox(
		List<Supplier<AbstractFractal>> fractalSuppliers
	) {
		JComboBox<Supplier<AbstractFractal>> comboBox = new JComboBox<>();
		for (Supplier<AbstractFractal> supplier : fractalSuppliers) {
			final String supplierName = getSupplierName(supplier);
			comboBox.addItem(new Supplier<AbstractFractal>() {
				public AbstractFractal get() {
					return supplier.get();
				}

				public String toString() {
					return supplierName;
				}
			});
		}

		return comboBox;
	}
	
	protected static void createAndShowGUI(
		String title,
		List<Supplier<AbstractFractal>> fractalSuppliers,
		List<Image> icons
	) {
		FractalPanel fractalPanel = new FractalPanel();
		JComboBox<Supplier<AbstractFractal>> comboBox = getComboBox(fractalSuppliers);

		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setIconImages(icons);
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
	
	public static void startGUI(List<Supplier<AbstractFractal>> fractalSuppliers) {
		startGUI(fractalSuppliers, DEFAULT_TITLE);
	}
	
	protected static void startGUI(
		List<Supplier<AbstractFractal>> fractalSuppliers,
		String title
	) {
		startGUI(fractalSuppliers, title, new ArrayList<>());
	}
	
	protected static void startGUI(
		List<Supplier<AbstractFractal>> fractalSuppliers,
		List<Image> icons
	) {
		startGUI(fractalSuppliers, DEFAULT_TITLE, new ArrayList<>());
	}
	
	protected static void startGUI(
		List<Supplier<AbstractFractal>> fractalSuppliers,
		String title,
		List<Image> icons
	) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(title, fractalSuppliers, icons);
			}
		});
	}
}

