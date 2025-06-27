package geometric.fractals;

import utils.color.NamedPalettes;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Polygon;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.awt.Color;

public class CakeSierpinskiCarpet extends AbstractGeometricFractal<CakeSierpinskiCarpet> {
	public static final int ITERATIONS = 4;
	public static Color[] COLORS = NamedPalettes.BIRTHDAY;

	protected Map<Color,Color> colorMapping;
	protected List<List<Polygon>> levels;
	protected List<Transformable> fractalComponents;

	public CakeSierpinskiCarpet() {
		this(getSuggestedIterations());
	}

	public CakeSierpinskiCarpet(int iterations) {
		this(iterations, true);
	}

	public CakeSierpinskiCarpet(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public CakeSierpinskiCarpet(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 6;
	}

	protected void init() {
		super.init();

		double length = 500;
		List<Polygon> currentSquares = new LinkedList<>();
		currentSquares.add(
			Polygon.createSquare(
				new Point(-length/2, length/2), length, Color.WHITE, true
			)
		);
		fractalComponents = new LinkedList<>(currentSquares);

		for (int ii=0; ii < ITERATIONS; ii++) {
			List<Polygon> newSquares = new LinkedList<>();
			for (Polygon square : currentSquares) {
				Polygon baseSquare = new Polygon(square).scale(1.0/3.0);
				baseSquare.translate(
					square.getVertex(0).getX() - baseSquare.getVertex(0).getX(),
					square.getVertex(0).getY() - baseSquare.getVertex(0).getY()
				);
				baseSquare.setPaint(COLORS[ii % COLORS.length]);

				double dx = square.getVertex(1).getX() - square.getVertex(0).getX();
				double dy = square.getVertex(3).getY() - square.getVertex(0).getY();
				for (int row = 0; row < 3; row++) {
					for (int col = 0; col < 3; col++) {
						Polygon tmpSquare = new Polygon(baseSquare).translate(
							row/3.0*dx, col/3.0*dy
						);
						if (row == 1 && col == 1) {
							fractalComponents.add(tmpSquare);
						} else {
							newSquares.add(tmpSquare);
						}
					}
				}
			}
			currentSquares = newSquares;
		}

		colorMapping = new HashMap<>();
		colorMapping.put(Color.WHITE, Color.WHITE);
		for (int ii=0; ii < COLORS.length; ii++) {
			colorMapping.put(COLORS[ii], COLORS[(ii+1) % COLORS.length]);
		}
	}

	public void reset() {
		super.reset();
		step();
	}

	public void step() {
		for (Transformable t : fractalComponents) {
			if (t instanceof Polygon) {
				Polygon square = (Polygon) t;
				square.setPaint( colorMapping.get(square.getPaint()) );
			}
		}
	}

	public CakeSierpinskiCarpet self() {
		return this;
	}

	public Iterator<Transformable> iterator() {
		return fractalComponents.iterator();
	}

	public String toString() {
		return "Cake Sierpinski Carpet";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

