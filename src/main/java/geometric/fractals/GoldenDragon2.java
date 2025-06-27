package geometric.fractals;

import utils.color.ColorUtils;
import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;

import java.awt.Color;

public class GoldenDragon2 extends GoldenDragon {
	public GoldenDragon2() {
		this(getSuggestedIterations());
	}

	public GoldenDragon2(int iterations) {
		this(iterations, true);
	}

	public GoldenDragon2(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public GoldenDragon2(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected void init() {
		super.init();

		double length = 600;
		double height = (Math.sqrt(3) / 6.0) * length;
		Point p1 = new Point(-length/3, 0, NamedColors.RED);
		Point p2 = new Point( length/3, 0, NamedColors.YELLOW);

		this.seed.clear();
		seed.add(new LineSegment(
			p1, p2,
			ColorUtils.blend((Color)p1.getPaint(), (Color)p2.getPaint())
		));
		seed.add(new LineSegment(
			p2, p1,
			ColorUtils.blend((Color)p1.getPaint(), (Color)p2.getPaint())
		));
	}

	public GoldenDragon2 self() {
		return this;
	}

	public String toString() {
		return "Golden Dragon 2nd Form";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

