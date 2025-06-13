package geometric;

import renderer.WorldViewer;

import utils.color.NamedColors;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Paint;

public class LineSegment extends AbstractShape<LineSegment>
	implements CompositeTransformable<LineSegment> {
	
	public static final double EPSILON = 1e-10;

	protected Point start;
	protected Point end;
	protected Paint paint;
	protected boolean drawEndpoints;

	public LineSegment(double x1, double y1, double x2, double y2) {
		this(x1, y1, x2, y2, NamedColors.WHITE);
	}

	public LineSegment(double x1, double y1, double x2, double y2, Paint paint) {
		this(x1, y1, x2, y2, paint, false);
	}
	
	public LineSegment(double x1, double y1, double x2, double y2, Paint paint, boolean drawEndpoints) {
		this(
			new Point(x1, y1, paint),
			new Point(x2, y2, paint),
			paint,
			drawEndpoints,
			false
		);
	}

	public LineSegment(Point start, Point end) {
		this(start, end, NamedColors.WHITE);
	}
	
	public LineSegment(Point start, Point end, Paint paint) {
		this(start, end, paint, false);
	}
	
	public LineSegment(Point start, Point end, Paint paint, boolean drawEndpoints) {
		this(start, end, paint, drawEndpoints, true);
	}
	
	public LineSegment(Point start, Point end, Paint paint, boolean drawEndpoints, boolean copy) {
		if (copy) {
			this.start = new Point(start);
			this.end = new Point(end);
		}
		else {
			this.start = start;
			this.end = end;
		}
		this.paint = paint;
		this.drawEndpoints = drawEndpoints;
	}

	public LineSegment(LineSegment other) {
		this(other.start, other.end, other.paint, other.drawEndpoints, true);
	}
	
	public Point getStart() { return start; }
	public Point getEnd() { return end; }
	public Paint getPaint() { return paint; }
	public boolean getDrawEndpoints() { return drawEndpoints; }

	public void setStart(Point start) { this.start = new Point(start); }
	public void setEnd(Point end) { this.end = new Point(end); }
	public void setPaint(Paint paint) { this.paint = paint; }
	public void setDrawEndpoints(boolean drawEndpoints) {
		this.drawEndpoints = drawEndpoints;
	}

	public double length() {
		return start.distance(end);
	}

	public Point interpolate(double t) {
		double x = (1 - t) * start.getX() + t * end.getX();
		double y = (1 - t) * start.getY() + t * end.getY();
		return new Point(x, y, null);
	}

	public Point getMidpoint() {
		return interpolate(0.5);
	}

	public boolean intersects(LineSegment other) {
		int o1 = orientation(this.start, this.end, other.start);
		int o2 = orientation(this.start, this.end, other.end);
		int o3 = orientation(other.start, other.end, this.start);
		int o4 = orientation(other.start, other.end, this.end);

		// General case
		if (o1 != o2 && o3 != o4) {
			return true;
		}

		// Special cases
		if (o1 == 0 && onSegment(this.start, other.start, this.end)) return true;
		if (o2 == 0 && onSegment(this.start, other.end, this.end)) return true;
		if (o3 == 0 && onSegment(other.start, this.start, other.end)) return true;
		if (o4 == 0 && onSegment(other.start, this.end, other.end)) return true;

		return false;
	}

	public double slope() {
		return start.slope(end);
	}

	private int orientation(Point p, Point q, Point r) {
		/*double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
			(q.getX() - p.getX()) * (r.getY() - q.getY());*/
		double val = q.cross(p,r);
		
		if (Math.abs(val) < EPSILON) return 0;
		return (val > 0) ? 1 : 2;
	}

	private boolean onSegment(Point p, Point q, Point r) {
		return q.getX() <= Math.max(p.getX(), r.getX()) &&
			q.getX() >= Math.min(p.getX(), r.getX()) &&
			q.getY() <= Math.max(p.getY(), r.getY()) &&
			q.getY() >= Math.min(p.getY(), r.getY());
	}

	public LineSegment toScreen(WorldViewer worldViewer) {
		return new LineSegment(
			start.toScreen(worldViewer),
			end.toScreen(worldViewer),
			paint,
			drawEndpoints,
			false
		);
	}

	public LineSegment toWorld(WorldViewer worldViewer) {
		return new LineSegment(
			start.toWorld(worldViewer),	
			end.toWorld(worldViewer),
			paint,
			drawEndpoints,
			false
		);
	}

	public boolean isVisibleOnScreen(WorldViewer worldViewer) {
		if(
			start.isVisibleOnScreen(worldViewer)
			|| end.isVisibleOnScreen(worldViewer)
		) return true;

		LineSegment screenSegment = toScreen(worldViewer);
		int width = worldViewer.getScreenWidth();
		int height = worldViewer.getScreenHeight();
		LineSegment topEdge = new LineSegment(0, 0, width, 0);
		LineSegment bottomEdge = new LineSegment(0, height, width, height);
		LineSegment leftEdge = new LineSegment(0, 0, 0, height);
		LineSegment rightEdge = new LineSegment(width, 0, width, height);
		
		return screenSegment.intersects(topEdge) ||
			screenSegment.intersects(bottomEdge) ||
			screenSegment.intersects(leftEdge) ||
			screenSegment.intersects(rightEdge);
	}
	
	public void drawWithPaint(Graphics g) {
		if (drawEndpoints) {
			start.draw(g);
			end.draw(g);
		}

		int startX = (int) Math.round(start.getX());
		int startY = (int) Math.round(start.getY());
		int endX = (int) Math.round(end.getX());
		int endY = (int) Math.round(end.getY());
		g.drawLine(startX, startY, endX, endY);
	}
	
	public void drawWithPaint(Graphics g, WorldViewer worldViewer) {
		this.toScreen(worldViewer).drawWithPaint(g);
	}

	public LineSegment self() {
		return this;
	}
	
	public Iterator<Transformable> iterator() {
		List<Transformable> points = Arrays.asList(start, end);
		return points.iterator();
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof LineSegment)) return false;
		LineSegment other = (LineSegment) obj;
		return this.start.equals(other.start) && this.end.equals(other.end);
	}
	
	public int compareTo(LineSegment other) {
		int cmp = this.start.compareTo(other.start);
		if (cmp != 0) return cmp;
		return this.end.compareTo(other.end);
	}

	public int hashCode() {
		return Objects.hash(start, end, paint);
	}

	public String toString() {
		return "LineSegment[" + start + " -> " + end + "]";
	}
}

