package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.LineSegment;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

public class HTree extends AbstractGeometricFractal<HTree> {
	protected Point left;
	protected Point right;
	protected List<LineSegment> seed;
	protected List<LineSegment> currentSegments;
	protected List<Transformable> fractalComponents;

	public HTree() {
		this(getSuggestedIterations());
	}

	public HTree(int iterations) {
		this(iterations, true);
	}

	public HTree(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public HTree(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 9;
	}

	public Paint getPaint() {
		return getPaint(
			this.left,
			this.right
		);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			this.left.toScreen(worldViewer),
			this.right.toScreen(worldViewer)
		);
	}

	protected Paint getPaint(Point left, Point right) {
		return new GradientPaint(
			(float)left.getX(), (float)left.getY(), NamedColors.BLUE,
			(float)right.getX(), (float)right.getY(), NamedColors.RED
		);
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double length = 300;
		Point p1 = new Point(-length/2, 0, null);
		Point p2 = new Point(p1).translate(length, 0);

		seed.add(
			new LineSegment(p1, p2, null)
		);

		left = new Point(p1);
		right = new Point(p2);
		currentSegments = seed;
		fractalComponents = new LinkedList<>(seed);
	}

	public void reset() {
		super.reset();
		currentSegments = seed;
		fractalComponents = new LinkedList<>(seed);
	}

	public void step() {
		double frac = 1/(2*Math.sqrt(2));
		Point tmpPoint;
		LineSegment newLineSegment;
		List<LineSegment> newSegments = new LinkedList<>();
		for (LineSegment lineSegment : currentSegments) {
			Point p1 = lineSegment.getStart();
			Point p2 = lineSegment.getEnd();

			tmpPoint = p1.interpolate(p2, frac);
			newLineSegment = new LineSegment(
				new Point(tmpPoint).rotateAround(p1, 90),
				new Point(tmpPoint).rotateAround(p1, -90),
				lineSegment.getPaint()
			);
			newSegments.add(newLineSegment);

			tmpPoint = p2.interpolate(p1, frac);
			newLineSegment = new LineSegment(
				new Point(tmpPoint).rotateAround(p2, 90),
				new Point(tmpPoint).rotateAround(p2, -90),
				lineSegment.getPaint()
			);
			newSegments.add(newLineSegment);
		}
		fractalComponents.addAll(newSegments);
		currentSegments = newSegments;
	}

	public HTree self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "H-Tree";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

