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
	private static final boolean DEFAULT_DEEP_COPY = true;

	public Triangle(Point A, Point B, Point C) {
		this(A, B, C, DEFAULT_COLOR);
	}

	public Triangle(Point A, Point B, Point C, Paint paint) {
		this(A, B, C, paint, DEFAULT_FILL);
	}

	public Triangle(Point A, Point B, Point C, Paint paint, boolean fill) {
		this(
			Arrays.asList(A, B, C),
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
		this(vertices, paint, fill, DEFAULT_DEEP_COPY);
	}
	
	protected Triangle(Point[] vertices, Paint paint, boolean fill, boolean deepCopy) {
		this(Arrays.asList(vertices), paint, fill, deepCopy);
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
		this(other.vertices, other.paint, other.fill, DEFAULT_DEEP_COPY);
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
		// area: |Ax(By - Cy) + Bx(Cy - Ay) + Cx(Ay - By)| / 2
		Point A = getVertex(0);
		Point B = getVertex(1);
		Point C = getVertex(2);
		return Math.abs(
			A.getX() * (B.getY() - C.getY()) +
			B.getX() * (C.getY() - A.getY()) +
			C.getX() * (A.getY() - B.getY())
		) / 2.0;
	}

	public Point centroid() {
		Iterator<Point> iter = getVertices().iterator();
		Point A = iter.next();
		Point B = iter.next();
		Point C = iter.next();
		return new Point(
			(A.getX() + B.getX() + C.getX()) / 3,
			(A.getY() + B.getY() + C.getY()) / 3
		);
	}

	public Point orthocenter() {
		Iterator<Point> iter = getVertices().iterator();
		Point A = iter.next();
		Point B = iter.next();
		Point C = iter.next();
		double Ax = A.getX(), Ay = A.getY();
		double Bx = B.getX(), By = B.getY();
		double Cx = C.getX(), Cy = C.getY();

		double mBC, mAC;

		boolean BC_vertical = Math.abs(Bx - Cx) < EPSILON;
		boolean AC_vertical = Math.abs(Ax - Cx) < EPSILON;

		if (AC_vertical && BC_vertical) {
				throw new ArithmeticException("Degenerate triangle: two vertical sides");
		} else if (BC_vertical) {
			// BC vertical, altitude from A is horizontal
			mAC = (Ay - Cy) / (Ax - Cx);
			double mB = -1 / mAC;
			double yA = Ay;
			double cB = By - mB * Bx;
			double x = Cx;
			double y = mB * x + cB;
			return new Point(x, y);
		} else if (AC_vertical) {
			// AC vertical, altitude from B is horizontal
			mBC = (By - Cy) / (Bx - Cx);
			double mA = -1 / mBC;
			double yB = By;
			double cA = Ay - mA * Ax;
			double x = Cx;
			double y = mA * x + cA;
			return new Point(x, y);
		} else {
			// general case
			mBC = (By - Cy) / (Bx - Cx);
			mAC = (Ay - Cy) / (Ax - Cx);
			double mA = -1 / mBC;
			double mB = -1 / mAC;
			double cA = Ay - mA * Ax;
			double cB = By - mB * Bx;
			double x = (cB - cA) / (mA - mB);
			double y = mA * x + cA;
			return new Point(x, y);
		}
	}

	public static Point footOfAltitude(Point A, Point B, Point C) {
		double Ax = A.getX();
		double Ay = A.getY();

		double mBC;
		try {
			mBC = B.slope(C);  // slope of BC
		} catch (ArithmeticException e) {
			// BC is vertical
			return new Point(B.getX(), Ay);
		}

		if (Math.abs(mBC) < EPSILON) {
			// BC horizontal line
			return new Point(Ax, B.getY());
		}

		double mA = -1 / mBC;
		double cBC = B.getY() - mBC * B.getX();
		double cA = Ay - mA * Ax;
		double x = (cA - cBC) / (mBC - mA);
		double y = mBC * x + cBC;

		return new Point(x, y);
	}

	public Triangle orthicTriangle() {
		Iterator<Point> iter = getVertices().iterator();
		Point A = iter.next();
		Point B = iter.next();
		Point C = iter.next();
		
		Point footFromA = footOfAltitude(A, B, C);
		Point footFromB = footOfAltitude(B, A, C);
		Point footFromC = footOfAltitude(C, A, B);
		
		return new Triangle(
			Arrays.asList(footFromA, footFromB, footFromC), paint, fill, false
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

		double Ax = A.getX(), Ay = A.getY();
		double BX = B.getX(), By = B.getY();
		double Cx = C.getX(), Cy = C.getY();

		double d = 4 * signedArea();

		if (Math.abs(d) < 1e-10) {
			throw new ArithmeticException("Degenerate triangle: cannot compute circumcircle.");
		}

		double x1Sq = A.dot(A);
		double x2Sq = B.dot(B);
		double x3Sq = C.dot(C);

		double ux = (x1Sq * (By - Cy) +
			x2Sq * (Cy - Ay) +
			x3Sq * (Ay - By)) / d;

		double uy = (x1Sq * (Cx - BX) +
			x2Sq * (Ax - Cx) +
			x3Sq * (BX - Ax)) / d;

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

		return new Triangle(
			Arrays.asList(top, bottomRight, bottomLeft), paint, fill, false
		);
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

		return new Triangle(
			Arrays.asList(top, bottomRight, bottomLeft), paint, fill, true
		);
	}

}

