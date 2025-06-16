package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Circle;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class SierpinskiCircle1 extends AbstractGeometricFractal<SierpinskiCircle1> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Circle> seed;
	protected List<Circle> currentCircles;
	protected List<Transformable> fractalComponents;

	public SierpinskiCircle1() {
		this(getSuggestedIterations());
	}

	public SierpinskiCircle1(int iterations) {
		this(iterations, true);
	}

	public SierpinskiCircle1(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiCircle1(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 7;
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
			new float[] {0.0f, 1.0f},
			new Color[] {NamedColors.BLUE, NamedColors.RED}
			//MultipleGradientPaint.CycleMethod.REPEAT
		);
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();
		Circle circle = new Circle(0, 0, 250, null, false);

		seed.add(circle);

		double height = 500;
		double base = (2 * height) / Math.sqrt(3);
		Point p1 = new Point(0, height/2);
		Point p2 = new Point(p1).translate(-base/2, -height);
		Point p3 = new Point(p1).translate(base/2, -height);
		center = new Triangle(new Point[] {p1,p2,p3}).centroid();
		pointOnCircle = new Point(p1);
		currentCircles = seed;
		fractalComponents = new LinkedList<>(seed);
	}

	public void reset() {
		super.reset();
		currentCircles = seed;
		fractalComponents.clear();
		fractalComponents.addAll(seed);
	}

	public void step() {
		List<Circle> nextCircles = new LinkedList<>();
		for (Circle circle : currentCircles) {
			double newRadius  = 3/(3 + 2*Math.sqrt(3)) * circle.getRadius();
			Circle newCricle = new Circle(
				circle.getX(),
				circle.getY() + (circle.getRadius() - newRadius),
				newRadius,
				circle.getPaint(),
				circle.isFilled()
			);
			nextCircles.add(newCricle);
			nextCircles.add(
				new Circle(newCricle).rotateAround(circle.getCenter(), 120)
			);
			nextCircles.add(
				new Circle(newCricle).rotateAround(circle.getCenter(), -120)
			);

		}
		fractalComponents.addAll(nextCircles);
		currentCircles = nextCircles;
	}

	public SierpinskiCircle1 self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Circle 1";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

