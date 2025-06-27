package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Polygon;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class PythagorasTree extends AbstractGeometricFractal<PythagorasTree> {
	protected List<Polygon> seed;
	protected List<Polygon> currentSquares;
	protected List<Transformable> fractalComponents;

	public PythagorasTree() {
		this(getSuggestedIterations());
	}

	public PythagorasTree(int iterations) {
		this(iterations, true);
	}

	public PythagorasTree(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public PythagorasTree(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();
		currentSquares = new LinkedList<>();

		double length = 125;
		Point p1 = new Point(-length/2, length/2).translate(0,-175);
		seed.add(
			Polygon.createSquare(
				p1, length, NamedColors.SADDLE_BROWN, false
			)
		);

		currentSquares = seed;
		fractalComponents = new LinkedList<>(seed);
	}

	public void reset() {
		super.reset();
		currentSquares = seed;
		fractalComponents = new LinkedList<>(seed);
	}

	public void step() {
		double base = ((Polygon) fractalComponents.get(0)).area();

		Point oldPoint;
		Point newPoint;
		Polygon newSquare;
		List<Polygon> newSquares = new LinkedList<>();
		for (Polygon square : currentSquares) {
			oldPoint = square.getVertex(1);
			newSquare = new Polygon(square)
				.scale(Math.sin(Math.toRadians(45)));
			newPoint = newSquare.getVertex(2);
			newSquare.translate(
				oldPoint.getX()-newPoint.getX(),
				oldPoint.getY()-newPoint.getY()
			).rotateAround(oldPoint, -45);
			if (newSquare.area() / base < 0.1) {
				newSquare.setPaint(NamedColors.DARK_GREEN);
			}
			newSquares.add(newSquare);

			oldPoint = square.getVertex(0);
			newSquare = new Polygon(square)
				.scale(Math.sin(Math.toRadians(45)));
			newPoint = newSquare.getVertex(3);
			newSquare.translate(
				oldPoint.getX()-newPoint.getX(),
				oldPoint.getY()-newPoint.getY()
			).rotateAround(oldPoint, 45);
			if (newSquare.area() / base < 0.1) {
				newSquare.setPaint(NamedColors.DARK_GREEN);
			}
			newSquares.add(newSquare);
		}
		fractalComponents.addAll(newSquares);
		currentSquares = newSquares;
	}

	public PythagorasTree self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Pythagoras Tree";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

