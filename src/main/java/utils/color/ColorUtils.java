package utils.color;

import java.awt.Color;

import java.util.Collections;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class ColorUtils {
	public static double rgbColorDistance(Color c1, Color c2) {
		return Math.sqrt(
			Math.pow(c1.getRed() - c2.getRed(), 2)
			+ Math.pow(c1.getGreen() - c2.getGreen(), 2)
			+ Math.pow(c1.getBlue() - c2.getBlue(), 2)
		);
	}

	public static double labColorDistance(Color c1, Color c2) {
		double[] lab1 = rgbToLab(c1);
		double[] lab2 = rgbToLab(c2);
		return Math.sqrt(
			Math.pow(lab1[0] - lab2[0], 2)
			+ Math.pow(lab1[1] - lab2[1], 2)
			+ Math.pow(lab1[2] - lab2[2], 2)
		);
	}

	public static double[] rgbToLab(Color color) {
		double[] xyz = rgbToXyz(color.getRed(), color.getGreen(), color.getBlue());
		return xyzToLab(xyz[0], xyz[1], xyz[2]);
	}

	public static double[] rgbToXyz(int r, int g, int b) {
		double[] rgb = new double[] { r / 255.0, g / 255.0, b / 255.0 };
		for (int i = 0; i < 3; i++) {
			rgb[i] = rgb[i] <= 0.04045
				? rgb[i] / 12.92
				: Math.pow((rgb[i] + 0.055) / 1.055, 2.4);
		}

		double x = rgb[0] * 0.4124 + rgb[1] * 0.3576 + rgb[2] * 0.1805;
		double y = rgb[0] * 0.2126 + rgb[1] * 0.7152 + rgb[2] * 0.0722;
		double z = rgb[0] * 0.0193 + rgb[1] * 0.1192 + rgb[2] * 0.9505;
		return new double[] { x * 100, y * 100, z * 100 };
	}

	public static double[] xyzToLab(double x, double y, double z) {
		double[] ref = { 95.047, 100.000, 108.883 };
		double[] xyz = { x / ref[0], y / ref[1], z / ref[2] };
		for (int i = 0; i < 3; i++) {
			xyz[i] = xyz[i] > 0.008856
				? Math.cbrt(xyz[i])
				: (7.787 * xyz[i]) + (16.0 / 116);
		}
		double l = (116 * xyz[1]) - 16;
		double a = 500 * (xyz[0] - xyz[1]);
		double b = 200 * (xyz[1] - xyz[2]);
		return new double[] { l, a, b };
	}

	public static List<String> getClosestNamedColors(Color target, int count) {
		return NamedColors.getNameToColorMap().entrySet().stream()
			.map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), labColorDistance(target, entry.getValue())))
			.sorted(Comparator.comparingDouble(Map.Entry::getValue))
			.limit(count)
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
	}

	public static List<String> getClosestNamedColors(String colorName, int count) {
		Color target = NamedColors.getColorByName(colorName);
		return getClosestNamedColors(target, count);
	}

	public static String getClosestNamedColor(Color target) {
		return getClosestNamedColors(target, 1).stream().findFirst().orElse(null);
	}

	public static String getClosestNamedColor(String colorName) {
		return getClosestNamedColors(colorName, 1).stream().findFirst().orElse(null);
	}

	public static double clamp(double value) {
		return Math.max(0.0, Math.min(1.0, value));
	}
	
	public static Color average(Color c1, Color c2) {
		return blend(c1, c2);
	}

	public static Color blend(Color c1, Color c2) {
		return blend(c1, c2, 0.5);
	}

	public static Color blend(Color c1, Color c2, double t) {
		double w2 = clamp(t);
		double w1 = 1 - w2;

		int red   = (int) Math.round(c1.getRed()   * w1 + c2.getRed()   * w2);
		int green = (int) Math.round(c1.getGreen() * w1 + c2.getGreen() * w2);
		int blue  = (int) Math.round(c1.getBlue()  * w1 + c2.getBlue()  * w2);
		int alpha = (int) Math.round(c1.getAlpha() * w1 + c2.getAlpha() * w2);

		return new Color(red, green, blue, alpha);
	}

	public static Color invert(Color color) {
		return new java.awt.Color(
			255 - color.getRed(),
			255 - color.getGreen(),
			255 - color.getBlue(),
			color.getAlpha()
		);
	}

	public static Color interpolateColor(Color[] colors, double position) {
		if (colors == null || colors.length == 0) {
			throw new IllegalArgumentException("Color array must not be empty.");
		}

		if (position <= 0f) return colors[0];
		if (position >= 1f) return colors[colors.length - 1];

		double scaledPos = position * (colors.length - 1);
		int index = (int) Math.floor(scaledPos);
		double t = scaledPos - index;
		Color c1 = colors[index];
		Color c2 = colors[index + 1];

		return blend(c1, c2, t);
	}

	public static Color[] generateGradient(Color[] colors, int steps) {
		if (colors == null || colors.length == 0) {
			throw new IllegalArgumentException("Color array must not be empty.");
		}
		if (steps < 2) {
			throw new IllegalArgumentException("Gradient must have at least 2 steps.");
		}

		Color[] gradient = new Color[steps];
		for (int ii = 0; ii < steps; ii++) {
			double position = (double) ii / (steps - 1);
			gradient[ii] = interpolateColor(colors, position);
		}
		return gradient;
	}
}

