package renderer.painter;

import renderer.viewer.WorldViewer;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.util.Arrays;

public class LinearGradientPainter extends Painter {
	protected float startX, startY;
	protected float endX, endY;
	protected float[] fractions;
	protected Color[] colors;
	protected CycleMethod cycleMethod;

	public LinearGradientPainter(
		float startX, float startY, float endX, float endY,
		float[] fractions, Color[] colors
	) {
		this(startX, startY, endX, endY, fractions, colors, CycleMethod.NO_CYCLE);
	}

	public LinearGradientPainter(
		float startX, float startY, float endX, float endY,
		float[] fractions, Color[] colors, CycleMethod cycleMethod
	) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.fractions = fractions;
		this.colors = colors;
		this.cycleMethod = cycleMethod;
	}

	public LinearGradientPainter(
		Point2D start, Point2D end, float[] fractions, Color[] colors
	) {
		this(start, end, fractions, colors, CycleMethod.NO_CYCLE);
	}

	public LinearGradientPainter(
		Point2D start, Point2D end, float[] fractions,
		Color[] colors, CycleMethod cycleMethod
	) {
		this(
			(float) start.getX(),
			(float) start.getY(),
			(float) end.getX(),
			(float) end.getY(),
			fractions,
			colors,
			cycleMethod
		);
	}

	protected static Paint getPaint(
		float x1, float y1, float x2, float y2,
		float[] fractions, Color[] colors, CycleMethod cycleMethod
	) {
		return new LinearGradientPaint(x1, y1, x2, y2, fractions, colors, cycleMethod);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(startX),
			worldViewer.worldToScreenY(startY),
			worldViewer.worldToScreenX(endX),
			worldViewer.worldToScreenY(endY),
			fractions,
			colors,
			cycleMethod
		);
	}

	public String toString() {
		return String.format(
			"LinearGradientPainter[%n" +
			"  start      (%f, %f)%n" +
			"  end        (%f, %f)%n" +
			"  fractions  %s%n" +
			"  colors     %s%n" +
			"  cycle      %s%n" +
			"]",
			startX, startY, endX, endY,
			Arrays.toString(fractions),
			Arrays.toString(colors),
			cycleMethod
		);
	}
}

