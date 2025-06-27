package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Circle;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;

public class SierpinskiCircle1 extends AbstractGeometricFractal<SierpinskiCircle1> {
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

	protected void init() {
		super.init();

		seed = new LinkedList<>();
		Triangle triangle = Triangle.createEquilateralFromTop(
			new Point(0, 250), 500, null, false
		);
		Circle circle = triangle.circumcircle();//new Circle(0, 0, 250, null, false);

		seed.add(circle);
		
		currentCircles = seed;
		fractalComponents = new LinkedList<>(seed);

		setPainter(PaintFactory.getRadialPainter(
			triangle.centroid(), triangle.getVertex(0), NamedColors.BLUE, NamedColors.RED
		));
		/*setPainter(PaintFactory.getTriGradientPainter(
			triangle.getVertex(0), NamedColors.RED,
			triangle.getVertex(1), NamedColors.BLUE,
			triangle.getVertex(2), NamedColors.GREEN
		));*/
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

