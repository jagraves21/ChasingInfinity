package geometric;

import renderer.viewer.WorldViewer;

import utils.color.NamedColors;

import java.util.Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

public class Circle extends AbstractShape<Circle> {
	public static final double DEFAULT_RADIUS = 10;
	public static final boolean DEFAULT_FILL = false;

	protected Point center;
	protected double radius;
	protected Paint paint;
	protected boolean fill;

	public Circle() {
		this(0, 0);
	}

	public Circle(Point center) {
		this(center, DEFAULT_RADIUS);
	}

	public Circle(Point center, double radius) {
		this(center, radius, DEFAULT_COLOR);
	}

	public Circle(Point center, double radius, Paint paint) {
		this(center, radius, paint, DEFAULT_FILL);
	}

	public Circle(Point center, double radius, Paint paint, boolean fill) {
		this(center, radius, paint, fill, true);
	}
	
	protected Circle(Point center, double radius, Paint paint, boolean fill, boolean copy) {
		super();
		if (copy) {
			this.center = new Point(center);
		} else {
			this.center = center;
		}
		this.radius = radius;
		this.paint = paint;
		this.fill = fill;
	}

	public Circle(double x, double y) {
		this(x, y, DEFAULT_RADIUS);
	}

	public Circle(double x, double y, double radius) {
		this(x, y, radius, DEFAULT_COLOR);
	}

	public Circle(double x, double y, double radius, Paint paint) {
		this(x, y, radius, paint, DEFAULT_FILL);
	}

	public Circle(double x, double y, double radius, Paint paint, boolean fill) {
		this(
			new Point(x, y, paint),
			radius,
			paint,
			fill,
			DEFAULT_FILL
		);
	}

	public Circle(Circle other) {
		this(other.center, other.radius, other.paint, other.fill, true);
	}

	public double getX() { return center.getX(); }
	public double getY() { return center.getY(); }
	public double getRadius() { return radius; }
	public Point getCenter() { return center; }
	public Paint getPaint() { return paint; }
	public boolean isFilled() { return fill; }

	public void setX(double x) { center.setX(x); }
	public void setY(double y) { center.setY(y); }
	public void setRadius(double radius) { this.radius = radius; }
	public void setPaint(Paint paint) { this.paint = paint; }
	public void setFill(boolean fill) { this.fill = fill; }
	public void setCenter(Point center) { this.center.set(center); }
	public void setCenter(double x, double y) { center.set(x, y); }

	public double area() {
		return Math.PI * radius * radius;
	}
	
	public double circumference() {
		return 2 * Math.PI * radius;
	}

	public boolean overlaps(Circle other) {
		double distanceSquared = this.center.distanceSquared(other.center);
		double radiusSum = this.getRadius() + other.getRadius();
		return distanceSquared < radiusSum * radiusSum;
	}

	public boolean contains(Point p) {
		return center.distanceSquared(p) <= radius * radius + EPSILON;
	}

	public boolean contains(Circle other) {
		return contains(other, false);
	}

	public boolean contains(Circle other, boolean strict) {
		double radiusDiff = this.getRadius() - other.getRadius();

		if (strict) {
			// strict: inner circle must be *fully inside* (no touching)
			// if radiusDiff is negative, the other circle is larger and not fully contained
			if (radiusDiff <= EPSILON) return false;

			double centerDistanceSquared = this.center.distanceSquared(other.center);
			double radiusDiffSquared = radiusDiff * radiusDiff;

			return centerDistanceSquared < radiusDiffSquared - EPSILON;
		} else {
			// non-strict: touching is allowed
			// if radiusDiff is negative, the other circle is larger and not fully contained
			if (radiusDiff < -EPSILON) return false;

			double centerDistanceSquared = this.center.distanceSquared(other.center);
			double radiusDiffSquared = radiusDiff * radiusDiff;

			return centerDistanceSquared <= radiusDiffSquared + EPSILON;
		}
	}

	public Circle invert(Circle other) {
		// 'this' is the inversion circle (center O, radius R)
		Point O = this.center;
		double R = this.radius;

		// Circle to be inverted
		Point C = other.center;
		double r = other.radius;

		// Step 1: Compute distance from center of inversion to center of other circle
		double dx = C.getX() - O.getX();
		double dy = C.getY() - O.getY();
		double d = Math.sqrt(dx * dx + dy * dy);

		// Safety check: Circle passes through the inversion center
		if (Math.abs(d - r) < EPSILON) {
			throw new IllegalArgumentException("Cannot invert a circle that passes through the center of inversion.");
		}

		// Safety check: Denominator too small (could lead to instability or undefined behavior)
		double denom = (d * d) - (r * r);
		if (Math.abs(denom) < EPSILON) {
			throw new ArithmeticException("Inversion radius undefined due to near-zero denominator.");
		}

		// Step 2: Compute scale factor for projecting along the direction vector from O to C
		double scale = (d * R * R) / denom;

		// Unit vector in the direction from O to C
		double ux = dx / d;
		double uy = dy / d;

		// Step 3: Compute the center of the inverted circle using the scaled direction
		double cx = O.getX() + ux * scale;
		double cy = O.getY() + uy * scale;
		Point invertedCenter = new Point(cx, cy, C.visualRadius, C.paint); // replicate visual attributes

		// Step 4: Compute the radius of the inverted circle
		double invertedRadius = Math.abs((R * R * r) / denom);

		return new Circle(invertedCenter, invertedRadius, other.paint, other.fill);
	}


