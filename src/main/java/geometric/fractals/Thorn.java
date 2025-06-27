package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.Paint;

public class Thorn extends AbstractGeometricFractal<Thorn> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public Thorn() {
		this(getSuggestedIterations());
	}

	public Thorn(int iterations) {
		this(iterations, true);
	}

	public Thorn(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public Thorn(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 5;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double length = 500;
		Point p1 = new Point(-length/2, length/2, null);
		Point p2 = new Point(p1).translate(length, 0);
		Point p3 = new Point(p1).translate(length, -length);
		Point p4 = new Point(p1).translate(0, -length);

		seed.add(new LineSegment(p1, p2, null));
		seed.add(new LineSegment(p2, p3, null));
		seed.add(new LineSegment(p3, p4, null));
		seed.add(new LineSegment(p4, p1, null));

		fractalComponents = seed;
			
		setPainter(PaintFactory.getRadialPainter(
			p1.getMidpoint(p3), p1,
			new float[] {0.5f, 1.0f},
			new Color[] {NamedColors.RED, NamedColors.BLACK}
		));
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		double theta = Math.toDegrees(Math.atan(9) - (30 * Math.PI / 180.0));
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof LineSegment) {
				LineSegment lineSegment = (LineSegment) t;
				Paint paint = lineSegment.getPaint();
				Point p1 = lineSegment.getStart();
				Point p5 = lineSegment.getEnd();

				Point p2 = p1.interpolate(p5, 0.45);
				Point p4 = p5.interpolate(p1, 0.45);
				double d1 = p1.distance(p2);
				double d2 = p2.distance(p4)/2;
				double length = Math.sqrt(Math.pow(d1, 2) + Math.pow(d2, 2));
				Point p3 = p4.interpolate(p1, length/p1.distance(p4)).rotateAround(p4, theta);

				newComponents.add(new LineSegment(p1, p2, paint));
				newComponents.add(new LineSegment(p2, p3, paint));
				newComponents.add(new LineSegment(p3, p4, paint));
				newComponents.add(new LineSegment(p4, p5, paint));

			} else {
				newComponents.add(t);
			}
		}

		fractalComponents = newComponents;
	}

	public Thorn self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Thorn";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

