package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class CakeSquareStar extends AbstractLSystemFractal {
	public CakeSquareStar() {
		this(getSuggestedIterations());
	}

	public CakeSquareStar(int maxIterations) {
		this(maxIterations, true);
	}

	public CakeSquareStar(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public CakeSquareStar(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public CakeSquareStar(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public CakeSquareStar(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public CakeSquareStar(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public CakeSquareStar(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 4;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(0)
			.setY(0)
			.setLineLength(180)
			.setTurningAngle(9)
			.setLineLengthScale(2 + Math.sqrt(2))
			.build();
	}

	public Paint getPaint() {
		return getPaint(0, 0, 180);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(0),
			worldViewer.worldToScreenY(0),
			180 * worldViewer.getZoom()
		);
	}

	protected Paint getPaint(double x, double y, double radius) {
		return NamedColors.YELLOW;
		/*return new RadialGradientPaint(
			(float) x,
			(float) y,
			(float) radius,
			new float[] {0.5f, 1.0f},
			new Color[] {NamedColors.SCHOOL_BUS_YELLOW, NamedColors.BARN_RED}
		);*/
	}

	protected void init() {
		super.init();
		addRule('F', "F[+++++F----------F]-----F++++++++++F-----F");
		addRule('S', "S<");
	}

	public String getAxiom() {
		return "S ++[F]++++++++[F]++++++++[F]++++++++[F]++++++++[F]";
	}

	public String toString() {
		return "Cake Square Star";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
