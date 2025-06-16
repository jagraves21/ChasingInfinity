package gui;

import geometric.AbstractGeometricFractal;
import geometric.GeometricFractalRegistry;

import renderer.AbstractFractal;

import java.util.ArrayList;
import java.util.List;

import java.util.function.Supplier;

import java.awt.Image;

import javax.swing.ImageIcon;

import java.net.URL;

public class GeometricFractalsApp {
	public static final String TITLE = "Geometric Fractals";

	public static final String[] ICON_FILENAMES = {
		"/geometric_256.png",
		"/geometric_128.png",
		"/geometric_64.png",
		"/geometric_48.png",
		"/geometric_32.png",
		"/geometric_16.png"
	};

	protected static List<Image> getIcons() {
		List<Image> icons = new ArrayList<>();
		for (String filename : ICON_FILENAMES) {
			try {
				URL url = GeometricFractalsApp.class.getResource(filename);
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
		List<Supplier<AbstractFractal>> fractalSuppliers = new ArrayList<>();

		GeometricFractalRegistry.loadAllFractals();
		for (Supplier<AbstractGeometricFractal<?>> supplier : GeometricFractalRegistry.getAll().values()) {
			Supplier<AbstractFractal> abstractSupplier = () -> supplier.get();
			fractalSuppliers.add(abstractSupplier);
		}

		List<Image> icons = getIcons();

		FractalViewerApp.startGUI(fractalSuppliers, TITLE, icons);
	}
}

