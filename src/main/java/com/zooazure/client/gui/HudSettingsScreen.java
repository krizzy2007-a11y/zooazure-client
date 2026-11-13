package com.zooazure.client.gui;

import com.zooazure.client.config.Feature;
import com.zooazure.client.config.ZooConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HudSettingsScreen extends Screen {
    private static final int W = 420, H = 214;
    private final Screen parent;
    private int left, top;

    public HudSettingsScreen(Screen parent) {
        super(Text.literal("ZooAzure Settings"));
        this.parent = parent;
    }

    private static Text label(String title, boolean on) {
        return Text.literal(title + "  ")
                .append(Text.literal(on ? "ON" : "OFF").formatted(on ? Formatting.GREEN : Formatting.RED));
    }

    @Override
    protected void init() {
        left = (width - W) / 2;
        top = (height - H) / 2;

        Feature[] features = Feature.values();
        int cw = 128, ch = 26, gap = 6;
        int startX = left + (W - (cw * 3 + gap * 2)) / 2;
        int startY = top + 50;

        for (int i = 0; i < features.length; i++) {
            Feature f = features[i];
            int cx = startX + (i % 3) * (cw + gap);
            int cy = startY + (i / 3) * (ch + gap);
            addDrawableChild(ButtonWidget.builder(label(f.title, f.enabled()), b -> {
                f.toggle();
                b.setMessage(label(f.title, f.enabled()));
            }).dimensions(cx, cy, cw, ch).build());
        }

        int by = startY + 3 * (ch + gap) + 4;
        addDrawableChild(ButtonWidget.builder(label("Custom Title Screen", ZooConfig.get().customTitleScreen), b -> {
            ZooConfig.get().customTitleScreen = !ZooConfig.get().customTitleScreen;
            ZooConfig.save();
            b.setMessage(label("Custom Title Screen", ZooConfig.get().customTitleScreen));
        }).dimensions(startX, by, cw * 2 + gap, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(startX + cw * 2 + gap * 2, by, cw, 20).build());
    }

    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.renderBackground(ctx, mouseX, mouseY, delta);
        ctx.fill(left - 1, top - 1, left + W + 1, top + H + 1, 0xFF2E7BFF);
        ctx.fill(left, top, left + W, top + H, 0xF0101420);
        ctx.fill(left, top + 38, left + W, top + 39, 0xFF1E2A44);
        Branding.drawFlame(ctx, left + 12, top + 8, 2);
        ctx.drawTextWithShadow(textRenderer, Branding.NAME, left + 44, top + 9, 0xFFFFFFFF);
        ctx.drawTextWithShadow(textRenderer, Branding.VERSION, left + 44, top + 21, 0xFF7788AA);
        ctx.drawTextWithShadow(textRenderer, "HUD Settings", left + W - 12 - textRenderer.getWidth("HUD Settings"), top + 15, 0xFF55AAFF);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
