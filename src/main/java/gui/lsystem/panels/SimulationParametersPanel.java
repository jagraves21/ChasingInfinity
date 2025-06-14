package gui.lsystem.panels;

import gui.components.IntegerValidatedTextField;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.border.TitledBorder;

public class SimulationParametersPanel extends JPanel {
	public static final long serialVersionUID = 1L;

	public static final int FIELD_COLUMNS = 6;

	protected IntegerValidatedTextField fpsField;
	protected IntegerValidatedTextField upsField;
	protected IntegerValidatedTextField iterationsField;
	protected JCheckBox resetCheckBox;
	protected JButton startButton;
	protected JButton stopButton;

	public SimulationParametersPanel() {
		initializeComponents();
		setDefaults();

		setLayout(new GridBagLayout());
		setTitledBorder("Simulation Parameters");
		layoutComponents();
	}

	protected void setTitledBorder(String title) {
		JLabel sampleLabel = new JLabel();
		Font labelFont = sampleLabel.getFont();
		Font titleFont = labelFont.deriveFont(Font.BOLD, labelFont.getSize() + 2);

		TitledBorder border = BorderFactory.createTitledBorder(title);
		border.setTitleFont(titleFont);

		setBorder(border);
	}

	protected void initializeComponents() {
		fpsField = new IntegerValidatedTextField(FIELD_COLUMNS);
		upsField = new IntegerValidatedTextField(FIELD_COLUMNS);
		iterationsField = new IntegerValidatedTextField(FIELD_COLUMNS);
		resetCheckBox = new JCheckBox("Reset");
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
	}

	protected void setDefaults() {
		fpsField.setText("60");
		upsField.setText("1");
		iterationsField.setText("10");
		resetCheckBox.setSelected(false);
	}

	protected int addLabeledField(String label, JTextField field, GridBagConstraints gbc, int row) {
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(new JLabel(label), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(field, gbc);

		return row + 1;
	}

	protected int addLeftAlignedComponent(Component comp, GridBagConstraints gbc, int row, int colSpan) {
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = colSpan;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		add(comp, gbc);
		return row + 1;
	}

	protected int addButtons(JButton button1, JButton button2, GridBagConstraints gbc, int row) {
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(button1, gbc);

		gbc.gridx = 1;
		gbc.gridy = row;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(button2, gbc);

		return row + 1;
	}

	protected int addCenteredComponent(Component comp, GridBagConstraints gbc, int row, int colSpan) {
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = colSpan;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		add(comp, gbc);
		return row + 1;
	}

	protected void layoutComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int row = 0;
		row = addLabeledField("FPS:", fpsField, gbc, row);
		row = addLabeledField("UPS:", upsField, gbc, row);
		row = addLabeledField("Iterations:", iterationsField, gbc, row);
		row = addLeftAlignedComponent(resetCheckBox, gbc, row, 2);

		row = addButtons(startButton, stopButton, gbc, row);

		gbc.gridy = row;
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.PAGE_START;
		add(new JPanel(), gbc);
	}

	public int getFPS() throws NumberFormatException {
		return Integer.parseInt(fpsField.getText().trim());
	}

	public int getUPS() throws NumberFormatException {
		return Integer.parseInt(upsField.getText().trim());
	}

	public int getIterations() throws NumberFormatException {
		return Integer.parseInt(iterationsField.getText().trim());
	}

	public boolean isResetSelected() {
		return resetCheckBox.isSelected();
	}

	public JButton getStartButton() {
		return startButton;
	}

	public JButton getStopButton() {
		return stopButton;
	}
}

