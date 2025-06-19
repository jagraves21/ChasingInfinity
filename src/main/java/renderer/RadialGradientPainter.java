package renderer;

import renderer.viewer.WorldViewer;

import java.util.Arrays;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import java.awt.MultipleGradientPaint.CycleMethod;

public class RadialGradientPainter extends Painter {
	public final float centerX, centerY;
	public final float radius;
	public final float focusX, focusY;
	public final float[] fractions;
	public final Color[] colors;
	public final CycleMethod cycleMethod;

	public RadialGradientPainter(
		float centerX, float centerY, float radius,
		float[] fractions, Color[] colors
	) {
		this(centerX, centerY, radius, centerX, centerY, fractions, colors, CycleMethod.NO_CYCLE);
	}

	public RadialGradientPainter(
		float centerX, float centerY, float radius,
		float[] fractions, Color[] colors,
		CycleMethod cycleMethod
	) {
		this(centerX, centerY, radius, centerX, centerY, fractions, colors, cycleMethod);
	}

	public RadialGradientPainter(
		float centerX, float centerY, float radius,
		float focusX, float focusY,
		float[] fractions, Color[] colors,
		CycleMethod cycleMethod
	) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.focusX = focusX;
		this.focusY = focusY;
		this.fractions = fractions.clone();
		this.colors = colors.clone();
		this.cycleMethod = cycleMethod;
	}

	protected static Paint getPaint(
		float centerX, float centerY, float radius,
		float focusX, float focusY,
		float[] fractions, Color[] colors,
		CycleMethod cycleMethod
	) {
		return new RadialGradientPaint(
			new Point2D.Float(centerX, centerY),
			radius,
			new Point2D.Float(focusX, focusY),
			fractions,
			colors,
			cycleMethod
		);
	}

	@Override
	public Paint getPaint() {
		return getPaint(centerX, centerY, radius, focusX, focusY, fractions, colors, cycleMethod);
	}

	@Override
	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(centerX),
			worldViewer.worldToScreenY(centerY),
			(float) (radius * worldViewer.getZoom()),
			worldViewer.worldToScreenX(focusX),
			worldViewer.worldToScreenY(focusY),
			fractions,
			colors,
			cycleMethod
		);
	}

	public String toString() {
		return String.format(
			"RadialGradientPainter[center=(%f, %f), radius=%f, focus=(%f, %f), fractions=%s, colors=%s, cycle=%s]",
			centerX, centerY, radius, focusX, focusY,
			Arrays.toString(fractions),
			Arrays.toString(colors),
			cycleMethod
		);
	}
}

