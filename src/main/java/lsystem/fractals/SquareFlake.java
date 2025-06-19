package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class SquareFlake extends AbstractLSystemFractal {
	public SquareFlake() {
		this(getSuggestedIterations());
	}

	public SquareFlake(int maxIterations) {
		this(maxIterations, true);
	}

	public SquareFlake(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public SquareFlake(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public SquareFlake(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public SquareFlake(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public SquareFlake(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public SquareFlake(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 4;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX( -(150 + 150*Math.sqrt(2)/2) )
			.setY(0)
			.setLineLength(150)
			.setTurningAngle(45)
			.setLineLengthScale( 2+Math.sqrt(2) )
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
			new float[] {0.40f, 0.60f, 0.80f, 1.0f},
			new Color[] {
				NamedColors.PERSIMMON,
				NamedColors.ROSSO_CORSA,
				NamedColors.SEANCE,
				NamedColors.PAUA
			}
		);
	}

	protected void init() {
		super.init();
		addRule('F', "F[+F[+F]--F]-F[-F]++F-F");
		addRule('S', "S<");
	}

	public String getAxiom() {
		return "S F[+F[+F]--F]-F[-F]++F-F";
	}

	public String toString() {
		return "Square Flake";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
