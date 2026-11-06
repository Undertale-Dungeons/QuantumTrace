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

    public QuantumTraceScreen(Screen parent) {
        super(Component.literal("QuantumTrace"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = Math.min(280, this.width - 32);
        int buttonX = (this.width - buttonWidth) / 2;
        int top = Math.max(58, this.height / 2 - 76);

        modeButton = addRenderableWidget(Button.builder(modeLabel(), button -> {
            QuantumTraceConfig.cycleMode();
            modeButton.setMessage(modeLabel());
        }).bounds(buttonX, top, buttonWidth, 20).build());

        themeButton = addRenderableWidget(Button.builder(themeLabel(), button -> {
            QuantumTraceConfig.cycleTheme();
            themeButton.setMessage(themeLabel());
        }).bounds(buttonX, top + 25, buttonWidth, 20).build());

        presetButton = addRenderableWidget(Button.builder(presetLabel(), button -> {
            QuantumTraceConfig.cyclePreset();
            presetButton.setMessage(presetLabel());
        }).bounds(buttonX, top + 50, buttonWidth, 20).build());

        peabertButton = addRenderableWidget(Button.builder(peabertLabel(), button -> {
            QuantumTraceConfig.togglePeabert();
            peabertButton.setMessage(peabertLabel());
        }).bounds(buttonX, top + 75, buttonWidth, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Open Iris Shader Packs"), button -> openIris())
                .bounds(buttonX, top + 105, buttonWidth, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Done"), button -> onClose())
                .bounds(buttonX, top + 135, buttonWidth, 20).build());
    }

    private void openIris() {
        try {
            Class<?> screenClass = Class.forName("net.irisshaders.iris.gui.screen.ShaderPackScreen");
            Object screen = screenClass.getConstructor(Screen.class).newInstance(this);
            this.minecraft.gui.setScreen((Screen) screen);
        } catch (ReflectiveOperationException exception) {
            System.err.println("[QuantumTrace] Iris shader screen unavailable: " + exception.getMessage());
            this.minecraft.gui.setScreen(new InfoScreen(this,
                    "Iris is not installed. Add Iris for real shader packs."));
        }
    }

    private Component modeLabel() {
        return Component.literal("Graphics profile: " + QuantumTraceConfig.getMode());
    }
    private Component themeLabel() {
        return Component.literal("UI theme: " + QuantumTraceConfig.getTheme());
    }
    private Component presetLabel() {
        return Component.literal("Quality: " + QuantumTraceConfig.getPreset());
    }
    private Component peabertLabel() {
        return Component.literal("Peabert mascot: " + (QuantumTraceConfig.isPeabertEnabled() ? "ON" : "OFF"));
    }

    private int accent() {
        return switch (QuantumTraceConfig.getTheme()) {
            case "Metal Gear" -> 0xFF9BAA83;
            case "2D Minecraft" -> 0xFF65B85A;
            case "Lemons Eats Limes" -> 0xFFD4E85B;
            default -> 0xFFFFFFFF;
        };
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int accent = accent();
        graphics.fill(0, 0, this.width, this.height, 0xB010141C);
        graphics.fill(this.width / 2 - 165, this.height / 2 - 112,
                this.width / 2 + 165, this.height / 2 + 170, 0xE018202B);
        graphics.fill(this.width / 2 - 165, this.height / 2 - 112,
                this.width / 2 + 165, this.height / 2 - 109, accent);
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        String heading = "QUANTUMTRACE";
        graphics.text(this.font, heading, (this.width - this.font.width(heading)) / 2,
                this.height / 2 - 100, accent, true);
        String subtitle = "Graphics and interface settings";
        graphics.text(this.font, subtitle, (this.width - this.font.width(subtitle)) / 2,
                this.height / 2 - 84, 0xFFB8C7D9, true);

        if (QuantumTraceConfig.isPeabertEnabled()) {
            String peabert = "PEABERT IS WATCHING";
            graphics.text(this.font, peabert, (this.width - this.font.width(peabert)) / 2,
                    this.height / 2 + 99, accent, true);
        }
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }

    private static final class InfoScreen extends Screen {
        private final Screen parent;
        private final String message;
        private InfoScreen(Screen parent, String message) {
            super(Component.literal("QuantumTrace"));
            this.parent = parent;
            this.message = message;
        }
        @Override protected void init() {
            addRenderableWidget(Button.builder(Component.literal("Back"), button ->
                    this.minecraft.gui.setScreen(parent))
                    .bounds(this.width / 2 - 100, this.height / 2 + 20, 200, 20).build());
        }
        @Override public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
            super.extractRenderState(graphics, mouseX, mouseY, delta);
            graphics.text(this.font, message, (this.width - this.font.width(message)) / 2,
                    this.height / 2, 0xFFFFFFFF, true);
        }
    }
}
