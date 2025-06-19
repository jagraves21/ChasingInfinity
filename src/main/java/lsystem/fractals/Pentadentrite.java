package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class Pentadentrite extends AbstractLSystemFractal {
	public Pentadentrite() {
		this(getSuggestedIterations());
	}

	public Pentadentrite(int maxIterations) {
		this(maxIterations, true);
	}

	public Pentadentrite(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public Pentadentrite(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public Pentadentrite(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public Pentadentrite(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public Pentadentrite(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public Pentadentrite(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 5;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-250)
			.setY(-100)
			.setLineLength(500)
			.setTurningAngle(-72)
			.setLineLengthScale( 2/(3-Math.sqrt(5)) )
			.setAngleShift(
				-72 + Math.toDegrees(Math.asin(
 					((Math.sqrt(5)-1) / 4) * Math.sqrt((25+Math.sqrt(5)) / 62)
 				))
			)
			.setLineLengthScale( 1/Math.sqrt((6-Math.sqrt(5))/31) )
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
			new float[] {0/5f, 1/5f, 2/5f, 3/5f, 4/5f, 5/5f},
			new Color[] {
				NamedColors.MIDNIGHT_BLUE,
				NamedColors.MAXIMUM_RED_PURPLE,
				NamedColors.PANSY_PURPLE,
				NamedColors.SPANISH_RED,
				NamedColors.PUMPKIN,
				NamedColors.SCHOOL_BUS_YELLOW
			}
		);
	}

	protected void init() {
		super.init();
		addRule('F', "F-F+F++F-F-F");
		addRule('S', "(-T<)");
		addRule('T', "-T<");
	}

	public String getAxiom() {
		return "S F";
	}

	public String toString() {
		return "Pentadentrite";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
