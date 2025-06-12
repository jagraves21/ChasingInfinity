package lsystem.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class DragonFamily extends AbstractLSystemFractal {
	public static final Color[] COLORS = {
		NamedColors.RED,
		NamedColors.GREEN,
		NamedColors.BLUE,
		NamedColors.ORANGE,
	};

	protected int color_index;

	public DragonFamily() {
		this(getSuggestedIterations());
	}

	public DragonFamily(int maxIterations) {
		this(maxIterations, true);
	}

	public DragonFamily(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public DragonFamily(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public DragonFamily(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public DragonFamily(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public DragonFamily(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public DragonFamily(int maxIterations, boolean reset, TurtleState initialTurtle) {
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
		addRule('X', "#X+YF");
		addRule('Y', "FX-Y");
		addRule('S', "v-^S<");
		color_index = 0;
	}

	public String getAxiom() {
		return "S FX";
	}

	public String toString() {
		return "Dragon Family";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
