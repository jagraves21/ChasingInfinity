package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

public class SierpinskiSnowflake extends AbstractGeometricFractal<SierpinskiSnowflake> {
	public final double RATIO = 1.0/3.0;
	public final double LEGS = RATIO;
	public final double BASE = 1.0 - 2*RATIO;
	public final double INTERIOR_ANGLE = Math.toDegrees( Math.acos((BASE/2)/LEGS) );
	public final double EXTERIOR_ANGLE = INTERIOR_ANGLE - 180;


	Point topLeft;
	Point bottomRight;

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

	public Paint getPaint() {
		return getPaint(
			this.topLeft,
			this.bottomRight
		);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			this.topLeft.toScreen(worldViewer),
			this.bottomRight.toScreen(worldViewer)
		);
	}

	protected Paint getPaint(Point topLeft, Point bottomRight) {
		return new GradientPaint(
			(float)topLeft.getX(), (float)topLeft.getY(), NamedColors.BLUE,
			(float)bottomRight.getX(), (float)bottomRight.getY(), NamedColors.CYAN
		);
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		Point origin = new Point(0, 0, null);//, NamedColors.WHITE);
		Point p1 = new Point(origin).translate(0.0, 250.0);
		Point p2 = new Point(p1).rotate(-DEG_120);
		Point p3 = new Point(p1).rotate(DEG_120);

		seed.add(new LineSegment(p1,p2,null));
		seed.add(new LineSegment(p2,p3,null));
		seed.add(new LineSegment(p3,p1,null));
		seed.add(new Triangle(p1,p2,p3,null,false));

		topLeft = new Point(p1).rotate(DEG_45);
		bottomRight = new Point(p1).rotate(-DEG_135);
		fractalShapes = new LinkedList<>(seed);
		fractalComponents = new LinkedList<>();
		fractalComponents.add(seed.get(3));
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

