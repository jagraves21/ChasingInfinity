package renderer.viewer;

import java.util.Objects;

import java.awt.geom.AffineTransform;

public class IdentityWorldViewer extends AbstractWorldViewer {

	public IdentityWorldViewer() {
		super(1, 1, 0, 0, 1);
	}

	public IdentityWorldViewer(WorldViewer other) {
        this();
    }

	public void setScreenWidth(int screenWidth) { }
	public void setScreenHeight(int screenHeight) { }
	public void setScreenSize(int screenWidth, int screenHeight) { }
	public void setViewCenterX(double viewCenterX) { }
	public void setViewCenterY(double viewCenterY) { }
	public void setViewCenter(double viewCenterX, double viewCenterY) { }
	public void setZoom(double zoom) { }
	
	public void recenter(int screenX, int screenY) { }

	protected void zoomByFactor(double worldX, double worldY, double factor) { }
	public void zoomByFactorAtScreen(int screenX, int screenY, double factor) { }
	public void zoomByFactorAtWorld(double worldX, double worldY, double factor) { }

	public int worldToScreenX(double worldX) {
		return (int) Math.round(worldX);
	}

	public int worldToScreenY(double worldY) {
		return (int) Math.round(worldY);
	}

	public double screenToWorldX(int screenX) {
		return screenX;
	}

	public double screenToWorldY(int screenY) {
		return screenY;
	}

	public AffineTransform getWorldToScreenTransform() {
		return new AffineTransform();
	}
	
	public AffineTransform getWorldToScreenTransform(AffineTransform existingTransform) {
		return new AffineTransform(existingTransform);
	}

	protected void onScreenSizeChanged() { }
}

