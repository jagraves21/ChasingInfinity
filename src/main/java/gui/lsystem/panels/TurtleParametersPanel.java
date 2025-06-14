package gui.lsystem.panels;

import gui.components.DoubleValidatedTextField;

import lsystem.TurtleState;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.border.TitledBorder;

public class TurtleParametersPanel extends JPanel {
	public static final long serialVersionUID = 1L;

	public static final int TEXT_COLUMNS = 6;

	public static final double DEFAULT_X = 0.0;
	public static final double DEFAULT_Y = 0.0;
	public static final double DEFAULT_ANGLE = 0.0;
	public static final double DEFAULT_LINE_LENGTH = 10.0;
	public static final double DEFAULT_LINE_WIDTH = 1.0;
	public static final double DEFAULT_TURNING_ANGLE = 90.0;
	public static final boolean DEFAULT_SWAP_TURN_DIRS = false;
	public static final double DEFAULT_ANGLE_SCALE = 2.0;
	public static final double DEFAULT_ANGLE_SHIFT = 1.0;
	public static final double DEFAULT_LINE_LENGTH_SCALE = 2.0;
	public static final double DEFAULT_LINE_LENGTH_SHIFT = 1.0;
	public static final double DEFAULT_LINE_WIDTH_SCALE = 2.0;
	public static final double DEFAULT_LINE_WIDTH_SHIFT = 1.0;
	public static final double REVERSE_DIRECTION_ANGLE = 180.0;

	protected DoubleValidatedTextField xField;
	protected DoubleValidatedTextField yField;
	protected DoubleValidatedTextField angleField;
	protected DoubleValidatedTextField lineLengthField;
	protected DoubleValidatedTextField lineWidthField;

	protected DoubleValidatedTextField turningAngleField;
	protected JCheckBox swapTurnDirsCheck;

	protected DoubleValidatedTextField angleScaleField;
	protected DoubleValidatedTextField angleShiftField;

	protected DoubleValidatedTextField lineLengthScaleField;
	protected DoubleValidatedTextField lineLengthShiftField;

	protected DoubleValidatedTextField lineWidthScaleField;
	protected DoubleValidatedTextField lineWidthShiftField;

	protected JButton resetButton;

	public TurtleParametersPanel() {
		initializeComponents();
		setDefaults();

		setLayout(new GridBagLayout());
		setTitledBorder("Turtle Parameters");
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
		xField = new DoubleValidatedTextField(TEXT_COLUMNS);
		yField = new DoubleValidatedTextField(TEXT_COLUMNS);
		angleField = new DoubleValidatedTextField(TEXT_COLUMNS);
		lineLengthField = new DoubleValidatedTextField(TEXT_COLUMNS);
		lineWidthField = new DoubleValidatedTextField(TEXT_COLUMNS);
		turningAngleField = new DoubleValidatedTextField(TEXT_COLUMNS);

		swapTurnDirsCheck = new JCheckBox("Swap +/- Directions");
		swapTurnDirsCheck.setSelected(DEFAULT_SWAP_TURN_DIRS);

		angleScaleField = new DoubleValidatedTextField(TEXT_COLUMNS);
		angleShiftField = new DoubleValidatedTextField(TEXT_COLUMNS);
		lineLengthScaleField = new DoubleValidatedTextField(TEXT_COLUMNS);
		lineLengthShiftField = new DoubleValidatedTextField(TEXT_COLUMNS);
		lineWidthScaleField = new DoubleValidatedTextField(TEXT_COLUMNS);
		lineWidthShiftField = new DoubleValidatedTextField(TEXT_COLUMNS);

		resetButton = new JButton("Reset Defaults");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDefaults();
			}
		});
	}

	protected void setDefaults() {
		xField.setText(Double.toString(DEFAULT_X));
		yField.setText(Double.toString(DEFAULT_Y));
		angleField.setText(Double.toString(DEFAULT_ANGLE));
		lineLengthField.setText(Double.toString(DEFAULT_LINE_LENGTH));
		lineWidthField.setText(Double.toString(DEFAULT_LINE_WIDTH));
		turningAngleField.setText(Double.toString(DEFAULT_TURNING_ANGLE));
		swapTurnDirsCheck.setSelected(DEFAULT_SWAP_TURN_DIRS);
		angleScaleField.setText(Double.toString(DEFAULT_ANGLE_SCALE));
		angleShiftField.setText(Double.toString(DEFAULT_ANGLE_SHIFT));
		lineLengthScaleField.setText(Double.toString(DEFAULT_LINE_LENGTH_SCALE));
		lineLengthShiftField.setText(Double.toString(DEFAULT_LINE_LENGTH_SHIFT));
		lineWidthScaleField.setText(Double.toString(DEFAULT_LINE_WIDTH_SCALE));
		lineWidthShiftField.setText(Double.toString(DEFAULT_LINE_WIDTH_SHIFT));
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
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int row = 0;
		row = addLabeledField("X Position:", xField, gbc, row);
		row = addLabeledField("Y Position:", yField, gbc, row);
		row = addLabeledField("Angle (degrees):", angleField, gbc, row);
		row = addLabeledField("Line Length:", lineLengthField, gbc, row);
		row = addLabeledField("Line Width:", lineWidthField, gbc, row);
		row = addLabeledField("Turning Angle:", turningAngleField, gbc, row);

		row = addLeftAlignedComponent(swapTurnDirsCheck, gbc, row, 2);

		row = addLabeledField("Angle Scale:", angleScaleField, gbc, row);
		row = addLabeledField("Angle Shift:", angleShiftField, gbc, row);
		row = addLabeledField("Line Length Scale:", lineLengthScaleField, gbc, row);
		row = addLabeledField("Line Length Shift:", lineLengthShiftField, gbc, row);
		row = addLabeledField("Line Width Scale:", lineWidthScaleField, gbc, row);
		row = addLabeledField("Line Width Shift:", lineWidthShiftField, gbc, row);

		row = addCenteredComponent(resetButton, gbc, row, 2);

		gbc.gridy = row;
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.PAGE_START;
		add(new JPanel(), gbc);
	}

	protected double getValue(String variableNmae, JTextField textField) throws NumberFormatException {
		try {
			return Double.parseDouble(textField.getText().trim());
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(
				String.format("Invalid double for %s: %s", variableNmae, textField.getText())
			);
		}
	}

	public TurtleState getTurtle() throws NumberFormatException {
		return new TurtleState.Builder()
			.setX(getValue("X Position", xField))
			.setY(getValue("Y Position", yField))
			.setAngle(getValue("Angle", angleField))
			.setLineLength(getValue("Line Length", lineLengthField))
			.setLineWidth(getValue("Line Width", lineWidthField))
			.setTurningAngle(getValue("Turning Angle", turningAngleField))
			.setSwapTurnDirs(swapTurnDirsCheck.isSelected())
			.setAngleScale(getValue("Angle Scale", angleScaleField))
			.setAngleShift(getValue("Angle Shift", angleShiftField))
			.setLineLengthScale(getValue("Line Length Scale", lineLengthScaleField))
			.setLineLengthShift(getValue("Line Length Shift", lineLengthShiftField))
			.setLineWidthScale(getValue("Line Width Scale", lineWidthScaleField))
			.setLineWidthShift(getValue("Line Width Shift", lineWidthShiftField))
			.build();
	}
}

