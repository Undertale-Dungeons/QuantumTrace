package dev.quantumtrace;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.platform.InputConstants;

public final class QuantumTraceClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            ResourceLocation.fromNamespaceAndPath("quantumtrace", "main")
    );

    private static final KeyMapping OPEN_MENU = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.quantumtrace.open_menu",
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_O,
                    CATEGORY
            )
    );

    @Override
    public void onInitializeClient() {
        QuantumTraceConfig.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_MENU.consumeClick()) {
                client.gui.setScreen(new QuantumTraceScreen(client.gui.screen()));
            }
        });
    }
}
