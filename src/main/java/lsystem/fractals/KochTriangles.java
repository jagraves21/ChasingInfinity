package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class KochTriangles extends AbstractLSystemFractal {
	public KochTriangles() {
		this(getSuggestedIterations());
	}

	public KochTriangles(int maxIterations) {
		this(maxIterations, true);
	}

	public KochTriangles(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public KochTriangles(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public KochTriangles(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public KochTriangles(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public KochTriangles(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public KochTriangles(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 5;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-200)
			.setY(-150)
			.setLineLength(400)
			.setTurningAngle(-40)
			.build();
	}

	public Paint getPaint() {
		return getPaint(0, -50, 215);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(0),
			worldViewer.worldToScreenY(-50),
			215 * worldViewer.getZoom()
		);
	}

	protected Paint getPaint(double x, double y, double radius) {
        return new RadialGradientPaint(
			(float) x,
			(float) y,
			(float) radius,
			new float[] {0.0f, 0.5f, 1.0f},
			new Color[] {NamedColors.CYAN, NamedColors.MAGENTA, NamedColors.YELLOW}
		);
	}

	protected void init() {
		super.init();
		addRule('F', "F+++<F---F---F+++>F");
		addRule('S', "S<");
	}

	public String getAxiom() {
		return "S F---F---F";
	}

	public String toString() {
		return "Koch Triangles";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
