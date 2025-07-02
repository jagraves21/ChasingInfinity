package geometric;

import utils.color.NamedColors;

import renderer.viewer.WorldViewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

public abstract class AbstractShape<T extends AbstractShape<T>> implements Transformable, Comparable<T> {
	public static final Color DEFAULT_COLOR = NamedColors.WHITE;
	public static final double EPSILON = 1e-9;

	public Paint getPaint() { return null; }
	public Paint getPaint(WorldViewer worldViewer) {
		return this.toScreen(worldViewer).getPaint();
	}

	public abstract T toScreen(WorldViewer worldViewer);
	public abstract T toWorld(WorldViewer worldViewer);

	protected abstract void drawWithPaint(Graphics g);
	protected abstract void drawWithPaint(Graphics g, WorldViewer worldViewer);

	public void draw(Graphics g) {
		Graphics2D  g2d = (Graphics2D) g;
		Paint previousPaint = null;
		Paint currentPaint = getPaint();
		if (currentPaint != null && !currentPaint.equals(g2d.getPaint())) {
			previousPaint = g2d.getPaint();
			g2d.setPaint(currentPaint);
		}
		drawWithPaint(g);
		if(previousPaint != null) {
			g2d.setPaint(previousPaint);
		}
	}

	public void draw(Graphics g, WorldViewer worldViewer) {
		Graphics2D  g2d = (Graphics2D) g;
		Paint previousPaint = null;
		Paint currentPaint = getPaint(worldViewer);
		if (currentPaint != null && !currentPaint.equals(g2d.getPaint())) {
			previousPaint = g2d.getPaint();
			g2d.setPaint(currentPaint);
		}
		drawWithPaint(g, worldViewer);
		if(previousPaint != null) {
			g2d.setPaint(previousPaint);
		}
	}

	public abstract int compareTo(T other);
}

