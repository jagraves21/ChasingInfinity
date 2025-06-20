package renderer.viewer;

import java.awt.geom.AffineTransform;

public class DefaultWorldViewer extends AbstractWorldViewer implements Comparable<DefaultWorldViewer> {

	public DefaultWorldViewer(
		int screenWidth, int screenHeight, double viewCenterX, double viewCenterY, double zoom
	) {
		super(screenWidth, screenHeight, viewCenterX, viewCenterY, zoom);
	}

	public DefaultWorldViewer(WorldViewer other) {
		super(other);
	}

	public int worldToScreenX(double worldX) {
		return (int) Math.round((worldX - viewCenterX) * zoom + screenWidth / 2.0);
	}

	public int worldToScreenY(double worldY) {
		return (int) Math.round((worldY - viewCenterY) * zoom + screenHeight / 2.0);
	}

	public double screenToWorldX(int screenX) {
		return (screenX - screenWidth / 2.0) / zoom + viewCenterX;
	}

	public double screenToWorldY(int screenY) {
		return (screenY - screenHeight / 2.0) / zoom + viewCenterY;
	}

	public AffineTransform getWorldToScreenTransform() {
		AffineTransform transform = new AffineTransform();
		transform.translate(screenWidth / 2.0, screenHeight / 2.0);
		transform.scale(zoom, zoom);
		transform.translate(-viewCenterX, -viewCenterY);
		return transform;
	}

	public AffineTransform getWorldToScreenTransform(AffineTransform existingTransform) {
		AffineTransform transform = getWorldToScreenTransform();
		AffineTransform copy = new AffineTransform(existingTransform);
		copy.concatenate(transform);
		return copy;
	}

	public int compareTo(DefaultWorldViewer other) {
		int cmp = compareBase(other);
		return cmp;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof DefaultWorldViewer)) return false;
		return super.equals(obj);
	}

	public String toString() {
		return String.format(
			"DefaultWorldViewer[%n" +
			"  screenWidth   %d%n" +
			"  screenHeight  %d%n" +
			"  viewCenterX   %.2f%n" +
			"  viewCenterY   %.2f%n" +
			"  zoom		  %.2f%n" +
			"]",
			screenWidth, screenHeight, viewCenterX, viewCenterY, zoom
		);
	}
}

