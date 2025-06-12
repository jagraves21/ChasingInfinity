package lsystem.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class FourDragons extends AbstractLSystemFractal {
	public static final Color[] COLORS = {
		NamedColors.RED,
		NamedColors.GREEN,
		NamedColors.BLUE,
		NamedColors.ORANGE,
	};

	protected int color_index;

	public FourDragons() {
		this(getSuggestedIterations());
	}

	public FourDragons(int maxIterations) {
		this(maxIterations, true);
	}

	public FourDragons(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public FourDragons(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public FourDragons(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public FourDragons(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public FourDragons(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public FourDragons(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 18;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(0)
			.setY(0)
			.setLineLength(200)
			.setTurningAngle(90)
			.setAngleScale(2)
			.setLineLengthScale( Math.sqrt(2) )
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
		addRule('X', "X+YF");
		addRule('Y', "FX-Y");
		addRule('S', "v-^S<");
		color_index = 0;
	}

	public String getAxiom() {
		return "~S [FX]+#[FX]+#[FX]+#[FX]";
	}

	public String toString() {
		return "Four Dragons";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
