package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;

public class SierpinskiRelatives extends AbstractGeometricFractal<SierpinskiRelatives> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiRelatives() {
		this(getSuggestedIterations());
	}

	public SierpinskiRelatives(int iterations) {
		this(iterations, true);
	}

	public SierpinskiRelatives(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiRelatives(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 8;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double length = 500;
		Point p1 = new Point(-length/2, length/2);
		Point p2 = new Point(p1).translate(length, 0);
		Point p3 = new Point(p1).translate(length, -length);
		Point p4 = new Point(p1).translate(0, -length);

		seed.add(
			new Polygon(
				new Point[] {p1,p2,p3,p4},
				new Color(255/3, 255/3, 255/3),
				true
			)
		);

		fractalComponents = seed;
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	protected Point[] rotate(Point[] vertices, int shift) {
		if (vertices.length == 0) return vertices;
		shift = ((shift % vertices.length) + vertices.length) % vertices.length;
		if (shift == 0) return vertices;

		int count = 0;
		for (int start = 0; count < vertices.length; start++) {
			int current = start;
			Point prev = vertices[start];

			do {
				int next = (current + shift) % vertices.length;
				Point temp = vertices[next];
				vertices[next] = prev;
				prev = temp;
				current = next;
				count++;
			} while (start != current);
		}

		return vertices;
	}

	protected Polygon translateTopLeft(Polygon square, Point[] points) {
		Polygon newSquare = new Polygon(points, square.getPaint(), square.isFilled());
		if(fractalComponents.size() == 1) {
			newSquare.setPaint(Color.RED);
		}
		return newSquare;
	}

	protected Polygon translateTopRight(Polygon square, Point[] points) {
		return null;
	}

	protected Polygon translateBottomRight(Polygon square, Point[] points) {
		Polygon newSquare = new Polygon(points, square.getPaint(), square.isFilled());
		if(fractalComponents.size() == 1) {
			newSquare.setPaint(Color.BLUE);
		}
		return newSquare;
	}

	protected Polygon translateBottomLeft(Polygon square, Point[] points) {
		Polygon newSquare = new Polygon(points, square.getPaint(), square.isFilled());
		if(fractalComponents.size() == 1) {
			newSquare.setPaint(Color.GREEN);
		}
		return newSquare;
	}

	protected Polygon[] step_helper(
		Polygon square,
		Point topLeft,
		Point topRight,
		Point bottomRight,
		Point bottomLeft,
		Point top,
		Point right,
		Point bottom,
		Point left,
		Point middle
	) {
		List<Polygon> results = new ArrayList<>(4);
		Polygon newSquare;

		newSquare = translateTopLeft(square, new Point[] {topLeft, top, middle, left});
		if(newSquare != null) {
			results.add(newSquare);
		}

		newSquare = translateTopRight(square, new Point[] {top, topRight, right, middle});
		if(newSquare != null) {
			results.add(newSquare);
		}

		newSquare = translateBottomLeft(square, new Point[] {left, middle, bottom, bottomLeft});
		if(newSquare != null) {
			results.add(newSquare);
		}

		newSquare = translateBottomRight(square, new Point[] {middle, right, bottomRight, bottom});
		if(newSquare != null) {
			results.add(newSquare);
		}

		return results.toArray(new Polygon[0]);
	}

	protected Polygon[] step_helper(Polygon square) {
		Iterator<Point> iter = square.getVertices().iterator();
		Point topLeft = iter.next();
		Point topRight = iter.next();
		Point bottomRight = iter.next();
		Point bottomLeft = iter.next();
		Point top = topLeft.getMidpoint(topRight);
		Point right = topRight.getMidpoint(bottomRight);
		Point bottom = bottomRight.getMidpoint(bottomLeft);
		Point left = bottomLeft.getMidpoint(topLeft);
		Point middle = topLeft.getMidpoint(bottomRight);

		return step_helper(
			square,
			topLeft, topRight, bottomRight, bottomLeft,
			top, right, bottom, left, middle
		);
	}

	public void step() {
		List<Transformable> newSquares = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof  Polygon) {
				Polygon square = (Polygon) t;
				Collections.addAll(
					newSquares,
					step_helper(square)
				);
			}
		}
		fractalComponents = newSquares;
	}

	public SierpinskiRelatives self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Relatives";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