	public Circle invert2(Circle other) {
		// 'this' is the inversion circle (center O, radius R)
		Point O = this.center;
		double R = this.radius;

		// Step 1: Compute the inversion of the center of 'other'
		Point C = other.center;
		double r = other.radius;

		// Check if the circle passes through the center of inversion
		if (Math.abs(C.distance(O) - r) < EPSILON) {
			throw new IllegalArgumentException("Cannot invert a circle that passes through the center of inversion.");
		}

		// Invert the center of the other circle
		Point invertedCenter = C.invert(this);

		// Step 2: Compute distance OC and use formula to get inverted radius
		double d = O.distance(C);

		// Inversion radius formula:
		// r' = (R^2 * r) / (d^2 - r^2)
		double denom = (d * d) - (r * r);
		if (Math.abs(denom) < EPSILON) {
			throw new ArithmeticException("Inversion radius undefined due to near-zero denominator.");
		}
		double invertedRadius = (R * R * r) / denom;

		return new Circle(invertedCenter, Math.abs(invertedRadius), other.paint, other.fill);
	}


	public Circle toScreen(WorldViewer worldViewer) {
		return new Circle(
			center.toScreen(worldViewer),
			radius / worldViewer.getZoom(),
			paint,
			fill,
			false
		);
	}

	public Circle toWorld(WorldViewer worldViewer) {
		return new Circle(
			center.toWorld(worldViewer),
			radius * worldViewer.getZoom(),
			paint,
			fill,
			false
		);
	}

	public Circle translate(double dx, double dy) {
		center.translate(dx, dy);
		return this;
	}

	public Circle scale(double factor) {
		center.scale(factor);
		radius *= factor;
		return this;
	}

	public Circle rotate(double angleDegrees) {
		center.rotate(angleDegrees);
		return this;
	}

	public Circle rotateAround(Point pivot, double angleDegrees) {
		center.rotateAround(pivot, angleDegrees);
		return this;
	}

	public Circle reflectAcrossXAxis() {
		center.reflectAcrossXAxis();
		return this;
	}

	public Circle reflectAcrossYAxis() {
		center.reflectAcrossYAxis();
		return this;
	}

	public Circle reflectAcrossLine(double slope) {
		center.reflectAcrossLine(slope);
		return this;
	}

	public Circle shearX(double factor) {
		center.shearX(factor);
		return this;
	}

	public Circle shearY(double factor) {
		center.shearY(factor);
		return this;
	}

	public Circle normalize(double width, double height) {
		center.normalize(width, height);
		return this;
	}

	public BoundingBox getBoundingBox() {
		return new BoundingBox(
			center.getX()-getRadius(),
			center.getY()-getRadius(),
			center.getX()+getRadius(),
			center.getY()+getRadius()
		);
	}

	public boolean isVisibleOnScreen(WorldViewer worldViewer) {
		Circle screenCircle = this.toScreen(worldViewer);
		int drawX = (int)Math.round(screenCircle.getX() - screenCircle.getRadius());
		int drawY = (int)Math.round(screenCircle.getY() - screenCircle.getRadius());
		int drawDiameter = (int)Math.max(1, Math.round(2 * screenCircle.getRadius()));

		boolean offLeft = drawX + drawDiameter < 0;
		boolean offTop = drawY + drawDiameter < 0;
		boolean offRight = drawX > worldViewer.getScreenWidth();
		boolean offBottom = drawY > worldViewer.getScreenHeight();

		return !(offLeft || offTop || offRight || offBottom);
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
		return this.center.equals(other.center) &&
			Double.compare(this.getRadius(), other.getRadius()) == 0;
	}

	public int compareTo(Circle other) {
		int cmp = this.center.compareTo(other.center);
		if (cmp != 0) return cmp;
		return Double.compare(this.getRadius(), other.getRadius());
	}

	public int hashCode() {
		return center.hashCode();
	}

	public String toString() {
		return String.format(
			"Circle{x=%.2f, y=%.2f, radius=%.2f, paint=%s}",
			getX(), getY(), getRadius(), getPaint()
		);
	}
}

