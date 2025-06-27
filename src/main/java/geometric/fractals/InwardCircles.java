package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Circle;
import geometric.Point;
import geometric.Transformable;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;

public class InwardCircles extends AbstractGeometricFractal<InwardCircles> {
	protected List<Circle> seed;
	protected List<Circle> currentCircles;
	protected List<Transformable> fractalComponents;

	public InwardCircles() {
		this(getSuggestedIterations());
	}

	public InwardCircles(int iterations) {
		this(iterations, true);
	}

	public InwardCircles(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public InwardCircles(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 4;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();
		Circle circle = new Circle(0, 0, 250, null, false);

		seed.add(circle);

		currentCircles = seed;
		fractalComponents = new LinkedList<>(seed);
		
		Point center = new Point(circle.getCenter());
		Point pointOnCircle = new Point(center).translate(0, -circle.getRadius());
		float[] dist = {0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
        Color[] colors = {
			NamedColors.RED, NamedColors.ORANGE, NamedColors.YELLOW,
			NamedColors.GREEN, NamedColors.BLUE
		};
		setPainter(PaintFactory.getRadialPainter(
			center, pointOnCircle, dist, colors
		));
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

	public InwardCircles self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Inward Circles";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

