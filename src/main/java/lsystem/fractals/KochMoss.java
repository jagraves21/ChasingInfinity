package lsystem.fractals;
import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Paint;

public class KochMoss extends AbstractLSystemFractal {
	public KochMoss() {
		this(getSuggestedIterations());
	}

	public KochMoss(int maxIterations) {
		this(maxIterations, true);
	}

	public KochMoss(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public KochMoss(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public KochMoss(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public KochMoss(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public KochMoss(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public KochMoss(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 5;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-250)
			.setY(250)
			.setLineLength(500)
			.setTurningAngle(90)
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
			new float[] {0.5f, 0.7f, 0.9f},
			new Color[] {
				NamedColors.GREEN,
				NamedColors.CRYSTAL_GREEN,
				NamedColors.SACRAMENTO_STATE_GREEN
			}
		);
	}

	protected void init() {
		super.init();
		addRule('F', "FF-F--F-F");
		addRule('S', "S<");
	}

	public String getAxiom() {
		return "S F-F-F-F";
	}

	public String toString() {
		return "Koch Moss";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
