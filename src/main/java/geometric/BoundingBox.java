package geometric;

import renderer.Drawable;
import renderer.WorldViewer;

import java.util.Objects;

import java.awt.Color;
import java.awt.Graphics;

public class BoundingBox implements Drawable, Comparable<BoundingBox> {
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;

	public BoundingBox() {
		this(0, 0, 0, 0);
	}

	public BoundingBox(Point p1, Point p2) {
		this(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	public BoundingBox(BoundingBox other) {
		this(other.minX, other.minY, other.maxX, other.maxY);
	}

	public BoundingBox(double minX, double minY, double maxX, double maxY) {
		this.minX = Math.min(minX, maxX);
		this.minY = Math.min(minY, maxY);
		this.maxX = Math.max(minX, maxX);
		this.maxY = Math.max(minY, maxY);
	}
	
	public double getMinX() { return minX; }
	public double getMinY() { return minY; }
	public double getMaxX() { return maxX; }
	public double getMaxY() { return maxY; }

	public double getWidth() { return maxX - minX; }
	public double getHeight() { return maxY - minY; }

	public boolean contains(Point p) {
		return p.getX() >= minX && p.getX() <= maxX &&
			p.getY() >= minY && p.getY() <= maxY;
	}

	public Point getCenter() {
		return new Point((minX + maxX) / 2.0, (minY + maxY) / 2.0);
	}
	
	public BoundingBox include(Point p) {
		include(p.getX(), p.getY());
		return this;
	}

	public BoundingBox include(double x, double y) {
		if (x < minX) minX = x;
		if (x > maxX) maxX = x;
		if (y < minY) minY = y;
		if (y > maxY) maxY = y;
		return this;
	}

	public BoundingBox include(BoundingBox other) {
		include(other.minX, other.minY);
		include(other.maxX, other.maxY);
		return this;
	}

	public boolean intersects(BoundingBox other) {
		return this.maxX > other.minX && this.minX < other.maxX &&
			this.maxY > other.minY && this.minY < other.maxY;
	}

	public boolean isVisibleOnScreen(WorldViewer worldViewer) {
		BoundingBox screenToWorldBoudingBox = new BoundingBox(
			worldViewer.screenToWorldX(0),
			worldViewer.screenToWorldY(0),
			worldViewer.screenToWorldX(worldViewer.getScreenWidth()),
			worldViewer.screenToWorldY(worldViewer.getScreenHeight())
		);
		return this.intersects(screenToWorldBoudingBox);
	}
	
	public void draw(Graphics g) {

	}

	public void draw(Graphics g, WorldViewer worldViewer) {
		Color oldColor = g.getColor();
		g.setColor(Color.GREEN);

		double transformedMinX = worldViewer.worldToScreenX(minX);
		double transformedMinY = worldViewer.worldToScreenX(minY);
		double transformedMaxX = worldViewer.worldToScreenX(maxX);
		double transformedMaxY = worldViewer.worldToScreenX(maxY);

		g.drawRect(
			(int) Math.round(transformedMinX),
			(int) Math.round(transformedMinY),
			(int) Math.round(transformedMaxX - transformedMinX),
			(int) Math.round(transformedMaxY - transformedMinY)
		);

		g.setColor(oldColor);
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof BoundingBox)) return false;
		BoundingBox other = (BoundingBox) obj;
		return Double.compare(minX, other.minX) == 0 &&
			Double.compare(minY, other.minY) == 0 &&
			Double.compare(maxX, other.maxX) == 0 &&
			Double.compare(maxY, other.maxY) == 0;
	}

	public int compareTo(BoundingBox other) {
		int cmp = Double.compare(this.minX, other.minX);
		if (cmp != 0) return cmp;

		cmp = Double.compare(this.minY, other.minY);
		if (cmp != 0) return cmp;

		cmp = Double.compare(this.maxX, other.maxX);
		if (cmp != 0) return cmp;

		return Double.compare(this.maxY, other.maxY);
	}

	public int hashCode() {
		return Objects.hash(minX, minY, maxX, maxY);
	}


	public String toString() {
		return "BoundingBox[min=(" + minX + ", " + minY + 
			"), max=(" + maxX + ", " + maxY + ")]";
	}
}

