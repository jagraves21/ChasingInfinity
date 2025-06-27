package geometric.fractals;

import utils.color.NamedPalettes;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.Paint;

public class SierpinskiSnowflake extends AbstractGeometricFractal<SierpinskiSnowflake> {
	public static final double RATIO = 1.0/3.0;
	public static final double LEGS = RATIO;
	public static final double BASE = 1.0 - 2*RATIO;
	public static final double INTERIOR_ANGLE = Math.toDegrees( Math.acos((BASE/2)/LEGS) );
	public static final double EXTERIOR_ANGLE = INTERIOR_ANGLE - 180;

	protected List<Transformable> seed;
	protected List<Transformable> fractalShapes;
	protected List<Transformable> fractalComponents;

	public SierpinskiSnowflake() {
		this(getSuggestedIterations());
	}

	public SierpinskiSnowflake(int iterations) {
		this(iterations, true);
	}

	public SierpinskiSnowflake(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public SierpinskiSnowflake(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		Point origin = new Point();
		Triangle triangle = Triangle.createEquilateralFromCenter(
			origin, 250, null, false
		);
		Iterator<Point> iter = triangle.getVertices().iterator();
		Point p1 = iter.next();
		Point p2 = iter.next();
		Point p3 = iter.next();

		seed.add(new LineSegment(p1,p2,null));
		seed.add(new LineSegment(p2,p3,null));
		seed.add(new LineSegment(p3,p1,null));
		seed.add(triangle);

		fractalShapes = new LinkedList<>(seed);
		fractalComponents = new LinkedList<>();
		fractalComponents.add(triangle);

		setPainter(PaintFactory.getRadialPainter(
			origin, triangle.getVertex(0), NamedPalettes.SNOW
		));
	}

	public void reset() {
		super.reset();
		fractalShapes.clear();
		fractalShapes.addAll(seed);
		fractalComponents.clear();
		fractalComponents.add(seed.get(3));
	}

	public void step() {
		List<Transformable> newFractalShapes = new LinkedList<>();
		for (Transformable component : fractalShapes) {
			if (component instanceof LineSegment) {
				LineSegment linesegment = (LineSegment) component;
				Paint paint = linesegment.getPaint();

				Point p1 = linesegment.getStart();
				Point p5 = linesegment.getEnd();
				Point p2 = p1.interpolate(p5, RATIO);
				Point p3 = new Point(p1).rotateAround(p2, EXTERIOR_ANGLE);
				Point p4 = p5.interpolate(p1, RATIO);

				newFractalShapes.add(new LineSegment(p1, p2, paint));
				newFractalShapes.add(new LineSegment(p2, p3, paint));
				newFractalShapes.add(new LineSegment(p3, p4, paint));
				newFractalShapes.add(new LineSegment(p4, p5, paint));

				Triangle triangle = new Triangle(p2, p3, p4, null, false);
				newFractalShapes.add(triangle);
				fractalComponents.add(triangle);
			} else if (component instanceof Triangle) {
				Triangle triangle = (Triangle) component;
				Iterator<Point> iter = triangle.getVertices().iterator();
				Point p1 = iter.next();
				Point p2 = iter.next();
				Point p3 = iter.next();

				Point t2 = p1.getMidpoint(p2);
				Point t1 = p1.getMidpoint(p3);
				Point t3 = p2.getMidpoint(p3);

				Triangle triangle1 = new Triangle(new Point[] {p1,t2,t1}, null, false);
				Triangle triangle2 = new Triangle(new Point[] {t1,t3,p3}, null, false);
				Triangle triangle3 = new Triangle(new Point[] {t2,p2,t3}, null, false);

				newFractalShapes.add(triangle1);
				newFractalShapes.add(triangle2);
				newFractalShapes.add(triangle3);
				fractalComponents.add(triangle1);
				fractalComponents.add(triangle2);
				fractalComponents.add(triangle3);
			}
		}
		fractalShapes = newFractalShapes;
	}

	public SierpinskiSnowflake self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Snowflake";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

