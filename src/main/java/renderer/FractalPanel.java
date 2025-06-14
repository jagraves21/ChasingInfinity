package renderer;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class FractalPanel extends AnimationPanel {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 600;
	public static final int DEFAULT_FPS = 60;
	public static final int DEFAULT_UPS = 1;

	protected WorldViewer worldViewer;
	protected final List<Drawable> drawables = new CopyOnWriteArrayList<>();

	public FractalPanel() {
		this(DEFAULT_FPS);
	}

	public FractalPanel(int fps) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, fps);
	}

	public FractalPanel(int width, int height) {
		this(width, height, DEFAULT_FPS);
	}

	public FractalPanel(int width, int height, int fps) {
		this(width, height, fps, DEFAULT_UPS);
	}

	public FractalPanel(int width, int height, int fps, int ups) {
		super(width, height, fps, ups);
		worldViewer = new WorldViewer(width, height, 0, 0, 1.0);
		addMouseListeners();
	}

	protected void addMouseListeners() {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				recenter(e.getX(), e.getY());
			}
		});

		addMouseWheelListener(new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				double rotation = e.getPreciseWheelRotation();
				double scaleBase = (rotation < 0) ? 1.01 : 0.99; // Zoom in when scrolling up, zoom out when scrolling down
				double zoomFactor = Math.pow(scaleBase, Math.abs(rotation)) * e.getScrollAmount();
				zoomAtScreen(e.getX(), e.getY(), zoomFactor);
			}
		});
	}

	public void moveViewTo(double x, double y) {
		worldViewer.setViewCenter(x, y);
	}

	public void recenter(int screenX, int screenY) {
		worldViewer.recenter(screenX, screenY);
	}

	public void setZoom(double zoom) {
		worldViewer.setZoom(zoom);
	}

	public void zoomAtScreen(int screenX, int screenY, double zoomFactor) {
		worldViewer.zoomByFactorAtScreen(screenX, screenY, zoomFactor);
	}

	public void zoomAtWorld(double worldX, double worldY, double zoomFactor) {
		worldViewer.zoomByFactorAtWorld(worldX, worldY, zoomFactor);
	}

	public void addDrawable(Drawable d) {
		drawables.add(d);
	}

	public void removeDrawable(Drawable d) {
		drawables.remove(d);
	}

	public void clearDrawables() {
		drawables.clear();
	}

	protected void update() {
		for (Drawable d : drawables) {
			d.update();
		}
	}

	protected void draw(Graphics g, int width, int height) {
		worldViewer.setScreenSize(width, height);
		g.setColor(getBackground());
		g.fillRect(0, 0, width, height);
		g.setColor(Color.GREEN);
		g.drawLine(0, 0, width, height);
		g.drawLine(0, height, width, 0);

		WorldViewer tmpViewer = new WorldViewer(worldViewer);
		for (Drawable d : drawables) {
			if (d.isVisibleOnScreen(tmpViewer)) {
				d.draw(g, tmpViewer);
			}
		}
	}
}
