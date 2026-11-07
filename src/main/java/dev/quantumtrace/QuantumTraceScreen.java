package dev.quantumtrace;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;

public final class QuantumTraceScreen extends Screen {
    private static final Identifier PEABERT = Identifier.fromNamespaceAndPath("quantumtrace", "textures/gui/peabert.jpg");
    private final Screen parent;
    private Button modeButton;
    private Button themeButton;
    private Button presetButton;
    private Button peabertButton;
    private int titleClicks;
    private long lastTitleClick;
    private boolean secretUnlocked;

    public QuantumTraceScreen(Screen parent) {
        super(Component.literal("QuantumTrace"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int panelWidth = Math.min(560, this.width - 32);
        int left = (this.width - panelWidth) / 2;
        int buttonWidth = Math.min(340, panelWidth - 48);
        int x = left + 24;
        int y = Math.max(86, this.height / 2 - 70);

        modeButton = addRenderableWidget(Button.builder(modeLabel(), b -> {
            QuantumTraceConfig.cycleMode();
            modeButton.setMessage(modeLabel());
        }).bounds(x, y, buttonWidth, 24).build());

        themeButton = addRenderableWidget(Button.builder(themeLabel(), b -> {
            QuantumTraceConfig.cycleTheme();
            themeButton.setMessage(themeLabel());
        }).bounds(x, y + 32, buttonWidth, 24).build());

        presetButton = addRenderableWidget(Button.builder(presetLabel(), b -> {
            QuantumTraceConfig.cyclePreset();
            presetButton.setMessage(presetLabel());
        }).bounds(x, y + 64, buttonWidth, 24).build());

        peabertButton = addRenderableWidget(Button.builder(peabertLabel(), b -> {
            if (secretUnlocked) {
                QuantumTraceConfig.togglePeabert();
                peabertButton.setMessage(peabertLabel());
            }
        }).bounds(x, y + 96, buttonWidth, 24).build());
        peabertButton.active = secretUnlocked;

        addRenderableWidget(Button.builder(Component.literal("DONE  /  SAVE SETTINGS"), b -> onClose())
                .bounds(x, y + 142, buttonWidth, 24).build());
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
        int titleY = this.height / 2 - 105;
        if (button == 0 && mouseY >= titleY - 8 && mouseY <= titleY + 12
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
        int panelWidth = Math.min(560, width - 32);
        int left = (width - panelWidth) / 2;
        int top = Math.max(18, height / 2 - 126);
        g.fill(0, 0, width, height, 0xC9060A12);
        g.fill(left, top, left + panelWidth, top + 300, 0xF0121B29);
        g.fill(left, top, left + panelWidth, top + 4, a);
        g.fill(left + 16, top + 16, left + 18, top + 284, a);
        g.fill(left + 26, top + 53, left + panelWidth - 20, top + 54, 0xFF354255);
        super.extractRenderState(g, mouseX, mouseY, delta);

        String title = "QUANTUMTRACE";
        g.text(font, title, left + 28, top + 18, a, true);
        g.text(font, "VISUAL CONTROL CENTER  /  CLIENT CONFIGURATION",
                left + 28, top + 38, 0xFF9EADBF, false);
        g.text(font, "PROFILE", left + 28, top + 67, 0xFF71839A, false);
        g.text(font, "Choose a look and performance preset", left + 28, top + 206,
                0xFF9EADBF, false);
        g.text(font, "Settings are saved automatically", left + 28, top + 220,
                0xFF71839A, false);

        if (secretUnlocked) {
            g.fill(left + panelWidth - 145, top + 68, left + panelWidth - 25, top + 246, 0xFF263246);
            if (QuantumTraceConfig.isPeabertEnabled()) {
                g.blit(RenderPipelines.GUI_TEXTURED, PEABERT,
                        left + panelWidth - 137, top + 76, 0, 0, 110, 152, 110, 152);
                g.text(font, "PEABERT", left + panelWidth - 112, top + 234, a, true);
            } else {
                g.text(font, "SECRET FOUND", left + panelWidth - 125, top + 145, a, true);
                g.text(font, "Enable Peabert", left + panelWidth - 123, top + 161, 0xFF9EADBF, false);
            }
        } else {
            String hint = "CLASSIFIED  //  tap the title 7 times";
            g.text(font, hint, left + panelWidth - 190, top + 225, 0xFF65758A, false);
        }
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(parent);
    }
}
