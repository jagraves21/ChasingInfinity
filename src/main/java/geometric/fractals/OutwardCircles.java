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

public class OutwardCircles extends AbstractGeometricFractal<OutwardCircles> {
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

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double radius = 250/3.0;
		Circle circle = new Circle(0, 0, radius, null, false);
		seed.add(circle);

		currentCircles = seed;
		fractalComponents = new LinkedList<>(seed);

		setPainter(PaintFactory.getRadialPainter(
			new Point(0,0), new Point(0, 2.5*radius),
			new float[] {0.0f, 0.25f, 0.5f, 0.75f, 1.0f},
			new Color[] {
				NamedColors.RED, NamedColors.ORANGE, NamedColors.YELLOW,
				NamedColors.GREEN, NamedColors.BLUE
			}
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

