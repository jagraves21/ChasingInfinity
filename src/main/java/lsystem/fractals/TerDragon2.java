package lsystem.fractals;

import utils.color.NamedPalettes;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class TerDragon2 extends AbstractLSystemFractal {
	public static final Color[] COLORS = NamedPalettes.SPECTRUM;

	protected int color_index;

	public TerDragon2() {
		this(getSuggestedIterations());
	}

	public TerDragon2(int maxIterations) {
		this(maxIterations, true);
	}

	public TerDragon2(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public TerDragon2(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public TerDragon2(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public TerDragon2(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public TerDragon2(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public TerDragon2(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(0)
			.setY(0)
			.setLineLength(200)
			.setTurningAngle(120)
			.setAngleScale(2)
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
		addRule('F', "F-F+F");
		addRule('S', "vvA+^^T<");
		addRule('A', "A+");
		addRule('T', "T<");
		color_index = 0;
	}

	public String getAxiom() {
		return "S [F]v+^#[F]v+^#[F]v+^#[F]v+^#[F]v+^#[F]";
	}

	public String toString() {
		return "Ter Dragon 2nd Form";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
