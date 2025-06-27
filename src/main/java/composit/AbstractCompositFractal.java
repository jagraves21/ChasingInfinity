package composit;

import renderer.AbstractFractal;
import renderer.Drawable;
import renderer.viewer.WorldViewer;

import utils.ArgumentParser;

import java.lang.reflect.Method;

import java.util.LinkedList;
import java.util.List;

import java.awt.Graphics;

public abstract class AbstractCompositFractal extends AbstractFractal {
	protected List<Drawable> drawables;
	protected List<AbstractFractal> fractals;
	
	public void addDrawable(Drawable drawable) {
		drawables.add(drawable);
	}

	public void addFractal(AbstractFractal fractal) {
		fractals.add(fractal);
		addDrawable(fractal);
	}

	protected void init() {
		drawables = new LinkedList<>();
		fractals = new LinkedList<>();
	}

	public void reset() {
		fractals.forEach(AbstractFractal::reset);
	}

	public void step() {
		fractals.forEach(AbstractFractal::step);
	}

	public void update() {
		fractals.forEach(AbstractFractal::update);
	}

	public boolean isVisibleOnScreen(WorldViewer worldViewer) {
		for (Drawable d : drawables) {
			if (d.isVisibleOnScreen(worldViewer)) {
				return true;
			}
		}
		return false;
	}

	public void draw(Graphics g, WorldViewer worldViewer) {
		for (Drawable d : drawables) {
			d.draw(g, worldViewer);
		}
	}

	/*public static void display(String[] args) {
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
	}*/
}
