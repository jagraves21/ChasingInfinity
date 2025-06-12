package geometric;

import renderer.WorldViewer;

import utils.color.NamedColors;

import java.util.Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

public class Point extends AbstractShape<Point> {
	public static final double RADIUS = 3;

	protected double x;
	protected double y;
	protected double radius;
	protected Paint paint;

	public Point() {
		this(0, 0);
	}

	public Point(double x, double y) {
		this(x, y, RADIUS);
	}

	public Point(double x, double y, double radius) {
		this(x, y, radius, NamedColors.WHITE);
	}

	public Point(double x, double y, Paint paint) {
		this(x, y, RADIUS, paint);
	}

	public Point(double x, double y, double radius, Paint paint) {
		super();
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.paint = paint;
	}

	public Point(Point other) {
		this(other.x, other.y, other.radius, other.paint);
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public double getRadius() { return radius; }
	public Paint getPaint() { return paint; }

	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setRadius(double radius) {
		if (radius < 0) {
			throw new IllegalArgumentException(
				"Radius cannot be negative: " + radius
			);
		}
		this.radius = radius;
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

	public Point interpolate(Point other, double t) {
		double x = (1 - t) * this.x + t * other.x;
		double y = (1 - t) * this.y + t * other.y;
		double radius = this.radius;
		if (this.radius != other.radius) {
			radius = (1 - t) * this.radius + t * other.radius;
		}
		Paint paint = this.paint;
		return new Point(x, y, radius, paint);
	}

	public Point getMidpoint(Point other) {
		return interpolate(other, 0.5);
	}

	public double slope(Point other) {
		double deltaX = this.x - other.x;
		double deltaY = this.y - other.y;
		if (deltaX == 0) {
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

	public Point toScreen(WorldViewer worldViewer) {
		return new Point(
			worldViewer.worldToScreenX(x),
			worldViewer.worldToScreenY(y),
			radius,
			paint
		);
	}

	public Point toWorld(WorldViewer worldViewer) {
		return new Point(
			worldViewer.screenToWorldX( (int) Math.round(x) ),
			worldViewer.screenToWorldY( (int) Math.round(y) ),
			radius,
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
		int drawX = (int)Math.round(screenPoint.x - screenPoint.radius);
		int drawY = (int)Math.round(screenPoint.y - screenPoint.radius);
		int drawDiameter = (int)Math.max(1, Math.round(2 * screenPoint.radius));

		boolean offLeft = drawX + drawDiameter < 0;
		boolean offTop = drawY + drawDiameter < 0;
		boolean offRight = drawX > worldViewer.getScreenWidth();
		boolean offBottom = drawY > worldViewer.getScreenHeight();

		return !(offLeft || offTop || offRight || offBottom);
	}
	
	public void drawWithPaint(Graphics g) {
		if (paint == null) return;
		int diameter = (int)Math.max(1, Math.round(2 * radius));
		int drawX = (int)Math.round(x - radius);
		int drawY = (int)Math.round(y - radius);
		g.fillOval(drawX, drawY, diameter, diameter);
	}

	public void drawWithPaint(Graphics g, WorldViewer worldViewer) {
		this.toScreen(worldViewer).drawWithPaint(g);
	}

	public java.awt.Point asAWTPoint() {
		return new java.awt.Point(
			(int) Math.round(x),
			(int) Math.round(y)
		);
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
		return Objects.hash(x, y, radius, paint);
	}

	public String toString() {
		return String.format("Point{x=%.2f, y=%.2f, paint=%s}", x, y, paint);
	}
}

