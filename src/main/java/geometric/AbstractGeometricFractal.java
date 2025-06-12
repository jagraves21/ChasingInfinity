package geometric;

import renderer.AbstractFractal;
import renderer.Drawable;
import renderer.WorldViewer;

import java.lang.reflect.Method;

import utils.ArgumentParser;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Paint;

import java.awt.geom.AffineTransform;

public abstract class AbstractGeometricFractal<T extends AbstractGeometricFractal<T>> 
	extends AbstractFractal implements CompositeTransformable<T> {

	protected int curIteration;
	protected int maxIterations;
	boolean reset;
	
	public AbstractGeometricFractal(int maxIterations, boolean reset) {
		super();
		this.maxIterations = maxIterations;
		this.reset = reset;
		this.curIteration = 0;
	}

	public static int getSuggestedIterations() {
		return 5;
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
		return null;
	}

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint();
	}

	protected void init() {
		this.curIteration = 0;
	}

	public void reset() {
		this.curIteration = 0;
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
			curIteration++;
			step();
		}
	}

	public boolean isVisibleOnScreen(WorldViewer worldViewer) {
		return true;
	}

	public void draw1(Graphics g, WorldViewer worldViewer) {
		Graphics2D g2d = (Graphics2D) g.create();
		Paint paint = getPaint(worldViewer);
		if(paint != null) g2d.setPaint(paint);
		for (Drawable d : this) {
			if (d.isVisibleOnScreen(worldViewer)) {
				d.draw(g2d, worldViewer);
			}
		}
		g2d.dispose();
	}

	public void draw2(Graphics g, WorldViewer worldViewer) {
		System.out.println(worldViewer);
		Graphics2D g2d = (Graphics2D) g.create();
		AffineTransform originalTransform = g2d.getTransform();

		AffineTransform worldToScreen =
			worldViewer.getWorldToScreenTransform(originalTransform);
		g2d.setTransform(worldToScreen);
		g2d.setStroke(
			new java.awt.BasicStroke((float)(1/worldViewer.getZoom()))
		);

		Paint paint = getPaint(worldViewer);
		if(paint != null) g2d.setPaint(paint);
		for (Drawable d : this) {
			if (d.isVisibleOnScreen(worldViewer)) {
				d.draw(g2d);
			}
		}

		// g2d.setTransform(originalTransform);
		g2d.dispose();
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
			AbstractGeometricFractal<?> fractal = (AbstractGeometricFractal<?>) instance;
			AbstractGeometricFractal.display(
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
