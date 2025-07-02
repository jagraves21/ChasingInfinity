package geometric;

import utils.color.NamedColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import java.awt.Color;
import java.awt.Paint;

public class Triangle extends BasePolygon<Triangle> {
	public static final boolean DEFAULT_FILL = false;

	public Triangle(Point p1, Point p2, Point p3) {
		this(p1, p2, p3, DEFAULT_COLOR);
	}

	public Triangle(Point p1, Point p2, Point p3, Paint paint) {
		this(p1, p2, p3, paint, DEFAULT_FILL);
	}

	public Triangle(Point p1, Point p2, Point p3, Paint paint, boolean fill) {
		this(
			Arrays.asList(p1, p2, p3),
			paint,
			fill,
			true
		);
	}

	public Triangle(Point[] vertices) {
		this(vertices, DEFAULT_COLOR);
	}

	public Triangle(Point[] vertices, Paint paint) {
		this(vertices, DEFAULT_COLOR, DEFAULT_FILL);
	}

	public Triangle(Point[] vertices, Paint paint, boolean fill) {
		this(Arrays.asList(vertices), paint, fill, true);
	}

	public Triangle(List<Point> vertices) {
		this(vertices, DEFAULT_COLOR);
	}

	public Triangle(List<Point> vertices, Paint paint) {
		this(vertices, DEFAULT_COLOR, DEFAULT_FILL);
	}

	public Triangle(List<Point> vertices, Paint paint, boolean fill) {
		this(vertices, paint, fill, true);
	}

	protected Triangle(List<Point> vertices, Paint paint, boolean fill, boolean deepCopy) {
		super(vertices, paint, fill, deepCopy);
		if (vertices == null || vertices.size() < 3) {
			throw new IllegalArgumentException("A triangle must have 3 vertices.");
		}
	}

	public Triangle(Triangle other) {
		this(other.vertices, other.paint, other.fill, true);
	}

	protected BasePolygon<Triangle> createCopy(
		List<Point> vertices, Paint paint, boolean fill, boolean deepCopy
	) {
		return new Triangle(vertices, paint, fill, deepCopy);
	}

	public Triangle self() {
		return this;
	}

	public double area() {
		// area: |x1(y2 - y3) + x2(y3 - y1) + x3(y1 - y2)| / 2
		Point p1 = getVertex(0);
		Point p2 = getVertex(1);
		Point p3 = getVertex(2);
		return Math.abs(
			p1.getX() * (p2.getY() - p3.getY()) +
			p2.getX() * (p3.getY() - p1.getY()) +
			p3.getX() * (p1.getY() - p2.getY())
		) / 2.0;
	}

	public Point centroid() {
		Point p1 = getVertex(0);
		Point p2 = getVertex(1);
		Point p3 = getVertex(2);
		return new Point(
			(p1.getX() + p2.getX() + p3.getX()) / 3,
			(p1.getY() + p2.getY() + p3.getY()) / 3
		);
	}

	public Circle incircle() {
		Iterator<Point> iter = getVertices().iterator();
		Point A = iter.next();
		Point B = iter.next();
		Point C = iter.next();

		double a = B.distance(C);
		double b = A.distance(C);
		double c = A.distance(B);

		double perimeter = a + b + c;
		double x = (a * A.getX() + b * B.getX() + c * C.getX()) / perimeter;
		double y = (a * A.getY() + b * B.getY() + c * C.getY()) / perimeter;
		Point incenter = new Point(x, y);

		double semiPerimeter = perimeter / 2.0;
		double area = area();
		double inradius = area / semiPerimeter;

		return new Circle(incenter, inradius, getPaint());
	}

	public Circle circumcircle() {
		Iterator<Point> iter = getVertices().iterator();
		Point A = iter.next();
		Point B = iter.next();
		Point C = iter.next();

		double x1 = A.getX(), y1 = A.getY();
		double x2 = B.getX(), y2 = B.getY();
		double x3 = C.getX(), y3 = C.getY();

		double d = 4 * signedArea();

		if (Math.abs(d) < 1e-10) {
			throw new ArithmeticException("Degenerate triangle: cannot compute circumcircle.");
		}

		double x1Sq = A.dot(A);
		double x2Sq = B.dot(B);
		double x3Sq = C.dot(C);

		double ux = (x1Sq * (y2 - y3) +
			x2Sq * (y3 - y1) +
			x3Sq * (y1 - y2)) / d;

		double uy = (x1Sq * (x3 - x2) +
			x2Sq * (x1 - x3) +
			x3Sq * (x2 - x1)) / d;

		Point circumcenter = new Point(ux, uy);
		double radius = circumcenter.distance(A);
		return new Circle(circumcenter, radius, getPaint());
	}

	public String toString() {
		return String.format(
			"Triangle{vertices=%s, paint=%s, fill=%b}", vertices, paint, fill
		);
	}

	public static Triangle createEquilateralFromCenter(Point center, double height) {
		return createEquilateralFromCenter(center, height, DEFAULT_COLOR);
	}

	public static Triangle createEquilateralFromCenter(Point center, double height, Paint paint) {
		return createEquilateralFromCenter(center, height, paint, DEFAULT_FILL);
	}

	public static Triangle createEquilateralFromCenter(Point center, double height, Paint paint, boolean fill) {
		double halfHeight = height / 2.0;
		double dx = height * (Math.sqrt(3) / 2.0);

		Point top = new Point(center).translate(0, height);
		Point bottomRight = new Point(center).translate(dx, -halfHeight);
		Point bottomLeft = new Point(center).translate(-dx, -halfHeight);

		Point[] vertices = new Point[] { top, bottomRight, bottomLeft };

		return new Triangle(vertices, paint, fill);
	}

	public static Triangle createEquilateralFromTop(Point top, double height) {
		return createEquilateralFromTop(top, height, DEFAULT_COLOR);
	}

	public static Triangle createEquilateralFromTop(Point top, double height, Paint paint) {
		return createEquilateralFromTop(top, height, paint, DEFAULT_FILL);
	}

	public static Triangle createEquilateralFromTop(Point top, double height, Paint paint, boolean fill) {
		double dx = height / Math.sqrt(3);

		Point bottomRight = new Point(top).translate(dx, -height);
		Point bottomLeft = new Point(top).translate(-dx, -height);

		Point[] vertices = new Point[] {top, bottomRight, bottomLeft};
		return new Triangle(vertices, paint, fill);
	}

}

