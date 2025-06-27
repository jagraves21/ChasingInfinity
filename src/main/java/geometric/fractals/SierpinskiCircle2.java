package geometric.fractals;

import geometric.AbstractGeometricFractal;
import geometric.Circle;

import java.util.LinkedList;
import java.util.List;

public class SierpinskiCircle2 extends SierpinskiCircle1 {
	public SierpinskiCircle2() {
		this(getSuggestedIterations());
	}

	public SierpinskiCircle2(int iterations) {
		this(iterations, true);
	}

	public SierpinskiCircle2(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiCircle2(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 10;
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

	public SierpinskiCircle2 self() {
		return this;
	}

	public String toString() {
		return "Sierpinski Circle 2";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

