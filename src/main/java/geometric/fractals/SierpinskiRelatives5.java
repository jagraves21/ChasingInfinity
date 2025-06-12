package geometric.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import geometric.AbstractGeometricFractal;
import geometric.Point;
import geometric.Transformable;
import geometric.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.Paint;

public class SierpinskiRelatives5 extends SierpinskiRelatives {
	public SierpinskiRelatives5() {
		this(getSuggestedIterations());
	}

	public SierpinskiRelatives5(int iterations) {
		this(iterations, true);
	}

	public SierpinskiRelatives5(boolean reset) {
		this(getSuggestedIterations(), true);
	}

	public SierpinskiRelatives5(int iterations, boolean reset) {
		super(iterations, reset);
	}
	
	protected Polygon translateTopLeft(Polygon square, Point[] points) {
		return super.translateTopLeft(
			square, rotate(points, 3)
		);
	}

	protected Polygon translateTopRight(Polygon square, Point[] points) {
		return super.translateTopRight(
			square, points
		);
	}

	protected Polygon translateBottomLeft(Polygon square, Point[] points) {
		return super.translateBottomLeft(
			square, rotate(points, 3)
		);
	}

	protected Polygon translateBottomRight(Polygon square, Point[] points) {
		return super.translateBottomRight(
			square, rotate(points, 2)
		);
	}

	public SierpinskiRelatives5 self() {
		return this;
	}

	public String toString() {
		return "Sierpinski Relatives 5";
	}

	public static void main(String[] args) {
		AbstractGeometricFractal.display(args);
	}
}

