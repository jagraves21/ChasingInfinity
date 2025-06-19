package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class LevyTapestryOutside extends AbstractLSystemFractal {
	public LevyTapestryOutside() {
		this(getSuggestedIterations());
	}

	public LevyTapestryOutside(int maxIterations) {
		this(maxIterations, true);
	}

	public LevyTapestryOutside(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public LevyTapestryOutside(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public LevyTapestryOutside(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public LevyTapestryOutside(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public LevyTapestryOutside(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public LevyTapestryOutside(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(175/2.0)
			.setY(-175/2.0)
			.setLineLength(175)
			.setTurningAngle(45)
			.setLineLengthScale(Math.sqrt(2))
			.build();
	}

	public Paint getPaint() {
		return getPaint(0, 0, 175*Math.sqrt(2));
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(0),
			worldViewer.worldToScreenY(0),
			175*Math.sqrt(2) * worldViewer.getZoom()
		);
	}

	protected Paint getPaint(double x, double y, double radius) {
		return new RadialGradientPaint(
			(float) x,
			(float) y,
			(float) radius,
			new float[] {0.75f, 1.0f},
			new Color[] {NamedColors.ORANGE, NamedColors.BLUE}
		);
	}

	protected void init() {
		super.init();
		addRule('F', "-F++F-");
		addRule('S', "S<");
	}

	public String getAxiom() {
		return "S ^+vF^+vF^+vF^+vF";
	}

	public String toString() {
		return "Levy Tapestry Outside";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
