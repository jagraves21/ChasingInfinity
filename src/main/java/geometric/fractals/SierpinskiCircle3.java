package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Circle;
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

public class SierpinskiCircle3 extends AbstractGeometricFractal<SierpinskiCircle3> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Triangle> seed;
	protected List<Triangle> currentTriangles;
	protected List<Transformable> fractalComponents;

	public SierpinskiCircle3() {
		this(getSuggestedIterations());
	}

	public SierpinskiCircle3(int iterations) {
		this(iterations, true);
	}

	public SierpinskiCircle3(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiCircle3(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 8;
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

		double height = 500;
		double base = (2 * height) / Math.sqrt(3);
		Point p1 = new Point(0, height/2);
		Point p2 = new Point(p1).translate(-base/2, -height);
		Point p3 = new Point(p1).translate(base/2, -height);

		seed.add(
			new Triangle(new Point[] {p1,p2,p3}, null, false)
		);

		center = new Triangle(new Point[] {p1,p2,p3}).centroid();
		pointOnCircle = new Point(p1);
		currentTriangles = seed;
		fractalComponents = new LinkedList<>();
		fractalComponents.add(seed.get(0).incircle());
	}

	public void reset() {
		super.reset();
		currentTriangles = seed;
		fractalComponents.clear();
		fractalComponents.add(seed.get(0).incircle());
	}

	public void step() {
		List<Triangle> newTriangles = new LinkedList<>();
		for (Triangle triangle : currentTriangles) {
			Iterator<Point> iter = triangle.getVertices().iterator();
			Point p1 = iter.next();
			Point p2 = iter.next();
			Point p3 = iter.next();

			Point t2 = p1.getMidpoint(p2);
			Point t1 = p1.getMidpoint(p3);
			Point t3 = p2.getMidpoint(p3);

			newTriangles.add(
				new Triangle(new Point[] {p1,t2,t1}, triangle.getPaint(), triangle.isFilled())
			);
			newTriangles.add(
				new Triangle(new Point[] {t1,t3,p3}, triangle.getPaint(), triangle.isFilled())
			);
			newTriangles.add(
				new Triangle(new Point[] {t2,p2,t3}, triangle.getPaint(), triangle.isFilled())
			);
		}
		currentTriangles = newTriangles;
		//fractalComponents.clear();
		for (Triangle triangle : currentTriangles) {
			fractalComponents.add(triangle.incircle());
		}
	}

	public SierpinskiCircle3 self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Circle 3";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

