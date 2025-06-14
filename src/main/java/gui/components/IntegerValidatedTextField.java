package gui.components;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.text.Document;

public class IntegerValidatedTextField extends ValidatedTextField {
	public static final long serialVersionUID = 1L;

	public IntegerValidatedTextField() {
        super();
    }

    public IntegerValidatedTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
    }

    public IntegerValidatedTextField(int columns) {
        super(columns);
    }

    public IntegerValidatedTextField(String text) {
        super(text);
    }

    public IntegerValidatedTextField(String text, int columns) {
        super(text, columns);
    }

	protected ValidationResult validateInput(String input) {
		if (input == null || input.trim().isEmpty()) {
			return ValidationResult.invalid("Input cannot be empty");
		}
		try {
			Integer.parseInt(input.trim());
			return ValidationResult.valid();
		} catch (NumberFormatException nfe) {
			return ValidationResult.invalid("Invalid integer format");
		}
	}
}

