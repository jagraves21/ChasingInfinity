package gui;

import lsystem.AbstractLSystemFractal;
import lsystem.LSystemFractalRegistry;

import renderer.AbstractFractal;

import java.util.ArrayList;
import java.util.List;

import java.util.function.Supplier;

import java.awt.Image;

import javax.swing.ImageIcon;

import java.net.URL;

public class LSystemFractalsApp {
	public static final String TITLE = "L-System Fractals";

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
				URL url = LSystemFractalsApp.class.getResource(filename);
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

		LSystemFractalRegistry.loadAllFractals();
		for (Supplier<AbstractLSystemFractal> supplier : LSystemFractalRegistry.getAll().values()) {
			Supplier<AbstractFractal> abstractSupplier = () -> supplier.get();
			fractalSuppliers.add(abstractSupplier);
		}

		List<Image> icons = getIcons();

		FractalViewerApp.startGUI(fractalSuppliers, TITLE, icons);
	}
}

