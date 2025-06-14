package renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public abstract class AnimationPanel extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	protected static final String memory = "Ran out of memory.\nTry runing with -Xmx1g option.\n";

	protected volatile int fps;
	protected volatile int ups;
	private volatile Thread animationThread;

	public AnimationPanel() {
		this(60);
	}

	public AnimationPanel(int fps) {
		this(800, 600, fps);
	}

	public AnimationPanel(int width, int height) {
		this(width, height, 60);
	}

	public AnimationPanel(int width, int height, int fps) {
		this(width, height, 60, 30);
	}

	public AnimationPanel(int width, int height, int fps, int ups) {
		super();
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.BLACK);
		//setBackground(new Color(0,0,0,50));
		this.fps = fps;
		this.ups = ups;
	}

	public synchronized int getFPS() { return fps; }
	public synchronized int getUPS() { return ups; }

	public synchronized void setFPS(int fps) { this.fps = fps; }
	public synchronized void setUPS(int ups) { this.ups = ups; }

	public void start() {
		if (animationThread == null || !animationThread.isAlive()) {
			animationThread = new Thread(this);
			animationThread.start();
		}
	}

	public void stop() {
		if (animationThread != null) {
			while (animationThread.isAlive()) {
				animationThread.interrupt();
				try {
					animationThread.join(100);
				} catch (InterruptedException ie) {

				}
			}
		}
	}

	public void run() {
		int fps = getFPS();
		int ups = getUPS();
		int fpsDelay = 1000 / fps; // milliseconds between frames
		int upsDelay = 1000 / ups; // milliseconds between updates

		long lastUpdate = System.currentTimeMillis();
		long lastFrame = lastUpdate;

		BufferStrategy bufferStrategy = null;
		while (!Thread.currentThread().isInterrupted()) {
			long now = System.currentTimeMillis();

			if (now - lastUpdate >= upsDelay) {
				update();
				//lastUpdate += upsDelay;
				lastUpdate = now;
			}

			if (now - lastFrame >= fpsDelay) {
				try {
					if (bufferStrategy == null || bufferStrategy.contentsLost()) {
						createBufferStrategy(2);
						bufferStrategy = getBufferStrategy();
					}
					draw(bufferStrategy);
				} catch (IllegalStateException ise) {
					ise.printStackTrace();
					bufferStrategy = null;
				}
				//lastFrame += fpsDelay
				lastFrame = now;
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	protected void draw(BufferStrategy bufferStrategy) {
		boolean success = false;
		if (bufferStrategy == null) return;
		do {
			do {
				Graphics g = bufferStrategy.getDrawGraphics();
				try {
					draw(g, getWidth(), getHeight());
				} catch (OutOfMemoryError e) {
					System.out.println(memory);
					System.exit(-1);
				} catch (Exception e) {
					System.err.println("Drawing failed: " + e.getMessage());
					e.printStackTrace();
				} finally {
					bufferStrategy.show();
					g.dispose();
				}
			} while (bufferStrategy.contentsRestored());
		} while (bufferStrategy.contentsLost());
	}

	protected abstract void update();
	protected abstract void draw(Graphics g, int width, int height);
}
