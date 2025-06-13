package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class OuterSierpinskiTriangle extends AbstractGeometricFractal<OuterSierpinskiTriangle> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Triangle> seed;
	protected List<Triangle> currentTriangles;
	protected List<Transformable> fractalComponents;

	public OuterSierpinskiTriangle() {
		this(getSuggestedIterations());
	}

	public OuterSierpinskiTriangle(int iterations) {
		this(iterations, true);
	}

	public OuterSierpinskiTriangle(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public OuterSierpinskiTriangle(int iterations, boolean reset) {
		super(iterations, reset);
	}

		public Paint getPaint() {
		return getPaint(
			this.center,
			this.pointOnCircle
		);
	}

	public static int getSuggestedIterations() {
		return 10;
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

		double height = 500;
		double base = (2 * height) / Math.sqrt(3);
		Point p1 = new Point(0, height/2);
		Point p2 = new Point(p1).translate(-base/2, -height);
		Point p3 = new Point(p1).translate(base/2, -height);

		Point t1 = p1.getMidpoint(p3);
		Point t2 = p1.getMidpoint(p2);
		Point t3 = p2.getMidpoint(p3);

        seed.add(
            new Triangle(new Point[] {t1,t2,t3}, null, false)
        );

		center = new Triangle(new Point[] {p1,p2,p3}).centroid();
		pointOnCircle = new Point(p1);
		currentTriangles = seed;
		fractalComponents = new LinkedList<>(seed);
	}

	public void reset() {
		super.reset();
		currentTriangles = seed;
		fractalComponents.clear();
		fractalComponents.addAll(seed);
	}

	public void step() {
		double dx, dy;
		Point midpoint, vertex;
		Triangle newTriangle;
		List<Triangle> nextTriangles = new LinkedList<>();
		for (Triangle triangle : currentTriangles) {
			Iterator<Point> iter = triangle.getVertices().iterator();
			Point p1 = iter.next();
			Point p2 = iter.next();
			Point p3 = iter.next();

			newTriangle = new Triangle(triangle).scale(0.5);
			//newTriangle.setPaint(NamedColors.RED);
			vertex = newTriangle.getVertex(2);
			midpoint = p1.getMidpoint(p2);
			newTriangle.translate(midpoint.getX() - vertex.getX(), midpoint.getY() - vertex.getY());
			nextTriangles.add(newTriangle);

			newTriangle = new Triangle(triangle).scale(0.5);
			//newTriangle.setPaint(NamedColors.BLUE);
			vertex = newTriangle.getVertex(1);
			midpoint = p1.getMidpoint(p3);
			newTriangle.translate(midpoint.getX() - vertex.getX(), midpoint.getY() - vertex.getY());
			nextTriangles.add(newTriangle);

			newTriangle = new Triangle(triangle).scale(0.5);
			//newTriangle.setPaint(NamedColors.GREEN);
			vertex = newTriangle.getVertex(0);
			midpoint = p2.getMidpoint(p3);
			newTriangle.translate(midpoint.getX() - vertex.getX(), midpoint.getY() - vertex.getY());
			nextTriangles.add(newTriangle);
		}
		fractalComponents.addAll(nextTriangles);
		currentTriangles = nextTriangles;
	}

	public OuterSierpinskiTriangle self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Outer Sierpinski Triangle";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

