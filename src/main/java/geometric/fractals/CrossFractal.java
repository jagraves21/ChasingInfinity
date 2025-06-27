package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Polygon;
import geometric.Transformable;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class CrossFractal extends AbstractGeometricFractal<CrossFractal> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public CrossFractal() {
		this(getSuggestedIterations());
	}

	public CrossFractal(int iterations) {
		this(iterations, true);
	}

	public CrossFractal(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public CrossFractal(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double length = 500;
		Point topLeft = new Point(-length/2,  length/2);
		Polygon square = Polygon.createSquare(topLeft, length, null, false);
		seed.add(square);
		
		fractalComponents = seed;

		setPainter(PaintFactory.getRadialPainter(
			square.centroid(), topLeft, NamedColors.GREEN, NamedColors.BLUE
		));
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		Polygon newSquare;
		Point c1, c2, cS;
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof Polygon) {
				Polygon square = (Polygon) t;
				Iterator<Point> iter = square.getVertices().iterator();
				Point p1 = iter.next();
				Point p2 = iter.next();
				Point p3 = iter.next();
				Point p4 = iter.next();

				newSquare = new Polygon(square).scale(1.0/3.0);
				c1 = newSquare.getVertex(0);
				c2 = p1.interpolate(p2, 1.0/3.0);
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);
				cS = newSquare.getVertex(2);

				newSquare = new Polygon(square).scale(1.0/3.0);
				c1 = newSquare.getVertex(0);
				c2 = p1.interpolate(p4, 1.0/3.0);
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);

				newSquare = new Polygon(square).scale(1.0/3.0);
				c1 = newSquare.getVertex(1);
				c2 = p2.interpolate(p3, 1.0/3.0);
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);

				newSquare = new Polygon(square).scale(1.0/3.0);
				c1 = newSquare.getVertex(2);
				c2 = p3.interpolate(p4, 1.0/3.0);
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);

				newSquare = new Polygon(square).scale(1.0/3.0);
				c1 = newSquare.getVertex(1);
				c2 = cS;
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);
			}
			else {
				newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public CrossFractal self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Cross Fractal";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

