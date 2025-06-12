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

public class SierpinskiArrowhead extends AbstractGeometricFractal<SierpinskiArrowhead> {
	protected Point center;
	protected Point pointOnCircle;

	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiArrowhead() {
		this(getSuggestedIterations());
	}

	public SierpinskiArrowhead(int iterations) {
		this(iterations, true);
	}

	public SierpinskiArrowhead(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiArrowhead(int iterations, boolean reset) {
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

		seed.add(
			new LineSegment(p3, p2, null)
		);

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

	public SierpinskiArrowhead self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Arrowhead";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

