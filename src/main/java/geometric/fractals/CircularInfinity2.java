package geometric.fractals;

import geometric.AbstractGeometricFractal;
import geometric.Circle;

import java.util.LinkedList;
import java.util.List;

public class CircularInfinity2 extends CircularInfinity1 {
	public CircularInfinity2() {
		this(getSuggestedIterations());
	}

	public CircularInfinity2(int iterations) {
		this(iterations, true);
	}

	public CircularInfinity2(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public CircularInfinity2(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected void init() {
		super.init();
		for (Circle circle : seed) {
			circle.scale(2.0/3.0);
		}
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
			nextCircles.add(
				new Circle(
					circle.getCenter(),
					circle.getRadius()/2,
					COLORS[this.curIteration % COLORS.length]
				).translate(0, -circle.getRadius())
			);
			nextCircles.add(
				new Circle(
					circle.getCenter(),
					circle.getRadius()/2,
					COLORS[this.curIteration % COLORS.length]
				).translate(0, circle.getRadius())
			);
		}
		fractalComponents.addAll(nextCircles);
		currentCircles = nextCircles;
	}

	public CircularInfinity2 self() {
		return this;
	}

	public String toString() {
		return "Circular Infinity 2";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

