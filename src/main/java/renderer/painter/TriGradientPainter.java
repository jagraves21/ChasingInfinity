package renderer.painter;

import renderer.paint.TriGradientPaint;
import renderer.viewer.WorldViewer;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class TriGradientPainter extends Painter {
	protected float x1, y1;
	protected float x2, y2;
	protected float x3, y3;
	protected Color c1, c2, c3;

	public TriGradientPainter(
		float x1, float y1, Color c1,
		float x2, float y2, Color c2,
		float x3, float y3, Color c3
	) {
		this.x1 = x1;
		this.y1 = y1;
		this.c1 = c1;
		this.x2 = x2;
		this.y2 = y2;
		this.c2 = c2;
		this.x3 = x3;
		this.y3 = y3;
		this.c3 = c3;
	}

	public TriGradientPainter(
		Point2D p1, Color c1,
		Point2D p2, Color c2,
		Point2D p3, Color c3
	) {
		this(
			(float) p1.getX(), (float) p1.getY(), c1,
			(float) p2.getX(), (float) p2.getY(), c2,
			(float) p3.getX(), (float) p3.getY(), c3
		);
	}

	protected static Paint getPaint(
		float x1, float y1, Color c1,
		float x2, float y2, Color c2,
		float x3, float y3, Color c3
	) {
		return new TriGradientPaint(
			new Point2D.Float(x1, y1), c1,
			new Point2D.Float(x2, y2), c2,
			new Point2D.Float(x3, y3), c3
		);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(x1), worldViewer.worldToScreenY(y1), c1,
			worldViewer.worldToScreenX(x2), worldViewer.worldToScreenY(y2), c2,
			worldViewer.worldToScreenX(x3), worldViewer.worldToScreenY(y3), c3
		);
	}

	@Override
	public String toString() {
		return String.format(
			"TriGradientPainter[%n" +
			"  p1         (%f, %f) color=%s%n" +
			"  p2         (%f, %f) color=%s%n" +
			"  p3         (%f, %f) color=%s%n" +
			"]",
			x1, y1, c1,
			x2, y2, c2,
			x3, y3, c3
		);
	}
}

