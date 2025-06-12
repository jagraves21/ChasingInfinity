package geometric;

import utils.color.NamedColors;

import java.util.Arrays;
import java.util.List;

import java.awt.Paint;

public class Polygon extends BasePolygon<Polygon> {
	public Polygon(Point[] vertices) {
		this(vertices, NamedColors.WHITE);
	}
	
	public Polygon(Point[] vertices, Paint paint) {
		this(vertices, paint, false);
	}

	public Polygon(Point[] vertices, Paint paint, boolean fill) {
		this(Arrays.asList(vertices), paint, fill, true);
	}
	
	public Polygon(List<Point> vertices) {
		this(vertices, NamedColors.WHITE);
	}

	public Polygon(List<Point> vertices, Paint paint) {
		this(vertices, paint, false);
	}
	
	public Polygon(List<Point> vertices, Paint paint, boolean fill) {
		this(vertices, paint, fill, true);
	}
	
	protected Polygon(List<Point> vertices, Paint paint, boolean fill, boolean deepCopy) {
		super(vertices, paint, fill, deepCopy);
	}
	
	public Polygon(Polygon other) {
		this(other.vertices, other.paint, other.fill, true);
	}
	
	protected BasePolygon<Polygon> createCopy(
		List<Point> vertices, Paint paint, boolean fill, boolean deepCopy
	) {
		return new Polygon(vertices, paint, fill, deepCopy);
	}

	public Polygon self() {
		return this;
	}

	public String toString() {
		return String.format(
			"Polygon{vertices=%s, paint=%s, fill=%b}", vertices, paint, fill
		);
	}
}
