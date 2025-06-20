package renderer.viewer;

import java.util.Objects;

import java.awt.geom.AffineTransform;

public abstract class AbstractWorldViewer implements WorldViewer {
	protected int screenWidth;
	protected int screenHeight;
	protected double viewCenterX;
	protected double viewCenterY;
	protected double zoom;

	protected AbstractWorldViewer(int screenWidth, int screenHeight, double viewCenterX, double viewCenterY, double zoom) {
		setScreenSize(screenWidth, screenHeight);
		this.viewCenterX = viewCenterX;
		this.viewCenterY = viewCenterY;
		setZoom(zoom);
	}

	protected AbstractWorldViewer(WorldViewer other) {
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
		onScreenSizeChanged();
	}

	public void setScreenHeight(int screenHeight) {
		if (screenHeight <= 0) {
			throw new IllegalArgumentException("Screen height must be positive.");
		}
		this.screenHeight = screenHeight;
		onScreenSizeChanged();
	}

	public void setScreenSize(int screenWidth, int screenHeight) {
		if (screenWidth <= 0 || screenHeight <= 0) {
			throw new IllegalArgumentException("Screen dimensions must be positive.");
		}
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		onScreenSizeChanged();
	}

	public void setViewCenterX(double viewCenterX) { this.viewCenterX = viewCenterX; }
	public void setViewCenterY(double viewCenterY) { this.viewCenterY = viewCenterY; }

	public void setViewCenter(double viewCenterX, double viewCenterY) {
		this.viewCenterX = viewCenterX;
		this.viewCenterY = viewCenterY;
	}

	public void setZoom(double zoom) {
		if (zoom <= 0.0) {
			throw new IllegalArgumentException("Zoom must be positive.");
		}
		this.zoom = zoom;
	}

	public void recenter(int screenX, int screenY) {
		this.viewCenterX = screenToWorldX(screenX);
		this.viewCenterY = screenToWorldY(screenY);
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

	public abstract int worldToScreenX(double worldX);
	public abstract int worldToScreenY(double worldY);
	public abstract double screenToWorldX(int screenX);
	public abstract double screenToWorldY(int screenY);
	public abstract AffineTransform getWorldToScreenTransform();
	public abstract AffineTransform getWorldToScreenTransform(AffineTransform existingTransform);

	protected void onScreenSizeChanged() {
		// default: no op
		// supclasses override as needed
	}

	protected int compareBase(AbstractWorldViewer other) {
		int cmp = Integer.compare(screenWidth, other.screenWidth);
		if (cmp != 0) return cmp;

		cmp = Integer.compare(screenHeight, other.screenHeight);
		if (cmp != 0) return cmp;

		cmp = Double.compare(viewCenterX, other.viewCenterX);
		if (cmp != 0) return cmp;

		cmp = Double.compare(viewCenterY, other.viewCenterY);
		if (cmp != 0) return cmp;

		cmp = Double.compare(zoom, other.zoom);
		return cmp;
	}


	public int hashCode() {
		return Objects.hash(screenWidth, screenHeight, viewCenterX, viewCenterY, zoom);
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		AbstractWorldViewer other = (AbstractWorldViewer) obj;
		return screenWidth == other.screenWidth &&
			screenHeight == other.screenHeight &&
			Double.compare(viewCenterX, other.viewCenterX) == 0 &&
			Double.compare(viewCenterY, other.viewCenterY) == 0 &&
			Double.compare(zoom, other.zoom) == 0;
	}
}

