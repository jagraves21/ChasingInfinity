package lsystem;

import geometric.Circle;
import geometric.LineSegment;
import geometric.Point;

import renderer.viewer.WorldViewer;

import java.awt.Graphics;

/**
 * Turtle Graphics Symbol Table
 *
 * Symbol  | Action                           | Effect on Turtle
 * ------- | -------------------------------- | --------------------------------------------------------------
 * F       | Move forward by line length drawing a line
 * f       | Move forward by line length without drawing a line
 * +       | Turn left by turning angle
 * -       | Turn right by turning angle
 * |       | Reverse direction (i.e., turn by 180 degrees)

 * ~       | Swap the meaning of + and -

 * .       | Draw dot at current location
 * {       | Open a polygon (not yet implemented)
 * }       | Close a polygon and fill it with fill colour (not implemented)

 * [       | Save the current state
 * ]       | Restore last saved state

 * v       | Divide turning angle by current angle scale factor
 * ^       | Multiply turning angle by current angle scale factor
 * (       | Subtract current turning angle shift amount from turning angle
 * )       | Add current turning angle shift amount to turning angle

 * <       | Divide line length by current line length scale factor
 * >       | Multiply line length by current line length scale factor
 * :       | Add current line length shift amount to line length
 * ;       | Subtract current line length shift amount from line length

 * /       | Divide line width by current line width scale factor
 * \       | Multiply line width by current line width scale factor
 * '       | Subtract current line width shift amount from line width
 * "       | Add current line width shift amount to line width

 * #       | Cycle to the next color in the palette

 */

public class TurtleState {
	public static final double DEFAULT_X = 0.0;
	public static final double DEFAULT_Y = 0.0;
	public static final double DEFAULT_ANGLE = 0.0;
	public static final double DEFAULT_LINE_LENGTH = 10.0;
	public static final double DEFAULT_LINE_WIDTH = 1.0;
	public static final double DEFAULT_TURNING_ANGLE = 0.0;
	public static final boolean DEFAULT_SWAP_TURN_DIRS = false;
	public static final double DEFAULT_ANGLE_SCALE = 2.0;
	public static final double DEFAULT_ANGLE_SHIFT = 1.0;
	public static final double DEFAULT_LINE_LENGTH_SCALE = 2.0;
	public static final double DEFAULT_LINE_LENGTH_SHIFT = 1.0;
	public static final double DEFAULT_LINE_WIDTH_SCALE = 2.0;
	public static final double DEFAULT_LINE_WIDTH_SHIFT = 1.0;
	public static final double REVERSE_DIRECTION_ANGLE = 180.0;

	public double x;                      // X position of the drawing turtle
	public double y;                      // Y position of the drawing turtle
	public double angle;                  // Current direction in degrees (0 is pointing to the right)
	public double lineLength;             // Current length of the line to draw
	public double lineWidth;              // Current line width

	public double turningAngle;           // The turning angle used for + and - symbols
	public boolean swapTurnDirs;          // Flag to swap + and - actions

	public double angleScale;             // Multiplicative scaling factor for the angle
	public double angleShift;             // Additive increment for the angle

	public double lineLengthScale;        // Multiplicative scaling factor for the line length
	public double lineLengthShift;        // Additive increment for the line length

	public double lineWidthScale;         // Multiplicative scaling factor for the line width
	public double lineWidthShift;         // Additive increment for the line width

	public TurtleState previousState;

	public Circle circle;
	public LineSegment lineSegment;

	private TurtleState(Builder builder) {
		this.x = builder.x;
		this.y = builder.y;
		this.angle = builder.angle;
		this.lineLength = builder.lineLength;
		this.lineWidth = builder.lineWidth;
		this.turningAngle = builder.turningAngle;
		this.swapTurnDirs = builder.swapTurnDirs;
		this.angleScale = builder.angleScale;
		this.angleShift = builder.angleShift;
		this.lineLengthScale = builder.lineLengthScale;
		this.lineLengthShift = builder.lineLengthShift;
		this.lineWidthScale = builder.lineWidthScale;
		this.lineWidthShift = builder.lineWidthShift;
		this.previousState = null;

		this.lineSegment = new LineSegment(
			new Point(0,0,null), new Point(0,0,null), null
		);
		this.circle = new Circle(0, 0, lineWidth, java.awt.Color.RED, true);
	}

