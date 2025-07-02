package geometric.fractals;

import utils.color.NamedPalettes;

import geometric.AbstractGeometricFractal;
import geometric.Circle;
import geometric.Point;
import geometric.Transformable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.awt.Color;

public class CircleInversion extends AbstractGeometricFractal<CircleInversion> {
	public static final int WIDTH = 450;
	public static final int HEIGHT = 450;
	public static final Color[] COLORS = NamedPalettes.GAS_ON_WATER;

	protected Random random;
	protected List<Circle> seed;
	protected List<Circle> currentCircles;
	protected List<Transformable> fractalComponents;

	public CircleInversion() {
		this(getSuggestedIterations());
	}

	public CircleInversion(int iterations) {
		this(iterations, true);
	}

	public CircleInversion(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public CircleInversion(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 3;
	}

	protected void init() {
		super.init();

		this.random = new java.util.Random(1355959557249L);

		seed = new LinkedList<>();

		int maxCircles = 10;
		int maxTries = 500;
		double scale = .25;
		Random tmpRandom = new java.util.Random(1355959557249L);
		while (seed.size() < maxCircles	&& maxTries-- > 0) {
			double x = (tmpRandom.nextDouble() * WIDTH) - (WIDTH/2.0);
			double y = -1 * ((tmpRandom.nextDouble() * HEIGHT) - (HEIGHT/2.0));
			double radius = WIDTH * scale;
			Circle cNew = new Circle(new Point(x, y), radius, getRandomColor(), false);

			boolean overlaps = false;
			for (Circle cOld : seed) {
				if (cNew.overlaps(cOld)) {
					overlaps = true;
					break;
				}
			}

			if (!overlaps) {
				seed.add(cNew);
				scale *= .9;
			}
		}

		currentCircles = seed;
		fractalComponents = new LinkedList<>(seed);
	}

	protected Color getRandomColor() {
		int red = (int) Math.round(255 * random.nextDouble());
		int green = (int) Math.round(255 * random.nextDouble());
		int blue = (int) Math.round(255 * random.nextDouble());

		return new Color(red, green, blue);
	}

	public void reset() {
		super.reset();
		currentCircles = seed;
		fractalComponents.clear();
		fractalComponents.addAll(seed);
	}

	public void step() {
		List<Circle> nextCircles = new LinkedList<>();
		for (Circle cOuter : seed) {
			for (Circle cInner : currentCircles) {
				if (cInner != cOuter && !cInner.overlaps(cOuter)) {
					nextCircles.add(cOuter.invert(cInner));
				}
			}	
		}
		
		fractalComponents.addAll(nextCircles);
		currentCircles = nextCircles;
	}

	public CircleInversion self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Circle Inversion";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

