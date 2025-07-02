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

public class TernaryTree extends AbstractGeometricFractal<TernaryTree> {
	protected List<LineSegment> seed;
	protected List<LineSegment> currentLineSegments;
	protected List<Transformable> fractalComponents;

	public TernaryTree() {
		this(getSuggestedIterations());
	}

	public TernaryTree(int iterations) {
		this(iterations, true);
	}

	public TernaryTree(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public TernaryTree(int iterations, boolean reset) {
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
			new LineSegment(center, center.getMidpoint(p1), NamedColors.RED)
		);
		seed.add(
			new LineSegment(center, center.getMidpoint(p2), NamedColors.GREEN)
		);
		seed.add(
			new LineSegment(center, center.getMidpoint(p3), NamedColors.BLUE)
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
		for(LineSegment lineSegment : currentLineSegments) {
			Point p1 = lineSegment.getStart();
			Point p2 = lineSegment.getEnd();
			Point pNew = lineSegment.getMidpoint()
				.rotateAround(p2, 180);

			LineSegment baseSegment = new LineSegment(
				p2, pNew, lineSegment.getPaint()
			);
			
			nextSegments.add(baseSegment);

			nextSegments.add(
				new LineSegment(baseSegment).rotateAround(p2, 120)
			);

			nextSegments.add(
				new LineSegment(baseSegment).rotateAround(p2, -120)
			);

		}
		fractalComponents.addAll(nextSegments);
		currentLineSegments = nextSegments;	
	}

	public TernaryTree self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Ternary Tree";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

