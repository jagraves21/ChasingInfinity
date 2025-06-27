package utils.color;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.awt.Color;

public class NamedPalettes extends NamedColors {
	public static final Color[] RGB = new Color[]{
		RED,
		GREEN,
		BLUE
	};

	public static final Color[] RAINBOW = new Color[]{
		RED,
		ORANGE,
		YELLOW,
		GREEN,
		BLUE,
		LIGHT_SLATE_BLUE,
		DARK_VIOLET
	};

	public static final Color[] SPECTRUM = new Color[]{
		RED,
		YELLOW,
		GREEN,
		BLUE,
		BLUE_VIOLET,
		LIGHT_SLATE_BLUE,
	};

	public static final Color[] xFOREST = new Color[]{
		DARK_OLIVE_GREEN,
		FOREST_GREEN,
		AO,
		SADDLE_BROWN,
		SIENNA
	};

	public static final Color[] xSEAWEED = new Color[]{
		BRIGHT_CERULEAN,
		SKOBELOFF,
		CRYSTAL_GREEN,
		SWAMP_MOSS,
		HOT_PEPPER_GREEN,
		MYRTLE
	};

	public static final Color[] xOCEAN = new Color[]{
		BLUE,
		AZURE,
		DEEP_SKY_BLUE,
		STEEL_BLUE,
		DARK_SLATE_BLUE
	};

	public static final Color[] xAQUA = new Color[]{
		BLUE,
		CYAN,
		GREEN
	};

	public static final Color[] xSUNSET = new Color[]{
		TOMATO,
		DARK_ORANGE,
		CYBER_YELLOW,
		STRAWBERRY,
		DEEP_SAFFRON
	};

	public static final Color[] xTROPICAL = new Color[]{
		HOT_PINK,
		ORANGE_JUICE,
		CYAN,
		SCHOOL_BUS_YELLOW,
		FOREST_GREEN
	};

	public static final Color[] xNEON = new Color[]{
		GREEN,
		CYAN,
		MAGENTA,
		YELLOW,
		RED
	};

	public static final Color[] xSPACE = new Color[]{
		CINDER,
		DARK_BLUE,
		BLOOD_RED,
		LIGHT_SLATE_BLUE,
		DARK_VIOLET
	};

	public static final Color[] xTWILIGHT = new Color[]{
		LAVENDER_MIST,
		LAVENDER_BLUE,
		CRYSTAL_BLUE,
		BLUE_VIOLET,
		DARK_SLATE_BLUE,
		LIGHT_SLATE_BLUE,
		CINDER,
		LIGHT_SLATE_BLUE,
		DARK_SLATE_BLUE,
		BLUE_VIOLET,
		CRYSTAL_BLUE,
		LAVENDER_BLUE,
		LAVENDER_MIST
	};

	public static final Color[] ICE_FIRE = new Color[]{
		DARK_BLUE,
		BLUE,
		RED,
		BLOOD_RED,
	};

	public static final Color[] xZEBRA = new Color[]{
		BLACK,
		WHITE
	};

	public static final Color[] xCANDY_CANE = new Color[]{
		WHITE,
		RED,
	};

	public static final Color[] xMAGMA = new Color[]{
		CINDER,
		BLACK_BEAN,
		BARN_RED,
		SINOPIA,
		XANTHOUS,
	};

	public static final Color[] xLAVA = new Color[]{
		BLACK,
		RED,
		YELLOW
	};

	public static final Color[] xPINK_BLUE = new Color[]{
		HOT_PINK,
		LIGHT_PINK,
		LIGHT_BLUE,
		LIGHT_SKY_BLUE
	};

	public static final Color[] OIL_SLICK = new Color[]{
		ROSE,
		CYAN,
		SCHOOL_BUS_YELLOW,
		DEEP_PINK,
		MIDNIGHT_BLUE,
		SPRING_GREEN,
		DARK_ORANGE,
		BLUE_VIOLET
	};

	public static final Color[] GAS_ON_WATER = new Color[]{
		MAGENTA,
		CYAN,
		YELLOW,
		ROSE,
		AZURE,
		SPRING_GREEN,
		RED_ORANGE,
		VERONICA
	};

	public static final Color[] BIRTHDAY = new Color[]{
		NamedColors.MOUNTAIN_MEADOW,
		NamedColors.TITANIUM_YELLOW,
		NamedColors.ORANGE_PEEL,
		NamedColors.DARK_ORCHID,
		NamedColors.SPANISH_RED,
		NamedColors.TUFTS_BLUE
	};
	
	public static final Color[] SNOW = new Color[]{
		NamedColors.ALICE_BLUE,
		NamedColors.LAVENDER_MIST,
		NamedColors.CYAN,
	};

	public static final Color[] xTEST = new Color[]{
		new Color(149, 69, 133),
		new Color(47, 63, 111),
		new Color(94, 196, 228),
		new Color(221, 238, 255),
		new Color(255, 185, 97),
		new Color(234, 117, 0),
		new Color(134, 68, 180),
		new Color(94, 196, 228)
	};

	private static final Map<String, Color[]> paletteMap = new HashMap<>();
	static {
		registerPalette("RGB", RGB);
		registerPalette("RAINBOW", RAINBOW);
		registerPalette("SPECTRUM", SPECTRUM);
		registerPalette("xFOREST", xFOREST);
		registerPalette("xSEAWEED", xSEAWEED);
		registerPalette("xOCEAN", xOCEAN);
		registerPalette("xAQUA", xAQUA);
		registerPalette("xSUNSET", xSUNSET);
		registerPalette("xTROPICAL", xTROPICAL);
		registerPalette("xNEON", xNEON);
		registerPalette("xSPACE", xSPACE);
		registerPalette("xTWILIGHT", xTWILIGHT);
		registerPalette("ICE_FIRE", ICE_FIRE);
		registerPalette("xZEBRA", xZEBRA);
		registerPalette("xCANDY_CANE", xCANDY_CANE);
		registerPalette("xMAGMA", xMAGMA);
		registerPalette("xLAVA", xLAVA);
		registerPalette("xPINK_BLUE", xPINK_BLUE);
		registerPalette("OIL_SLICK", OIL_SLICK);
		registerPalette("GAS_ON_WATER", GAS_ON_WATER);
		registerPalette("BIRTHDAY", BIRTHDAY);
		registerPalette("SNOW", SNOW);
		registerPalette("xTEST", xTEST);
	}


	private static void registerPalette(String name, Color[] palette) {
		if (name == null) {
			throw new IllegalArgumentException("Palette name cannot be null");
		} else if (palette == null) {
			throw new IllegalArgumentException("Palette cannot be null");
		}
		name = name.toUpperCase();
		paletteMap.put(name, palette);
	}

	public static boolean isNamedPalette(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Palette name cannot be null");
		}
		return paletteMap.containsKey(name.toUpperCase());
	}

	public static Color[] getPaletteByName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Palette name cannot be null");
		}
		Color[] palette = paletteMap.get(name.toUpperCase());
		if (palette == null) {
			throw new IllegalArgumentException("Unknown palette name: " + name);
		}
		return palette.clone();
	}

	public static Map<String, Color[]> getNameToPaletteMap() {
		return Collections.unmodifiableMap(paletteMap);
	}
}
