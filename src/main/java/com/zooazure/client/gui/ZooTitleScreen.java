package com.zooazure.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ZooTitleScreen extends Screen {
    public ZooTitleScreen() {
        super(Text.literal(Branding.NAME));
    }

    @Override
    protected void init() {
        int bw = 170, bh = 20, x = width / 2 - bw / 2;
        int y = height / 2 - 30;

        addDrawableChild(ButtonWidget.builder(Text.literal("Singleplayer"), b -> client.setScreen(new SelectWorldScreen(this)))
                .dimensions(x, y, bw, bh).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Multiplayer"), b -> client.setScreen(new MultiplayerScreen(this)))
                .dimensions(x, y + 24, bw, bh).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Mods / HUD"), b -> client.setScreen(new HudSettingsScreen(this)))
                .dimensions(x, y + 48, bw, bh).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Store"), b -> ConfirmLinkScreen.open(this, Branding.STORE_URL))
                .dimensions(x, y + 72, bw, bh).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Quit Game").formatted(Formatting.RED), b -> client.scheduleStop())
                .dimensions(x, y + 104, bw, bh).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Join our Discord"), b -> ConfirmLinkScreen.open(this, Branding.DISCORD_URL))
                .dimensions(width - 120, height - 30, 110, 20).build());
    }

    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fillGradient(0, 0, width, height, 0xFF0D1426, 0xFF020306);
        int logoY = height / 2 - 100;
        Branding.drawFlame(ctx, width / 2 - 24, logoY, 4);
        String t = Branding.NAME;
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal(t).formatted(Formatting.BOLD, Formatting.AQUA), width / 2, logoY + 62, 0xFFFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal(Branding.NAME + " " + Branding.VERSION + " | Made for Minecraft"),
                width / 2, height - 14, 0xFF667799);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
