package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class SierpinskiCurve extends AbstractGeometricFractal<SierpinskiCurve> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiCurve() {
		this(getSuggestedIterations());
	}

	public SierpinskiCurve(int iterations) {
		this(iterations, true);
	}

	public SierpinskiCurve(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiCurve(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		Triangle triangle = Triangle.createEquilateralFromTop(
			new Point(0, 250), 500, null, false
		);
		Iterator<Point> iter = triangle.getVertices().iterator();
		Point p1 = iter.next();
		Point p3 = iter.next();
		Point p2 = iter.next();
		
		Point t1 = p1.interpolate(p3, 0.25);
		Point t2 = p1.interpolate(p2, 0.25);
		Point t3 = p1.interpolate(p2, 0.5);
		Point t4 = p1.interpolate(p2, 0.75);
		Point t5 = p2.interpolate(p3, 0.25);
		Point t6 = p2.interpolate(p3, 0.5);
		Point t7 = p2.interpolate(p3, 0.75);
		Point t8 = p3.interpolate(p1, 0.25);
		Point t9 = p3.interpolate(p1, 0.5);

		seed.add(new LineSegment(t1, t2, null));
		seed.add(new LineSegment(t3, t2, null));
		seed.add(new LineSegment(t4, t3, null));
		seed.add(new LineSegment(t4, t5, null));
		seed.add(new LineSegment(t6, t5, null));
		seed.add(new LineSegment(t7, t6, null));
		seed.add(new LineSegment(t7, t8, null));
		seed.add(new LineSegment(t9, t8, null));
		seed.add(new LineSegment(t1, t9, null));

		fractalComponents = seed;

		setPainter(PaintFactory.getRadialPainter(
			triangle.centroid(), p1, NamedColors.BLUE, NamedColors.RED
		));
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof LineSegment) {
				LineSegment lineSegment = (LineSegment) t;
				Point p1 = lineSegment.getStart();
				Point p2 = lineSegment.getEnd();
				Point t1 = lineSegment.getMidpoint().rotateAround(p1, -60);
				Point t2 = lineSegment.getMidpoint().rotateAround(p2, 60);

				newComponents.add(
					new LineSegment(t1, p1, lineSegment.getPaint())
				);
				newComponents.add(
					new LineSegment(t1, t2, lineSegment.getPaint())
				);
				newComponents.add(
					new LineSegment(p2, t2, lineSegment.getPaint())
				);
			}
			else {
				newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public SierpinskiCurve self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Curve";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

