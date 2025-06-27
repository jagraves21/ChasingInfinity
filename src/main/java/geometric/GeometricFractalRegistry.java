package geometric;

import java.util.Collections;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.Map;

import java.util.function.Supplier;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.net.URLDecoder;

public class GeometricFractalRegistry {
	private static final String PACKAGE_NAME = "geometric.fractals";

	private static final Map<String, Supplier<AbstractGeometricFractal<?>>> FRACTALS = new TreeMap<>(Collections.reverseOrder());

	public static void register(String name, Supplier<AbstractGeometricFractal<?>> supplier) {
		FRACTALS.put(name, supplier);
	}

	public static AbstractGeometricFractal<?> getFractal(String name) {
		Supplier<AbstractGeometricFractal<?>> supplier = getSupplier(name);
		return supplier.get();
	}

	public static Supplier<AbstractGeometricFractal<?>> getSupplier(String name) {
		Supplier<AbstractGeometricFractal<?>> supplier = FRACTALS.get(name);
		if (supplier == null) {
			throw new IllegalArgumentException("No fractal registered with name: " + name);
		}
		return supplier;
	}

	public static Map<String, Supplier<AbstractGeometricFractal<?>>> getAll() {
		return Collections.unmodifiableMap(FRACTALS);
	}

	public static void loadAllFractals() {
		String packageName = PACKAGE_NAME;
		String packagePath = packageName.replace('.', '/');

		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> resources = classLoader.getResources(packagePath);

			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				String protocol = resource.getProtocol();

				if ("file".equals(protocol)) {
					File dir = new File(URLDecoder.decode(resource.getFile(), "UTF-8"));
					if (dir.exists()) {
						for (File file : dir.listFiles()) {
							if (file.getName().endsWith(".class")) {
								String className = packageName + "." + file.getName().replace(".class", "");
								loadClass(className);
							}
						}
					}
				} else if ("jar".equals(protocol)) {
					String path = resource.getPath();
					String jarPath = path.substring(5, path.indexOf('!'));
					try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
						Enumeration<JarEntry> entries = jar.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							String name = entry.getName();

							if (name.startsWith(packagePath) && name.endsWith(".class") && !entry.isDirectory()) {
								String className = name.replace('/', '.').replace(".class", "");
								loadClass(className);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load fractals from: " + packagePath, e);
		}
	}

	private static void loadClass(String className) {
		try {
			Class<?> rawClass = Class.forName(className);
			if (!AbstractGeometricFractal.class.isAssignableFrom(rawClass)) return;

			@SuppressWarnings("unchecked")
			Class<? extends AbstractGeometricFractal<?>> clazz =
				(Class<? extends AbstractGeometricFractal<?>>) rawClass;

			String simpleName = clazz.getSimpleName();

			Supplier<AbstractGeometricFractal<?>> supplier = () -> {
				try {
					return clazz.getDeclaredConstructor().newInstance();
				} catch (Exception e) {
					throw new RuntimeException("Failed to instantiate fractal: " + className, e);
				}
			};

			register(simpleName, supplier);
			System.out.println("Registered fractal: " + simpleName);

		} catch (ClassNotFoundException e) {
			System.err.println("Could not load class: " + className);
			e.printStackTrace();
		}
	}
}

