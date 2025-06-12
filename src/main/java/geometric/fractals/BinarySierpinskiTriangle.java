package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Polygon;
import geometric.Transformable;
import geometric.Triangle;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.Paint;

public class BinarySierpinskiTriangle extends AbstractGeometricFractal<BinarySierpinskiTriangle> {
	protected Point center;
    protected Point pointOnCircle;

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
			new Color[] {NamedColors.BLUE, NamedColors.RED}
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
			new Polygon(new Point[] {p1,p2,p3,p4}, null, false)
		);

		Point pM = p1.getMidpoint(p2);
		center = new Triangle(new Point[] {pM,p3,p4}).centroid();
		pointOnCircle = pM;
		fractalComponents = seed;
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

