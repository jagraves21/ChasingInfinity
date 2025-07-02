package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.awt.Color;

public class TernaryTree2 extends AbstractGeometricFractal<TernaryTree2> {
	protected List<LineSegment> seed;
	protected List<LineSegment> currentLineSegments;
	protected List<Transformable> fractalComponents;

	public TernaryTree2() {
		this(getSuggestedIterations());
	}

	public TernaryTree2(int iterations) {
		this(iterations, true);
	}

	public TernaryTree2(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public TernaryTree2(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		Triangle triangle = Triangle.createEquilateralFromTop(
			new Point(0, 250), 500, null, false
		);
		Iterator<Point> iter = triangle.getVertices().iterator();
		Point p1 = iter.next();
		Point p2 = iter.next();
		Point p3 = iter.next();
		Point center = triangle.centroid();

		seed.add(
			new LineSegment(center, p1, NamedColors.RED)
		);
		seed.add(
			new LineSegment(center, p2, NamedColors.GREEN)
		);
		seed.add(
			new LineSegment(center, p3, NamedColors.BLUE)
		);

		currentLineSegments = seed;
		fractalComponents = new LinkedList<>(seed);
	}

	public void reset() {
		super.reset();
		currentLineSegments = seed;
		fractalComponents.clear();
		fractalComponents.addAll(seed);
	}

	public void step() {
		LineSegment newSegment;
		List<LineSegment> nextSegments = new LinkedList<>();
		for (LineSegment lineSegment : currentLineSegments) {
			Point p1 = lineSegment.getStart();
			Point p2 = lineSegment.getEnd();
			Point pMid = lineSegment.getMidpoint();

			LineSegment baseSegment = new LineSegment(
				pMid, p2, lineSegment.getPaint()
			);

			newSegment = new LineSegment(baseSegment).rotateAround(pMid, 120);
			fractalComponents.add(newSegment);
			nextSegments.add(newSegment);

			newSegment = new LineSegment(baseSegment).rotateAround(pMid, -120);
			fractalComponents.add(newSegment);
			nextSegments.add(newSegment);

			nextSegments.add(baseSegment);
		}
		currentLineSegments = nextSegments;	
	}

	public TernaryTree2 self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Ternary Tree 2";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

