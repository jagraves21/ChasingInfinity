package geometric.fractals;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Triangle;

import java.util.LinkedList;
import java.util.Iterator;

public class SierpinskiPedals extends SierpinskiPedal {
	public static final double EPSILON = 1e-9;
	public static final int N_ITERATIONS = 7;

	public SierpinskiPedals() {
		this(getSuggestedIterations());
	}

	public SierpinskiPedals(int iterations) {
		this(iterations, true);
	}

	public SierpinskiPedals(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiPedals(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 200;
	}

	public static int getSuggestedUPS() {
		return 15;
	}
	
	protected void init() {
		super.init();
		seed.get(0).scale(0.9).translate(0, -50);
		step();
	}

	public void reset() {
		super.reset();
		step();
	}

	public static Point[] findPerpendicularPointAtA(Point A, Point B) {
		return findPerpendicularPointAtA(A, B, A.getMidpoint(B));
	}
	
	public static Point[] findPerpendicularPointAtA(Point A, Point B, Point M) {
		double r = A.distance(B);

		double ax = A.getX();
		double ay = A.getY();
		double mx = M.getX();
		double my = M.getY();

		double bx = B.getX();
		double by = B.getY();

		double dx = bx - ax;
		double dy = by - ay;

		if (Math.abs(dy) < EPSILON) {
			// AB is horizontal
			double x = ax;
			double delta = r * r - (x - mx) * (x - mx);
			if (delta < 0) throw new ArithmeticException("No intersection on the circle.");
			double sqrtDelta = Math.sqrt(delta);
			double y1 = my + sqrtDelta;
			double y2 = my - sqrtDelta;

			Point p1 = new Point(x, y1);
			Point p2 = new Point(x, y2);

			if (dx > 0) {
				return new Point[] { p1, p2 };
			} else {
				return new Point[] { p2, p1 };
			}
		} else if (Math.abs(dx) < EPSILON) {
			// AB is vertical
			double y = ay;
			double delta = r * r - (y - my) * (y - my);
			if (delta < 0) throw new ArithmeticException("No intersection on the circle.");
			double sqrtDelta = Math.sqrt(delta);
			double x1 = mx + sqrtDelta;
			double x2 = mx - sqrtDelta;

			Point p1 = new Point(x1, y);
			Point p2 = new Point(x2, y);

			if (dy > 0) {
				return new Point[] { p2, p1 };
			} else {
				return new Point[] { p1, p2 };
			}
		} else {
			// general case
			double m_perp = -dx / dy;
			double Y = ay - my - m_perp * ax;

			double Acoeff = 1 + m_perp * m_perp;
			double Bcoeff = 2 * (-mx + m_perp * Y);
			double Ccoeff = mx * mx + Y * Y - r * r;

			double discriminant = Bcoeff * Bcoeff - 4 * Acoeff * Ccoeff;
			if (discriminant < 0) throw new ArithmeticException("No intersection on the circle.");

			double sqrtDisc = Math.sqrt(discriminant);
			double x1 = (-Bcoeff + sqrtDisc) / (2 * Acoeff);
			double y1 = ay + m_perp * (x1 - ax);

			double x2 = (-Bcoeff - sqrtDisc) / (2 * Acoeff);
			double y2 = ay + m_perp * (x2 - ax);

			Point p1 = new Point(x1, y1);
			Point p2 = new Point(x2, y2);

			double orient1 = (bx - ax) * (y1 - ay) - (by - ay) * (x1 - ax);
			double orient2 = (bx - ax) * (y2 - ay) - (by - ay) * (x2 - ax);

			if (orient1 > 0) {
				return new Point[] { p1, p2 };
			} else if (orient2 > 0) {
				return new Point[] { p2, p1 };
			} else {
				throw new ArithmeticException("No counterclockwise intersection found.");
			}
		}
	}

	public void step() {
		/*double percent = 0;
		if (maxIterations != 0) {
			percent = curIteration / (double) maxIterations;
			int newMax = maxIterations/2;
			int newCur = newMax -
				Math.abs(
						(curIteration % maxIterations) -
						newMax
						);
			percent = newCur / (double) newMax;
		}*/
		double percent = curIteration / (double)maxIterations;
		if (Double.isNaN(percent)) percent = 0;
		
		Triangle triangle = new Triangle((Triangle) seed.get(0));
		Iterator<Point> iter = triangle.getVertices().iterator();
		Point A = iter.next();
		Point B = iter.next();
		Point C = iter.next();
		Point M = B.getMidpoint(C);

		Point A1 = findPerpendicularPointAtA(C, B, M)[0];
		A.set(A1.rotateAround(M, -percent*60));

		double x = 0;
		double y = 100;
		double xr = B.distance(C)/2;
		double yr = 50;
		double theta = percent*Math.toRadians(360);

		A.set(x + xr*Math.cos(theta), y + yr*Math.sin(theta));

		fractalComponents = new LinkedList<>();
		fractalComponents.add(triangle);
		for(int ii=0; ii < N_ITERATIONS; ii++) {
			super.step();
		}
	}

	public SierpinskiPedals self() {
		return this;
	}

	public String toString() {
		return "Sierpinski Pedals";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

