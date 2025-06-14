package lsystem.fractals;

import utils.color.NamedPalettes;

import renderer.WorldViewer;

import lsystem.AbstractLSystemFractal;
import lsystem.TurtleState;

import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

public class Shield extends AbstractLSystemFractal {
	public Shield() {
		this(getSuggestedIterations());
	}

	public Shield(int maxIterations) {
		this(maxIterations, true);
	}

	public Shield(boolean reset) {
		this(getSuggestedIterations(), reset);
	}

	public Shield(int maxIterations, boolean reset) {
		this(maxIterations, reset, getInitialTurtleState());
	}

	public Shield(TurtleState initialTurtle) {
		this(getSuggestedIterations(), initialTurtle);
	}

	public Shield(int maxIterations, TurtleState initialTurtle) {
		this(maxIterations, true, initialTurtle);
	}

	public Shield(boolean reset, TurtleState initialTurtle) {
		this(getSuggestedIterations(), reset, initialTurtle);
	}

	public Shield(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super(maxIterations, reset, initialTurtle);
	}

	public static int getSuggestedIterations() {
		return 10;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder()
			.setX(0)
			.setY(0)
			.setLineLength(70)
			.setTurningAngle(15)
			.build();
	}

	public Paint getPaint() {
		return getPaint(0, 0, 400);
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint(
			worldViewer.worldToScreenX(0),
			worldViewer.worldToScreenY(0),
			400 * worldViewer.getZoom()
		);
	}

	protected Paint getPaint(double x, double y, double radius) {
		int count = this.curIteration+2;
		Color[] baseColors = NamedPalettes.OIL_SLICK;
		Color[] colors = new Color[count];
		float[] dist = new float[count];
		for (int ii=0; ii < count; ii++) {
			colors[ii] = baseColors[ii % baseColors.length];
			dist[ii] = ii / (float) (dist.length-1);
		}

		return new RadialGradientPaint(
			(float) x,
			(float) y,
			(float) radius,
			dist,
			colors
		);
	}

	protected void init() {
		super.init();
		addRule('A', "X+X+X+X+X+X+");
		addRule('X', "[F+F+F+F[---X-Y]+++++F++++++++F-F-F-F]");
		addRule('Y', "[F+F+F+F[---Y]+++++F++++++++F-F-F-F]");
		addRule('S', "<");
	}

	public String getAxiom() {
		StringBuilder stringBuilder = new StringBuilder();
		String axiom = "S AAAA";
		for (int ii=0; ii < axiom.length(); ii++) {
			char symbol = axiom.charAt(ii);
			if (symbol == 'A') {
				stringBuilder.append("X+X+X+X+X+X+");
			} else {
				stringBuilder.append(symbol);
			}
		}

		axiom = stringBuilder.toString();
		stringBuilder.setLength(0);
		for (int ii=0; ii < axiom.length(); ii++) {
			char symbol = axiom.charAt(ii);
			if (symbol == 'X') {
				stringBuilder.append("[F+F+F+F[---X-Y]+++++F++++++++F-F-F-F]");
			} else {
				stringBuilder.append(axiom.charAt(ii));
			}
		}

		return stringBuilder.toString();
	}

	public void reset() {
		super.reset();
		this.initialTurtle.lineLengthScale = 1;
	}

	public void step() {
		super.step();
		this.initialTurtle.lineLengthScale = this.curIteration+2;
	}

	public String toString() {
		return "Shield";
	}

	public static void main(String[] args) {
		AbstractLSystemFractal.display(args);
	}
}
