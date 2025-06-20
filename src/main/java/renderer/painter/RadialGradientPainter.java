package renderer.painter;

import renderer.viewer.WorldViewer;

import java.util.Arrays;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import java.awt.MultipleGradientPaint.CycleMethod;

public class RadialGradientPainter extends Painter {
	protected float cx, cy;
	protected float radius;
	protected float fx, fy;
	protected float[] fractions;
	protected Color[] colors;
	protected CycleMethod cycleMethod;

	public RadialGradientPainter(
		float cx, float cy, float radius, float[] fractions, Color[] colors
	) {
		this(cx, cy, radius, fractions, colors, CycleMethod.NO_CYCLE);
	}
	
	public RadialGradientPainter(
		float cx, float cy, float radius, float[] fractions,
		Color[] colors, CycleMethod cycleMethod
	) {
		this(cx, cy, radius, cx, cy, fractions, colors, cycleMethod);
	}
	
	public RadialGradientPainter(
		float cx, float cy, float radius, float fx, float fy,
		float[] fractions, Color[] colors, CycleMethod cycleMethod
	) {
		this.cx = cx; this.cy = cy;
		this.radius = radius;
		this.fx = fx; this.fy = fy;
		this.fractions = fractions;
		this.colors = colors;
		this.cycleMethod = cycleMethod;
	}
	
	public RadialGradientPainter(
		Point2D center, float radius, float[] fractions, Color[] colors
	) {
		this(center, radius, fractions, colors, CycleMethod.NO_CYCLE);
	}
	
	public RadialGradientPainter(
		Point2D center, float radius, float[] fractions,
		Color[] colors, CycleMethod cycleMethod
	) {
		this(center, radius, center, fractions, colors, cycleMethod);
	}
	
	public RadialGradientPainter(
		Point2D center, float radius, Point2D focus,
		float[] fractions, Color[] colors, CycleMethod cycleMethod
	) {
		this(
			(float) center.getX(),
			(float) center.getY(),
			radius,
			(float) focus.getX(),
			(float) focus.getY(),
			fractions,
			colors,
			cycleMethod
		);
	}
	
	protected static Paint getPaint(
		float centerX, float centerY,
		float radius,
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

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(cx),
			worldViewer.worldToScreenY(cy),
			(float) (radius * worldViewer.getZoom()),
			worldViewer.worldToScreenX(fx),
			worldViewer.worldToScreenY(fy),
			fractions,
			colors,
			cycleMethod
		);
	}

	public String toString() {
		return String.format(
			"RadialGradientPainter[%n" + 
			"  center      (%f, %f)%n" + 
			"  radius      %f%n" +
			"  focus       (%f, %f)%n" + 
			"  fractions   %s%n" +
			"  colors      %s%n" +
			"  cycle       %s%n" +
			"]",
			cx, cy, radius, fx, fy,
			Arrays.toString(fractions),
			Arrays.toString(colors),
			cycleMethod
		);
	}
}

