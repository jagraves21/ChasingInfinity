package renderer.viewer;

import java.util.Objects;
import java.awt.geom.AffineTransform;

public class DefaultWorldViewer implements WorldViewer, Comparable<DefaultWorldViewer> {
	protected int screenWidth;
	protected int screenHeight;
	protected double viewCenterX;
	protected double viewCenterY;
	protected double zoom;

	public DefaultWorldViewer(int screenWidth, int screenHeight, double viewCenterX, double viewCenterY, double zoom) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.viewCenterX = viewCenterX;
		this.viewCenterY = viewCenterY;
		this.zoom = zoom;
	}

	public DefaultWorldViewer(WorldViewer other) {
		this(
			other.getScreenWidth(),
			other.getScreenHeight(),
			other.getViewCenterX(),
			other.getViewCenterY(),
			other.getZoom()
		);
	}

	public int getScreenWidth() { return screenWidth; }
	public int getScreenHeight() { return screenHeight; }
	public double getViewCenterX() { return viewCenterX; }
	public double getViewCenterY() { return viewCenterY; }
	public double getZoom() { return zoom; }

	public void setScreenWidth(int screenWidth) {
		if (screenWidth <= 0) {
			throw new IllegalArgumentException("Screen width must be positive.");
		}
		this.screenWidth = screenWidth;
	}

	public void setScreenHeight(int screenHeight) {
		if (screenHeight <= 0) {
			throw new IllegalArgumentException("Screen height must be positive.");
		}
		this.screenHeight = screenHeight;
	}

	public void setScreenSize(int screenWidth, int screenHeight) {
		if (screenWidth <= 0 || screenHeight <= 0) {
			throw new IllegalArgumentException("Screen dimensions must be positive.");
		}
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	public void setViewCenterX(double viewCenterX) { this.viewCenterX = viewCenterX; }
	public void setViewCenterY(double viewCenterY) { this.viewCenterY = viewCenterY; }

	public void setViewCenter(double viewCenterX, double viewCenterY) {
		this.viewCenterX = viewCenterX;
		this.viewCenterY = viewCenterY;
	}

	public void recenter(int screenX, int screenY) {
		this.viewCenterX = screenToWorldX(screenX);
		this.viewCenterY = screenToWorldY(screenY);
	}

	public void setZoom(double zoom) {
		if (zoom <= 0.0) {
			throw new IllegalArgumentException("Zoom must be positive.");
		}
		this.zoom = zoom;
	}

	protected void zoomByFactor(double worldX, double worldY, double factor) {
		if (factor == 0.0) {
			throw new IllegalArgumentException("Zoom factor cannot be zero.");
		}

		double newZoom = this.zoom * factor;
		this.viewCenterX = worldX - (worldX - viewCenterX) * (this.zoom / newZoom);
		this.viewCenterY = worldY - (worldY - viewCenterY) * (this.zoom / newZoom);
		this.zoom = newZoom;
	}

	public void zoomByFactorAtScreen(int screenX, int screenY, double factor) {
		double worldX = screenToWorldX(screenX);
		double worldY = screenToWorldY(screenY);
		zoomByFactor(worldX, worldY, factor);
	}

	public void zoomByFactorAtWorld(double worldX, double worldY, double factor) {
		zoomByFactor(worldX, worldY, factor);
	}

	public int worldToScreenX(double worldX) {
		return (int) Math.round((worldX - viewCenterX) * zoom);
	}

	public int worldToScreenY(double worldY) {
		return (int) Math.round((worldY - viewCenterY) * zoom);
	}

	public double screenToWorldX(int screenX) {
		return screenX / zoom + viewCenterX;
	}

	public double screenToWorldY(int screenY) {
		return screenY / zoom + viewCenterY;
	}

	public AffineTransform getWorldToScreenTransform() {
		AffineTransform transform = new AffineTransform();
		transform.scale(zoom, zoom);
		transform.translate(-viewCenterX, -viewCenterY);
		return transform;
	}

	public AffineTransform getWorldToScreenTransform(AffineTransform existingTransform) {
		AffineTransform transform = getWorldToScreenTransform();
		existingTransform = new AffineTransform(existingTransform);
		existingTransform.concatenate(transform);
		return existingTransform;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof DefaultWorldViewer)) return false;
		DefaultWorldViewer other = (DefaultWorldViewer) obj;
		return screenWidth == other.screenWidth &&
			screenHeight == other.screenHeight &&
			Double.compare(viewCenterX, other.viewCenterX) == 0 &&
			Double.compare(viewCenterY, other.viewCenterY) == 0 &&
			Double.compare(zoom, other.zoom) == 0;
	}

	public int compareTo(DefaultWorldViewer other) {
		int cmp;
		cmp = Integer.compare(screenWidth, other.getScreenWidth());
		if (cmp != 0) return cmp;

		cmp = Integer.compare(screenHeight, other.getScreenHeight());
		if (cmp != 0) return cmp;

		cmp = Double.compare(viewCenterX, other.getViewCenterX());
		if (cmp != 0) return cmp;

		cmp = Double.compare(viewCenterY, other.getViewCenterY());
		if (cmp != 0) return cmp;

		cmp = Double.compare(zoom, other.getZoom());
		return cmp;
	}

	public int hashCode() {
		return Objects.hash(screenWidth, screenHeight, viewCenterX, viewCenterY, zoom);
	}

	public String toString() {
		return String.format(
			"DefaultWorldViewer[%n" +
			"  screenWidth   %d%n" +
			"  screenHeight  %d%n" +
			"  viewCenterX   %.2f%n" +
			"  viewCenterY   %.2f%n" +
			"  zoom          %.2f%n" +
			"]",
			screenWidth, screenHeight, viewCenterX, viewCenterY, zoom
		);
	}
}

