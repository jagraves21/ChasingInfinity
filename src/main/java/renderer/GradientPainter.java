package renderer;

import renderer.viewer.CartesianWorldViewer;
import renderer.viewer.WorldViewer;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.geom.Point2D;

public class GradientPainter extends Painter {
	public final float x1, y1;
	public final Color color1;
	public final float x2, y2;
	public final Color color2;
	public final boolean cyclic;

	public GradientPainter(
		float x1, float y1, Color color1,
		float x2, float y2, Color color2
	) {
		this(x1, y1, color1, x2, y2, color2, false);
	}

	public GradientPainter(
		float x1, float y1, Color color1,
		float x2, float y2, Color color2,
		boolean cyclic
	) {
		this.x1 = x1;
		this.y1 = y1;
		this.color1 = color1;
		this.x2 = x2;
		this.y2 = y2;
		this.color2 = color2;
		this.cyclic = cyclic;
	}

	public GradientPainter(
			Point2D pt1, Color color1,
			Point2D pt2, Color color2
	) {
		this(pt1, color1, pt2, color2, false);
	}

	public GradientPainter(
			Point2D pt1, Color color1,
			Point2D pt2, Color color2,
			boolean cyclic
	) {
		this(
			(float) pt1.getX(), (float) pt1.getY(), color1,
			(float) pt2.getX(), (float) pt2.getY(), color2,
			cyclic
		);
	}

	protected static Paint getPaint(
		float x1, float y1, Color color1,
		float x2, float y2, Color color2,
		boolean cyclic
	) {
		return new GradientPaint(x1, y1, color1, x2, y2, color2, cyclic);
	}

	public Paint getPaint() {
		return getPaint(x1, y1, color1, x2, y2, color2, cyclic);
	}


	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(x1),
			worldViewer.worldToScreenY(y1),
			color1,
			worldViewer.worldToScreenX(x2),
			worldViewer.worldToScreenY(y2),
			color2,
			cyclic
		);
	}

	public String toString() {
		return String.format(
			"GradientPainter[(%f, %f) %s -> (%f, %f) %s, cyclic=%b]",
			x1, y1, color1, x2, y2, color2, cyclic
		);
	}
}

