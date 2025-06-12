package geometric.fractals;

import utils.color.ColorUtils;
import utils.color.NamedColors;
import utils.color.NamedPalettes;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.Paint;

public class CesaroCurves extends AbstractGeometricFractal<CesaroCurves> {
	public static final int N_STEPS = 6;

	protected double RATIO = 0.49;
	protected double LEGS = RATIO;
	protected double BASE = 1.0 - 2*RATIO;
	protected double INTERIOR_ANGLE = -Math.toDegrees( Math.acos((BASE/2)/LEGS) );
	protected double EXTERIOR_ANGLE = INTERIOR_ANGLE - 180;

	Point center;
	Point pointOnCircle;
	Color[] colors = null;

	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public CesaroCurves() {
		this(getSuggestedIterations());
	}

	public CesaroCurves(int iterations) {
		this(iterations, true);
	}

	public CesaroCurves(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public CesaroCurves(int iterations, boolean reset) {
		super(iterations, reset);
		colors = ColorUtils.generateGradient(
			NamedPalettes.OIL_SLICK, (int) Math.pow(4, N_STEPS+1)
		);
	}

	public static int getSuggestedIterations() {
		return 100;
	}

	public static int getSuggestedUPS() {
		return 15;
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
		Color c1 = colors[curIteration % colors.length];
		Color c2 = colors[
			(colors.length - (curIteration % colors.length)) % colors.length
		];

		return new RadialGradientPaint(
			(float) center.getX(),
			(float) center.getY(),
			(float) center.distance(pointOnCircle),
			new float[] {0.3f, 1.0f},
			new Color[] {NamedColors.RED, NamedColors.YELLOW}
			//new Color[] {c1, c2}
			//MultipleGradientPaint.CycleMethod.REPEAT
		);
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		/*double length = 600;
		double height = (Math.sqrt(3) / 6.0) * length;
		Point p1 = new Point(-length/2, -height/2, null);
		Point p2 = new Point( length/2, -height/2, null);
		seed.add(new LineSegment(p1,p2,null));*/

		double length = 500;
		Point p1 = new Point(-length/2,  length/2, null);
		Point p2 = new Point( length/2,  length/2, null);
		Point p3 = new Point( length/2, -length/2, null);
		Point p4 = new Point(-length/2, -length/2, null);
		seed.add(new LineSegment(p1,p2,null));
		seed.add(new LineSegment(p2,p3,null));
		seed.add(new LineSegment(p3,p4,null));
		seed.add(new LineSegment(p4,p1,null));

		center = p1.getMidpoint(p2);
		pointOnCircle = new Point(p1);
		step();
	}

	public void reset() {
		super.reset();
		step();
	}

	public void step() {
		double percent = 0;
		if (maxIterations != 0) {
			percent = curIteration / (double) maxIterations;
			int newMax = maxIterations/2;
			int newCur = newMax -
				Math.abs(
					(curIteration % maxIterations) -
					newMax
				);
			percent = newCur / (double) newMax;
		}

		RATIO = 0.25 + percent * (0.5-0.25);
		LEGS = RATIO;
		BASE = 1.0 - 2*RATIO;
		INTERIOR_ANGLE = -Math.toDegrees( Math.acos((BASE/2)/LEGS) );
		EXTERIOR_ANGLE = INTERIOR_ANGLE - 180;

		fractalComponents = seed;
		for (int ii=0; ii < N_STEPS; ii++) {
			stepHelper();
		}
	}
	protected void stepHelper() {
		int ii=0;
		if (colors == null) colors = NamedPalettes.OIL_SLICK;
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

				newComponents.add(new LineSegment(p1, p2, colors[ii++ % colors.length]));
				newComponents.add(new LineSegment(p2, p3, colors[ii++ % colors.length]));
				newComponents.add(new LineSegment(p3, p4, colors[ii++ % colors.length]));
				newComponents.add(new LineSegment(p4, p5, colors[ii++ % colors.length]));
			} else {
				newComponents.add(component);
			}
		}

		fractalComponents = newComponents;
	}


	public CesaroCurves self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Cesaro Curves";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

