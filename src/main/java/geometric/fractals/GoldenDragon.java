package geometric.fractals;

import utils.color.ColorUtils;
import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.LineSegment;
import geometric.Point;
import geometric.Transformable;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.Paint;

public class GoldenDragon extends AbstractGeometricFractal<GoldenDragon> {
	public static final double PHI = (1 + Math.sqrt(5))/2;
	public static final double RATIO = Math.pow(1/PHI, 1/PHI);
	public static final double ANGLE_A = Math.toDegrees(Math.acos(
		(1 + Math.pow(RATIO,2) - Math.pow(RATIO,4)) / (2*RATIO)
	));
	public static final double ANGLE_B = Math.toDegrees(Math.acos(
		(1 + Math.pow(RATIO,4) - Math.pow(RATIO,2)) / (2*Math.pow(RATIO,2))
	));
	public static final double ANGLE_C = 180 - (ANGLE_A + ANGLE_B);

	protected Point center;
	protected Point pointOnCircle;
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public GoldenDragon() {
		this(getSuggestedIterations());
	}

	public GoldenDragon(int iterations) {
		this(iterations, true);
	}

	public GoldenDragon(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public GoldenDragon(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 18;
	}

	public Paint getPaint() {
		return getPaint(
			this.center,
			this.pointOnCircle
		);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			this.center.toScreen(worldViewer),
			this.pointOnCircle.toScreen(worldViewer)
		);
	}

	protected Paint getPaint(Point center, Point pointOnCircle) {
		return new RadialGradientPaint(
			(float) center.getX(),
			(float) center.getY(),
			(float) center.distance(pointOnCircle),
			new float[] {0.3f, 1.0f},
			new Color[] {NamedColors.RED, NamedColors.YELLOW}
			//MultipleGradientPaint.CycleMethod.REPEAT
		);
	}

	protected void init() {
		super.init();

		this.seed = new LinkedList<>();

		double length = 600;
		double height = (Math.sqrt(3) / 6.0) * length;
		Point p1 = new Point(-length/3, -height/2, NamedColors.RED);
		Point p2 = new Point( length/2, -height/2, NamedColors.YELLOW);

		seed.add(new LineSegment(
			p1, p2,
			ColorUtils.blend((Color)p1.getPaint(), (Color)p2.getPaint())
		));

		center = p1.getMidpoint(p2);
		pointOnCircle = new Point(p1);
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
				LineSegment lineSegment = (LineSegment) component;
				Paint paint = lineSegment.getPaint();
				boolean drawEndpoints = lineSegment.getDrawEndpoints();

				Point p1 = lineSegment.getStart();
				Point p3 = lineSegment.getEnd();
				Point p2 = p1.interpolate(p3, RATIO).rotateAround(p1, ANGLE_A);
				p2.setPaint(ColorUtils.blend(
					(Color)p1.getPaint(),
					(Color)p3.getPaint(),
					RATIO
				));

				newComponents.add(new LineSegment(
					p1, p2,
					ColorUtils.blend((Color)p1.getPaint(), (Color)p2.getPaint()),
					drawEndpoints
				));
				newComponents.add(new LineSegment(
					p3, p2,
					ColorUtils.blend((Color)p3.getPaint(), (Color)p2.getPaint()),
					drawEndpoints
				));
			}
			else {
				newComponents.add(component);
			}
		}
		fractalComponents = newComponents;
	}

	public GoldenDragon self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Golden Dragon";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

