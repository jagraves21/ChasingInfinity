package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class TerDragon extends AbstractLSystemFractal {
	public TerDragon() {
		this(getSuggestedIterations());
	}

	public TerDragon(int maxIterations) {
		this(maxIterations, true);
	}

	public TerDragon(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public TerDragon(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public TerDragon(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public TerDragon(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public TerDragon(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public TerDragon(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 12;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-300)
			.setY(0)
			.setLineLength(600)
			.setTurningAngle(120)
			.setAngleScale(4)
			.setLineLengthScale(Math.sqrt(3))
			.build();
	}

	public Paint getPaint() {
		return getPaint(0, 0, 360);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(0),
			worldViewer.worldToScreenY(0),
			360 * worldViewer.getZoom()
		);
	}

	protected Paint getPaint(double x, double y, double radius) {
		return new RadialGradientPaint(
			(float) x,
			(float) y,
			(float) radius,
			new float[] {0.3f, 1.0f},
			new Color[] {NamedColors.BLUE, NamedColors.RED}
		);
	}

	protected void init() {
		super.init();
		addRule('F', "F-F+F");
		addRule('S', "vA+^T<");
		addRule('A', "A+");
		addRule('T', "T<");
	}

	public String getAxiom() {
		return "S F";
	}

	public String toString() {
		return "Ter Dragon";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
