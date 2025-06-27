package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class SierpinskiTriangle extends AbstractGeometricFractal<SierpinskiTriangle> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiTriangle() {
		this(getSuggestedIterations());
	}

	public SierpinskiTriangle(int iterations) {
		this(iterations, true);
	}

	public SierpinskiTriangle(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiTriangle(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double height = 500;
		Triangle triangle = Triangle.createEquilateralFromTop(
			new Point(0, height/2), height, null, false
		);
		seed.add(triangle);
		
		fractalComponents = seed;

		setPainter(PaintFactory.getRadialPainter(
			triangle.centroid(), triangle.getVertex(0), NamedColors.BLUE, NamedColors.RED
		));
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof Triangle) {
				Triangle triangle = (Triangle) t;
				Iterator<Point> iter = triangle.getVertices().iterator();
				Point p1 = iter.next();
				Point p2 = iter.next();
				Point p3 = iter.next();

				Point t2 = p1.getMidpoint(p2);
				Point t1 = p1.getMidpoint(p3);
				Point t3 = p2.getMidpoint(p3);

				newComponents.add(
					new Triangle(new Point[] {p1,t2,t1}, triangle.getPaint(), triangle.isFilled())
				);
				newComponents.add(
					new Triangle(new Point[] {t1,t3,p3}, triangle.getPaint(), triangle.isFilled())
				);
				newComponents.add(
					new Triangle(new Point[] {t2,p2,t3}, triangle.getPaint(), triangle.isFilled())
				);
			}
			else {
				newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public SierpinskiTriangle self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Triangle";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

