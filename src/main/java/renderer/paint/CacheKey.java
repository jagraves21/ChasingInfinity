package renderer.paint;

import java.util.Objects;

public class CacheKey {
	private final double ax, ay, bx, by, cx, cy;
	private final int[] c1RGBA, c2RGBA, c3RGBA;

	public CacheKey(
		double ax, double ay, int[] c1RGBA,
		double bx, double by, int[] c2RGBA,
		double cx, double cy, int[] c3RGBA
	) {
		this.ax = ax; this.ay = ay;
		this.bx = bx; this.by = by;
		this.cx = cx; this.cy = cy;

		this.c1RGBA = c1RGBA.clone();
		this.c2RGBA = c2RGBA.clone();
		this.c3RGBA = c3RGBA.clone();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CacheKey)) return false;
		CacheKey other = (CacheKey) o;
		return doubleEquals(ax, other.ax) && doubleEquals(ay, other.ay) &&
			   doubleEquals(bx, other.bx) && doubleEquals(by, other.by) &&
			   doubleEquals(cx, other.cx) && doubleEquals(cy, other.cy) &&
			   arrayEquals(c1RGBA, other.c1RGBA) &&
			   arrayEquals(c2RGBA, other.c2RGBA) &&
			   arrayEquals(c3RGBA, other.c3RGBA);
	}

	private boolean doubleEquals(double a, double b) {
		return Double.compare(a, b) == 0;
	}

	private boolean arrayEquals(int[] a, int[] b) {
		if (a == b) return true;
		if (a == null || b == null || a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(
			doubleHash(ax), doubleHash(ay),
			doubleHash(bx), doubleHash(by),
			doubleHash(cx), doubleHash(cy)
		);
		result = 31 * result + arrayHash(c1RGBA);
		result = 31 * result + arrayHash(c2RGBA);
		result = 31 * result + arrayHash(c3RGBA);
		return result;
	}

	private int arrayHash(int[] arr) {
		if (arr == null) return 0;
		int result = 1;
		for (int val : arr) {
			result = 31 * result + val;
		}
		return result;
	}

	private int doubleHash(double value) {
		long bits = Double.doubleToLongBits(value);
		return (int)(bits ^ (bits >>> 32));
	}
}

