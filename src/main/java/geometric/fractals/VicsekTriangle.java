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
import java.awt.RadialGradientPaint;
import java.awt.Paint;

public class VicsekTriangle extends AbstractGeometricFractal<VicsekTriangle> {
	protected Point center;
	protected Point pointOnCircle;
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

	public Paint getPaint() {
		return getPaint(
			this.center,
			this.pointOnCircle
		);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			this.center.toScreen(worldViewer),
			this.pointOnCircle.toScreen(worldViewer)
		);
	}

	protected Paint getPaint(Point center, Point pointOnCircle) {
		return new RadialGradientPaint(
			(float) center.getX(),
			(float) center.getY(),
			(float) center.distance(pointOnCircle),
			new float[] {0.0f, 1.0f},
			new Color[] {NamedColors.AZURE, NamedColors.ROSE}
			//MultipleGradientPaint.CycleMethod.REPEAT
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

		center = p1.getMidpoint(p3);;
		pointOnCircle = new Point(p3);
		fractalComponents = seed;
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

