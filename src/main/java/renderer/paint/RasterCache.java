package renderer.paint;

import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

public class RasterCache extends LinkedHashMap<RasterKey, BufferedImage> {
	private static final long serialVersionUID = 1L;

	private final int maxSize;

	public RasterCache(int maxSize) {
		super(maxSize, 0.75f, true);  // access order for LRU
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<RasterKey, BufferedImage> eldest) {
		return size() > maxSize;
	}
}

