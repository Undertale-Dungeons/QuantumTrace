package dev.quantumtrace;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;

public final class QuantumTraceScreen extends Screen {
    private static final Identifier PEABERT = Identifier.fromNamespaceAndPath("quantumtrace", "textures/gui/peabert.jpg");
    private static final int PANEL_HEIGHT = 416;

    private final Screen parent;
    private Button modeButton;
    private Button themeButton;
    private Button presetButton;
    private Button peabertButton;
    private Button ambientOcclusionButton;
    private Button cutoutLeavesButton;
    private Button transparencyButton;
    private Button entityShadowsButton;
    private Button vignetteButton;
    private Button newGlintButton;
    private Button rayTracingButton;

    private int titleClicks;
    private long lastTitleClick;
    private boolean secretUnlocked;

    public QuantumTraceScreen(Screen parent) {
        super(Component.literal("QuantumTrace"));
        this.parent = parent;
    }

    private int panelTop() {
        return Math.max(12, (this.height - PANEL_HEIGHT) / 2);
    }

    @Override
    protected void init() {
        int panelWidth = Math.min(690, this.width - 32);
        int left = (this.width - panelWidth) / 2;
        int gap = 12;
        int buttonWidth = (panelWidth - 48 - gap) / 2;
        int leftX = left + 24;
        int rightX = leftX + buttonWidth + gap;
        int y = panelTop() + 78;
        int row = 29;

        modeButton = addRenderableWidget(Button.builder(modeLabel(), b -> {
            QuantumTraceConfig.cycleMode();
            modeButton.setMessage(modeLabel());
        }).bounds(leftX, y, buttonWidth, 22).build());

        themeButton = addRenderableWidget(Button.builder(themeLabel(), b -> {
            QuantumTraceConfig.cycleTheme();
            themeButton.setMessage(themeLabel());
        }).bounds(leftX, y + row, buttonWidth, 22).build());

        presetButton = addRenderableWidget(Button.builder(presetLabel(), b -> {
            QuantumTraceConfig.cyclePreset();
            applyQualityPreset();
            presetButton.setMessage(presetLabel());
            refreshGraphicsLabels();
        }).bounds(leftX, y + row * 2, buttonWidth, 22).build());

        peabertButton = addRenderableWidget(Button.builder(peabertLabel(), b -> {
            if (secretUnlocked) {
                QuantumTraceConfig.togglePeabert();
                peabertButton.setMessage(peabertLabel());
            }
        }).bounds(leftX, y + row * 3, buttonWidth, 22).build());
        peabertButton.active = secretUnlocked;

        ambientOcclusionButton = addRenderableWidget(Button.builder(ambientOcclusionLabel(), b -> {
            this.minecraft.options.ambientOcclusion().set(!this.minecraft.options.ambientOcclusion().get());
            saveGameOptions();
            refreshGraphicsLabels();
        }).bounds(rightX, y, buttonWidth, 22).build());

        cutoutLeavesButton = addRenderableWidget(Button.builder(cutoutLeavesLabel(), b -> {
            this.minecraft.options.cutoutLeaves().set(!this.minecraft.options.cutoutLeaves().get());
            saveGameOptions();
            refreshGraphicsLabels();
        }).bounds(rightX, y + row, buttonWidth, 22).build());

        transparencyButton = addRenderableWidget(Button.builder(transparencyLabel(), b -> {
            this.minecraft.options.improvedTransparency().set(!this.minecraft.options.improvedTransparency().get());
            saveGameOptions();
            refreshGraphicsLabels();
        }).bounds(rightX, y + row * 2, buttonWidth, 22).build());

        entityShadowsButton = addRenderableWidget(Button.builder(entityShadowsLabel(), b -> {
            this.minecraft.options.entityShadows().set(!this.minecraft.options.entityShadows().get());
            saveGameOptions();
            refreshGraphicsLabels();
        }).bounds(rightX, y + row * 3, buttonWidth, 22).build());

        vignetteButton = addRenderableWidget(Button.builder(vignetteLabel(), b -> {
            this.minecraft.options.vignette().set(!this.minecraft.options.vignette().get());
            saveGameOptions();
            refreshGraphicsLabels();
        }).bounds(rightX, y + row * 4, buttonWidth, 22).build());

        newGlintButton = addRenderableWidget(Button.builder(
                Component.literal("NEW GLINT     RENDERER IN PROGRESS"), b -> {}
        ).bounds(rightX, y + row * 5, buttonWidth, 22).build());
        newGlintButton.active = false;

        rayTracingButton = addRenderableWidget(Button.builder(
                Component.literal("HARDWARE RAY TRACING     BACKEND NOT READY"), b -> {}
        ).bounds(rightX, y + row * 6, buttonWidth, 22).build());
        rayTracingButton.active = false;

        addRenderableWidget(Button.builder(Component.literal("APPLY VANILLA MAXXING"), b -> applyVanillaMaxxing())
                .bounds(rightX, panelTop() + 294, buttonWidth, 22).build());

        addRenderableWidget(Button.builder(Component.literal("DONE  /  SAVE SETTINGS"), b -> onClose())
                .bounds(leftX, panelTop() + 368, panelWidth - 48, 24).build());

        refreshGraphicsLabels();
    }

