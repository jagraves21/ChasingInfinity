package lsystem.fractals;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class MooreCurve extends AbstractLSystemFractal {
	public MooreCurve() {
		this(getSuggestedIterations());
	}

	public MooreCurve(int maxIterations) {
		this(maxIterations, true);
	}

	public MooreCurve(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public MooreCurve(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public MooreCurve(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public MooreCurve(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public MooreCurve(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public MooreCurve(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 6;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(250)
			.setY(-250)
			.setAngle(90)
			.setLineLength(500)
			.setTurningAngle(90)
			.build();
	}

	public Paint getPaint() {
		return getPaint(0, 0, 350);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(0),
			worldViewer.worldToScreenY(0),
			350 * worldViewer.getZoom()
		);
	}

	protected Paint getPaint(double x, double y, double radius) {
		return new RadialGradientPaint(
			(float) x,
			(float) y,
			(float) radius,
			new float[] {0.0f, 0.33f, 0.66f, 1.0f},
			new Color[] {
				NamedColors.ORANGE, NamedColors.RED,
				NamedColors.BLUE, NamedColors.GREEN
			}
		);
	}

	protected void init() {
		super.init();
		addRule('L', "-RF+LFL+FR-");
		addRule('R', "+LF-RFR-FL+");
		addRule('S', "<+MMf-");
		addRule('M', "MMf");
	}

	public String getAxiom() {
		return "S LFL+F+LFL";
	}

	public void reset() {
		super.reset();
		this.initialTurtle.lineLengthScale = 1;
	}

	public void step() {
		super.step();
		this.initialTurtle.lineLengthScale = Math.pow(2,this.curIteration+2)-1;
	}

	public String toString() {
		return "Moore Curve";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
