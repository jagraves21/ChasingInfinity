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

public class KochLine extends AbstractGeometricFractal<KochLine> {
	public static final double RATIO = 1.0/3.0;
	public static final double LEGS = RATIO;
	public static final double BASE = 1.0 - 2*RATIO;
	public static final double INTERIOR_ANGLE = Math.toDegrees( Math.acos((BASE/2)/LEGS) );
	public static final double EXTERIOR_ANGLE = INTERIOR_ANGLE - 180;

	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public KochLine() {
		this(getSuggestedIterations());
	}

	public KochLine(int iterations) {
		this(iterations, true);
	}

	public KochLine(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public KochLine(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		double length = 600;
		double height = (Math.sqrt(3) / 6.0) * length;
		Point p1 = new Point(-length/2, -height/2, null);
		Point p2 = new Point( length/2, -height/2, null);

		seed.add(new LineSegment(p1,p2,null));
		fractalComponents = seed;

		Point center = p1.getMidpoint(p2);
		Point pointOnCircle = new Point(p1);
		setPainter(PaintFactory.getRadialPainter(
			p1.getMidpoint(p2), p1,
			new float[] {0.3f, 1.0f},
			new Color[] {NamedColors.RED, NamedColors.YELLOW}
		));
			
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable component : fractalComponents) {
			if (component instanceof LineSegment) {
				LineSegment linesegment = (LineSegment) component;
				Paint paint = linesegment.getPaint();

				Point p1 = linesegment.getStart();
				Point p5 = linesegment.getEnd();
				Point p2 = p1.interpolate(p5, RATIO);
				Point p3 = new Point(p1).rotateAround(p2, EXTERIOR_ANGLE);
				Point p4 = p5.interpolate(p1, RATIO);

				newComponents.add(new LineSegment(p1, p2, paint));
				newComponents.add(new LineSegment(p2, p3, paint));
				newComponents.add(new LineSegment(p3, p4, paint));
				newComponents.add(new LineSegment(p4, p5, paint));
			}
			else {
				newComponents.add(component);
			}
		}
		fractalComponents = newComponents;
	}

	public KochLine self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Koch Line";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

