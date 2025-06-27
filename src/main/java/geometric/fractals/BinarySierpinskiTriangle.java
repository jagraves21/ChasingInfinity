package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Polygon;
import geometric.Transformable;
import geometric.Triangle;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class BinarySierpinskiTriangle extends AbstractGeometricFractal<BinarySierpinskiTriangle> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public BinarySierpinskiTriangle() {
		this(getSuggestedIterations());
	}

	public BinarySierpinskiTriangle(int iterations) {
		this(iterations, true);
	}

	public BinarySierpinskiTriangle(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public BinarySierpinskiTriangle(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 6;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double length = 500;
		Point p1 = new Point(-length/2, length/2);
		seed.add( Polygon.createSquare(p1, length, null, false) );
		
		fractalComponents = seed;

		Triangle triangle = Triangle.createEquilateralFromTop(
			new Point(0, 250), 500, null, false
		);
		setPainter(PaintFactory.getRadialPainter(
			triangle.centroid(), triangle.getVertex(0), NamedColors.BLUE, NamedColors.RED
		));
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		Polygon newSquare;
		Point c1, c2;
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof Polygon) {
				Polygon square = (Polygon) t;
				Iterator<Point> iter = square.getVertices().iterator();
				Point p1 = iter.next();
				Point p2 = iter.next();
				Point p3 = iter.next();
				Point p4 = iter.next();

				newSquare = new Polygon(square).scale(0.5);
				c1 = newSquare.getVertex(3);
				c2 = p4;
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);

				newSquare = new Polygon(square).scale(0.5);
				c1 = newSquare.getVertex(2);
				c2 = p3;
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);

				newSquare = new Polygon(square).scale(0.5);
				c1 = newSquare.getVertex(0);
				c2 = p1.interpolate(p2, 1.0/4.0);
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);
			}
			else {
				newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public BinarySierpinskiTriangle self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Binary Sierpinski Triangle";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

