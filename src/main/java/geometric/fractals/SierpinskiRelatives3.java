package geometric.fractals;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Polygon;

public class SierpinskiRelatives3 extends SierpinskiRelatives {
	public SierpinskiRelatives3() {
		this(getSuggestedIterations());
	}

	public SierpinskiRelatives3(int iterations) {
		this(iterations, true);
	}

	public SierpinskiRelatives3(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiRelatives3(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected Polygon translateTopLeft(Polygon square, Point[] points) {
		return super.translateTopLeft(
			square, rotate(points, 3)
		);
	}

	protected Polygon translateTopRight(Polygon square, Point[] points) {
		return super.translateTopRight(
			square, points
		);
	}

	protected Polygon translateBottomLeft(Polygon square, Point[] points) {
		return super.translateBottomLeft(
			square, points
		);
	}

	protected Polygon translateBottomRight(Polygon square, Point[] points) {
		return super.translateBottomRight(
			square, points
		);
	}

	public SierpinskiRelatives3 self() {
		return this;
	}

	public String toString() {
		return "Sierpinski Relatives 3";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

