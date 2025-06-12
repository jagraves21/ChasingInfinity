package lsystem.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class LevyTapestryInside extends AbstractLSystemFractal {
	public LevyTapestryInside() {
		this(getSuggestedIterations());
	}

	public LevyTapestryInside(int maxIterations) {
		this(maxIterations, true);
	}

	public LevyTapestryInside(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public LevyTapestryInside(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public LevyTapestryInside(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public LevyTapestryInside(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public LevyTapestryInside(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public LevyTapestryInside(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(250/2.0)
			.setY(250/2.0)
			.setLineLength(250)
			.setTurningAngle(45)
			.setLineLengthScale(Math.sqrt(2))
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
		return "S ^-vF^-vF^-vF^-vF";
	}

	public String toString() {
		return "Levy Tapestry Inside";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