	public static class Builder {
		private double x = DEFAULT_X;
		private double y = DEFAULT_Y;
		private double angle = DEFAULT_ANGLE;
		private double lineLength = DEFAULT_LINE_LENGTH;
		private double lineWidth = DEFAULT_LINE_WIDTH;
		private double turningAngle = DEFAULT_TURNING_ANGLE;
		private boolean swapTurnDirs = DEFAULT_SWAP_TURN_DIRS;
		private double angleScale = DEFAULT_ANGLE_SCALE;
		private double angleShift = DEFAULT_ANGLE_SHIFT;
		private double lineLengthScale = DEFAULT_LINE_LENGTH_SCALE;
		private double lineLengthShift = DEFAULT_LINE_LENGTH_SHIFT;
		private double lineWidthScale = DEFAULT_LINE_WIDTH_SCALE;
		private double lineWidthShift = DEFAULT_LINE_WIDTH_SHIFT;

		public Builder setX(double x) { this.x = x; return this; }
		public Builder setY(double y) { this.y = y; return this; }
		public Builder setAngle(double angle) { this.angle = angle; return this; }
		public Builder setLineLength(double lineLength) { this.lineLength = lineLength; return this; }
		public Builder setLineWidth(double lineWidth) { this.lineWidth = lineWidth; return this; }
		public Builder setTurningAngle(double turningAngle) { this.turningAngle = turningAngle; return this; }
		public Builder setSwapTurnDirs(boolean swapTurnDirs) { this.swapTurnDirs = swapTurnDirs; return this; }
		public Builder setAngleScale(double angleScale) { this.angleScale = angleScale; return this; }
		public Builder setAngleShift(double angleShift) { this.angleShift = angleShift; return this; }
		public Builder setLineLengthScale(double scale) { this.lineLengthScale = scale; return this; }
		public Builder setLineLengthShift(double shift) { this.lineLengthShift = shift; return this; }
		public Builder setLineWidthScale(double scale) { this.lineWidthScale = scale; return this; }
		public Builder setLineWidthShift(double shift) { this.lineWidthShift = shift; return this; }

		public TurtleState build() {
			return new TurtleState(this);
		}
	}

	// Clone constructor for creating a copy of the current state (used in stack push/pop)
	public TurtleState(TurtleState other) {
		this.x = other.x;
		this.y = other.y;
		this.angle = other.angle;
		this.lineLength = other.lineLength;
		this.lineWidth = other.lineWidth;
		this.turningAngle = other.turningAngle;
		this.swapTurnDirs = other.swapTurnDirs;
		this.angleScale = other.angleScale;
		this.angleShift = other.angleShift;
		this.lineLengthScale = other.lineLengthScale;
		this.lineLengthShift = other.lineLengthShift;
		this.lineWidthScale = other.lineWidthScale;
		this.lineWidthShift = other.lineWidthShift;
		this.previousState = other.previousState;
		this.lineSegment = new LineSegment(other.lineSegment);
		this.circle = new Circle(other.circle);
	}

	public void moveForward() {
		x += lineLength * Math.cos(Math.toRadians(angle));
		y += lineLength * Math.sin(Math.toRadians(angle));
	}

	public void moveForwardAndDraw(Graphics g, WorldViewer worldViewer) {
		double prevX = x;
		double prevY = y;
		moveForward();

		lineSegment.getStart().set(prevX, prevY);
		lineSegment.getEnd().set(x, y);
		if( lineSegment.isVisibleOnScreen(worldViewer)) {
			lineSegment.drawWithPaint(g, worldViewer);
		}
	}

	public void rotate(double rotation) {
		angle += rotation;
		angle = (angle + 360.0) % 360.0;
	}

	public void swapDirection() {
		swapTurnDirs = !swapTurnDirs;
	}

	public void drawDot(Graphics g, WorldViewer worldViewer) {
		circle.setCenter(x, y);
		circle.setRadius(lineWidth);
		circle.draw(g, worldViewer);
	}

	protected void push() {
		this.previousState = new TurtleState(this);
	}

	protected void pop() {
		TurtleState prev = this.previousState;
		this.x = prev.x;
		this.y = prev.y;
		this.angle = prev.angle;
		this.lineLength = prev.lineLength;
		this.lineWidth = prev.lineWidth;
		this.turningAngle = prev.turningAngle;
		this.swapTurnDirs = prev.swapTurnDirs;
		this.angleScale = prev.angleScale;
		this.angleShift = prev.angleShift;
		this.lineLengthScale = prev.lineLengthScale;
		this.lineLengthShift = prev.lineLengthShift;
		this.lineWidthScale = prev.lineWidthScale;
		this.lineWidthShift = prev.lineWidthShift;
		this.previousState = prev.previousState;
		this.lineSegment = new LineSegment(prev.lineSegment);
		this.circle = new Circle(prev.circle);
	}

