package geometric.fractals;

import geometric.AbstractGeometricFractal;
import geometric.Polygon;

public class BinarySierpinskiTriangleFilled extends BinarySierpinskiTriangle {

	public BinarySierpinskiTriangleFilled() {
		this(getSuggestedIterations());
	}

	public BinarySierpinskiTriangleFilled(int iterations) {
		this(iterations, true);
	}

	public BinarySierpinskiTriangleFilled(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public BinarySierpinskiTriangleFilled(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 6;
	}

	protected void init() {
		super.init();
		Polygon polygon = (Polygon) seed.get(0);
		polygon.setFill(true);
	}

	public BinarySierpinskiTriangleFilled self() {
		return this;
	}

	public String toString() {
		return "Binary Sierpinski Triangle (Filled)";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

