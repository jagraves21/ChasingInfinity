package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
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

public class SierpinskiCircle extends AbstractGeometricFractal<SierpinskiCircle> {
	protected Point center;
	protected Point pointOnCircle;

	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiCircle() {
		this(getSuggestedIterations());
	}

	public SierpinskiCircle(int iterations) {
		this(iterations, true);
	}

	public SierpinskiCircle(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiCircle(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	public Paint getPaint() {
		return Color.CYAN;
		/*return getPaint(
			this.center,
			this.pointOnCircle
		);*/
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return Color.CYAN;
		/*return getPaint(
			this.center.toScreen(worldViewer),
			this.pointOnCircle.toScreen(worldViewer)
		);*/
	}

	protected Paint getPaint(Point center, Point pointOnCircle) {
		return Color.CYAN;
		/*return new RadialGradientPaint(
			(float) center.getX(),
			(float) center.getY(),
			(float) center.distance(pointOnCircle),
			new float[] {0.0f, 1.0f},
			new Color[] {NamedColors.BLUE, NamedColors.RED}
			//MultipleGradientPaint.CycleMethod.REPEAT
		);*/
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		List<Triangle> nextTriangles = new LinkedList<>();
		Point p1 = new Point(0, 0, 1);
		Point p2 = new Point(0.5, Math.sqrt(3)/2, 1);
		Point p3 = new Point(1, 0, 1);
		nextTriangles.add(
			new Triangle(new Point[] {p1,p2,p3}, null, false)
		);

		seed.addAll(nextTriangles.get(0).getVertices());
		for (int ii=0; ii < 10; ii++) {
			List<Triangle> newTriangles = new LinkedList<>();
			for (Triangle triangle : nextTriangles) {
				Iterator<Point> iter = triangle.getVertices().iterator();
				p1 = iter.next();
				p2 = iter.next();
				p3 = iter.next();

				Point t2 = p1.getMidpoint(p2);
				Point t1 = p1.getMidpoint(p3);
				Point t3 = p2.getMidpoint(p3);
				seed.add(t1);
				seed.add(t2);
				seed.add(t3);

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
			nextTriangles = newTriangles;
		}

		fractalComponents = seed;

		this.scale(250);
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable t : seed) {
			if (t instanceof Point) {
				Point point = (Point) t;
				double theta = 2 * Math.PI* point.getX();
				double radius = point.getY();
				newComponents.add(new Point(
					radius * Math.cos(theta),
					radius * Math.sin(theta),
					1
				));
			}
			else {
				newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public SierpinskiCircle self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Circle";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

