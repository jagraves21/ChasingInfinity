package geometric;

import renderer.viewer.WorldViewer;

import utils.color.NamedColors;

import java.util.Objects;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.RadialGradientPaint;

import java.awt.geom.Point2D;

import java.awt.image.BufferedImage; 

public class Bubble extends Circle {
	public static final boolean DEFAULT_FILL = true;

	private Color color;
	private RadialGradientPaint gradientPaint;

	public Bubble() {
		this(0, 0);
	}

	public Bubble(Point center) {
		this(center, DEFAULT_RADIUS);
	}

	public Bubble(Point center, double radius) {
		this(center, radius, DEFAULT_COLOR);
	}

	public Bubble(Point center, double radius, Color color) {
		this(center, radius, color, true);
	}
	
	private Bubble(Point center, double radius, Color color, boolean copy) {
		super(
			center,
			radius,
			createGradientPaint(center, radius, color),
			DEFAULT_FILL,
			copy
		);
		this.color = color;
	}

	public Bubble(double x, double y) {
		this(x, y, DEFAULT_RADIUS);
	}

	public Bubble(double x, double y, double radius) {
		this(x, y, radius, DEFAULT_COLOR);
	}

	public Bubble(double x, double y, double radius, Color color) {
		this(
			new Point(x, y, color),
			radius,
			color,
			false
		);
	}

	public Bubble(Bubble other) {
		this(other.center, other.radius, other.color, true);
	}

	public static Bubble fromCircle(Circle circle) {
		Color color = circle.getPaint() instanceof Color ? (Color) circle.getPaint() : DEFAULT_COLOR;
		return Bubble.fromCircle(
			circle,
			color
		);
	}
	
	public static Bubble fromCircle(Circle circle, Color color) {
		return new Bubble(
			circle.getCenter(), circle.getRadius(), color, true
		);
	}

	public Bubble toScreen(WorldViewer worldViewer) {
		Circle screenCircle = super.toScreen(worldViewer);
		return new Bubble(screenCircle.getCenter(), screenCircle.getRadius(), this.color, screenCircle.isFilled());
	}

	protected static RadialGradientPaint createGradientPaint(Point center, double radius, Color color) {
		float[] dist = {0.7f, 1.0f};
		Color[] colors = {
			new Color(color.getRed(), color.getGreen(), color.getBlue(), 0),
			new Color(color.getRed(), color.getGreen(), color.getBlue(), 255)
		};
		return new RadialGradientPaint(
			new Point2D.Double(center.getX(), center.getY()),
			(float) radius,
			dist,
			colors
		);
	}

	public Color getColor() {
		return color;
	}

	public Bubble toWorld(WorldViewer worldViewer) {
		Circle worldCircle = super.toWorld(worldViewer);
		return new Bubble(worldCircle.getCenter(), worldCircle.getRadius(), this.color, worldCircle.isFilled());
	}

	public Bubble invert(Circle other) {
		Color color = other instanceof Bubble ? ((Bubble) other).getColor() : this.getColor();
		Circle c = super.invert(other);
		return new Bubble(c.getCenter(), c.getRadius(), color, c.isFilled());
	}

	public Bubble invert2(Circle other) {
		Color color = other instanceof Bubble ? ((Bubble) other).getColor() : this.getColor();
		Circle c = super.invert2(other);
		return new Bubble(c.getCenter(), c.getRadius(), color, c.isFilled());
	}

	public String toString() {
		return String.format(
			"Bubble{x=%.2f, y=%.2f, radius=%.2f, paint=%s}",
			getX(), getY(), getRadius(), getPaint()
		);
	}
}

