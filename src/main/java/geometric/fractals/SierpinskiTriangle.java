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

public class SierpinskiTriangle extends AbstractGeometricFractal<SierpinskiTriangle> {
	protected Point center;
	protected Point pointOnCircle;

	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiTriangle() {
		this(getSuggestedIterations());
	}

	public SierpinskiTriangle(int iterations) {
		this(iterations, true);
	}

	public SierpinskiTriangle(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiTriangle(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 10;
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

		/*Point t1 = p1.getMidpoint(p3);
		Point t2 = p1.getMidpoint(p2);
		Point t3 = p2.getMidpoint(p3);

		seed.add(
			new Triangle(new Point[] {p1,t2,t1}, NamedColors.RED, true)
		);
		seed.add(
			new Triangle(new Point[] {t1,t3,p3}, NamedColors.GREEN, true)
		);
		seed.add(
			new Triangle(new Point[] {t2,p2,t3}, NamedColors.BLUE, true)
		);*/

		center = new Triangle(new Point[] {p1,p2,p3}).centroid();
		pointOnCircle = new Point(p1);
		fractalComponents = seed;
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof Triangle) {
				Triangle triangle = (Triangle) t;
				Iterator<Point> iter = triangle.getVertices().iterator();
				Point p1 = iter.next();
				Point p2 = iter.next();
				Point p3 = iter.next();

				Point t2 = p1.getMidpoint(p2);
				Point t1 = p1.getMidpoint(p3);
				Point t3 = p2.getMidpoint(p3);

				newComponents.add(
					new Triangle(new Point[] {p1,t2,t1}, triangle.getPaint(), triangle.isFilled())
				);
				newComponents.add(
					new Triangle(new Point[] {t1,t3,p3}, triangle.getPaint(), triangle.isFilled())
				);
				newComponents.add(
					new Triangle(new Point[] {t2,p2,t3}, triangle.getPaint(), triangle.isFilled())
				);
			}
			else {
				newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public SierpinskiTriangle self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Triangle";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

