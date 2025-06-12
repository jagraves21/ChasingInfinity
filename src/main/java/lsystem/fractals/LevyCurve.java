package lsystem.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.GradientPaint;
import java.awt.Paint;

public class LevyCurve extends AbstractLSystemFractal {
	public LevyCurve() {
		this(getSuggestedIterations());
	}

	public LevyCurve(int maxIterations) {
		this(maxIterations, true);
	}

	public LevyCurve(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public LevyCurve(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public LevyCurve(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public LevyCurve(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public LevyCurve(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public LevyCurve(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 17;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-175)
			.setY(-150)
			.setLineLength(350)
			.setTurningAngle(45)
			.setLineLengthScale(Math.sqrt(2))
			.build();
	}

	public Paint getPaint() {
		return getPaint(176.78, 176.78, -176.78, -176.78);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(-176.78),
			worldViewer.worldToScreenY(176.78),
			worldViewer.worldToScreenX(176.78),
			worldViewer.worldToScreenY(-176.78)
		);
	}

	protected Paint getPaint(double x1, double y1, double x2, double y2) {
		return new GradientPaint(
			(float)x1, (float)y1, NamedColors.BLUE,
			(float)x2, (float)y2, NamedColors.CYAN
		);
	}

	protected void init() {
		super.init();
		addRule('F', "+F--F+");
		addRule('S', "S<");
	}

	public String getAxiom() {
		return "S F";
	}

	public String toString() {
		return "Levy Curve";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
