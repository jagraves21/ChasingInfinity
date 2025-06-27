package geometric.fractals;

import geometric.AbstractGeometricFractal;
import geometric.Circle;

import java.util.LinkedList;
import java.util.List;

public class CircularInfinity3 extends CircularInfinity1 {
	public CircularInfinity3() {
		this(getSuggestedIterations());
	}

	public CircularInfinity3(int iterations) {
		this(iterations, true);
	}

	public CircularInfinity3(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public CircularInfinity3(int iterations, boolean reset) {
		super(iterations, reset);
	}
	
	protected void init() {
		super.init();
		for (Circle circle : seed) {
			circle.scale(1.25);
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
				).translate(-circle.getRadius()/2, 0)
			);
			nextCircles.add(
				new Circle(
					circle.getCenter(),
					circle.getRadius()/2,
					COLORS[this.curIteration % COLORS.length]
				).translate(circle.getRadius()/2, 0)
			);
		}
		fractalComponents.addAll(nextCircles);
		currentCircles = nextCircles;
	}

	public CircularInfinity3 self() {
		return this;
	}

	public String toString() {
		return "Circular Infinity 3";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

