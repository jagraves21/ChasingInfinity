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

public class SierpinskiCurve extends AbstractGeometricFractal<SierpinskiCurve> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiCurve() {
		this(getSuggestedIterations());
	}

	public SierpinskiCurve(int iterations) {
		this(iterations, true);
	}

	public SierpinskiCurve(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiCurve(int iterations, boolean reset) {
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
		Point p1 = new Point(0, height/2, null);
		Point p2 = new Point(p1).translate(-base/2, -height);
		Point p3 = new Point(p1).translate(base/2, -height);

		Point t1 = p1.interpolate(p3, 0.25);
		Point t2 = p1.interpolate(p2, 0.25);
		Point t3 = p1.interpolate(p2, 0.5);
		Point t4 = p1.interpolate(p2, 0.75);
		Point t5 = p2.interpolate(p3, 0.25);
		Point t6 = p2.interpolate(p3, 0.5);
		Point t7 = p2.interpolate(p3, 0.75);
		Point t8 = p3.interpolate(p1, 0.25);
		Point t9 = p3.interpolate(p1, 0.5);

		seed.add(new LineSegment(t1, t2, null));
		seed.add(new LineSegment(t3, t2, null));
		seed.add(new LineSegment(t4, t3, null));
		seed.add(new LineSegment(t4, t5, null));
		seed.add(new LineSegment(t6, t5, null));
		seed.add(new LineSegment(t7, t6, null));
		seed.add(new LineSegment(t7, t8, null));
		seed.add(new LineSegment(t9, t8, null));
		seed.add(new LineSegment(t1, t9, null));

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
			if (t instanceof LineSegment) {
				LineSegment lineSegment = (LineSegment) t;
				Point p1 = lineSegment.getStart();
				Point p2 = lineSegment.getEnd();
				Point t1 = lineSegment.getMidpoint().rotateAround(p1, -60);
				Point t2 = lineSegment.getMidpoint().rotateAround(p2, 60);

				newComponents.add(
					new LineSegment(t1, p1, lineSegment.getPaint())
				);
				newComponents.add(
					new LineSegment(t1, t2, lineSegment.getPaint())
				);
				newComponents.add(
					new LineSegment(p2, t2, lineSegment.getPaint())
				);
			}
			else {
				newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public SierpinskiCurve self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Curve";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

