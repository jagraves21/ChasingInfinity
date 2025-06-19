package renderer;

import renderer.viewer.WorldViewer;

import java.awt.Paint;

public abstract class Painter {
	public abstract Paint getPaint();

	public Paint getPaint(WorldViewer worldViewer) {
		return getPaint();
	}
}
