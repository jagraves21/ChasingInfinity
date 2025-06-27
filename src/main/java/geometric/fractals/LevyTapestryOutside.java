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

public class LevyTapestryOutside extends AbstractGeometricFractal<LevyTapestryOutside> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public LevyTapestryOutside() {
		this(getSuggestedIterations());
	}

	public LevyTapestryOutside(int iterations) {
		this(iterations, true);
	}

	public LevyTapestryOutside(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public LevyTapestryOutside(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		double length = 175;
		Point p1 = new Point(-length/2, -length/2, null);
		Point p2 = new Point(-length/2,  length/2, null);
		Point p3 = new Point( length/2,  length/2, null);
		Point p4 = new Point( length/2, -length/2, null);

		seed.add(new LineSegment(p1,p2,null));
		seed.add(new LineSegment(p2,p3,null));
		seed.add(new LineSegment(p3,p4,null));
		seed.add(new LineSegment(p4,p1,null));

		fractalComponents = seed;

		setPainter(PaintFactory.getRadialPainter(
			new Point(), new Point(length,length),
			new float[] {0.75f, 1.0f},
			new Color[] {NamedColors.ORANGE, NamedColors.BLUE}
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
				Point p2 = linesegment.getEnd();
				Point pTmp = p1.interpolate(p2, 1/Math.sqrt(2))
					.rotateAround(p1, 45);

				newComponents.add(new LineSegment(p1, pTmp, paint));
				newComponents.add(new LineSegment(pTmp, p2, paint));
			}
			else {
				newComponents.add(component);
			}
		}
		fractalComponents = newComponents;
	}

	public LevyTapestryOutside self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Levy Tapestry Outside";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

