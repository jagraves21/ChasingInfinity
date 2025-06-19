package lsystem.fractals;

import utils.color.ColorUtils;
import utils.color.NamedPalettes;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;

public class LevyCurves extends AbstractLSystemFractal {
	public static final int ITERATIONS = 13;

	protected String axiom = null;
	protected int color_index;
	protected Color[] colors;

	public LevyCurves() {
		this(getSuggestedIterations());
	}

	public LevyCurves(int maxIterations) {
		this(maxIterations, true);
	}

	public LevyCurves(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public LevyCurves(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public LevyCurves(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public LevyCurves(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public LevyCurves(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public LevyCurves(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 500;
	}

	public static int getSuggestedUPS() {
		return 30;
	}

	public static double getLineLenghtScale(double turningAngle) {
		return 2 * Math.cos(Math.toRadians(turningAngle));
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(-175)
			.setY(-150)
			.setLineLength(350)
			.setTurningAngle(0)
			.setLineLengthScale(2)
			.build();
	}

	public Paint getPaint() {
		return getPaint(176.78, 176.78, -176.78, -176.78);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(-176.78),
			worldViewer.worldToScreenY(176.78),
			worldViewer.worldToScreenX(176.78),
			worldViewer.worldToScreenY(-176.78)
		);
	}

	protected Paint getPaint(double x1, double y1, double x2, double y2) {
		return colors[color_index++ % colors.length];
	}

	protected void init() {
		super.init();
		color_index = 0;
		colors = ColorUtils.generateGradient(
			NamedPalettes.SPECTRUM,
			(int) Math.pow(2, ITERATIONS+1)
		);
		// addRule('F', "-F++F-");
		// addRule('S', "S<");
	}

	public String getAxiom() {
		if (axiom == null) {
			String currentSequence = "S F";
			for(int ii=0; ii < ITERATIONS; ii++) {
				StringBuilder nextSequenceBuilder = new StringBuilder();
				for (int jj = 0; jj < currentSequence.length(); jj++) {
					char symbol = currentSequence.charAt(jj);
					if (symbol == 'F') {
						nextSequenceBuilder.append("+F#--F#+");
					} else if (symbol == 'S') {
						nextSequenceBuilder.append("S<");
					} else {
						nextSequenceBuilder.append(symbol);
					}
				}
				currentSequence = nextSequenceBuilder.toString();
			}
			axiom = currentSequence;
		}

		return axiom;
	}

	public void step() {
		double percent = 0;
		if (maxIterations != 0) {
			percent = curIteration / (double) maxIterations;
			int newMax = maxIterations/2;
			int newCur = newMax -
				Math.abs(
					(curIteration % maxIterations) -
					newMax
				);
			percent = newCur / (double) newMax;
		}
		double turningAngle = 45 * percent;
		this.initialTurtle.turningAngle = turningAngle;
		this.initialTurtle.lineLengthScale = 2 * Math.cos(Math.toRadians(turningAngle));
		color_index = 0;
	}

	public void reset() {
		super.reset();
		double turningAngle = 0;
		this.initialTurtle.turningAngle = turningAngle;
		this.initialTurtle.lineLengthScale = 2 * Math.cos(Math.toRadians(turningAngle));
		color_index = 0;
	}

	public String toString() {
		return "Levy Curves";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
