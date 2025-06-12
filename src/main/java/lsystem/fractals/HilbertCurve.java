package lsystem.fractals;

import utils.color.NamedColors;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class HilbertCurve extends AbstractLSystemFractal {
	public HilbertCurve() {
		this(getSuggestedIterations());
	}

	public HilbertCurve(int maxIterations) {
		this(maxIterations, true);
	}

	public HilbertCurve(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public HilbertCurve(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public HilbertCurve(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public HilbertCurve(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public HilbertCurve(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public HilbertCurve(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 6;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(250)
			.setY(250)
			.setAngle(180)
			.setLineLength(500)
			.setTurningAngle(90)
			.setLineLengthScale(3)
			.build();
	}

	public Paint getPaint() {
		return getPaint(0, 0, 250 * Math.sqrt(2));
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(0),
			worldViewer.worldToScreenY(0),
			250 * Math.sqrt(2) * worldViewer.getZoom()
		);
	}

	protected Paint getPaint(double x, double y, double radius) {
		return new RadialGradientPaint(
			(float) x,
			(float) y,
			(float) radius,
			new float[] {0.0f, 1.0f},
			new Color[] {NamedColors.GREEN, NamedColors.BLUE}
		);
	}

	protected void init() {
		super.init();
		addRule('L', "+RF-LFL-FR+");
		addRule('R', "-LF+RFR+FL-");
		addRule('S', "<");
	}

	public String getAxiom() {
		return "S +RF-LFL-FR+";
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
		return "Hilbert Curve";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
