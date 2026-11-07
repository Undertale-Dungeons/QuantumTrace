package dev.quantumtrace;

import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Optional bridge to the bundled Caustica LGPL renderer.
 *
 * Reflection keeps QuantumTrace's own compile classpath independent from the nested
 * mod. The nested Fabric loader still loads Caustica as a normal mod.
 */
public final class CausticaBridge {
    private static final String CONFIG = "dev.comfyfluffy.caustica.CausticaConfig";
    private static final String RT = CONFIG + "$Rt";
    private static final String COMPOSITE = RT + "$Composite";
    private static final String DLSS_RR = RT + "$DlssRr";
    private static final String RT_DEVICE = "dev.comfyfluffy.caustica.rt.RtDeviceBringup";
    private static final String RT_CONTEXT = "dev.comfyfluffy.caustica.rt.RtContext";
    private static final System.Logger LOGGER = System.getLogger("QuantumTrace");

    private CausticaBridge() {}

    public static boolean isAvailable() {
        return FabricLoader.getInstance().isModLoaded("caustica");
    }

    public static boolean isRayTracingEnabled() {
        return readBoolean(RT, "ENABLED", false);
    }

    public static boolean isRayTracingActive() {
        if (!isAvailable() || !readStaticBoolean(RT_DEVICE, "rtRequested")) {
            return false;
        }
        return invokeStatic(RT_CONTEXT, "currentOrNull") != null;
    }

    public static String rayTracingStatus() {
        if (!isAvailable()) return "ENGINE NOT LOADED";
        if (!isRayTracingEnabled()) return "DISABLED";
        if (isRayTracingActive()) return "ACTIVE";
        if (readStaticBoolean(RT_DEVICE, "rtRequested")) return "INITIALIZING";
        return "VULKAN RT DEVICE NOT DETECTED";
    }

    /**
     * Changes Caustica's persisted device-level option. Vulkan features are negotiated
     * when Minecraft creates the graphics device, so enabling RT after launch requires
     * a restart if the device was created without RT support.
     */
    public static boolean setRayTracingEnabled(boolean enabled) {
        return setSetting(RT, "ENABLED", Boolean.valueOf(enabled));
    }

    public static int samplesPerPixel() {
        return readInt(COMPOSITE, "SPP", 1);
    }

    public static int maxBounces() {
        return readInt(COMPOSITE, "MAX_BOUNCES", 4);
    }

    public static boolean setQuality(int samples, int bounces) {
        int safeSamples = Math.clamp(samples, 1, 8);
        int safeBounces = Math.clamp(bounces, 2, 8);
        try {
            Object spp = getSetting(COMPOSITE, "SPP");
            Object bounceSetting = getSetting(COMPOSITE, "MAX_BOUNCES");
            setObject(spp, Integer.valueOf(safeSamples));
            setObject(bounceSetting, Integer.valueOf(safeBounces));
            saveCausticaConfig();
            return true;
        } catch (ReflectiveOperationException | RuntimeException exception) {
            LOGGER.log(System.Logger.Level.WARNING, "Could not update Caustica quality settings", exception);
            return false;
        }
    }

    private static boolean readBoolean(String holder, String field, boolean fallback) {
        Object value = readSettingValue(holder, field);
        return value instanceof Boolean bool ? bool : fallback;
    }

    private static int readInt(String holder, String field, int fallback) {
        Object value = readSettingValue(holder, field);
        return value instanceof Number number ? number.intValue() : fallback;
    }

    private static Object readSettingValue(String holder, String field) {
        try {
            Object setting = getSetting(holder, field);
            return setting.getClass().getMethod("value").invoke(setting);
        } catch (ReflectiveOperationException | RuntimeException exception) {
            return null;
        }
    }

    private static Object getSetting(String holder, String field) throws ReflectiveOperationException {
        if (!isAvailable()) {
            throw new IllegalStateException("Caustica is not loaded");
        }
        ClassLoader loader = CausticaBridge.class.getClassLoader();
        Class<?> holderClass = Class.forName(holder, true, loader);
        Field settingField = holderClass.getField(field);
        return settingField.get(null);
    }

    private static boolean setSetting(String holder, String field, Object value) {
        try {
            setObject(getSetting(holder, field), value);
            saveCausticaConfig();
            return true;
        } catch (ReflectiveOperationException | RuntimeException exception) {
            LOGGER.log(System.Logger.Level.WARNING, "Could not update Caustica setting " + field, exception);
            return false;
        }
    }

    private static void setObject(Object setting, Object value) throws ReflectiveOperationException {
        Method setter = setting.getClass().getMethod("set", value.getClass());
        setter.invoke(setting, value);
    }

    private static void saveCausticaConfig() throws ReflectiveOperationException {
        Class<?> configClass = Class.forName(CONFIG, true, CausticaBridge.class.getClassLoader());
        configClass.getMethod("save").invoke(null);
    }

    private static boolean readStaticBoolean(String className, String methodName) {
        Object value = invokeStatic(className, methodName);
        return value instanceof Boolean bool && bool;
    }

    private static Object invokeStatic(String className, String methodName) {
        try {
            Class<?> type = Class.forName(className, true, CausticaBridge.class.getClassLoader());
            return type.getMethod(methodName).invoke(null);
        } catch (ReflectiveOperationException | RuntimeException exception) {
            return null;
        }
    }
}
