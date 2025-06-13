package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Polygon;
import geometric.Transformable;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class BoxFractal extends AbstractGeometricFractal<BoxFractal> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public BoxFractal() {
		this(getSuggestedIterations());
	}

	public BoxFractal(int iterations) {
		this(iterations, true);
	}

	public BoxFractal(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public BoxFractal(int iterations, boolean reset) {
		super(iterations, reset);
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
			new Color[] {NamedColors.GREEN, NamedColors.BLUE}
			//MultipleGradientPaint.CycleMethod.REPEAT
		);
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double length = 500;
		Point p1 = new Point(-length/2,  length/2);
		Point p2 = new Point( length/2,  length/2);
		Point p3 = new Point( length/2, -length/2);
		Point p4 = new Point(-length/2, -length/2);

		seed.add(
			new Polygon(new Point[] {p1,p2,p3,p4}, null, false)
		);

		center = p1.getMidpoint(p3);
		pointOnCircle = new Point(p1);
		fractalComponents = seed;
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
				c2 = p1;
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);
				cS = newSquare.getVertex(2);

				newSquare = new Polygon(square).scale(1.0/3.0);
				c1 = newSquare.getVertex(1);
				c2 = p2;
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);

				newSquare = new Polygon(square).scale(1.0/3.0);
				c1 = newSquare.getVertex(2);
				c2 = p3;
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);

				newSquare = new Polygon(square).scale(1.0/3.0);
				c1 = newSquare.getVertex(3);
				c2 = p4;
				newSquare.translate(c2.getX() - c1.getX(), c2.getY() - c1.getY());
				newComponents.add(newSquare);

				newSquare = new Polygon(square).scale(1.0/3.0);
				c1 = newSquare.getVertex(0);
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

	public BoxFractal self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Box Fracrtal";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

