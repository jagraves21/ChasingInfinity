package geometric;

import renderer.viewer.WorldViewer;

import utils.color.NamedColors;

import java.util.Objects;

import java.awt.Graphics;
import java.awt.Paint;

public class Point extends AbstractShape<Point> {
	public static final double EPSILON = 1e-9;
	public static final double DEFAULT_VISUAL_RADIUS = 3;

	protected double x;
	protected double y;
	protected double visualRadius;
	protected Paint paint;

	public Point() {
		this(0, 0);
	}

	public Point(double x, double y) {
		this(x, y, DEFAULT_VISUAL_RADIUS);
	}

	public Point(double x, double y, double visualRadius) {
		this(x, y, visualRadius, NamedColors.WHITE);
	}

	public Point(double x, double y, Paint paint) {
		this(x, y, DEFAULT_VISUAL_RADIUS, paint);
	}

	public Point(double x, double y, double visualRadius, Paint paint) {
		super();
		this.x = x;
		this.y = y;
		this.visualRadius = visualRadius;
		this.paint = paint;
	}

	public Point(Point other) {
		this(other.x, other.y, other.visualRadius, other.paint);
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public double getVisualRadius() { return visualRadius; }
	public Paint getPaint() { return paint; }

	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setVisualRadius(double visualRadius) {
		if (visualRadius < 0) {
			throw new IllegalArgumentException(
				"Radius cannot be negative: " + visualRadius
			);
		}
		this.visualRadius = visualRadius;
	}
	public void setPaint(Paint paint) { this.paint = paint; }
	public void set(Point other) { this.set(other.x, other.y); }
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double distance(Point other) {
		return Math.hypot(x - other.x, y - other.y);
	}

	public double distanceSquared(Point other) {
		double dx = x - other.x;
		double dy = y - other.y;
		return (dx * dx) + (dy * dy);
	}

	public double slope(Point other) {
		double deltaX = other.x - this.x;
		double deltaY = other.y - this.y;
		if (Math.abs(deltaX) < EPSILON) {
			throw new ArithmeticException("Slope is undefined for vertical lines.");
		}
		return deltaY / deltaX;
	}

	public double dot(Point other) {
		return (this.x * other.x) + (this.y * other.y);
	}

	public double cross(Point other) {
		return this.x * other.y - this.y * other.x;
	}

	public double cross(Point p2, Point p3) {
		double dx1 = p2.x - this.x;
		double dy1 = p2.y - this.y;
		double dx2 = p3.x - this.x;
		double dy2 = p3.y - this.y;
		return dx1 * dy2 - dy1 * dx2;
	}

	public double polarAngle() {
		double angle = Math.toDegrees(Math.atan2(y, x));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	public Point interpolate(Point other, double t) {
		double x = (1 - t) * this.x + t * other.x;
		double y = (1 - t) * this.y + t * other.y;
		double visualRadius = this.visualRadius;
		if (this.visualRadius != other.visualRadius) {
			visualRadius = (1 - t) * this.visualRadius + t * other.visualRadius;
		}
		Paint paint = this.paint;
		return new Point(x, y, visualRadius, paint);
	}

	public Point getMidpoint(Point other) {
		return interpolate(other, 0.5);
	}

	public Point invert(Circle circle) {
		double cx = circle.getX();
		double cy = circle.getY();
		double r = circle.getRadius();
		double dx = this.x - cx;
		double dy = this.y - cy;
		double distSq = dx * dx + dy * dy;

		if (distSq < EPSILON) {
			throw new ArithmeticException("Cannot invert the center point of the circle.");
		}

		double scale = (r * r) / distSq;
		double invertedX = cx + dx * scale;
		double invertedY = cy + dy * scale;

		return new Point(invertedX, invertedY, this.visualRadius, this.paint);
	}

	public Point toScreen(WorldViewer worldViewer) {
		return new Point(
			worldViewer.worldToScreenX(x),
			worldViewer.worldToScreenY(y),
			visualRadius,
			paint
		);
	}

	public Point toWorld(WorldViewer worldViewer) {
		return new Point(
			worldViewer.screenToWorldX( (int) Math.round(x) ),
			worldViewer.screenToWorldY( (int) Math.round(y) ),
			visualRadius,
			paint
		);
	}

	public Point translate(double dx, double dy) {
		x += dx;
		y += dy;
		return this;
	}

	public Point scale(double factor) {
		x *= factor;
		y *= factor;
		return this;
	}

	public Point rotate(double angleDegrees) {
		double radians = Math.toRadians(angleDegrees);
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		double newX = x * cos - y * sin;
		double newY = x * sin + y * cos;
		x = newX;
		y = newY;
		return this;
	}

	public Point rotateAround(Point pivot, double angleDegrees) {
		double dx = x - pivot.x;
		double dy = y - pivot.y;
		double radians = Math.toRadians(angleDegrees);
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		double newX = dx * cos - dy * sin;
		double newY = dx * sin + dy * cos;
		x = newX + pivot.x;
		y = newY + pivot.y;
		return this;
	}

	public Point reflectAcrossXAxis() {
		y = -y;
		return this;
	}

	public Point reflectAcrossYAxis() {
		x = -x;
		return this;
	}

	public Point reflectAcrossLine(double slope) {
		double d = (x + y * slope) / (1 + slope * slope);
		double newX = 2 * d - x;
		double newY = 2 * d * slope - y;
		x = newX;
		y = newY;
		return this;
	}

	public Point shearX(double factor) {
		x += factor * y;
		return this;
	}

	public Point shearY(double factor) {
		y += factor * x;
		return this;
	}

	public Point normalize(double width, double height) {
		x /= width;
		y /= height;
		return this;
	}

	public BoundingBox getBoundingBox() {
		return new BoundingBox(x, y, x, y);
	}

	public boolean isVisibleOnScreen(WorldViewer worldViewer) {
		Point screenPoint = this.toScreen(worldViewer);
		int drawX = (int)Math.round(screenPoint.x - screenPoint.visualRadius);
		int drawY = (int)Math.round(screenPoint.y - screenPoint.visualRadius);
		int drawDiameter = (int)Math.max(1, Math.round(2 * screenPoint.visualRadius));

		boolean offLeft = drawX + drawDiameter < 0;
		boolean offTop = drawY + drawDiameter < 0;
		boolean offRight = drawX > worldViewer.getScreenWidth();
		boolean offBottom = drawY > worldViewer.getScreenHeight();

		return !(offLeft || offTop || offRight || offBottom);
	}

	public void drawWithPaint(Graphics g) {
		if (paint == null) return;
		int diameter = (int)Math.max(1, Math.round(2 * visualRadius));
		int drawX = (int)Math.round(x - visualRadius);
		int drawY = (int)Math.round(y - visualRadius);
		g.fillOval(drawX, drawY, diameter, diameter);
	}

	public void drawWithPaint(Graphics g, WorldViewer worldViewer) {
		this.toScreen(worldViewer).drawWithPaint(g);
	}

	public java.awt.Point toAWTPoint() {
		return new java.awt.Point(
			(int) Math.round(x), (int) Math.round(y)
		);
	}

	public java.awt.geom.Point2D.Float toPoint2DFloat() {
		return new java.awt.geom.Point2D.Float(
			(float) x, (float) y
		);
	}

	public java.awt.geom.Point2D.Double toPoint2DDouble() {
		return new java.awt.geom.Point2D.Double(x, y);
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Point)) return false;
		Point other = (Point) obj;
		return Double.compare(x, other.x) == 0 &&
			Double.compare(y, other.y) == 0;
	}

	public int compareTo(Point other) {
		int cmp = Double.compare(this.x, other.x);
		if (cmp != 0) return cmp;
		return Double.compare(this.y, other.y);
	}

	public int hashCode() {
		return Objects.hash(x, y, visualRadius, paint);
	}

	public String toString() {
		return String.format("Point{x=%.2f, y=%.2f, paint=%s}", x, y, paint);
	}
}

