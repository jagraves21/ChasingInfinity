package geometric;

import renderer.viewer.WorldViewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import java.awt.Graphics;
import java.awt.Paint;

public abstract class BasePolygon<T extends BasePolygon<T>> extends AbstractShape<BasePolygon<T>>
	implements CompositeTransformable<T> {

	protected List<Point> vertices;
	protected Paint paint;
	protected boolean fill;

	protected BasePolygon(List<Point> vertices, Paint paint, boolean fill, boolean deepCopy) {
		if (vertices == null || vertices.size() < 3) {
			throw new IllegalArgumentException("A polygon must have at least 3 vertices.");
		}

		if (deepCopy) {
			this.vertices = new ArrayList<Point>(vertices.size());
			for (Point p : vertices) {
				this.vertices.add(new Point(p));
			}
		}
		else {
			this.vertices = vertices;
		}
		this.paint = paint;
		this.fill = fill;
	}

	protected abstract BasePolygon<T> createCopy(
		List<Point> vertices, Paint paint, boolean fill, boolean deepCopy
	);

	public java.awt.Polygon toAWTPolygon() {
		java.awt.Polygon polygon = new java.awt.Polygon();
		for (Point p : vertices) {
			polygon.addPoint(
				(int)Math.round(p.getX()),
				(int)Math.round(p.getY())
			);
		}
		return polygon;
	}

	public int getNPoints() { return vertices.size(); }
	public Point getVertex(int ii) { return vertices.get(ii); }
	public Paint getPaint() { return paint; }
	public boolean isFilled() { return fill; }

	public List<Point> getVertices() {
		return Collections.unmodifiableList(vertices);
	}

	public void setPaint(Paint paint) { this.paint = paint; }
	public void setFill(boolean fill) { this.fill = fill; }

	public double area() {
		return Math.abs(signedArea());
	}

	public double signedArea() {
		double sum = 0;
		Point p1 = vertices.get(vertices.size()-1);
		for (Point p2 : vertices) {
			//sum += (p1.getX() * p2.getY()) - (p2.getX() * p1.getY());
			sum += p1.cross(p2);
			p1 = p2;
		}
		return sum / 2.0;
	}

	public Point centroid() {
		double cx = 0;
		double cy = 0;
		double factor;
		double area6 = 6 * signedArea();
		if (area6 == 0) {
			throw new ArithmeticException("Cannot compute centroid of a degenerate polygon with zero area.");
		}

		Point p1 = vertices.get(vertices.size()-1);
		for (Point p2 : vertices) {
			//factor = (p1.getX() * p2.getY() - p2.getX() * p1.getY());
			factor = p1.cross(p2);
			cx += (p1.getX() + p2.getX()) * factor;
			cy += (p1.getY() + p2.getY()) * factor;
			p1 = p2;
		}

		return new Point(cx / area6, cy / area6);
	}

	public boolean isClockwise() {
		return signedArea() < 0;
	}

	public BasePolygon<T> reverseWinding() {
		List<Point> tmpVertices = new ArrayList<>(vertices.size());
		for (Point p : vertices) {
			tmpVertices.add(p);
		}
		java.util.Collections.reverse(tmpVertices);
		return createCopy(tmpVertices, paint, fill, false);
	}

	public boolean isPointInside(Point p) {
		int intersections = 0;

		// Iterate through each edge of the polygon
		Point p1 = vertices.get(vertices.size()-1);
		for (Point p2 : vertices) {
			// Check if p lies on a horizontal ray from p in the positive X direction
			if ((p.getY() > p1.getY()) != (p.getY() > p2.getY())) {
				// Compute the X coordinate of the intersection with the ray
				double intersectionX = (p.getY() - p1.getY()) * (p2.getX() - p1.getX()) / (p2.getY() - p1.getY()) + p1.getX();

				// If the intersection is to the right of p, we count it
				if (p.getX() < intersectionX) {
					intersections++;
				}
			}
			p1 = p2;
		}

		// If the number of intersections is odd, p is inside; otherwise, it's outside
		return (intersections % 2 == 1);
	}

	public BasePolygon<T> toScreen(WorldViewer worldViewer) {
		List<Point> screenVertices = new ArrayList<>(vertices.size());
		for (Point p : vertices) {
			screenVertices.add(p.toScreen(worldViewer));
		}
		return createCopy(screenVertices, paint, fill, false);
	}

	public BasePolygon<T> toWorld(WorldViewer worldViewer) {
		List<Point> worldVertices = new ArrayList<>(vertices.size());
		for (Point p : vertices) {
			worldVertices.add(p.toWorld(worldViewer));
		}
		return createCopy(worldVertices, paint, fill, false);
	}

	public boolean isVisibleOnScreen(WorldViewer worldViewer) {
		if (isFilled()) {
			Point corner = new Point(0, 0).toWorld(worldViewer);
			if (isPointInside(corner)) return true;
		}
		Point lastPoint = vertices.get(vertices.size()-1);
		LineSegment lineSegment = new LineSegment(lastPoint, lastPoint);
		for (Point p : vertices) {
			lineSegment.getStart().set(lineSegment.getEnd());
			lineSegment.getEnd().set(p);
			if (lineSegment.isVisibleOnScreen(worldViewer)) {
				return true;
			}
		}
		return false;
	}

	public void drawWithPaint(Graphics g) {
		java.awt.Polygon polygon = toAWTPolygon();
		if (isFilled()) g.fillPolygon(polygon);
		else g.drawPolygon(polygon);
	}

	public void drawWithPaint(Graphics g, WorldViewer worldViewer) {
		this.toScreen(worldViewer).drawWithPaint(g);
	}

	public abstract T self();

	public Iterator<Transformable> iterator() {
		return new Iterator<Transformable>() {
			private final Iterator<Point> iter = vertices.iterator();
			public boolean hasNext() { return iter.hasNext(); }
			public Transformable next() { return iter.next(); }
		};
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof BasePolygon)) return false;
		@SuppressWarnings("unchecked")
		BasePolygon<T> other = (BasePolygon<T>) obj;
		return isFilled() == other.isFilled() && vertices.equals(other.vertices);
	}

	public int compareTo(BasePolygon<T> other) {
		int sizeComparison = Integer.compare(this.vertices.size(), other.vertices.size());
		if (sizeComparison != 0) return sizeComparison;

		Iterator<Point> thisIter = this.vertices.iterator();
		Iterator<Point> thatIter = other.vertices.iterator();

		while (thisIter.hasNext() && thatIter.hasNext()) {
			int cmp = thisIter.next().compareTo(thatIter.next());
			if (cmp != 0) return cmp;
		}

		return 0;
	}

	public int hashCode() {
		return Objects.hash(vertices, paint, fill);
	}

	public String toString() {
		return String.format(
			"BasePolygon{vertices=%s, paint=%s, fill=%b}", vertices, paint, fill
		);
	}
}
