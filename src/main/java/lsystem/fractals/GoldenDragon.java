package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class GoldenDragon extends AbstractLSystemFractal {
	public GoldenDragon() {
		this(getSuggestedIterations());
	}

	public GoldenDragon(int maxIterations) {
		this(maxIterations, true);
	}

	public GoldenDragon(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public GoldenDragon(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public GoldenDragon(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public GoldenDragon(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public GoldenDragon(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public GoldenDragon(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 12;
	}

	public static TurtleState getInitialTurtleState() {
		double phi = (1 + Math.sqrt(5))/2;
		double r = Math.pow(1/phi, 1/phi);
		double A = Math.toDegrees(Math.acos(
			(1 + Math.pow(r,2) - Math.pow(r,4)) / (2*r)
		));
		double B = Math.toDegrees(Math.acos(
			(1 + Math.pow(r,4) - Math.pow(r,2)) / (2*Math.pow(r,2))
		));
		double C = 180 - (A + B);

		double turningAngle = A;
		double angleShift = -B;
		double lineLengthScale = r;
		System.out.printf("A = %f (%f)%n", A, Math.toRadians(A));
		System.out.printf("B = %f (%f)%n", B, Math.toRadians(B));
		System.out.printf("C = %f (%f)%n", C, Math.toRadians(C));
		System.out.printf("angleShift = %f (%f)%n", angleShift, Math.toRadians(angleShift));
		return new TurtleState.Builder()
			.setX(-200)
			.setY(0)
			.setAngle(0)
			.setLineLength(400)
			.setTurningAngle(turningAngle)
			.setAngleShift(angleShift)
			.setLineLengthScale(lineLengthScale)
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
		//addRule('F', ">+F(-)>F");
		//addRule('S', "v-^S<");

		addRule('F', "");
		addRule('X', "[F]+>FX(-)>FY<<");
		addRule('Y', "[F]-)+(>>FY)-(++<F-)-(X<");
		addRule('Z', "ZF");
	}

	public String getAxiom() {
		return "[F][X][Y]";
	}

	public String toString() {
		return "Golden Dragon";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
