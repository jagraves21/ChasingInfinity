package gui.lsystem.panels;

import gui.components.SingleCharValidatedTextField;

import java.util.HashMap;
import java.util.Map;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.border.TitledBorder;

public class ProductionRulesPanel extends JPanel {
	public static final long serialVersionUID = 1L;

	public static final int MAX_RULES = 10;
	public static final int RULE_KEY_COLUMNS = 3;
	public static final int RULE_VALUE_COLUMNS = 10;

	protected SingleCharValidatedTextField[] ruleKeyFields;
	protected JTextField[] ruleValueFields;

	public ProductionRulesPanel() {
		initializeComponents();
		setDefaults();

		setLayout(new GridBagLayout());
		setTitledBorder("Production Rules");
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
		ruleKeyFields = new SingleCharValidatedTextField[MAX_RULES];
		ruleValueFields = new JTextField[MAX_RULES];

		Font monoFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		for (int ii=0; ii < MAX_RULES; ii++) {
			ruleKeyFields[ii] = new SingleCharValidatedTextField(RULE_KEY_COLUMNS);
			ruleKeyFields[ii].setToolTipText("Predecessor");
			ruleKeyFields[ii].setFont(monoFont);

			ruleValueFields[ii] = new JTextField(RULE_VALUE_COLUMNS);
			ruleValueFields[ii].setToolTipText("Successor");
			ruleValueFields[ii].setFont(monoFont);
		}
	}

	protected void setDefaults() {
		ruleKeyFields[0].setText("X");
		ruleValueFields[0].setText("X+YF+");

		ruleKeyFields[1].setText("Y");
		ruleValueFields[1].setText("-FX-Y");
	}

	protected int addRuleRow(JTextField keyField, JTextField valueField, GridBagConstraints gbc, int row) {
		gbc.gridy = row;

		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(keyField, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		add(new JLabel("→"), gbc);

		gbc.gridx = 2;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(valueField, gbc);

		return row + 1;
	}

	protected void layoutComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int row = 0;
		for (int ii = 0; ii < MAX_RULES; ii++) {
			row = addRuleRow(ruleKeyFields[ii], ruleValueFields[ii], gbc, row);
		}

		gbc.gridy = row;
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.PAGE_START;
		add(new JPanel(), gbc);
	}

	public SingleCharValidatedTextField[] getRuleKeyFields() {
		return ruleKeyFields;
	}

	public JTextField[] getRuleValueFields() {
		return ruleValueFields;
	}

	public Map<Character, String> getProductionRules() {
		Map<Character, String> rules = new HashMap<>();
		for (int ii = 0; ii < MAX_RULES; ii++) {
			String keyText = ruleKeyFields[ii].getText().trim();
			String valueText = ruleValueFields[ii].getText().trim();

			if (keyText.isEmpty() && !valueText.isEmpty()) {
				throw new IllegalArgumentException(
					String.format("Rule %d has a successor without a predecessor: %s", (ii+1), valueText)
				);
			}

			if (!keyText.isEmpty()) {
				if (keyText.length() != 1) {
					throw new IllegalArgumentException(
						String.format("Rule %d predecessor must be a single character: %s", (ii+1), keyText)
					);
				}
				char key = keyText.charAt(0);
				rules.put(key, valueText);
			}
		}

		return rules;
	}
}
