package geometric.fractals;

import utils.color.NamedPalettes;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Polygon;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class TurningSquare extends AbstractGeometricFractal<TurningSquare> {
	protected Point center;
	protected Point pointOnCircle;
	protected Polygon seedSquare;
	protected Polygon currentSquare;
	protected List<Transformable> fractalComponents;

	public TurningSquare() {
		this(getSuggestedIterations());
	}

	public TurningSquare(int iterations) {
		this(iterations, true);
	}

	public TurningSquare(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public TurningSquare(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 200;
	}

	public static int getSuggestedUPS() {
		return 20;
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
		Color[] colors = NamedPalettes.OIL_SLICK;
		float[] dist = new float[colors.length];
		for(int ii=0; ii < dist.length; ii++) {
			dist[ii] = ii / (float) (dist.length-1);
		}
		return new RadialGradientPaint(
			(float) center.getX(),
			(float) center.getY(),
			(float) center.distance(pointOnCircle),
			dist,
			colors
		);
	}

	protected void init() {
		super.init();


		double length = 500;
		Point p1 = new Point(-length/2, length/2);
		Point p2 = new Point(p1).translate(length, 0);
		Point p3 = new Point(p1).translate(length, -length);
		Point p4 = new Point(p1).translate(0, -length);

		seedSquare = new Polygon(new Point[] {p1,p2,p3,p4}, null, false);

		center = p1.getMidpoint(p3);
		pointOnCircle = new Point(p2);

		currentSquare = seedSquare;
		fractalComponents = new LinkedList<>();
		fractalComponents.add(seedSquare);
	}

	public void reset() {
		super.reset();
		currentSquare = seedSquare;
		fractalComponents.clear();
		fractalComponents.add(seedSquare);
	}

	public void step() {
		Iterator<Point> iter = currentSquare.getVertices().iterator();
		Point p1 = iter.next();
		Point p2 = iter.next();
		Point p3 = iter.next();
		Point p4 = iter.next();

		Point tp1 = p1.interpolate(p2, 0.95);
		Point tp2 = p2.interpolate(p3, 0.95);
		Point tp3 = p3.interpolate(p4, 0.95);
		Point tp4 = p4.interpolate(p1, 0.95);

		currentSquare = new Polygon(
			new Point[] {tp1, tp2, tp3, tp4},
			currentSquare.getPaint(),
			currentSquare.isFilled()
		);
		fractalComponents.add(currentSquare);
	}

	public TurningSquare self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Turning Square";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

