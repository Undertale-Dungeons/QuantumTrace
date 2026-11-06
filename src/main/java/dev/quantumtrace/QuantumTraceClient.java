package dev.quantumtrace;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public final class QuantumTraceClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("quantumtrace", "main")
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
