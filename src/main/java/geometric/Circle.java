package geometric;

import renderer.viewer.WorldViewer;

import utils.color.NamedColors;

import java.util.Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

public class Circle extends AbstractShape<Circle> {
	Point point;
	boolean fill;

	public Circle() {
		this(0, 0);
	}

	public Circle(Point center) {
		this(center, 10);
	}

	public Circle(Point center, double radius) {
		this(center, radius, NamedColors.WHITE);
	}

	public Circle(Point center, double radius, Paint paint) {
		this(center, radius, paint, false);
	}

	public Circle(Point center, double radius, Paint paint, boolean fill) {
		this(
			new Point(center.getX(), center.getY(), radius, paint),
			fill,
			false
		);
	}

	public Circle(double x, double y) {
		this(x, y, 1);
	}

	public Circle(double x, double y, double radius) {
		this(x, y, radius, NamedColors.WHITE);
	}

	public Circle(double x, double y, double radius, Paint paint) {
		this(x, y, radius, paint, false);
	}

	public Circle(double x, double y, double radius, Paint paint, boolean fill) {
		this(
			new Point(x, y, radius, paint),
			fill,
			false
		);
	}

	private Circle(Point point, boolean fill, boolean copy) {
		super();
		if (copy) {
			this.point = new Point(point);
		} else {
			this.point = point;
		}
		this.fill = fill;
	}

	public Circle(Circle other) {
		this(other.point, other.isFilled(), true);
	}

	public double getX() { return point.getX(); }
	public double getY() { return point.getY(); }
	public double getRadius() { return point.getRadius(); }
	public Point getCenter() { return point; }
	public Paint getPaint() { return point.getPaint(); }
	public boolean isFilled() { return fill; }

	public void setX(double x) { point.setX(x); }
	public void setY(double y) { point.setY(y); }
	public void setRadius(double radius) { point.setRadius(radius); };
	public void setPaint(Paint paint) { point.setPaint(paint); }
	public void setFill(boolean fill) { this.fill = fill; }
	public void setCenter(Point center) { point.set(center); }
	public void setCenter(double x, double y) { point.set(x, y); }

	public Circle toScreen(WorldViewer worldViewer) {
		Point tmpPoint = point.toScreen(worldViewer);
		tmpPoint.setRadius(tmpPoint.getRadius() * worldViewer.getZoom());
		return new Circle(tmpPoint, fill, false);
	}

	public Circle toWorld(WorldViewer worldViewer) {
		Point tmpPoint = point.toWorld(worldViewer);
		tmpPoint.setRadius(tmpPoint.getRadius() / worldViewer.getZoom());
		return new Circle(tmpPoint, fill, false);
	}

	public Circle translate(double dx, double dy) {
		point.translate(dx, dy);
		return this;
	}

	public Circle scale(double factor) {
		point.scale(factor);
		point.setRadius(point.getRadius() * factor);
		return this;
	}

	public Circle rotate(double angleDegrees) {
		point.rotate(angleDegrees);
		return this;
	}

	public Circle rotateAround(Point pivot, double angleDegrees) {
		point.rotateAround(pivot, angleDegrees);
		return this;
	}

	public Circle reflectAcrossXAxis() {
		point.reflectAcrossXAxis();
		return this;
	}

	public Circle reflectAcrossYAxis() {
		point.reflectAcrossYAxis();
		return this;
	}

	public Circle reflectAcrossLine(double slope) {
		point.reflectAcrossLine(slope);
		return this;
	}

	public Circle shearX(double factor) {
		point.shearX(factor);
		return this;
	}

	public Circle shearY(double factor) {
		point.shearY(factor);
		return this;
	}

	public Circle normalize(double width, double height) {
		point.normalize(width, height);
		return this;
	}

	public BoundingBox getBoundingBox() {
		return new BoundingBox(
			point.getX()-point.getRadius(),
			point.getY()-point.getRadius(),
			point.getX()+point.getRadius(),
			point.getY()+point.getRadius()
		);
	}

	public boolean isVisibleOnScreen(WorldViewer worldViewer) {
		return point.isVisibleOnScreen(worldViewer);
	}

	public void drawWithPaint(Graphics g) {
		int diameter = (int)Math.max(1, Math.round(2 * getRadius()));
		int drawX = (int)Math.round(getX() - getRadius());
		int drawY = (int)Math.round(getY() - getRadius());
		if(fill) {
			g.fillOval(drawX, drawY, diameter, diameter);
		} else {
			g.drawOval(drawX, drawY, diameter, diameter);
		}
	}

	public void drawWithPaint(Graphics g, WorldViewer worldViewer) {
		this.toScreen(worldViewer).drawWithPaint(g);
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Circle)) return false;
		Circle other = (Circle) obj;
		return this.point.equals(other.point) &&
			Double.compare(this.getRadius(), other.getRadius()) == 0;
	}

	public int compareTo(Circle other) {
		int cmp = this.point.compareTo(other.point);
		if (cmp != 0) return cmp;
		return Double.compare(this.getRadius(), other.getRadius());
	}

	public int hashCode() {
		return point.hashCode();
	}

	public String toString() {
		return String.format(
			"Circle{x=%.2f, y=%.2f, radius=%.2f, paint=%s}",
			getX(), getY(), getRadius(), getPaint()
		);
	}
}

