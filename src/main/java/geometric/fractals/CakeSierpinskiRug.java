package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Polygon;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;

public class CakeSierpinskiRug extends AbstractGeometricFractal<CakeSierpinskiRug> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public CakeSierpinskiRug() {
		this(getSuggestedIterations());
	}

	public CakeSierpinskiRug(int iterations) {
		this(iterations, true);
	}

	public CakeSierpinskiRug(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public CakeSierpinskiRug(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 6;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double width = 500;
		double height = 500;
		Point p1 = new Point(-width/2, height/2);
		Point p2 = new Point(p1).translate(width, 0);
		Point p3 = new Point(p1).translate(width, -height);
		Point p4 = new Point(p1).translate(0, -height);

		seed.add(
			new Polygon(new Point[] {p1,p2,p3,p4}, null, true)
		);

		fractalComponents = new LinkedList<>(seed);

		setPainter(PaintFactory.getLinearPainter(
			p1.interpolate(p3, 1.0/3.0),
			p1.interpolate(p3, 2.0/3.0),
			NamedColors.INDIGO,
			NamedColors.ROSE
		));
	}

	public void reset() {
		super.reset();
		fractalComponents = new LinkedList<>(seed);
	}

	public void step() {
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof Polygon) {
				Polygon square = (Polygon) t;
				Iterator<Point> iter = square.getVertices().iterator();
				Point p1 = iter.next();
				Point p2 = iter.next();
				Point p3 = iter.next();
				Point p4 = iter.next();

				Point p11 = p1.interpolate(p4, 0.25);
				Point p12 = p2.interpolate(p3, 0.25);
				Point p13 = p2.interpolate(p3, 0.75);
				Point p14 = p1.interpolate(p4, 0.75);

				Point p21 = p11.interpolate(p12, 1.0/3.0);
				Point p22 = p11.interpolate(p12, 2.0/3.0);
				Point p23 = p14.interpolate(p13, 2.0/3.0);
				Point p24 = p14.interpolate(p13, 1.0/3.0);

				newComponents.add(
					new Polygon(new Point[] {p11,p1,p2,p12}, null, true)
				);
				newComponents.add(
					new Polygon(new Point[] {p4,p14,p13,p3}, null, true)
				);
				newComponents.add(
					new Polygon(new Point[] {p11,p21,p24,p14}, null, true)
				);
				newComponents.add(
					new Polygon(new Point[] {p22,p12,p13,p23}, null, true)
				);
			}
			else {
				newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public CakeSierpinskiRug self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Cake Sierpinski Rug";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

