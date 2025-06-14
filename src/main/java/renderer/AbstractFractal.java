package renderer;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public abstract class AbstractFractal implements Drawable {
	public AbstractFractal() {
		init();
	}

	protected abstract void init();
	public abstract void reset();
	public abstract void step();

	public static int getSuggestedFPS() {
		return 60;
	}

	public static int getSuggestedUPS() {
		return 1;
	}

	public static JFrame display(Drawable fractal, int width, int height, int fps, int ups) {
		System.out.println(fractal);
		System.out.println("width: " + width);
		System.out.println("height: " + height);
		System.out.println("fps: " + fps);
		System.out.println("ups: " + ups);
		JFrame frame = new JFrame(fractal.toString());
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(width, height));

		FractalPanel fractalPanel = new FractalPanel(width, height, fps, ups);
		fractalPanel.addDrawable(fractal);
		frame.add(fractalPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);

		frame.addWindowListener(
			new WindowAdapter() {
				public void windowOpened(WindowEvent e) {
					new SwingWorker<Void, Void>() {
						protected Void doInBackground() throws Exception {
							fractalPanel.start();
							return null;
						}
					}.execute();
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
		return frame;
	}
}
