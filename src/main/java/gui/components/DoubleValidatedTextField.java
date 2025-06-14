package gui.components;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.text.Document;

public class DoubleValidatedTextField extends ValidatedTextField {
	public static final long serialVersionUID = 1L;

	public DoubleValidatedTextField() {
        super();
    }

    public DoubleValidatedTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
    }

    public DoubleValidatedTextField(int columns) {
        super(columns);
    }

    public DoubleValidatedTextField(String text) {
        super(text);
    }

    public DoubleValidatedTextField(String text, int columns) {
        super(text, columns);
    }

	protected ValidationResult validateInput(String input) {
		if (input == null || input.trim().isEmpty()) {
			return ValidationResult.invalid("Input cannot be empty");
		}
		try {
			Double.parseDouble(input.trim());
			return ValidationResult.valid();
		} catch (NumberFormatException nfe) {
			return ValidationResult.invalid("Invalid number format");
		}
	}
}

