package geometric.fractals;

import utils.color.NamedPalettes;

import geometric.AbstractGeometricFractal;
import geometric.Bubble;
import geometric.Circle;
import geometric.Point;
import geometric.Transformable;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;

public class CircleInversion2 extends CircleInversion {
	public CircleInversion2() {
		this(getSuggestedIterations());
	}

	public CircleInversion2(int iterations) {
		this(iterations, true);
	}

	public CircleInversion2(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public CircleInversion2(int iterations, boolean reset) {
		super(iterations, reset);
	}

	public static int getSuggestedIterations() {
		return 3;
	}

	protected void init() {
		super.init();

		List<Circle> tmpSeed = new LinkedList<>();
		for(Circle circle : seed) {
			tmpSeed.add(Bubble.fromCircle(circle));
		}

		seed.clear();
		seed.addAll(tmpSeed);
		fractalComponents.clear();
		fractalComponents.addAll(tmpSeed);
	}

	public CircleInversion2 self() {
		return this;
	}

	public String toString() {
		return "Circle Inversion 2";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