    private void saveGameOptions() {
        this.minecraft.options.save();
    }

    private void applyQualityPreset() {
        switch (QuantumTraceConfig.getPreset()) {
            case "Potato" -> setVanillaGraphics(false, false, false, false, false, 0);
            case "Low" -> setVanillaGraphics(false, true, false, false, false, 1);
            case "Medium" -> setVanillaGraphics(true, true, true, true, false, 2);
            case "High" -> setVanillaGraphics(true, true, true, true, true, 4);
            default -> setVanillaGraphics(true, true, true, true, true, 7);
        }
    }

    private void applyVanillaMaxxing() {
        setVanillaGraphics(true, true, true, true, true, 7);
        refreshGraphicsLabels();
    }

    private void setVanillaGraphics(boolean ambientOcclusion, boolean cutoutLeaves,
                                    boolean improvedTransparency, boolean entityShadows,
                                    boolean vignette, int biomeBlend) {
        this.minecraft.options.ambientOcclusion().set(ambientOcclusion);
        this.minecraft.options.cutoutLeaves().set(cutoutLeaves);
        this.minecraft.options.improvedTransparency().set(improvedTransparency);
        this.minecraft.options.entityShadows().set(entityShadows);
        this.minecraft.options.vignette().set(vignette);
        this.minecraft.options.biomeBlendRadius().set(biomeBlend);
        saveGameOptions();
    }

    private void refreshGraphicsLabels() {
        if (ambientOcclusionButton != null) ambientOcclusionButton.setMessage(ambientOcclusionLabel());
        if (cutoutLeavesButton != null) cutoutLeavesButton.setMessage(cutoutLeavesLabel());
        if (transparencyButton != null) transparencyButton.setMessage(transparencyLabel());
        if (entityShadowsButton != null) entityShadowsButton.setMessage(entityShadowsLabel());
        if (vignetteButton != null) vignetteButton.setMessage(vignetteLabel());
    }

    private static String state(boolean enabled) {
        return enabled ? "ON" : "OFF";
    }

    private Component modeLabel() {
        return Component.literal("VISUAL PROFILE     " + QuantumTraceConfig.getMode());
    }

    private Component themeLabel() {
        return Component.literal("INTERFACE THEME     " + QuantumTraceConfig.getTheme());
    }

    private Component presetLabel() {
        return Component.literal("QUALITY PRESET     " + QuantumTraceConfig.getPreset());
    }

    private Component peabertLabel() {
        return Component.literal(secretUnlocked
                ? "SECRET MASCOT     PEABERT " + (QuantumTraceConfig.isPeabertEnabled() ? "ON" : "OFF")
                : "CLASSIFIED FEATURE     LOCKED");
    }

    private Component ambientOcclusionLabel() {
        return Component.literal("AMBIENT OCCLUSION     " + state(this.minecraft.options.ambientOcclusion().get()));
    }

