package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Polygon;
import geometric.Transformable;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class Eee extends AbstractGeometricFractal<Eee> {
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public Eee() {
		this(getSuggestedIterations());
	}

	public Eee(int iterations) {
		this(iterations, true);
	}

	public Eee(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public Eee(int iterations, boolean reset) {
		super(iterations, reset);
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		double width = 200;
		double height = 2 * width;
		Point topLeft = new Point(-width/2,  height/2);
		Polygon rectangle = Polygon.createRectangle(
			topLeft, width, height, null, true
		);
		seed.add(rectangle);

		fractalComponents = seed;

		setPainter(PaintFactory.getLinearPainter(
			topLeft, rectangle.getVertex(2), NamedColors.GREEN, NamedColors.BLUE
		));
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		double angle;
		Point pA1, pA2;
		Polygon newRectangle;
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof Polygon) {
				Polygon rectangle = (Polygon) t;
				Iterator<Point> iter = rectangle.getVertices().iterator();
				Point p1 = iter.next();
				Point p2 = iter.next();
				Point p3 = iter.next();
				Point p4 = iter.next();

				Point pTmp1 = p1.interpolate(p4, 0.25);
				Point pTmp2 = p1.getMidpoint(p2).interpolate(
					p4.getMidpoint(p3), 3.0/8.0
				);

				newRectangle = new Polygon(rectangle).scale(0.5);
				pA1 = pTmp1;
				pA2 = newRectangle.getVertex(0);
				angle = 0;
				newComponents.add(
					newRectangle
						.translate(pA1.getX()-pA2.getX(), pA1.getY()-pA2.getY())
						.rotateAround(pA1, angle)
				);

				newRectangle = new Polygon(rectangle).scale(0.5);
				pA1 = p1;
				pA2 = newRectangle.getVertex(3);
				angle = -90;
				newComponents.add(
					newRectangle
						.translate(pA1.getX()-pA2.getX(), pA1.getY()-pA2.getY())
						.rotateAround(pA1, angle)
				);

				newRectangle = new Polygon(rectangle).scale(0.5);
				pA1 = p4;
				pA2 = newRectangle.getVertex(0);
				angle = 90;
				newComponents.add(
					newRectangle
						.translate(pA1.getX()-pA2.getX(), pA1.getY()-pA2.getY())
						.rotateAround(pA1, angle)
				);

				newRectangle = new Polygon(rectangle).scale(0.25);
				pA1 = pTmp2;
				pA2 = newRectangle.getVertex(2);
				angle = -180;
				newComponents.add(
					newRectangle
						.translate(pA1.getX()-pA2.getX(), pA1.getY()-pA2.getY())
						.rotateAround(pA1, angle)
				);
			}
			else {
				newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public Eee self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Eee";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

