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

public class CrossKochCurve extends AbstractGeometricFractal<CrossKochCurve> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public CrossKochCurve() {
		this(getSuggestedIterations());
	}

	public CrossKochCurve(int iterations) {
		this(iterations, true);
	}

	public CrossKochCurve(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public CrossKochCurve(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		double length = 500;
		Point p1 = new Point(-length/2,  length/2, null);
		Point p2 = new Point( length/2, -length/2, null);

		int count = 2;
		for(int ii=0; ii < count; ii++) {
			double angle = (ii / (double) count) * 180;
			Point tmpP1 = new Point(p1).rotate(angle);
			Point tmpP2 = new Point(p2).rotate(angle);
			seed.add(new LineSegment(tmpP1,tmpP2,null));
			seed.add(new LineSegment(tmpP2,tmpP1,null));
		}

		fractalComponents = seed;

		setPainter(PaintFactory.getRadialPainter(
			new Point(0,0), p1,
			new float[] {0.3f, 1.0f},
			new Color[] {NamedColors.WHITE, NamedColors.BLUE}
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
				Point p2 = p1.interpolate(p5, 1.0/3.0);
				Point p4 = p1.interpolate(p5, 2.0/3.0);

				newComponents.add(new LineSegment(p1,p2,paint));
				newComponents.add(new LineSegment(p2,p4,paint).rotateAround(p2,60));
				newComponents.add(new LineSegment(p2,p4,paint).rotateAround(p4,-60));
				newComponents.add(new LineSegment(p4,p5,paint));
			}
			else {
				newComponents.add(component);
			}
		}
		fractalComponents = newComponents;
	}

	public CrossKochCurve self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Cross Koch Curve";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

