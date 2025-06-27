package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.LineSegment;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;

public class HTree extends AbstractGeometricFractal<HTree> {
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

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double length = 300;
		Point p1 = new Point(-length/2, 0, null);
		Point p2 = new Point(p1).translate(length, 0);

		seed.add(
			new LineSegment(p1, p2, null)
		);

		currentSegments = seed;
		fractalComponents = new LinkedList<>(seed);

		setPainter(PaintFactory.getLinearPainter(
			p1, p2,
			new float[] {0.0f, 0.49f, 0.51f, 1.0f},	
			new Color[] {
				NamedColors.BLUE, NamedColors.BLUE,
				NamedColors.RED, NamedColors.RED
			}
		));
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

