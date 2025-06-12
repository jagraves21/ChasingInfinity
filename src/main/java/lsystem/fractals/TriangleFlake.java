package lsystem.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.GradientPaint;
import java.awt.Paint;

public class TriangleFlake extends AbstractLSystemFractal {
	public TriangleFlake() {
		this(getSuggestedIterations());
	}

	public TriangleFlake(int maxIterations) {
		this(maxIterations, true);
	}

	public TriangleFlake(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public TriangleFlake(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public TriangleFlake(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public TriangleFlake(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public TriangleFlake(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public TriangleFlake(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 6;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-100)
			.setY(200 * Math.sqrt(3)/4)
			.setLineLength(200)
			.setTurningAngle(120)
			.setLineLengthScale(2)
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
		addRule('F', "F-F+F");
		addRule('S', "S<");
		addRule('M', "fM");
	}

	public String getAxiom() {
		return "[\\\\.]S+M- F+F+F";
	}

	public String toString() {
		return "Triangle Flake";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
