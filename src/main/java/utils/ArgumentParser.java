package utils;

import java.util.List;
import java.util.LinkedList;

public class ArgumentParser {
	public String callerName;
	public int width;
	public int height;
	public int iterations;
	public boolean reset;
	public int fps;
	public int ups;
	public boolean reflect;
	public List<String> classes;

	public ArgumentParser(
		String callerName,
		int width,
		int height,
		int iterations,
		boolean reset,
		int fps,
		int ups
	) {
		this(
			callerName,
			width,
			height,
			iterations,
			reset,
			fps,
			ups,
			true
		);
	}
	public ArgumentParser(
		String callerName,
		int width,
		int height,
		int iterations,
		boolean reset,
		int fps,
		int ups,
		boolean reflect
	) {
		this.callerName = callerName;
		this.width = width;
		this.height = height;
		this.iterations = iterations;
		this.reset = reset;
		this.fps = fps;
		this.ups = ups;
		this.reflect = reflect;
		this.classes = new LinkedList<String>();
	}

	public void printHelp(boolean withClasses, boolean withReflect) {
		if (withClasses) {
			System.out.println("Usage: java " + callerName + " [options]");
		} else {
			System.out.println("Usage: java " + callerName + " [options] <class files>");
		}
		System.out.println("Options:");
		System.out.println("  -w, --width <width>        Set the width of the fractal (default: 800)");
		System.out.println("  -h, --height <height>      Set the height of the fractal (default: 600)");
		System.out.println("  -i, --iterations <count>   Set the number of iterations");
		System.out.println("  --no-reset                 Disable animation looping");
		System.out.println("  --fps <fps>                Set the redraw frames per second");
		System.out.println("  --ups <fps>                Set the animation updates per second");
		if (withReflect) {
			System.out.println("  -n, --no-reflect           Prevent the GIF from being reflect (default: reflected)");
		}
		System.out.println("  --help                     Show this help message");
	}

	public void parseArguments(String[] args) {
		this.parseArguments(args, false, false);
	}

	@SuppressWarnings("fallthrough")
	public void parseArguments(String[] args, boolean withClasses, boolean withReflect) {
		for (String arg : args) {
			if (arg.equals("--help")) {
				printHelp(withClasses, withReflect);
				return;
			}
		}

		for (int ii = 0; ii < args.length; ii++) {
			if (withClasses && args[ii].endsWith(".class")) {
				String className = args[ii].substring(0, args[ii].lastIndexOf(".class"));
				classes.add(className);
			} else {
				switch (args[ii]) {
					case "-w":
					case "--width":
						if (ii + 1 < args.length) {
							try {
								width = Integer.parseInt(args[ii + 1]);
								ii++;
							} catch (NumberFormatException e) {
								System.out.println("Invalid width value. Must be an integer.");
								System.out.println();
								printHelp(withClasses, withReflect);
								System.exit(-1);
							}
						} else {
							System.out.println("No value provided for width.");
							System.out.println();
							printHelp(withClasses, withReflect);
							System.exit(-1);
						}
						break;
					case "-h":
					case "--height":
						if (ii + 1 < args.length) {
							try {
								height = Integer.parseInt(args[ii + 1]);
								ii++;
							} catch (NumberFormatException e) {
								System.out.println("Invalid height value. Must be an integer.");
								System.out.println();
								printHelp(withClasses, withReflect);
								System.exit(-1);
							}
						} else {
							System.out.println("No value provided for height.");
							System.out.println();
							printHelp(withClasses, withReflect);
							System.exit(-1);
						}
						break;
					case "-i":
					case "--iterations":
						if (ii + 1 < args.length) {
							try {
								iterations = Integer.parseInt(args[ii + 1]);
								ii++;
							} catch (NumberFormatException e) {
								System.out.println("Invalid iterations value. Must be an integer.");
								System.out.println();
								printHelp(withClasses, withReflect);
								System.exit(-1);
							}
						} else {
							System.out.println("No value provided for iterations.");
							System.out.println();
							printHelp(withClasses, withReflect);
							System.exit(-1);
						}
						break;
					case "--no-reset":
						reset = false;
						break;
					case "--fps":
						if (ii + 1 < args.length) {
							try {
								fps = Integer.parseInt(args[ii + 1]);
								ii++;
							} catch (NumberFormatException e) {
								System.out.println("Invalid fps value. Must be an integer.");
								System.out.println();
								printHelp(withClasses, withReflect);
								System.exit(-1);
							}
						} else {
							System.out.println("No value provided for fps.");
							System.out.println();
							printHelp(withClasses, withReflect);
							System.exit(-1);
						}
						break;
					case "--ups":
						if (ii + 1 < args.length) {
							try {
								ups = Integer.parseInt(args[ii + 1]);
								ii++;
							} catch (NumberFormatException e) {
								System.out.println("Invalid ups value. Must be an integer.");
								System.out.println();
								printHelp(withClasses, withReflect);
								System.exit(-1);
							}
						} else {
							System.out.println("No value provided for ups.");
							System.out.println();
							printHelp(withClasses, withReflect);
							System.exit(-1);
						}
						break;
					case "-n":
					case "--no-reflect":
						if (withReflect) {
							reflect = false;
							break;
						}
					default:
						System.out.println("Unknown argument: " + args[ii]);
						System.out.println();
						printHelp(withClasses, withReflect);
						System.exit(-1);
				}
			}
		}
	}
}
