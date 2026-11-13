package com.zooazure.client;

import com.zooazure.client.config.Feature;
import com.zooazure.client.config.ZooConfig;
import com.zooazure.client.gui.HudSettingsScreen;
import com.zooazure.client.gui.ZooTitleScreen;
import com.zooazure.client.hud.ZooHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class ZooAzureClient implements ClientModInitializer {
    public static final String MOD_ID = "zooazure";
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        ZooConfig.load();

        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of(MOD_ID, "main"));
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.zooazure.open_menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, category));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (openMenuKey.wasPressed()) {
                if (mc.currentScreen == null) mc.setScreen(new HudSettingsScreen(null));
            }
            // Swap the vanilla title screen for ours
            if (ZooConfig.get().customTitleScreen
                    && mc.currentScreen != null
                    && mc.currentScreen.getClass() == TitleScreen.class) {
                mc.setScreen(new ZooTitleScreen());
            }
        });

        HudElementRegistry.attachElementAfter(VanillaHudElements.CHAT, Identifier.of(MOD_ID, "hud"), new ZooHud());
        HudElementRegistry.attachElementAfter(VanillaHudElements.CHAT, Identifier.of(MOD_ID, "effects"), ZooHud::renderEffects);

        // Hide vanilla elements when our replacement / toggle says so
        HudElementRegistry.replaceElement(VanillaHudElements.SCOREBOARD, original -> (ctx, tc) -> {
            if (Feature.SCOREBOARD.enabled()) original.render(ctx, tc);
        });
        HudElementRegistry.replaceElement(VanillaHudElements.STATUS_EFFECTS, original -> (ctx, tc) -> {
            if (!Feature.EFFECTS.enabled()) original.render(ctx, tc);
        });
    }
}
