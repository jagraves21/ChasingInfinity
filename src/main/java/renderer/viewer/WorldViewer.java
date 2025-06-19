package renderer.viewer;

import java.awt.geom.AffineTransform;

public interface WorldViewer {
	int getScreenWidth();
	int getScreenHeight();
	double getViewCenterX();
	double getViewCenterY();
	double getZoom();

	void setScreenWidth(int screenWidth);
	void setScreenHeight(int screenHeight);
	void setScreenSize(int screenWidth, int screenHeight);

	void setViewCenterX(double viewCenterX);
	void setViewCenterY(double viewCenterY);
	void setViewCenter(double viewCenterX, double viewCenterY);

	void recenter(int screenX, int screenY);

	void setZoom(double zoom);

	void zoomByFactorAtScreen(int screenX, int screenY, double factor);
	void zoomByFactorAtWorld(double worldX, double worldY, double factor);

	int worldToScreenX(double worldX);
	int worldToScreenY(double worldY);

	double screenToWorldX(int screenX);
	double screenToWorldY(int screenY);

	AffineTransform getWorldToScreenTransform();
	AffineTransform getWorldToScreenTransform(AffineTransform existingTransform);
}

