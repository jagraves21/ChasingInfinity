package geometric.fractals;

import utils.color.NamedPalettes;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Polygon;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class TurningSquare extends AbstractGeometricFractal<TurningSquare> {
	protected Polygon seedSquare;
	protected Polygon currentSquare;
	protected List<Transformable> fractalComponents;

	public TurningSquare() {
		this(getSuggestedIterations());
	}

	public TurningSquare(int iterations) {
		this(iterations, true);
	}

	public TurningSquare(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public TurningSquare(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 200;
	}

	public static int getSuggestedUPS() {
		return 20;
	}

	protected void init() {
		super.init();

		double length = 500;
		seedSquare = Polygon.createSquare(
			new Point(-length/2, length/2), length, null, false
		);

		currentSquare = seedSquare;
		fractalComponents = new LinkedList<>();
		fractalComponents.add(seedSquare);
		
		setPainter(PaintFactory.getRadialPainter(
			seedSquare.centroid(), seedSquare.getVertex(0), NamedPalettes.OIL_SLICK
		));
	}

	public void reset() {
		super.reset();
		currentSquare = seedSquare;
		fractalComponents.clear();
		fractalComponents.add(seedSquare);
	}

	public void step() {
		Iterator<Point> iter = currentSquare.getVertices().iterator();
		Point p1 = iter.next();
		Point p2 = iter.next();
		Point p3 = iter.next();
		Point p4 = iter.next();

		Point tp1 = p1.interpolate(p2, 0.95);
		Point tp2 = p2.interpolate(p3, 0.95);
		Point tp3 = p3.interpolate(p4, 0.95);
		Point tp4 = p4.interpolate(p1, 0.95);

		currentSquare = new Polygon(
			new Point[] {tp1, tp2, tp3, tp4},
			currentSquare.getPaint(),
			currentSquare.isFilled()
		);
		fractalComponents.add(currentSquare);
	}

	public TurningSquare self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Turning Square";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

