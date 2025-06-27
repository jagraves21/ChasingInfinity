package renderer.paint;

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

public class TriGradientPaint implements Paint {

	private final Point2D p1, p2, p3;
	private final Color c1, c2, c3;

	public TriGradientPaint(Point2D p1, Color c1, Point2D p2, Color c2, Point2D p3, Color c3) {
		this.p1 = p1;
		this.c1 = c1;
		this.p2 = p2;
		this.c2 = c2;
		this.p3 = p3;
		this.c3 = c3;
	}

	public PaintContext createContext(
		ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds,
		AffineTransform xform, RenderingHints hints
	) {
		Point2D tp1 = xform.transform(p1, null);
		Point2D tp2 = xform.transform(p2, null);
		Point2D tp3 = xform.transform(p3, null);
		return new TriGradientPaintContext(tp1, c1, tp2, c2, tp3, c3);
	}

	public int getTransparency() {
		int a1 = c1.getAlpha();
		int a2 = c2.getAlpha();
		int a3 = c3.getAlpha();
		return ((a1 & a2 & a3) == 0xFF) ? OPAQUE : TRANSLUCENT;
	}
}

