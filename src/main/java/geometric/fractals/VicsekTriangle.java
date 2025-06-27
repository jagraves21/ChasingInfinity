package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Polygon;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;

public class VicsekTriangle extends AbstractGeometricFractal<VicsekTriangle> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public VicsekTriangle() {
		this(getSuggestedIterations());
	}

	public VicsekTriangle(int iterations) {
		this(iterations, true);
	}

	public VicsekTriangle(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public VicsekTriangle(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 5;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double length = 500;
		Polygon square = Polygon.createSquare(
			new Point(-length/2, length/2), length, null, true
		);
		seed.add(square);

		fractalComponents = seed;

		setPainter(PaintFactory.getRadialPainter(
			square.centroid(), square.getVertex(0),
			new Color[] {NamedColors.AZURE, NamedColors.ROSE}
		));
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		List<Transformable> newSquares = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof  Polygon) {
				Polygon square = (Polygon) t;
				Polygon baseSquare = new Polygon(square).scale(1.0/3.0);
				baseSquare.translate(
					square.getVertex(0).getX() - baseSquare.getVertex(0).getX(),
					square.getVertex(0).getY() - baseSquare.getVertex(0).getY()
				);

				double dx = square.getVertex(1).getX() - square.getVertex(0).getX();
				double dy = square.getVertex(3).getY() - square.getVertex(0).getY();
				for (int row = 0; row < 3; row++) {
					for (int col = 0; col < 3; col++) {
						if (!(
							(row == 0 && col == 0) ||
							(row == 0 && col == 1) ||
							(row == 1 && col == 0)
						)) {
							Polygon tmpSquare = new Polygon(baseSquare).translate(
								col/3.0*dx, row/3.0*dy
							);
							newSquares.add(tmpSquare);
						}
					}
				}
			}
		}
		fractalComponents = newSquares;
	}

	public VicsekTriangle self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Vicsek Triangle";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

