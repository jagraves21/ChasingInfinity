package geometric;

import utils.color.NamedColors;

import java.awt.Color;
import java.awt.Paint;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Triangle extends BasePolygon<Triangle> {
	public Triangle(Point p1, Point p2, Point p3) {
		this(p1, p2, p3, NamedColors.WHITE);
	}

	public Triangle(Point p1, Point p2, Point p3, Paint paint) {
		this(p1, p2, p3, paint, false);
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
		this(vertices, NamedColors.WHITE);
	}

	public Triangle(Point[] vertices, Paint paint) {
		this(vertices, NamedColors.WHITE, false);
	}

	public Triangle(Point[] vertices, Paint paint, boolean fill) {
		this(Arrays.asList(vertices), paint, fill, true);
	}

	public Triangle(List<Point> vertices) {
		this(vertices, NamedColors.WHITE);
	}

	public Triangle(List<Point> vertices, Paint paint) {
		this(vertices, NamedColors.WHITE, false);
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

	public String toString() {
		return String.format(
			"Triangle{vertices=%s, paint=%s, fill=%b}", vertices, paint, fill
        );
    }
}

