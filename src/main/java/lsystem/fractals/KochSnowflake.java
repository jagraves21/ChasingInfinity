package lsystem.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.GradientPaint;
import java.awt.Paint;

public class KochSnowflake extends AbstractLSystemFractal {
	public KochSnowflake() {
		this(getSuggestedIterations());
	}

	public KochSnowflake(int maxIterations) {
		this(maxIterations, true);
	}

	public KochSnowflake(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public KochSnowflake(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public KochSnowflake(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public KochSnowflake(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public KochSnowflake(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public KochSnowflake(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 4;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(0)
			.setY(250)
			.setAngle(240)
			.setLineLength( 250 * Math.sqrt(3) )
			.setTurningAngle(60)
			.setLineLengthScale(3)
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
		addRule('F', "F-F++F-F");
		addRule('S', "S<");
	}

	public String getAxiom() {
		return "S F++F++F";
	}

	public String toString() {
		return "Koch Snowflake";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
