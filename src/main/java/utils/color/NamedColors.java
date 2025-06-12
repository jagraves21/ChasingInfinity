package utils.color;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.awt.Color;

public class NamedColors extends BaseColors {
	public static boolean isNamedColor(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Color name cannot be null");
		}
		return nameToColor.containsKey(name.toUpperCase());
	}

	public static boolean isNamedColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}
		return colorToName.containsKey(color.getRGB());
	}

	public static Color getColorByName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Color name cannot be null");
		}
		Color color = nameToColor.get(name.toUpperCase());
		if (color == null) {
			throw new IllegalArgumentException("Unknown color name: " + name);
		}
		return color;
	}

	public static String getNameByColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}
		String name = colorToName.get(color.getRGB());
		if (name == null) {
			throw new IllegalArgumentException("Unknown color: " + color);
		}
		return name;
	}

	public static Map<String, Color> getNameToColorMap() {
		return Collections.unmodifiableMap(nameToColor);
	}
}
