package gui.components;

import java.awt.Color;

import javax.swing.JTextField;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.text.Document;


public abstract class ValidatedTextField extends JTextField {
	public static final long serialVersionUID = 1L;

	public static final Color INVALID_COLOR = new Color(255, 204, 204); // a soft red
	public static final Color VALID_COLOR = Color.WHITE;

	public ValidatedTextField() {
		super();
		init();
	}

	public ValidatedTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		init();
	}

	public ValidatedTextField(int columns) {
		super(columns);
		init();
	}

	public ValidatedTextField(String text) {
		super(text);
		init();
	}

	public ValidatedTextField(String text, int columns) {
		super(text, columns);
		init();
	}

	protected void init() {
		setupValidationListener();
		validateAndUpdateUI();
	}

	protected void setupValidationListener() {
		this.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent de) {
				validateAndUpdateUI();
			}

			public void removeUpdate(DocumentEvent de) {
				validateAndUpdateUI();
			}

			public void changedUpdate(DocumentEvent de) {
				validateAndUpdateUI();
			}
		});
	}

	protected void validateAndUpdateUI() {
		ValidationResult result = validateInput(getText());
		if (result.isValid()) {
			setBackground(VALID_COLOR);
			setToolTipText(null);
		} else {
			setBackground(INVALID_COLOR);
			setToolTipText(result.getErrorMessage());
		}
	}

	protected abstract ValidationResult validateInput(String input);

	public static class ValidationResult {
		private final boolean valid;
		private final String errorMessage;

		public ValidationResult(boolean valid, String errorMessage) {
			this.valid = valid;
			this.errorMessage = errorMessage;
		}

		public boolean isValid() {
			return valid;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public static ValidationResult valid() {
			return new ValidationResult(true, null);
		}

		public static ValidationResult invalid(String message) {
			return new ValidationResult(false, message);
		}
	}
}

