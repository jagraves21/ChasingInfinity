package renderer;

import utils.color.NamedColors;

import java.awt.Color;
import java.awt.Paint;

public class ColorPainter extends Painter {
	protected Color color;

	public ColorPainter() {
		this(NamedColors.WHITE);
	}

	public ColorPainter(Color color) {
		this.color = color;
	}

	public Paint getPaint() {
		return color;
	}
}