	public void scaleTurningAngle(double factor) {
		turningAngle *= factor;
		turningAngle = (turningAngle % 360.0 + 360.0) % 360.0;
	}

	public void shiftTurningAngle(double amount) {
		turningAngle += amount;
		turningAngle = (turningAngle % 360.0 + 360.0) % 360.0;
	}

	public void scaleLineLength(double factor) {
		lineLength *= factor;
	}

	public void shiftLineLength(double amount) {
		lineLength += amount;
	}

	public void scaleLineWidth(double factor) {
		lineWidth *= factor;
	}

	public void shiftLineWidth(double amount) {
		lineWidth += amount;
	}

	public void updateState(char symbol, Graphics g, WorldViewer worldViewer) {
		switch (symbol) {
			case 'F': // Move forward and draw a line
				moveForwardAndDraw(g, worldViewer);
				break;
			case 'f': // Move forward without drawing
				moveForward();
				break;
			case '+': // Turn left by the turning angle
				rotate(swapTurnDirs ? -turningAngle : turningAngle);
				break;
			case '-': // Turn right by the turning angle
				rotate(swapTurnDirs ? turningAngle : -turningAngle);
				break;
			case '|': // Reverse direction (180 degrees)
				rotate(REVERSE_DIRECTION_ANGLE);
				break;
			case '~': // Swap the meaning of '+' and '-'
				swapDirection();
				break;
			case '.': // Draw a dot with line width radius
				drawDot(g, worldViewer);
				break;
			case '{': // Open a polygon
				throw new UnsupportedOperationException(
					"Support for symbol '{' is not yet implemented."
				);
				//break;
			case '}': // Close a polygon and fill it with fill colour
				throw new UnsupportedOperationException(
					"Support for symbol '}' is not yet implemented."
				);
				//break;
			case '[': // Push current drawing state onto stack
				push();
				break;
			case ']': // Pop current drawing state from the stack
				pop();
				break;
			case 'v': // Divide turning angle by current turning angle scale factor
				scaleTurningAngle(1.0 / angleScale);
				break;
			case '^': // Multiply turning angle by current turning angle scale factor
				scaleTurningAngle(angleScale);
				break;
			case '(': // Subtract current turning angle shift amount from turning angle
				shiftTurningAngle(-angleShift);
				break;
			case ')': // Add current turning angle shift amount to turning angle
				shiftTurningAngle(angleShift);
				break;
			case '<': // Divide line length by current line length scale factor
				scaleLineLength(1.0 / lineLengthScale);
				break;
			case '>': // Multiply line length by current line length scale factor
				scaleLineLength(lineLengthScale);
				break;
			case ':': // Add current line length shift amount to line length
				shiftLineLength(lineLengthShift);
				break;
			case ';': // Subtract current line length shift amount from line length
				shiftLineLength(-lineLengthShift);
				break;
			case '/': // Divide line width by current line width scale factor
				scaleLineWidth(1.0 / lineWidthScale);
				break;
			case '\\': // Multiply line width by current line width scale factor
				scaleLineWidth(lineWidthScale);
				break;
			case '\'': // Subtract current line width shift amount from line width
				shiftLineWidth(-lineWidthShift);
				break;
			case '"': // Add current line width shift amount to line width
				shiftLineWidth(lineWidthShift);
				break;
			case 'S':
			case '#':
				break;
			default:
				//System.out.println("Unknown Symbol: " + symbol);
				// Ignore unknown symbols
				break;
		}
	}

	public String toString() {
		return String.format(
			"TurtleState:%n" +
			"  x=%.2f%n" +
			"  y=%.2f%n" +
			"  angle=%.2f°%n" +
			"  lineLength=%.2f%n" +
			"  lineWidth=%.2f%n" +
			"  turningAngle=%.2f°%n" +
			"  swapTurnDirs=%s%n" +
			"  angleScale=%.2f%n" +
			"  angleShift=%.2f%n" +
			"  lineLengthScale=%.2f%n" +
			"  lineLengthShift=%.2f%n" +
			"  lineWidthScale=%.2f%n" +
			"  lineWidthShift=%.2f",
			x, y, angle, lineLength, lineWidth, turningAngle, swapTurnDirs,
			angleScale, angleShift, lineLengthScale, lineLengthShift,
			lineWidthScale, lineWidthShift
		);
	}

}
