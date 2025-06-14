package renderer;

import java.awt.Graphics;

public interface Drawable {
	default void update() {}

	boolean isVisibleOnScreen(WorldViewer worldViewer);

	default void draw(Graphics g) {
		throw new UnsupportedOperationException("draw not implemented");
	}

	void draw(Graphics g, WorldViewer worldViewer);
}
