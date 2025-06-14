package gui.components;

import javax.swing.text.Document;

public class SingleCharValidatedTextField extends ValidatedTextField {
	public static final long serialVersionUID = 1L;

	public SingleCharValidatedTextField() {
		super();
	}

	public SingleCharValidatedTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
	}

	public SingleCharValidatedTextField(int columns) {
		super(columns);
	}

	public SingleCharValidatedTextField(String text) {
		super(text);
	}

	public SingleCharValidatedTextField(String text, int columns) {
		super(text, columns);
	}

	protected ValidationResult validateInput(String input) {
		if (input == null || input.isEmpty()) {
			return ValidationResult.invalid("Input cannot be empty");
		}
		if (input.length() != 1) {
			return ValidationResult.invalid("Input must be exactly one character");
		}
		if (Character.isWhitespace(input.charAt(0))) {
			return ValidationResult.invalid("Whitespace is not allowed");
		}
		return ValidationResult.valid();
	}
}

