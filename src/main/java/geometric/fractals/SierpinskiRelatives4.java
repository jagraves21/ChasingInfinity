package geometric.fractals;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Polygon;

public class SierpinskiRelatives4 extends SierpinskiRelatives {
	public SierpinskiRelatives4() {
		this(getSuggestedIterations());
	}

	public SierpinskiRelatives4(int iterations) {
		this(iterations, true);
	}

	public SierpinskiRelatives4(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiRelatives4(int iterations, boolean reset) {
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
			square, rotate(points, 0)
		);
	}

	protected Polygon translateBottomRight(Polygon square, Point[] points) {
		return super.translateBottomRight(
			square, rotate(points, 1)
		);
	}

	public SierpinskiRelatives4 self() {
		return this;
	}

	public String toString() {
		return "Sierpinski Relatives 4";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

