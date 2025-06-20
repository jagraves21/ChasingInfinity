package geometric.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class CesaroFractal extends AbstractGeometricFractal<CesaroFractal> {
	public static final double RATIO = 1.0/3.0;
	public static final double LEGS = RATIO;
	public static final double BASE = 1.0 - 2*RATIO;
	public static final double INTERIOR_ANGLE = -Math.toDegrees( Math.acos((BASE/2)/LEGS) );
	public static final double EXTERIOR_ANGLE = INTERIOR_ANGLE - 180;

	protected Point center;
	protected Point pointOnCircle;
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public CesaroFractal() {
		this(getSuggestedIterations());
	}

	public CesaroFractal(int iterations) {
		this(iterations, true);
	}

	public CesaroFractal(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public CesaroFractal(int iterations, boolean reset) {
		super(iterations, reset);
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
			center.toAWTPoint(),
			(float) center.distance(pointOnCircle),
			new float[] {0.5f, 1.0f},
			new Color[] {NamedColors.RED, NamedColors.BLUE},
			MultipleGradientPaint.CycleMethod.NO_CYCLE
		);
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		double length = 500;
		Point p1 = new Point(-length/2,  length/2, null);
		Point p2 = new Point( length/2,  length/2, null);
		Point p3 = new Point( length/2, -length/2, null);
		Point p4 = new Point(-length/2, -length/2, null);
		seed.add(new LineSegment(p1,p2,null));
		seed.add(new LineSegment(p2,p3,null));
		seed.add(new LineSegment(p3,p4,null));
		seed.add(new LineSegment(p4,p1,null));

		center = p1.getMidpoint(p3);
		pointOnCircle = new Point(p1);
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

	public CesaroFractal self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Cesaro Fractal";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

