package geometric.fractals;

import geometric.AbstractGeometricFractal;
import geometric.Circle;

import java.util.LinkedList;
import java.util.List;

public class CircularInfinity4 extends CircularInfinity1 {
	public CircularInfinity4() {
		this(getSuggestedIterations());
	}

	public CircularInfinity4(int iterations) {
		this(iterations, true);
	}

	public CircularInfinity4(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public CircularInfinity4(int iterations, boolean reset) {
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
			nextCircles.add(
				new Circle(
					circle.getCenter(),
					circle.getRadius()/2,
					COLORS[this.curIteration % COLORS.length]
				).translate(0, -circle.getRadius()/2)
			);
			nextCircles.add(
				new Circle(
					circle.getCenter(),
					circle.getRadius()/2,
					COLORS[this.curIteration % COLORS.length]
				).translate(0, circle.getRadius()/2)
			);
		}
		fractalComponents.addAll(nextCircles);
		currentCircles = nextCircles;
	}

	public CircularInfinity4 self() {
		return this;
	}

	public String toString() {
		return "Circular Infinity 4";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

