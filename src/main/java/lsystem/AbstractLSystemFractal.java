package lsystem;

import utils.ArgumentParser;
import utils.color.NamedColors;

import renderer.AbstractFractal;
import renderer.viewer.WorldViewer;

import java.lang.reflect.Method;

import java.util.Map;
import java.util.HashMap;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Paint;

import java.awt.geom.AffineTransform;

public abstract class AbstractLSystemFractal extends AbstractFractal {
	protected int curIteration;
	protected int maxIterations;
	protected boolean reset;
	protected String currentSequence;
	protected Map<Character,String> productionRules;
	protected TurtleState initialTurtle;

	public AbstractLSystemFractal(int maxIterations, boolean reset, TurtleState initialTurtle) {
		super();
		this.maxIterations = maxIterations;
		this.reset = reset;
		this.initialTurtle = new TurtleState(initialTurtle);
		this.curIteration = 0;
	}

	public static int getSuggestedIterations() {
		return 5;
	}

	public static TurtleState getInitialTurtleState() {
		return new TurtleState.Builder().build();
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public boolean getReset() {
		return reset;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public Paint getPaint() {
		return NamedColors.WHITE;
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint();
	}

	public abstract String getAxiom();

	public void addRule(char predecessor, String successor) {
		productionRules.put(predecessor, successor);
	}

	public void removeRule(char predecessor) {
		productionRules.remove(predecessor);
	}

	public void removeAllRules() {
		productionRules.clear();
	}

	protected void init() {
		this.productionRules = new HashMap<>();
		currentSequence = getAxiom();
	}

	public void reset() {
		this.curIteration = 0;
		currentSequence = getAxiom();
	}

	public static String getUniqueCharacters(String str) {
        java.util.Set<Character> uniqueChars = new java.util.TreeSet<>();
        for (char c : str.toCharArray()) {
            uniqueChars.add(c);
        }

        StringBuilder result = new StringBuilder();
        for (char c : uniqueChars) {
            result.append(c);
        }

        return result.toString();
    }

	public void step() {
		StringBuilder nextSequenceBuilder = new StringBuilder();
		for (int ii = 0; ii < currentSequence.length(); ii++) {
			char symbol = currentSequence.charAt(ii);

			String successor = productionRules.get(symbol);
			if (successor == null) {
				nextSequenceBuilder.append(symbol);
			} else {
				nextSequenceBuilder.append(successor);
			}
		}
		currentSequence = nextSequenceBuilder.toString();
		System.out.println( getUniqueCharacters(currentSequence) );
	}

	public void update() {
		if (curIteration >= maxIterations) {
			System.out.println("reset");
			if(reset) {
				reset();
			}
		}
		else {
			System.out.println("step");
			step();
			curIteration++;
		}
	}

	public boolean isVisibleOnScreen(WorldViewer worldViewer) {
		return true;
	}

	public void draw1(Graphics g, WorldViewer worldViewer) {
		Graphics2D g2d = (Graphics2D) g.create();
		try {
			Paint paint = getPaint(worldViewer);
			if(paint != null) g2d.setPaint(paint);

			TurtleState turtle = new TurtleState(initialTurtle);
			for (int ii = 0; ii < currentSequence.length(); ii++) {
				char symbol = currentSequence.charAt(ii);
				if (symbol == '#') {
					paint = getPaint(worldViewer);
					if(paint != null) g2d.setPaint(paint);
				}
				turtle.updateState(symbol, g2d, worldViewer);
			}
		} finally {
			g2d.dispose();
		}
	}

	public void draw2(Graphics g, WorldViewer worldViewer) {
		System.out.println(worldViewer);
		Graphics2D g2d = (Graphics2D) g.create();
		try {
			AffineTransform originalTransform = g2d.getTransform();

			AffineTransform worldToScreen =
				worldViewer.getWorldToScreenTransform(originalTransform);
			g2d.setTransform(worldToScreen);
			g2d.setStroke(
				new java.awt.BasicStroke((float)(1/worldViewer.getZoom()))
			);

			Paint paint = getPaint(worldViewer);
			if(paint != null) g2d.setPaint(paint);

			TurtleState turtle = new TurtleState(initialTurtle);
			for (int ii = 0; ii < currentSequence.length(); ii++) {
				char symbol = currentSequence.charAt(ii);
				turtle.updateState(symbol, g2d, worldViewer);
			}

			// this is not needed as dispose is called
			// g2d.setTransform(originalTransform);
		} finally {
			g2d.dispose();
		}
	}

	public void draw(Graphics g, WorldViewer worldViewer) {
		draw1(g, worldViewer);
	}

	public static void display(String[] args) {
		try {
			String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
			Class<?> callerClass = Class.forName(callerClassName);

			Method getSuggestedIterations = callerClass.getMethod("getSuggestedIterations");
			Method getSuggestedFPS = callerClass.getMethod("getSuggestedFPS");
			Method getSuggestedUPS = callerClass.getMethod("getSuggestedUPS");
			ArgumentParser argumentParser = new ArgumentParser(
				callerClassName,
				800,
				600,
				(int) getSuggestedIterations.invoke(null),
				true,
				(int) getSuggestedFPS.invoke(null),
				(int) getSuggestedUPS.invoke(null)
			);
			argumentParser.parseArguments(args);

			Object instance = callerClass.getConstructor(
				int.class, boolean.class
			).newInstance(argumentParser.iterations, argumentParser.reset);
			AbstractLSystemFractal fractal = (AbstractLSystemFractal) instance;
			AbstractLSystemFractal.display(
				fractal,
				argumentParser.width,
				argumentParser.height,
				argumentParser.fps,
				argumentParser.ups
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
