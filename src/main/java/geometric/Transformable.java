package geometric;

import renderer.Drawable;

public interface Transformable extends Drawable {
	// Angle constants in degrees
	double DEG_0   = 0.0;
	double DEG_30  = 30.0;
	double DEG_45  = 45.0;
	double DEG_60  = 60.0;
	double DEG_90  = 90.0;
	double DEG_120 = 120.0;
	double DEG_135 = 135.0;
	double DEG_150 = 150.0;
	double DEG_180 = 180.0;
	double DEG_270 = 270.0;
	double DEG_360 = 360.0;

	// Angle constants in radians
	double RAD_0   = Math.toRadians(DEG_0);
	double RAD_30  = Math.toRadians(DEG_30);
	double RAD_45  = Math.toRadians(DEG_45);
	double RAD_60  = Math.toRadians(DEG_60);
	double RAD_90  = Math.toRadians(DEG_90);
	double RAD_120 = Math.toRadians(DEG_120);
	double RAD_135 = Math.toRadians(DEG_135);
	double RAD_150 = Math.toRadians(DEG_150);
	double RAD_180 = Math.toRadians(DEG_180);
	double RAD_270 = Math.toRadians(DEG_270);
	double RAD_360 = Math.toRadians(DEG_360);

	// Transform operations
	Transformable translate(double dx, double dy);
	Transformable scale(double factor);
	Transformable rotate(double angleDegrees);
	Transformable rotateAround(Point pivot, double angleDegrees);
	Transformable reflectAcrossXAxis();
	Transformable reflectAcrossYAxis();
	Transformable reflectAcrossLine(double slope);
	Transformable shearX(double factor);
	Transformable shearY(double factor);
	Transformable normalize(double width, double height);
	BoundingBox getBoundingBox();
}
