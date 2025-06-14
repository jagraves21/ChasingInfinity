package geometric;

import java.util.Spliterator;
import java.util.stream.StreamSupport;

public interface CompositeTransformable<T extends CompositeTransformable<T>>
	extends Transformable, Iterable<Transformable> {

	abstract T self();

	default T translate(double dx, double dy) {
		for (Transformable item : this) {
			item.translate(dx, dy);
		}
		return self();
	}

	default T scale(double factor) {
		for (Transformable item : this) {
			item.scale(factor);
		}
		return self();
	}

	default T rotate(double angleDegrees) {
		for (Transformable item : this) {
			item.rotate(angleDegrees);
		}
		return self();
	}

	default T rotateAround(Point pivot, double angleDegrees) {
		for (Transformable item : this) {
			item.rotateAround(pivot, angleDegrees);
		}
		return self();
	}

	default T reflectAcrossXAxis() {
		for (Transformable item : this) {
			item.reflectAcrossXAxis();
		}
		return self();
	}

	default T reflectAcrossYAxis() {
		for (Transformable item : this) {
			item.reflectAcrossYAxis();
		}
		return self();
	}

	default T reflectAcrossLine(double slope) {
		for (Transformable item : this) {
			item.reflectAcrossLine(slope);
		}
		return self();
	}

	default T shearX(double factor) {
		for (Transformable item : this) {
			item.shearX(factor);
		}
		return self();
	}

	default T shearY(double factor) {
		for (Transformable item : this) {
			item.shearY(factor);
		}
		return self();
	}

	default T normalize(double width, double height) {
		for (Transformable item : this) {
			item.normalize(width, height);
		}
		return self();
	}

	/*default BoundingBox getBoundingBox() {
		Spliterator<Transformable> spliterator = this.spliterator();
		// false for sequential stream, true for parallel stream
		return StreamSupport.stream(spliterator, false)
			.map(Transformable::getBoundingBox)
			.reduce((bb1, bb2) -> bb1.include(bb2))
			.orElse(new BoundingBox(0, 0, 0, 0));
	}*/

	default BoundingBox getBoundingBox() {
		BoundingBox boundingBox = null;
		for (Transformable item : this) {
			if (boundingBox == null) {
				boundingBox = item.getBoundingBox();
			} else {
				boundingBox.include(item.getBoundingBox());
			}
		}

		if (boundingBox == null) {
			boundingBox = new BoundingBox(0, 0, 0, 0);
		}

		return boundingBox;
	}
}
