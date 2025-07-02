package gui;

import utils.color.NamedColors;

import geometric.Point;
import geometric.Triangle;

import geometric.AbstractGeometricFractal;
import geometric.GeometricFractalRegistry;

import composit.AbstractCompositFractal;

import renderer.AbstractFractal;

import java.util.ArrayList;
import java.util.List;

import java.util.function.Supplier;

import java.awt.Image;

import javax.swing.ImageIcon;

import java.net.URL;

public class SierpinskisApp {
	public static final String TITLE = "Sierpinski Triangles";

	public static final String[] ICON_FILENAMES = {
		"/icons/geometric_256.png",
		"/icons/geometric_128.png",
		"/icons/geometric_64.png",
		"/icons/geometric_48.png",
		"/icons/geometric_32.png",
		"/icons/geometric_16.png"
	};

	protected static List<Image> getIcons() {
		List<Image> icons = new ArrayList<>();
		for (String filename : ICON_FILENAMES) {
			try {
				URL url = SierpinskisApp.class.getResource(filename);
				if (url != null) {
					ImageIcon icon = new ImageIcon(url);
					icons.add(icon.getImage());
				} else {
					System.err.println("Warning: Icon resource not found: " + filename);
				}
			} catch (Exception e) {
				System.err.println("Error loading icon " + filename + ": " + e.getMessage());
			}
		}
		return icons;
	}

	public static void main(String[] args) {
		GeometricFractalRegistry.loadAllFractals();
		List<Supplier<AbstractFractal>> fractalSuppliers = new ArrayList<>();

		String[] geometricSierpinskis = {
			"BinarySierpinskiTriangle",
			"BinarySierpinskiTriangleFilled",
			"OuterSierpinskiTriangle",
			"SierpinskiArrowhead",
			"SierpinskiCircle1",
			"SierpinskiCircle2",
			"SierpinskiCircle3",
			"SierpinskiCurve",
			"SierpinskiTriangle",
			"TernaryTree",
			"TernaryTree2"
		};
		for (String name :geometricSierpinskis) {
			fractalSuppliers.add(() -> {
				AbstractFractal fractal = GeometricFractalRegistry.getFractal(name);
				AbstractCompositFractal compositFractal = new AbstractCompositFractal() {
					public String toString() {
						return fractal.toString();
					}	
				};
				compositFractal.addDrawable(Triangle.createEquilateralFromTop(
					new Point(0, 250), 500
				));
				compositFractal.addFractal(fractal);
				return compositFractal;
			});
		}

		List<Image> icons = getIcons();

		FractalViewerApp.startGUI(fractalSuppliers, TITLE, icons);
	}
}

