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

public class SierpinskiArrowhead extends AbstractGeometricFractal<SierpinskiArrowhead> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiArrowhead() {
		this(getSuggestedIterations());
	}

	public SierpinskiArrowhead(int iterations) {
		this(iterations, true);
	}

	public SierpinskiArrowhead(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiArrowhead(int iterations, boolean reset) {
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
		Point p2 = iter.next();
		Point p3 = iter.next();

		seed.add(
			new LineSegment(p3, p2, null)
		);
		
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

				Point t1 = lineSegment.getMidpoint().rotateAround(p1, 60);
				Point t2 = lineSegment.getMidpoint().rotateAround(p2, -60);

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

	public SierpinskiArrowhead self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Arrowhead";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

