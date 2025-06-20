package renderer.viewer;

import java.awt.geom.AffineTransform;
import java.util.Objects;

public class CartesianWorldViewer extends AbstractWorldViewer
	implements Comparable<CartesianWorldViewer> {

	protected boolean flipY;
	protected double screenCenterX;
	protected double screenCenterY;

	public CartesianWorldViewer(
		int screenWidth, int screenHeight, double viewCenterX, double viewCenterY, double zoom
	) {
		this(screenWidth, screenHeight, viewCenterX, viewCenterY, zoom, true);
	}

	protected CartesianWorldViewer(
		int screenWidth, int screenHeight, double viewCenterX, double viewCenterY, double zoom, boolean flipY
	) {
		super(screenWidth, screenHeight, viewCenterX, viewCenterY, zoom);
		this.flipY = flipY;
		updateScreenCenter();
	}

	public CartesianWorldViewer(WorldViewer other) {
		this(
			other.getScreenWidth(),
			other.getScreenHeight(),
			other.getViewCenterX(),
			other.getViewCenterY(),
			other.getZoom()
		);
	}

	protected void onScreenSizeChanged() {
		updateScreenCenter();
	}

	protected void updateScreenCenter() {
		this.screenCenterX = screenWidth / 2.0;
		this.screenCenterY = screenHeight / 2.0;
	}

	protected boolean getFlipY() {
		return flipY;
	}

	protected void setFlipY(boolean flipY) {
		this.flipY = flipY;
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

	public int compareTo(CartesianWorldViewer other) {
		int cmp = compareBase(other);
		if (cmp != 0) return cmp;

		cmp = Boolean.compare(flipY, other.flipY);
		return cmp;
	}

	public int hashCode() {
		return Objects.hash(super.hashCode(), flipY);
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof CartesianWorldViewer)) return false;
		if (!super.equals(obj)) return false;
		CartesianWorldViewer other = (CartesianWorldViewer) obj;
		return flipY == other.flipY;
	}

	public String toString() {
		return String.format(
			"CartesianWorldViewer[%n" +
			"  screenWidth   %d%n" +
			"  screenHeight  %d%n" +
			"  viewCenterX   %.2f%n" +
			"  viewCenterY   %.2f%n" +
			"  zoom		  %.2f%n" +
			"  flipY		 %s%n" +
			"]",
			screenWidth, screenHeight, viewCenterX, viewCenterY, zoom, flipY
		);
	}
}

