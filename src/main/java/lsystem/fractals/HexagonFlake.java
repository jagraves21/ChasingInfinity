package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class HexagonFlake extends AbstractLSystemFractal {
	public HexagonFlake() {
		this(getSuggestedIterations());
	}

	public HexagonFlake(int maxIterations) {
		this(maxIterations, true);
	}

	public HexagonFlake(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public HexagonFlake(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public HexagonFlake(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public HexagonFlake(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public HexagonFlake(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public HexagonFlake(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 4;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(2*Math.cos(Math.toRadians(30))*125)
			.setY(125)
			.setAngle(240)
			.setLineLength(125)
			.setTurningAngle(30)
			.setLineLengthScale(3)
			.build();
	}

	public Paint getPaint() {
		return getPaint(0, 0, 250);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(0),
			worldViewer.worldToScreenY(0),
			250 * worldViewer.getZoom()
		);
	}

	protected Paint getPaint(double x, double y, double radius) {
		return new RadialGradientPaint(
			(float) x,
			(float) y,
			(float) radius,
			new float[] {0.45f, 0.55f, 0.75f, 1.0f},
			new Color[] {
				NamedColors.ROSSO_CORSA,
				NamedColors.ORANGE_RED,
				NamedColors.ORANGE_PEEL,
				NamedColors.CYAN
			}
		);
	}

	protected void init() {
		super.init();
		addRule('F', "F[++F----F]--F++++F--F");
		addRule('S', "S<");
	}

	public String getAxiom() {
		return "S -F++F[++F]--F[++F]--F[++F]--F[++F]--F[++F]--F";
	}

	public String toString() {
		return "Hexagon Flake";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
