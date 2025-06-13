package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

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

public class SierpinskiCarpet extends AbstractGeometricFractal<SierpinskiCarpet> {
	protected Point topLeft;
	protected Point bottomRight;
	protected List<Polygon> seed;
	protected List<Polygon> currentSquares;
	protected List<Transformable> fractalComponents;

	public SierpinskiCarpet() {
		this(getSuggestedIterations());
	}

	public SierpinskiCarpet(int iterations) {
		this(iterations, true);
	}

	public SierpinskiCarpet(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiCarpet(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 6;
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

		double length = 500;
		Point p1 = new Point(-length/2, length/2);
		Point p2 = new Point(p1).translate(length, 0);
		Point p3 = new Point(p1).translate(length, -length);
		Point p4 = new Point(p1).translate(0, -length);

		seed.add(
			new Polygon(new Point[] {p1,p2,p3,p4}, null, true)
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
		List<Polygon> newSquares = new LinkedList<>();
		for (Polygon square : currentSquares) {
			Polygon baseSquare = new Polygon(square).scale(1.0/3.0);
			baseSquare.translate(
				square.getVertex(0).getX() - baseSquare.getVertex(0).getX(),
				square.getVertex(0).getY() - baseSquare.getVertex(0).getY()
			);
			baseSquare.setPaint(NamedColors.BLACK);

			double dx = square.getVertex(1).getX() - square.getVertex(0).getX();
			double dy = square.getVertex(3).getY() - square.getVertex(0).getY();
			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					Polygon tmpSquare = new Polygon(baseSquare).translate(
						row/3.0*dx, col/3.0*dy
					);
					if (row == 1 && col == 1) {
						fractalComponents.add(tmpSquare);
					} else {
						newSquares.add(tmpSquare);
					}
				}
			}
		}
		currentSquares = newSquares;
	}

	public SierpinskiCarpet self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Carpet";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

