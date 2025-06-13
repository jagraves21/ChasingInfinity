package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Circle;
import geometric.Point;
import geometric.Transformable;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class OutwardCircles extends AbstractGeometricFractal<OutwardCircles> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Circle> seed;
	protected List<Circle> currentCircles;
	protected List<Transformable> fractalComponents;

	public OutwardCircles() {
		this(getSuggestedIterations());
	}

	public OutwardCircles(int iterations) {
		this(iterations, true);
	}

	public OutwardCircles(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public OutwardCircles(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 5;
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
		float[] dist = {0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
        Color[] colors = {
			NamedColors.RED, NamedColors.ORANGE, NamedColors.YELLOW,
			NamedColors.GREEN, NamedColors.BLUE
		};
		return new RadialGradientPaint(
			(float) center.getX(),
			(float) center.getY(),
			(float) center.distance(pointOnCircle),
			dist,
			colors
			//MultipleGradientPaint.CycleMethod.REPEAT
		);
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();
		Circle circle = new Circle(0, 0, 250/3, null, false);

		seed.add(circle);

		center = new Point(circle.getCenter());
		pointOnCircle = new Point(center).translate(0, 2.5*circle.getRadius());
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
			Circle newCricle = new Circle(
				circle.getX(),
				circle.getY() + circle.getRadius()/2,
				circle.getRadius()/2,
				circle.getPaint(),
				circle.isFilled()
			).translate(0, circle.getRadius());

			nextCircles.add(newCricle);
			nextCircles.add(
				new Circle(newCricle).rotateAround(circle.getCenter(), 90)
			);
			nextCircles.add(
				new Circle(newCricle).rotateAround(circle.getCenter(), 180)
			);
			nextCircles.add(
				new Circle(newCricle).rotateAround(circle.getCenter(), 270)
			);
		}
		fractalComponents.addAll(nextCircles);
		currentCircles = nextCircles;
	}

	public OutwardCircles self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Outward Circles";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

