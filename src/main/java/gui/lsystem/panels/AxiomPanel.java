package gui.lsystem.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class AxiomPanel extends JPanel {
	public static final long serialVersionUID = 1L;

	public static final int TEXT_COLUMNS = 6;

	protected JTextField axiomField;

	public AxiomPanel() {
		initializeComponents();
		setDefaults();

		setLayout(new GridBagLayout());
		setTitledBorder("Axiom");
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
		axiomField = new JTextField(TEXT_COLUMNS);
	}

	protected void setDefaults() {
		axiomField.setText("FX");
	}

	protected int addLabeledField(String label, JTextField field, GridBagConstraints gbc, int row) {
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		add(new JLabel(label), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		add(field, gbc);

		return row + 1;
	}

	protected void layoutComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int row = 0;
		row = addLabeledField("Axiom:", axiomField, gbc, row);

		gbc.gridy = row;
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.PAGE_START;
		add(new JPanel(), gbc);
	}

	public String getAxiom() {
		return axiomField.getText();
	}

	public void setAxiom(String axiom) {
		axiomField.setText(axiom);
	}
}

