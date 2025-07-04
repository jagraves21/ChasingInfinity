package geometric.fractals;

import utils.color.NamedPalettes;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;
import geometric.utils.PaintFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Paint;

public class SierpinskiPedal extends AbstractGeometricFractal<SierpinskiPedal> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiPedal() {
		this(getSuggestedIterations());
	}

	public SierpinskiPedal(int iterations) {
		this(iterations, true);
	}

	public SierpinskiPedal(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiPedal(int iterations, boolean reset) {
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
		triangle.getVertex(0).translate(100, -10);
		
		fractalComponents = seed;

		setPainter(PaintFactory.getRadialPainter(
			triangle.centroid(), triangle.getVertex(0), NamedPalettes.GAS_ON_WATER
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
				Paint paint = triangle.getPaint(); 
				boolean fill = triangle.isFilled(); 
				Iterator<Point> iter = triangle.getVertices().iterator();
				Point A = iter.next();
				Point B = iter.next();
				Point C = iter.next();

				Point footFromA = Triangle.footOfAltitude(A, B, C);
				Point footFromB = Triangle.footOfAltitude(B, A, C);
				Point footFromC = Triangle.footOfAltitude(C, A, B);

				Collections.addAll(
					newComponents,
					new Triangle(A, footFromC, footFromB, paint, fill),
					new Triangle(footFromB, footFromA, C, paint, fill),
					new Triangle(footFromC, B, footFromA, paint, fill)
				);
			}
			else {
				//newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public SierpinskiPedal self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Pedal";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

