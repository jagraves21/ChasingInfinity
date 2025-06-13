package geometric.fractals;

import utils.color.NamedColors;

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

public class LevyCurve extends AbstractGeometricFractal<LevyCurve> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public LevyCurve() {
		this(getSuggestedIterations());
	}

	public LevyCurve(int iterations) {
		this(iterations, true);
	}

	public LevyCurve(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public LevyCurve(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
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
		return new RadialGradientPaint(
			(float) center.getX(),
			(float) center.getY(),
			(float) center.distance(pointOnCircle),
			new float[] {0.5f, 1.0f},
			new Color[] {NamedColors.YELLOW, NamedColors.RED}
			//MultipleGradientPaint.CycleMethod.REPEAT
		);
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		double length = 350;
		double height = -100;
		Point p1 = new Point(-length/2, height, null);
		Point p2 = new Point( length/2, height, null);

		seed.add(new LineSegment(p1,p2,null));

		center = new Point(0, -height);
		pointOnCircle = new Point(length, -height);
		fractalComponents = seed;
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

	public LevyCurve self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Levy Curve";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

