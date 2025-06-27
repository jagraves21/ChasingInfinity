package geometric.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class SierpinskiCircle3 extends AbstractGeometricFractal<SierpinskiCircle3> {
	protected List<Triangle> seed;
	protected List<Triangle> currentTriangles;
	protected List<Transformable> fractalComponents;

	public SierpinskiCircle3() {
		this(getSuggestedIterations());
	}

	public SierpinskiCircle3(int iterations) {
		this(iterations, true);
	}

	public SierpinskiCircle3(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiCircle3(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 8;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		Triangle triangle = Triangle.createEquilateralFromTop(
			new Point(0, 250), 500, null, false
		);

		seed.add(triangle);
		
		currentTriangles = seed;
		fractalComponents = new LinkedList<>();
		fractalComponents.add(seed.get(0).incircle());

		setPainter(PaintFactory.getRadialPainter(
			triangle.centroid(), triangle.getVertex(0), NamedColors.BLUE, NamedColors.RED
		));
	}

	public void reset() {
		super.reset();
		currentTriangles = seed;
		fractalComponents.clear();
		fractalComponents.add(seed.get(0).incircle());
	}

	public void step() {
		List<Triangle> newTriangles = new LinkedList<>();
		for (Triangle triangle : currentTriangles) {
			Iterator<Point> iter = triangle.getVertices().iterator();
			Point p1 = iter.next();
			Point p2 = iter.next();
			Point p3 = iter.next();

			Point t2 = p1.getMidpoint(p2);
			Point t1 = p1.getMidpoint(p3);
			Point t3 = p2.getMidpoint(p3);

			newTriangles.add(
				new Triangle(new Point[] {p1,t2,t1}, triangle.getPaint(), triangle.isFilled())
			);
			newTriangles.add(
				new Triangle(new Point[] {t1,t3,p3}, triangle.getPaint(), triangle.isFilled())
			);
			newTriangles.add(
				new Triangle(new Point[] {t2,p2,t3}, triangle.getPaint(), triangle.isFilled())
			);
		}
		currentTriangles = newTriangles;
		//fractalComponents.clear();
		for (Triangle triangle : currentTriangles) {
			fractalComponents.add(triangle.incircle());
		}
	}

	public SierpinskiCircle3 self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Circle 3";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

