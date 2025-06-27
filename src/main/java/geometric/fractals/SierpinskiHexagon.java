package geometric.fractals;

import utils.color.NamedColors;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Triangle;
import geometric.utils.PaintFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;

public class SierpinskiHexagon extends AbstractGeometricFractal<SierpinskiHexagon> {
	protected Point center;
	protected Point pointOnCircle;
	protected List<Transformable> seed;
	protected List<Transformable> fractalComponents;

	public SierpinskiHexagon() {
		this(getSuggestedIterations());
	}

	public SierpinskiHexagon(int iterations) {
		this(iterations, true);
	}

	public SierpinskiHexagon(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiHexagon(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	protected void init() {
		super.init();

		seed = new LinkedList<>();

		/*Color[] colors = utils.color.ColorUtils.generateGradient(
				utils.color.NamedPalettes.RAINBOW, 6
		);*/
		Triangle triangle = Triangle.createEquilateralFromTop(
			new Point(), 250, null, false
		);
		Iterator<Point> iter = triangle.getVertices().iterator();
		Point p1 = iter.next();
		Point p2 = iter.next();
		for(int ii=0; ii < 6; ii++) {
			double angle = ii / (double) 6 * 360;
			Triangle tmpTriangle = new Triangle(triangle).rotateAround(p1, angle);
			//tmpTriangle.setPaint(colors[ii % colors.length]);
			seed.add(tmpTriangle);
		}

		fractalComponents = seed;
			
		setPainter(PaintFactory.getRadialPainter(
			p1, p2,
			new float[] {0.0f, 0.2f, 0.50f, 0.80f, 1.0f},
			new Color[] {
				NamedColors.RED, NamedColors.YELLOW, NamedColors.GREEN,
				NamedColors.YELLOW, NamedColors.RED
			}
		));
	}

	public void reset() {
		super.reset();
		fractalComponents = seed;
	}

	public void step() {
		List<Transformable> newComponents = new LinkedList<>();
		for (Transformable t : fractalComponents) {
			if (t instanceof Triangle) {
				Triangle triangle = (Triangle) t;
				Iterator<Point> iter = triangle.getVertices().iterator();
				Point p1 = iter.next();
				Point p2 = iter.next();
				Point p3 = iter.next();

				Point t2 = p1.getMidpoint(p2);
				Point t1 = p1.getMidpoint(p3);
				Point t3 = p2.getMidpoint(p3);

				newComponents.add(
					new Triangle(new Point[] {p1,t2,t1}, triangle.getPaint(), triangle.isFilled())
				);
				newComponents.add(
					new Triangle(new Point[] {t1,t3,p3}, triangle.getPaint(), triangle.isFilled())
				);
				newComponents.add(
					new Triangle(new Point[] {t2,p2,t3}, triangle.getPaint(), triangle.isFilled())
				);
			}
			else {
				newComponents.add(t);
			}
		}
		fractalComponents = newComponents;
	}

	public SierpinskiHexagon self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Sierpinski Hexagon";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

