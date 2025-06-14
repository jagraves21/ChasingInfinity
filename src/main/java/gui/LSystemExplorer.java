package gui;

import gui.lsystem.panels.AxiomPanel;
import gui.lsystem.panels.ProductionRulesPanel;
import gui.lsystem.panels.TurtleParametersPanel;
import gui.lsystem.panels.SimulationParametersPanel;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import renderer.FractalPanel;

import java.util.Map;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class LSystemExplorer extends JFrame {
	public static final long serialVersionUID = 1L;

	protected AxiomPanel axiomPanel;
	protected ProductionRulesPanel productionRulesPanel;
	protected TurtleParametersPanel turtleParametersPanel;
	protected SimulationParametersPanel simulationParametersPanel;
	protected FractalPanel fractalPanel;

	public LSystemExplorer() {
		initializeComponents();
		attachListeners();
		layoutComponents();

		setTitle("L-System Fractal Explorer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null); // center on screen
	}

	protected void initializeComponents() {
		axiomPanel = new AxiomPanel();
		productionRulesPanel = new ProductionRulesPanel();
		turtleParametersPanel = new TurtleParametersPanel();
		simulationParametersPanel = new SimulationParametersPanel();
		simulationParametersPanel.getStopButton().setEnabled(false);

		fractalPanel = new FractalPanel();
	}

	protected void attachListeners() {
		simulationParametersPanel.getStartButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startAnimation();
			}
		});

		simulationParametersPanel.getStopButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopAnimation();
			}
		});

	}

	protected void layoutComponents() {
		setLayout(new BorderLayout());

		JPanel westPanel = new JPanel();
		westPanel.setLayout(new javax.swing.BoxLayout(westPanel, javax.swing.BoxLayout.Y_AXIS));
		westPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		westPanel.add(axiomPanel);
		westPanel.add(productionRulesPanel);
		add(new JScrollPane(westPanel), BorderLayout.WEST);

		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new javax.swing.BoxLayout(eastPanel, javax.swing.BoxLayout.Y_AXIS));
		eastPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		eastPanel.add(simulationParametersPanel);
		eastPanel.add(turtleParametersPanel);
		add(new JScrollPane(eastPanel), BorderLayout.EAST);

		add(fractalPanel, BorderLayout.CENTER);
	}

	private void setEnableFieldsRecursive(Component component, boolean enabled) {
		component.setEnabled(enabled);
		if (component instanceof java.awt.Container) {
			for (Component child : ((Container) component).getComponents()) {
				setEnableFieldsRecursive(child, enabled);
			}
		}
	}

	private void setEnableFields(boolean enabled) {
		final Component[] fields = new Component[] {
			axiomPanel,
			productionRulesPanel,
			turtleParametersPanel,
			simulationParametersPanel
		};
		for (Component component : fields) {
			setEnableFieldsRecursive(component, enabled);
		}
	}

	private void startAnimation() {
		setEnableFields(false);
		try {
			String axiom = axiomPanel.getAxiom();
			Map<Character,String> productionRules = productionRulesPanel.getProductionRules();
			int iterations = simulationParametersPanel.getIterations();
			int fps = simulationParametersPanel.getFPS();
			int ups = simulationParametersPanel.getUPS();
			boolean reset = simulationParametersPanel.isResetSelected();
			TurtleState turtle = turtleParametersPanel.getTurtle();

			AbstractLSystemFractal fractal = new AbstractLSystemFractal(
				iterations, reset, turtle
			) {
				public String getAxiom() {
					return axiom;
				}
			};
			for (Map.Entry<Character, String> entry : productionRules.entrySet()) {
				fractal.addRule(entry.getKey(), entry.getValue());
			}

			fractalPanel.clearDrawables();
			fractalPanel.setFPS(fps);
			fractalPanel.setUPS(ups);
			fractalPanel.moveViewTo(0, 0);
			fractalPanel.setZoom(1.0);
			fractalPanel.addDrawable(fractal);

			new SwingWorker<Void, Void>() {
				protected Void doInBackground() {
					fractalPanel.start();
					return null;
				}
				protected void done() {
					simulationParametersPanel.getStopButton().setEnabled(true);
				}
			}.execute();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(
				LSystemExplorer.this,
				"Error: " + ex.getMessage(),
				"Input Error",
				JOptionPane.ERROR_MESSAGE
			);
			setEnableFields(true);
			simulationParametersPanel.getStopButton().setEnabled(false);
		}
	}

	private void stopAnimation() {
		setEnableFields(false);
		new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				fractalPanel.stop();
				return null;
			}

			protected void done() {
				setEnableFields(true);
				simulationParametersPanel.getStopButton().setEnabled(false);
			}
		}.execute();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			LSystemExplorer gui = new LSystemExplorer();
			gui.setVisible(true);
		});
	}
}

