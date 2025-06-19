package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class DragonCurve extends AbstractLSystemFractal {
	public DragonCurve() {
		this(getSuggestedIterations());
	}

	public DragonCurve(int maxIterations) {
		this(maxIterations, true);
	}

	public DragonCurve(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public DragonCurve(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public DragonCurve(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public DragonCurve(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public DragonCurve(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public DragonCurve(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 12;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-200)
			.setY(75)
			.setAngle(0)
			.setLineLength(500)
			.setTurningAngle(90)
			.setAngleScale(2)
			.setLineLengthScale( Math.sqrt(2) )
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
		addRule('X', "X+YF");
		addRule('Y', "FX-Y");
		addRule('S', "v-^S<");
	}

	public String getAxiom() {
		return "S FX";
	}

	public String toString() {
		return "Dragon Curve";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
