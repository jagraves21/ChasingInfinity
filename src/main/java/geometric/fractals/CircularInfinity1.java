package geometric.fractals;

import utils.color.NamedPalettes;

import geometric.AbstractGeometricFractal;
import geometric.Circle;
import geometric.Point;
import geometric.Transformable;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;

public class CircularInfinity1 extends AbstractGeometricFractal<CircularInfinity1> {
	public static final Color[] COLORS = NamedPalettes.GAS_ON_WATER;

	protected List<Circle> seed;
	protected List<Circle> currentCircles;
	protected List<Transformable> fractalComponents;

	public CircularInfinity1() {
		this(getSuggestedIterations());
	}

	public CircularInfinity1(int iterations) {
		this(iterations, true);
	}

	public CircularInfinity1(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public CircularInfinity1(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 5;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();
		Circle circle = new Circle(0, 0, 200, COLORS[0], false);

		seed.add(circle);

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
			nextCircles.add(
				new Circle(
					circle.getCenter(),
					circle.getRadius()/2,
					COLORS[this.curIteration % COLORS.length]
				).translate(-circle.getRadius(), 0)
			);
			nextCircles.add(
				new Circle(
					circle.getCenter(),
					circle.getRadius()/2,
					COLORS[this.curIteration % COLORS.length]
				).translate(circle.getRadius(), 0)
			);
		}
		fractalComponents.addAll(nextCircles);
		currentCircles = nextCircles;
	}

	public CircularInfinity1 self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Circular Infinity 1";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

