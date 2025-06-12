package renderer;

import java.util.Objects;

import java.awt.geom.AffineTransform;

public class WorldViewer implements Comparable<WorldViewer> {
	protected int screenWidth;
	protected int screenHeight;
	protected double viewCenterX;
	protected double viewCenterY;
	protected double zoom;
	protected boolean flipY;

	protected double screenCenterX;
	protected double screenCenterY;
	
	public WorldViewer(int screenWidth, int screenHeight, double viewCenterX, double viewCenterY, double zoom) {
		this(screenWidth, screenHeight, viewCenterX, viewCenterY, zoom, true);
	}

	public WorldViewer(int screenWidth, int screenHeight, double viewCenterX, double viewCenterY, double zoom, boolean flipY) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.viewCenterX = viewCenterX;
		this.viewCenterY = viewCenterY;
		this.zoom = zoom;
		this.flipY = flipY;
		updateScreenCenter();
	}

	public WorldViewer(WorldViewer worldViewer) {
		this(
			worldViewer.screenWidth,
			worldViewer.screenHeight,
			worldViewer.viewCenterX,
			worldViewer.viewCenterY,
			worldViewer.zoom,
			worldViewer.flipY
		);
	}

	protected void updateScreenCenter() {
		this.screenCenterX = screenWidth / 2.0;
		this.screenCenterY = screenHeight / 2.0;
	}
	
	public int getScreenWidth() { return screenWidth; }
	public int getScreenHeight() { return screenHeight; }
	public double getViewCenterX() { return viewCenterX; }
	public double getViewCenterY() { return viewCenterY; }
	public double getZoom() { return zoom; }
	public boolean getFlipY() { return flipY; }

	public void setScreenWidth(int screenWidth) {
		if (screenWidth <= 0) {
			throw new IllegalArgumentException("Screen width must be positive.");
		}
		this.screenWidth = screenWidth;
		updateScreenCenter();
	}

	public void setScreenHeight(int screenHeight) {
		if (screenHeight <= 0) {
			throw new IllegalArgumentException("Screen height must be positive.");
		}
		this.screenHeight = screenHeight;
		updateScreenCenter();
	}

	public void setScreenSize(int screenWidth, int screenHeight) {
		if (screenWidth <= 0 || screenHeight <= 0) {
			throw new IllegalArgumentException("Screen dimensions must be positive.");
		}
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		updateScreenCenter();
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

	public void setFlipY(boolean flipY) { this.flipY = flipY; }

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
		return (int) Math.round((worldX - viewCenterX) * zoom + screenCenterX);
	}

	public int worldToScreenY(double worldY) {
		return (int) Math.round((worldY - viewCenterY) * (flipY ? -zoom : zoom) + screenCenterY);
	}

	public double screenToWorldX(int screenX) {
		return (screenX - screenCenterX) / zoom + viewCenterX;
	}

	public double screenToWorldY(int screenY) {
		return (screenY - screenCenterY) / (flipY ? -zoom : zoom) + viewCenterY;
	}

	public AffineTransform getWorldToScreenTransform() {
		AffineTransform transform = new AffineTransform();
		transform.translate(screenCenterX, screenCenterY);
		transform.scale(zoom, flipY ? -zoom : zoom);
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
        if (!(obj instanceof WorldViewer)) return false;
		WorldViewer other = (WorldViewer) obj;
		return screenWidth == other.screenWidth &&
			screenHeight == other.screenHeight &&
			Double.compare(viewCenterX, other.viewCenterX) == 0 &&
			Double.compare(viewCenterY, other.viewCenterY) == 0 &&
			Double.compare(zoom, other.zoom) == 0 &&
			flipY == other.flipY;
    }

	public int compareTo(WorldViewer other) {
		int cmp;
		cmp = Integer.compare(screenWidth, other.screenWidth);
		if (cmp != 0) return cmp;

		cmp = Integer.compare(screenHeight, other.screenHeight);
		if (cmp != 0) return cmp;

		cmp = Double.compare(viewCenterX, other.viewCenterX);
		if (cmp != 0) return cmp;

		cmp = Double.compare(viewCenterY, other.viewCenterY);
		if (cmp != 0) return cmp;

		cmp = Double.compare(zoom, other.zoom);
		if (cmp != 0) return cmp;

		return Boolean.compare(flipY, other.flipY);
	}

	public int hashCode() {
		return Objects.hash(screenWidth, screenHeight, viewCenterX, viewCenterY, zoom, flipY);
	}
	
	public String toString() {
		//return String.format("Point{x=%.2f, y=%.2f, paint=%s}", x, y, paint);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("WorldViwer[");
		stringBuilder.append(String.format("  screenWidth   %d\n", screenWidth));
		stringBuilder.append(String.format("  screenHeight  %d\n", screenHeight));
		stringBuilder.append(String.format("  viewCenterX   %.2f\n", viewCenterX));
		stringBuilder.append(String.format("  viewCenterY   %.2f\n", viewCenterY));
		stringBuilder.append(String.format("  zoom          %.2f\n", zoom));
		stringBuilder.append(String.format("  flipY         %b\n", flipY));
		stringBuilder.append(String.format("  screenCenterX %.2f\n", screenCenterX));
		stringBuilder.append(String.format("  screenCenterY %.2f\n", screenCenterY));
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}
}

