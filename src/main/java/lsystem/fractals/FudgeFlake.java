package lsystem.fractals;

import utils.color.NamedPalettes;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class FudgeFlake extends AbstractLSystemFractal {
	public static final Color[] COLORS = NamedPalettes.RGB;

	protected int color_index;

	public FudgeFlake() {
		this(getSuggestedIterations());
	}

	public FudgeFlake(int maxIterations) {
		this(maxIterations, true);
	}

	public FudgeFlake(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public FudgeFlake(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public FudgeFlake(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public FudgeFlake(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public FudgeFlake(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public FudgeFlake(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-200)
			.setY(-400 * Math.sqrt(3)/6)
			.setLineLength(400)
			.setTurningAngle(30)
			.setAngleScale(3)
			.setLineLengthScale(Math.sqrt(3))
			.build();
	}

	public Paint getPaint() {
		return COLORS[color_index++ % COLORS.length];
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint();
	}

	protected void init() {
		super.init();
		addRule('F', "+F----F++++F-");
		addRule('S', "S<");
		color_index = 0;
	}

	public String getAxiom() {
		return "S F++++#F++++#F";
	}

	public String toString() {
		return "Fudge Flake";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
