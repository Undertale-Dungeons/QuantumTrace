package dev.quantumtrace;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class QuantumTraceScreen extends Screen {
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
        int width = Math.min(300, this.width - 28);
        int x = (this.width - width) / 2;
        int y = Math.max(66, this.height / 2 - 65);

        modeButton = addRenderableWidget(Button.builder(modeLabel(), b -> {
            QuantumTraceConfig.cycleMode();
            modeButton.setMessage(modeLabel());
        }).bounds(x, y, width, 22).build());

        themeButton = addRenderableWidget(Button.builder(themeLabel(), b -> {
            QuantumTraceConfig.cycleTheme();
            themeButton.setMessage(themeLabel());
        }).bounds(x, y + 28, width, 22).build());

        presetButton = addRenderableWidget(Button.builder(presetLabel(), b -> {
            QuantumTraceConfig.cyclePreset();
            presetButton.setMessage(presetLabel());
        }).bounds(x, y + 56, width, 22).build());

        peabertButton = addRenderableWidget(Button.builder(peabertLabel(), b -> {
            if (secretUnlocked) {
                QuantumTraceConfig.togglePeabert();
                peabertButton.setMessage(peabertLabel());
            }
        }).bounds(x, y + 84, width, 22).build());
        peabertButton.active = secretUnlocked;

        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(x, y + 122, width, 22).build());
    }

    private Component modeLabel() {
        return Component.literal("VISUAL PROFILE   /   " + QuantumTraceConfig.getMode());
    }
    private Component themeLabel() {
        return Component.literal("INTERFACE THEME   /   " + QuantumTraceConfig.getTheme());
    }
    private Component presetLabel() {
        return Component.literal("QUALITY PRESET   /   " + QuantumTraceConfig.getPreset());
    }
    private Component peabertLabel() {
        return Component.literal(secretUnlocked
                ? "SECRET MASCOT   /   PEABERT " + (QuantumTraceConfig.isPeabertEnabled() ? "ON" : "OFF")
                : "SECRET OPTION   /   LOCKED");
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
        g.fill(0, 0, width, height, 0xB0080D16);
        int left = width / 2 - 174;
        int top = height / 2 - 118;
        g.fill(left, top, left + 348, top + 264, 0xF0141C29);
        g.fill(left, top, left + 348, top + 3, a);
        g.fill(left + 12, top + 12, left + 14, top + 250, a);
        super.extractRenderState(g, mouseX, mouseY, delta);

        String title = "QUANTUMTRACE";
        g.text(font, title, (width - font.width(title)) / 2, top + 15, a, true);
        String sub = "CLIENT VISUAL CONTROL";
        g.text(font, sub, (width - font.width(sub)) / 2, top + 31, 0xFF9EADBF, false);
        String hint = secretUnlocked ? "PEABERT PROTOCOL UNLOCKED" : "CLASSIFIED  //  tap the title 7 times";
        g.text(font, hint, (width - font.width(hint)) / 2, top + 238,
                secretUnlocked ? a : 0xFF65758A, false);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(parent);
    }
}
