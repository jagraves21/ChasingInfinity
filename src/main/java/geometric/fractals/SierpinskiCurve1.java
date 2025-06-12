package geometric.fractals;

import utils.color.ColorUtils;
import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;

public class SierpinskiCurve1 extends AbstractGeometricFractal<SierpinskiCurve1> {
	protected Point center;
	protected Point pointOnCircle;

	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiCurve1() {
		this(getSuggestedIterations());
	}

	public SierpinskiCurve1(int iterations) {
		this(iterations, true);
	}

	public SierpinskiCurve1(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiCurve1(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 7;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double height = 400;
		double base = (2 * height) / Math.sqrt(3);
		Point p1 = new Point(0, 2*height/3, null);
		Point p2 = new Point(p1).translate(base/2, -height);
		Point p3 = new Point(p1).translate(-base/2, -height);

		Point t1 = p1.interpolate(p3, 0.25);
		Point t2 = p1.interpolate(p2, 0.25);
		Point t3 = p1.interpolate(p2, 0.5);
		Point t4 = p1.interpolate(p2, 0.75);
		Point t5 = p2.interpolate(p3, 0.25);
		Point t6 = p2.interpolate(p3, 0.5);
		Point t7 = p2.interpolate(p3, 0.75);
		Point t8 = p3.interpolate(p1, 0.25);
		Point t9 = p3.interpolate(p1, 0.5);

		seed.add(new LineSegment(t1, t2, NamedColors.RED));
		seed.add(new LineSegment(t3, t2, NamedColors.GREEN));
		seed.add(new LineSegment(t4, t3, NamedColors.BLUE));
		seed.add(new LineSegment(t4, t5, NamedColors.RED));
		seed.add(new LineSegment(t6, t5, NamedColors.GREEN));
		seed.add(new LineSegment(t7, t6, NamedColors.BLUE));
		seed.add(new LineSegment(t7, t8, NamedColors.RED));
		seed.add(new LineSegment(t9, t8, NamedColors.GREEN));
		seed.add(new LineSegment(t1, t9, NamedColors.BLUE));

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
		for (int ii=0; ii < fractalComponents.size(); ii++) {
			LineSegment lineSegment = (LineSegment) fractalComponents.get(ii);
			Point p1 = lineSegment.getStart();
			Point p2 = lineSegment.getEnd();
			Point t1 = lineSegment.getMidpoint().rotateAround(p1, -60);
			Point t2 = lineSegment.getMidpoint().rotateAround(p2, 60);

			Color c1 = (Color)((LineSegment) fractalComponents.get(
				(ii - 1 + fractalComponents.size()) % fractalComponents.size()
			)).getPaint();
			Color c2 = (Color)((LineSegment) fractalComponents.get(
				ii
			)).getPaint();
			Color c3 = (Color)((LineSegment) fractalComponents.get(
				(ii + 1) % fractalComponents.size()
			)).getPaint();

			if (p1.polarAngle() <= p2.polarAngle()) {
				Color tmp = c1;
				c1 = c3;
				c3 = tmp;
			}

			newComponents.add(
				new LineSegment(t1, p1, ColorUtils.blend(c2, c1, 2.0/3.0))
			);
			newComponents.add(
				new LineSegment(t1, t2, c2)
			);
			newComponents.add(
				new LineSegment(p2, t2, ColorUtils.blend(c2, c3, 2.0/3.0))
			);
		}
		fractalComponents = newComponents;
	}

	public SierpinskiCurve1 self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Curve 1";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

