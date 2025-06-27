package renderer.paint;

import java.awt.Color;
import java.awt.PaintContext;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class TriGradientPaintContext implements PaintContext {
	private final double ax, ay, bx, by, cx, cy;
	private final int[] c1RGBA, c2RGBA, c3RGBA;
	private final double denom;
	private final double v0x, v0y, v1x, v1y;

	// Static cache shared among all contexts
	private static final RasterCache cache = new RasterCache(50);  // adjust size as needed

	public TriGradientPaintContext(
		Point2D p1, Color c1, Point2D p2, Color c2, Point2D p3, Color c3
	) {
		this.ax = p1.getX(); this.ay = p1.getY();
		this.bx = p2.getX(); this.by = p2.getY();
		this.cx = p3.getX(); this.cy = p3.getY();

		this.c1RGBA = new int[] { c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha() };
		this.c2RGBA = new int[] { c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha() };
		this.c3RGBA = new int[] { c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha() };

		// Precompute vectors for barycentric
		this.v0x = bx - cx;
		this.v0y = by - cy;
		this.v1x = ax - cx;
		this.v1y = ay - cy;

		this.denom = (v0y * v1x - v0x * v1y);
	}

	@Override
	public void dispose() {}

	@Override
	public ColorModel getColorModel() {
		return ColorModel.getRGBdefault();
	}

	@Override
	public Raster getRaster(int x, int y, int w, int h) {
		// Create a raster-specific cache key including x,y,w,h
		RasterKey rasterKey = new RasterKey(ax, ay, c1RGBAToColor(), bx, by, c2RGBAToColor(), cx, cy, c3RGBAToColor(), x, y, w, h);

		BufferedImage cachedImage = cache.get(rasterKey);
		if (cachedImage != null) {
			System.out.println("Cache HIT for raster:\n" + rasterKey);
			return cachedImage.getRaster();
		}
		System.out.println("Cache MISS for raster:\n" + rasterKey);

		WritableRaster raster = getColorModel().createCompatibleWritableRaster(w, h);
		int[] rgba = new int[4];

		for (int j = 0; j < h; j++) {
			double py = y + j;
			for (int i = 0; i < w; i++) {
				double px = x + i;
				interpolateColor(px, py, rgba);
				raster.setPixel(i, j, rgba);
			}
		}

		// Cache the raster as a BufferedImage for convenience
		BufferedImage img = new BufferedImage(getColorModel(), raster, false, null);
		cache.put(rasterKey, img);

		return raster;
	}

	private void interpolateColor(double px, double py, int[] out) {
		double v2x = px - cx;
		double v2y = py - cy;

		double u = (v0y * v2x - v0x * v2y) / denom;
		double v = (v2y * v1x - v2x * v1y) / denom;
		double w = 1.0 - u - v;

		for (int k = 0; k < 4; k++) {
			int val = (int) (u * c1RGBA[k] + v * c2RGBA[k] + w * c3RGBA[k]);
			out[k] = clamp(val);
		}
	}

	private int clamp(int value) {
		return value < 0 ? 0 : (value > 255 ? 255 : value);
	}

	// Helper methods to convert stored int[] RGBA to Color objects for cache keys

	private Color c1RGBAToColor() {
		return new Color(
			c1RGBA[0], c1RGBA[1], c1RGBA[2], c1RGBA[3]
		);
	}

	private Color c2RGBAToColor() {
		return new Color(
			c2RGBA[0], c2RGBA[1], c2RGBA[2], c2RGBA[3]
		);
	}

	private Color c3RGBAToColor() {
		return new Color(
			c3RGBA[0], c3RGBA[1], c3RGBA[2], c3RGBA[3]
		);
	}
}

