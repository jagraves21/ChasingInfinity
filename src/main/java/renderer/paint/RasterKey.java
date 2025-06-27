package renderer.paint;

import java.awt.Color;
import java.util.Objects;

public class RasterKey {
	private final double ax, ay, bx, by, cx, cy;
	private final Color c1, c2, c3;
	private final int x, y, w, h;

	public RasterKey(
		double ax, double ay, Color c1,
		double bx, double by, Color c2,
		double cx, double cy, Color c3,
		int x, int y, int w, int h
	) {
		this.ax = ax;
		this.ay = ay;
		this.bx = bx;
		this.by = by;
		this.cx = cx;
		this.cy = cy;
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RasterKey)) return false;
		RasterKey other = (RasterKey) o;
		return doubleEquals(ax, other.ax) && doubleEquals(ay, other.ay) &&
			   doubleEquals(bx, other.bx) && doubleEquals(by, other.by) &&
			   doubleEquals(cx, other.cx) && doubleEquals(cy, other.cy) &&
			   Objects.equals(c1, other.c1) && Objects.equals(c2, other.c2) && Objects.equals(c3, other.c3) &&
			   x == other.x && y == other.y && w == other.w && h == other.h;
	}

	private boolean doubleEquals(double a, double b) {
		return Double.compare(a, b) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			doubleHash(ax), doubleHash(ay), c1,
			doubleHash(bx), doubleHash(by), c2,
			doubleHash(cx), doubleHash(cy), c3,
			x, y, w, h
		);
	}

	private int doubleHash(double value) {
		long bits = Double.doubleToLongBits(value);
		return (int)(bits ^ (bits >>> 32));
	}

	private String colorArrayToString(int[] rgba) {
		return String.format("rgba(%d,%d,%d,%d)", rgba[0], rgba[1], rgba[2], rgba[3]);
	}

	public String toString() {
		return String.format(
			"RasterKey[p1=(%.2f, %.2f), c1=%s, p2=(%.2f, %.2f), c2=%s, p3=(%.2f, %.2f), c3=%s, x=%d, y=%d, w=%d, h=%d]",
			ax, ay, c1,
			bx, by, c2,
			cx, cy, c3,
			x, y, w, h
		);
	}
}

