package geometric.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Polygon;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

public class PythagorasTree extends AbstractGeometricFractal<PythagorasTree> {
	protected Point topLeft;
	protected Point bottomRight;
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

	public Paint getPaint() {
		return getPaint(
			this.topLeft,
			this.bottomRight
		);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			this.topLeft.toScreen(worldViewer),
			this.bottomRight.toScreen(worldViewer)
		);
	}

	protected Paint getPaint(Point topLeft, Point bottomRight) {
		Point p1 = topLeft.interpolate(bottomRight, 1.0/3.0);
		Point p2 = topLeft.interpolate(bottomRight, 2.0/3.0);
		return new GradientPaint(
			(float)p1.getX(), (float)p1.getY(), NamedColors.INDIGO,
			(float)p2.getX(), (float)p2.getY(), NamedColors.ROSE
		);
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();
		currentSquares = new LinkedList<>();

		double length = 125;
		Point p1 = new Point(-length/2, length/2)
			.translate(0,-175);
		Point p2 = new Point(p1).translate(length, 0);
		Point p3 = new Point(p1).translate(length, -length);
		Point p4 = new Point(p1).translate(0, -length);

		seed.add(
			new Polygon(new Point[] {p1,p2,p3,p4}, NamedColors.SADDLE_BROWN, false)
		);

		topLeft = new Point(p1);
		bottomRight = new Point(p3);
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

