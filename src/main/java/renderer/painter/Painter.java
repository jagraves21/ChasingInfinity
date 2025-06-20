package renderer.painter;

import renderer.viewer.IdentityWorldViewer;
import renderer.viewer.WorldViewer;

import java.awt.Paint;

public abstract class Painter {
	public Paint getPaint() {
		return getPaint(new IdentityWorldViewer());
	}

	public abstract Paint getPaint(WorldViewer worldViewer);
}
