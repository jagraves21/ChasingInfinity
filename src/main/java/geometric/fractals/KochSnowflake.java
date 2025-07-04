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

import java.awt.Paint;

public class KochSnowflake extends AbstractGeometricFractal<KochSnowflake> {
	public static final double RATIO = 1.0/3.0;
	public static final double LEGS = RATIO;
	public static final double BASE = 1.0 - 2*RATIO;
	public static final double INTERIOR_ANGLE = Math.toDegrees( Math.acos((BASE/2)/LEGS) );
	public static final double EXTERIOR_ANGLE = INTERIOR_ANGLE - 180;

	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public KochSnowflake() {
		this(getSuggestedIterations());
	}

	public KochSnowflake(int iterations) {
		this(iterations, true);
	}

	public KochSnowflake(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public KochSnowflake(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		Point origin = new Point(0, 0, null);//, NamedColors.WHITE);
		Point p1 = new Point(origin).translate(0.0, 250.0);
		Point p2 = new Point(p1).rotate(-DEG_120);
		Point p3 = new Point(p1).rotate(DEG_120);

		seed.add(new LineSegment(p1,p2,null));
		seed.add(new LineSegment(p2,p3,null));
		seed.add(new LineSegment(p3,p1,null));

		fractalComponents = seed;
		
		Point topLeft = new Point(p1).rotate(DEG_45);
		Point bottomRight = new Point(p1).rotate(-DEG_135);
		setPainter(PaintFactory.getLinearPainter(
			topLeft, bottomRight, NamedColors.BLUE, NamedColors.CYAN
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

	public KochSnowflake self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
		/*
		return new Iterator<Transformable>() {
			private final Iterator<Transformable> it = fractalComponents.iterator();
			public boolean hasNext() {
				return it.hasNext();
			}
			public Point next() {
				return ((LineSegment)it.next()).getStart();
			}
		};
		*/
	}

	public String toString() {
		return "Koch Snowflake";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