    private Component cutoutLeavesLabel() {
        return Component.literal("CUTOUT LEAVES     " + state(this.minecraft.options.cutoutLeaves().get()));
    }

    private Component transparencyLabel() {
        return Component.literal("IMPROVED TRANSPARENCY     " + state(this.minecraft.options.improvedTransparency().get()));
    }

    private Component entityShadowsLabel() {
        return Component.literal("ENTITY SHADOWS     " + state(this.minecraft.options.entityShadows().get()));
    }

    private Component vignetteLabel() {
        return Component.literal("VIGNETTE     " + state(this.minecraft.options.vignette().get()));
    }

    private int accent() {
        return switch (QuantumTraceConfig.getTheme()) {
            case "Metal Gear" -> 0xFFB4C49A;
            case "2D Minecraft" -> 0xFF65C466;
            case "Lemons Eats Limes" -> 0xFFE0E96A;
            default -> 0xFF79BFFF;
        };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int titleY = panelTop() + 14;
        if (button == 0 && mouseY >= titleY && mouseY <= titleY + 22
                && mouseX >= this.width / 2 - 110 && mouseX <= this.width / 2 + 110) {
            long now = System.currentTimeMillis();
            titleClicks = now - lastTitleClick > 2200 ? 1 : titleClicks + 1;
            lastTitleClick = now;
            if (titleClicks >= 7 && !secretUnlocked) {
                secretUnlocked = true;
                peabertButton.active = true;
                peabertButton.setMessage(peabertLabel());
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        int a = accent();
        int panelWidth = Math.min(690, width - 32);
        int left = (width - panelWidth) / 2;
        int rightX = left + 24 + (panelWidth - 48 - 12) / 2 + 12;
        int top = panelTop();

        g.fill(0, 0, width, height, 0xC9060A12);
        g.fill(left, top, left + panelWidth, top + PANEL_HEIGHT, 0xF0121B29);
        g.fill(left, top, left + panelWidth, top + 4, a);
        g.fill(left + 16, top + 16, left + 18, top + PANEL_HEIGHT - 16, a);
        g.fill(left + 26, top + 53, left + panelWidth - 20, top + 54, 0xFF354255);

        super.extractRenderState(g, mouseX, mouseY, delta);

        g.text(font, "QUANTUMTRACE", left + 28, top + 18, a, true);
        g.text(font, "VISUAL CONTROL CENTER  /  CLIENT CONFIGURATION",
                left + 28, top + 38, 0xFF9EADBF, false);
        g.text(font, "PROFILES", left + 28, top + 63, 0xFF71839A, false);
        g.text(font, "VANILLA GRAPHICS", rightX + 2, top + 63, 0xFF71839A, false);

        g.text(font, "RTX / RAY TRACING", left + 28, top + 221, a, true);
        g.text(font, "STATUS     NOT AVAILABLE IN THIS BUILD",
                left + 28, top + 239, 0xFFD6B47A, false);
        g.text(font, "Needs a Vulkan hardware RT backend,",
                left + 28, top + 255, 0xFF9EADBF, false);
        g.text(font, "device capability checks and a scene tracer.",
                left + 28, top + 269, 0xFF9EADBF, false);
        g.text(font, "No ray-traced effects are being rendered yet.",
                left + 28, top + 285, 0xFF71839A, false);

        if (secretUnlocked) {
            g.text(font, "SECRET UNLOCKED", left + 28, top + 314, a, true);
            if (QuantumTraceConfig.isPeabertEnabled()) {
                g.blit(RenderPipelines.GUI_TEXTURED, PEABERT,
                        left + 160, top + 300, 0, 0, 46, 64, 110, 152);
                g.text(font, "PEABERT", left + 214, top + 326, a, true);
            } else {
                g.text(font, "Peabert is ready to enable", left + 28, top + 330, 0xFF9EADBF, false);
            }
        } else {
            g.text(font, "CLASSIFIED  //  tap the title 7 times", left + 28, top + 318,
                    0xFF65758A, false);
        }
    }

    @Override
    public void onClose() {
        this.minecraft.options.save();
        this.minecraft.gui.setScreen(parent);
    }
}
