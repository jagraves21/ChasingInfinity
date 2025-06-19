package geometric.fractals;

import utils.color.NamedPalettes;

import renderer.viewer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Polygon;
import geometric.Transformable;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class TornSquare extends AbstractGeometricFractal<TornSquare> {
	public static final double RATIO = 0.48;
	public static final double LEGS = RATIO;
	public static final double BASE = 1.0 - 2*RATIO;
	public static final double INTERIOR_ANGLE = -Math.toDegrees( Math.acos((BASE/2)/LEGS) );
	public static final double EXTERIOR_ANGLE = INTERIOR_ANGLE - 180;

	protected Point center;
	protected Point pointOnCircle;
	protected Polygon seed;
	protected List<Transformable> fractalComponents;

	public TornSquare() {
		this(getSuggestedIterations());
	}

	public TornSquare(int iterations) {
		this(iterations, true);
	}

	public TornSquare(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public TornSquare(int iterations, boolean reset) {
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
		Color[] colors = NamedPalettes.GAS_ON_WATER;
		float[] fractions = new float[colors.length];
		for (int ii=0; ii < fractions.length; ii++) {
			fractions[ii] = ii / (float) (fractions.length-1);
		}

		return new RadialGradientPaint(
			center.asAWTPoint(),
			(float) center.distance(pointOnCircle),
			center.interpolate(pointOnCircle, 0.5).asAWTPoint(),
			fractions,
			colors,
			MultipleGradientPaint.CycleMethod.NO_CYCLE
		);
	}

	protected void init() {
		super.init();

		double length = 500;
		Point p1 = new Point(-length/2,  length/2, null);
		Point p2 = new Point( length/2,  length/2, null);
		Point p3 = new Point( length/2, -length/2, null);
		Point p4 = new Point(-length/2, -length/2, null);
		this.seed = new Polygon(
			new Point[] {p1, p2, p3, p4},
			new Color(243, 165, 74),
			true
		);

		center = p1.getMidpoint(p3);
		pointOnCircle = new Point(p1);
		fractalComponents = new LinkedList<>();
		fractalComponents.add(seed);
	}

	public void reset() {
		super.reset();
		fractalComponents.clear();
		fractalComponents.add(seed);
	}

	public void step() {
		Polygon polygon = (Polygon) fractalComponents.get(0);

		List<Point> points = new LinkedList<>();
		Point p1 = polygon.getVertex(polygon.getNPoints()-1);
		Iterator<Point> iter = polygon.getVertices().iterator();
		while (iter.hasNext()) {
			Point p5 = iter.next();
			Point p2 = p1.interpolate(p5, RATIO);
			Point p3 = new Point(p1).rotateAround(p2, EXTERIOR_ANGLE);
			Point p4 = p5.interpolate(p1, RATIO);
			points.add(p2);
			points.add(p3);
			points.add(p4);
			points.add(p5);
			p1 = p5;
		}
		fractalComponents.clear();
		fractalComponents.add(
			new Polygon(points, polygon.getPaint(), polygon.isFilled())
		);

		/*for (Transformable component : fractalComponents) {
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
		fractalComponents = newComponents;*/
	}

	public void draw(Graphics g, WorldViewer worldViewer) {
		Graphics tmpG = g.create();
		tmpG.setColor(new Color(4,10,123));
		tmpG.fillRect(
			0, 0, worldViewer.getScreenWidth(), worldViewer.getScreenHeight()
		);
		tmpG.dispose();
		super.draw(g, worldViewer);
	}

	public TornSquare self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Torn Square";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

