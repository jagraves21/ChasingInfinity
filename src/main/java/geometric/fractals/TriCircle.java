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

public class TriCircle extends AbstractGeometricFractal<TriCircle> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Circle> seed;
	protected List<Circle> currentCircles;
	protected List<Transformable> fractalComponents;

	public TriCircle() {
		this(getSuggestedIterations());
	}

	public TriCircle(int iterations) {
		this(iterations, true);
	}

	public TriCircle(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public TriCircle(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 10;
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

		center = new Point(circle.getCenter());
		pointOnCircle = new Point(center).translate(0, -circle.getRadius());
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

	public TriCircle self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Tri Circle";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

