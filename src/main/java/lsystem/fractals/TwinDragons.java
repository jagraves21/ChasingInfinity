package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class TwinDragons extends AbstractLSystemFractal {
	public static final Color[] COLORS = {
		NamedColors.RED, NamedColors.BLUE
	};

	protected int color_index;

	public TwinDragons() {
		this(getSuggestedIterations());
	}

	public TwinDragons(int maxIterations) {
		this(maxIterations, true);
	}

	public TwinDragons(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public TwinDragons(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public TwinDragons(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public TwinDragons(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public TwinDragons(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public TwinDragons(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 17;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-150)
			.setY(0)
			.setAngle(0)
			.setLineLength(300)
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
		return "v-^v-^S<< FX+YF+FX-YF+#FX+YF+FX-YF+";
	}

	public String toString() {
		return "Twin Dragons";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
