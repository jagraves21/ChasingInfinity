package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class Pentadentrite2 extends AbstractLSystemFractal {
	public static final Color[] COLORS = {
		NamedColors.RED,
		NamedColors.BLUE,
		NamedColors.GREEN,
		NamedColors.ORANGE,
		NamedColors.PURPLE
	};

	protected int color_index;

	public Pentadentrite2() {
		this(getSuggestedIterations());
	}

	public Pentadentrite2(int maxIterations) {
		this(maxIterations, true);
	}

	public Pentadentrite2(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public Pentadentrite2(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public Pentadentrite2(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public Pentadentrite2(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public Pentadentrite2(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public Pentadentrite2(int maxIterations, boolean reset, TurtleState initialTurtle) {
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
			.setTurningAngle(-72)
			.setLineLengthScale( 1/Math.sqrt((6-Math.sqrt(5))/31) )
			.setAngleShift(
				-72 + Math.toDegrees(Math.asin(
					((Math.sqrt(5)-1) / 4) * Math.sqrt((25+Math.sqrt(5)) / 62)
 				))
			)
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
		addRule('F', "F-F+F++F-F-F");
		addRule('S', "(-T<)");
		addRule('T', "-T<");
		color_index = 0;
	}

	public String getAxiom() {
		return "S F#-F#-F#-F#-F";
	}

	public String toString() {
		return "Pentadentrite 2nd Form";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
