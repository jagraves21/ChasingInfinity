package geometric.fractals;

import utils.color.ColorUtils;
import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

public class KochAntiSnowflake extends AbstractGeometricFractal<KochAntiSnowflake> {
	public static final double RATIO = 1.0/3.0;
	public static final double LEGS = RATIO;
	public static final double BASE = 1.0 - 2*RATIO;
	public static final double INTERIOR_ANGLE = Math.toDegrees( Math.acos((BASE/2)/LEGS) );
	public static final double EXTERIOR_ANGLE = INTERIOR_ANGLE - 180;

	protected Point topRight;
	protected Point bottomLeft;
	protected Color c1;
	protected Color c2;
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public KochAntiSnowflake() {
		this(getSuggestedIterations());
	}

	public KochAntiSnowflake(int iterations) {
		this(iterations, true);
	}

	public KochAntiSnowflake(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public KochAntiSnowflake(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public Paint getPaint() {
		return getPaint(
			this.topRight,
			this.bottomLeft
		);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			this.topRight.toScreen(worldViewer),
			this.bottomLeft.toScreen(worldViewer)
		);
	}

	protected Paint getPaint(Point topRight, Point bottomLeft) {
		return new GradientPaint(
			(float)topRight.getX(), (float)topRight.getY(), ColorUtils.invert(NamedColors.BLUE),
			(float)bottomLeft.getX(), (float)bottomLeft.getY(), ColorUtils.invert(NamedColors.CYAN)
		);
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		Point origin = new Point(0, 0, null);
		Point p1 = new Point(origin).translate(0.0, 250.0);
		Point p2 = new Point(p1).rotate(-DEG_120);
		Point p3 = new Point(p1).rotate(DEG_120);

		seed.add(new LineSegment(p2,p1,null));
		seed.add(new LineSegment(p3,p2,null));
		seed.add(new LineSegment(p1,p3,null));

		topRight = new Point(p1).rotate(-DEG_45);
		bottomLeft = new Point(p1).rotate(DEG_135);
		fractalComponents = seed;
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable component : fractalComponents) {
			if (component instanceof LineSegment) {
				LineSegment linesegment = (LineSegment) component;
				Paint paint = linesegment.getPaint();

				Point p1 = linesegment.getStart();
				Point p5 = linesegment.getEnd();
				Point p2 = p1.interpolate(p5, RATIO);
				Point p3 = new Point(p1).rotateAround(p2, EXTERIOR_ANGLE);
				Point p4 = p5.interpolate(p1, RATIO);

				newComponents.add(new LineSegment(p1, p2, paint));
				newComponents.add(new LineSegment(p2, p3, paint));
				newComponents.add(new LineSegment(p3, p4, paint));
				newComponents.add(new LineSegment(p4, p5, paint));
			}
			else {
				newComponents.add(component);
			}
		}
		fractalComponents = newComponents;
	}

	public KochAntiSnowflake self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Koch Anti Snowflake";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

