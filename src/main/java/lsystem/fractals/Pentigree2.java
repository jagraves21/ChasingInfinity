package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class Pentigree2 extends AbstractLSystemFractal {
	public static final Color[] COLORS = {
		NamedColors.RED,
		NamedColors.BLUE,
		NamedColors.GREEN,
		NamedColors.ORANGE,
		NamedColors.PURPLE
	};

	protected int color_index;

	public Pentigree2() {
		this(getSuggestedIterations());
	}

	public Pentigree2(int maxIterations) {
		this(maxIterations, true);
	}

	public Pentigree2(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public Pentigree2(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public Pentigree2(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public Pentigree2(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public Pentigree2(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public Pentigree2(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 5;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(
				300 / (2 * Math.sin(Math.PI/5)) * Math.cos(Math.toRadians(234))
			)
			.setY(
				300 / (2 * Math.sin(Math.PI/5)) * Math.sin(Math.toRadians(234))
			)
			.setLineLength(300)
			.setTurningAngle(-36)
			.setLineLengthScale( 2/(3-Math.sqrt(5)) )
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
		addRule('F', "-F--F++++F++F--F--F+");
		addRule('S', "S<");
		color_index = 0;
	}

	public String getAxiom() {
		return "S F#--F#--F#--F#--F";
	}

	public String toString() {
		return "Pentigree 2nd Form";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
