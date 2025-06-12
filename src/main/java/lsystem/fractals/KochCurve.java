package lsystem.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.Paint;

public class KochCurve extends AbstractLSystemFractal {
	public KochCurve() {
		this(getSuggestedIterations());
	}

	public KochCurve(int maxIterations) {
		this(maxIterations, true);
	}

	public KochCurve(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public KochCurve(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public KochCurve(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public KochCurve(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public KochCurve(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public KochCurve(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 5;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-250)
			.setY(-250)
			.setLineLength(500)
			.setTurningAngle(-90)
			.setLineLengthScale(3)
			.build();
	}

	public Paint getPaint() {
		return getPaint(0, 0, 250*Math.sqrt(2));
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(0),
			worldViewer.worldToScreenY(0),
			250*Math.sqrt(2) * worldViewer.getZoom()
		);
	}

	protected Paint getPaint(double x, double y, double radius) {
        return new RadialGradientPaint(
			(float) x,
			(float) y,
			(float) radius,
			new float[] {0.0f, 1.0f},
			new Color[] {NamedColors.GREEN, NamedColors.BLUE}
		);
	}

	protected void init() {
		super.init();
		addRule('F', "FF-F-F-F-F-F+F");
		addRule('S', "+S<");
	}

	public String getAxiom() {
		return "S F-F-F-F";
	}

	public String toString() {
		return "Koch Curve";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
