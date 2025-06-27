package geometric.fractals;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Polygon;

public class SierpinskiRelatives1 extends SierpinskiRelatives {
	public SierpinskiRelatives1() {
		this(getSuggestedIterations());
	}

	public SierpinskiRelatives1(int iterations) {
		this(iterations, true);
	}

	public SierpinskiRelatives1(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiRelatives1(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected Polygon translateTopLeft(Polygon square, Point[] points) {
		return super.translateTopLeft(
			square, rotate(points, 1)
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

	public SierpinskiRelatives1 self() {
		return this;
	}

	public String toString() {
		return "Sierpinski Relatives 1";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

