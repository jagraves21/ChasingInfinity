package geometric.utils;

import geometric.Point;

import renderer.painter.ColorPainter;
import renderer.painter.LinearGradientPainter;
import renderer.painter.Painter;
import renderer.painter.RadialGradientPainter;
import renderer.painter.TriGradientPainter;

import java.awt.Color;
import java.awt.MultipleGradientPaint.CycleMethod;

public class PaintFactory {

	public static float[] generateFractions(Color[] colors) {
		if (colors.length < 2) {
			throw new IllegalArgumentException("At least two colors are required to generate fractions.");
		}
		float[] fractions = new float[colors.length];
		for (int i = 0; i < colors.length; i++) {
			fractions[i] = (float) i / (colors.length - 1);
		}
		return fractions;
	}

	public static Painter getSolidPainter(Color color) {
		return new ColorPainter(color);
	}

	public static LinearGradientPainter getLinearPainter(
		Point start, Point end, Color color1, Color color2
	) {
		return getLinearPainter(
			start, end, new Color[]{color1, color2}
		);
	}

	public static LinearGradientPainter getLinearPainter(
		Point start, Point end, Color[] colors
	) {
		return getLinearPainter(
			start, end, generateFractions(colors), colors
		);
	}

	public static LinearGradientPainter getLinearPainter(
		Point start, Point end, float[] fractions, Color[] colors
	) {
		return getLinearPainter(
			start, end, fractions, colors, CycleMethod.NO_CYCLE
		);
	}

	public static LinearGradientPainter getLinearPainter(
		Point start, Point end, float[] fractions,
		Color[] colors, CycleMethod cycleMethod
	) {
		return new LinearGradientPainter(
			start.toPoint2DFloat(),
			end.toPoint2DFloat(),
			fractions,
			colors,
			cycleMethod
		);
	}

	public static RadialGradientPainter getRadialPainter(
		Point center, Point pointOnCircle, Color color1, Color color2
	) {
		return getRadialPainter(
			center, pointOnCircle,
			new Color[]{color1, color2}
		);
	}

	public static RadialGradientPainter getRadialPainter(
		Point center, Point pointOnCircle, Color[] colors
	) {
		return getRadialPainter(
			center, pointOnCircle, generateFractions(colors), colors
		);
	}

	public static RadialGradientPainter getRadialPainter(
		Point center, Point pointOnCircle, float[] fractions, Color[] colors
	) {
		return getRadialPainter(
			center, pointOnCircle, fractions, colors, CycleMethod.NO_CYCLE
		);
	}

	public static RadialGradientPainter getRadialPainter(
		Point center, Point pointOnCircle, float[] fractions, Color[] colors, CycleMethod cycleMethod
	) {
		return new RadialGradientPainter(
			center.toPoint2DFloat(),
			(float) center.distance(pointOnCircle),
			fractions,
			colors,
			cycleMethod
		);
	}

	public static TriGradientPainter getTriGradientPainter(
		Point p1, Color c1, Point p2, Color c2, Point p3, Color c3
	) {
		return new TriGradientPainter(
			p1.toPoint2DFloat(), c1,
			p2.toPoint2DFloat(), c2,
			p3.toPoint2DFloat(), c3
		);
	}
}

