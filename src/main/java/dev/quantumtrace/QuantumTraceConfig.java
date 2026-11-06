package dev.quantumtrace;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class QuantumTraceConfig {
    public static final String[] PRESETS = {"Potato", "Low", "Medium", "High", "Ultra"};

    private static final Properties PROPERTIES = new Properties();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("quantumtrace.properties");

    private static String mode = "Vanilla Remade";
    private static int presetIndex = 2;
    private static boolean rtxStyleRequested = false;

    private QuantumTraceConfig() {}

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
                PROPERTIES.load(input);
                mode = PROPERTIES.getProperty("mode", mode);
                presetIndex = parsePreset(PROPERTIES.getProperty("preset", "Medium"));
                rtxStyleRequested = Boolean.parseBoolean(
                        PROPERTIES.getProperty("rtxStyleRequested", "false")
                );
            } catch (IOException exception) {
                System.err.println("[QuantumTrace] Could not read config: " + exception.getMessage());
            }
        }
        save();
    }

    public static String getMode() {
        return mode;
    }

    public static String getPreset() {
        return PRESETS[presetIndex];
    }

    public static boolean isRtxStyleRequested() {
        return rtxStyleRequested;
    }

    public static void cycleMode() {
        mode = mode.equals("Vanilla Remade") ? "Realism" : "Vanilla Remade";
        save();
    }

    public static void cyclePreset() {
        presetIndex = (presetIndex + 1) % PRESETS.length;
        save();
    }

    public static void toggleRtxStyleRequest() {
        rtxStyleRequested = !rtxStyleRequested;
        save();
    }

    private static int parsePreset(String value) {
        for (int i = 0; i < PRESETS.length; i++) {
            if (PRESETS[i].equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 2;
    }

    private static void save() {
        PROPERTIES.setProperty("mode", mode);
        PROPERTIES.setProperty("preset", getPreset());
        PROPERTIES.setProperty("rtxStyleRequested", Boolean.toString(rtxStyleRequested));

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
                PROPERTIES.store(output, "QuantumTrace client settings");
            }
        } catch (IOException exception) {
            System.err.println("[QuantumTrace] Could not save config: " + exception.getMessage());
        }
    }
}
