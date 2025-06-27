package geometric.fractals;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Polygon;

public class SierpinskiRelatives6 extends SierpinskiRelatives {
	public SierpinskiRelatives6() {
		this(getSuggestedIterations());
	}

	public SierpinskiRelatives6(int iterations) {
		this(iterations, true);
	}

	public SierpinskiRelatives6(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiRelatives6(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected Polygon translateTopLeft(Polygon square, Point[] points) {
		return super.translateTopLeft(
			square, points
		);
	}

	protected Polygon translateTopRight(Polygon square, Point[] points) {
		return super.translateTopRight(
			square, points
		);
	}

	protected Polygon translateBottomLeft(Polygon square, Point[] points) {
		return super.translateBottomLeft(
			square, rotate(points, -2)
		);
	}

	protected Polygon translateBottomRight(Polygon square, Point[] points) {
		return super.translateBottomRight(
			square, rotate(points, -3)
		);
	}

	public SierpinskiRelatives6 self() {
		return this;
	}

	public String toString() {
		return "Sierpinski Relatives 6";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

