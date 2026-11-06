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
    private Button rtxButton;

    public QuantumTraceScreen(Screen parent) {
        super(Component.literal("QuantumTrace"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = 250;
        int buttonX = (this.width - buttonWidth) / 2;
        int top = this.height / 2 - 76;

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

        rtxButton = addRenderableWidget(Button.builder(rtxLabel(), button -> {
            QuantumTraceConfig.toggleRtxStyleRequest();
            rtxButton.setMessage(rtxLabel());
        }).bounds(buttonX, top + 100, buttonWidth, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Done"), button -> onClose())
                .bounds(buttonX, top + 130, buttonWidth, 20).build());
    }

    private Component modeLabel() {
        return Component.literal("Visual mode: " + QuantumTraceConfig.getMode());
    }

    private Component themeLabel() {
        return Component.literal("UI theme: " + QuantumTraceConfig.getTheme());
    }

    private Component presetLabel() {
        return Component.literal("Quality preset: " + QuantumTraceConfig.getPreset());
    }

    private Component peabertLabel() {
        return Component.literal("Shader mascot: Peabert " + (QuantumTraceConfig.isPeabertEnabled() ? "ON" : "OFF"));
    }

    private Component rtxLabel() {
        String state = QuantumTraceConfig.isRtxStyleRequested() ? "Requested" : "Off";
        return Component.literal("RTX-style effects: " + state + " (not implemented)");
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        String heading = "QUANTUMTRACE";
        graphics.text(this.font, heading, (this.width - this.font.width(heading)) / 2,
                this.height / 2 - 112, 0xFFFFFFFF, true);
        String note = "Choose a look. Peabert is a mascot toggle preview.";
        graphics.text(this.font, note, (this.width - this.font.width(note)) / 2,
                this.height / 2 - 95, 0xFFB8C7D9, true);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }
}
