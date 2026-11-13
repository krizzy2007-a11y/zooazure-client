package com.zooazure.client.hud;

import com.zooazure.client.config.Feature;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ZooHud implements HudElement {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int GRAY = 0xFFBBBBBB;
    private static final int ACCENT = 0xFF55AAFF;
    private static final String[] DIRS = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    @Override
    public void render(DrawContext ctx, RenderTickCounter tc) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        InputTracker.update(mc); // always poll so CPS stays accurate
        if (mc.options.hudHidden) return;

        TextRenderer tr = mc.textRenderer;
        int x = 5, y = 5;

        if (Feature.FPS.enabled()) { line(ctx, tr, "FPS: ", String.valueOf(mc.getCurrentFps()), x, y); y += 11; }

        if (Feature.PING.enabled()) {
            var nh = mc.getNetworkHandler();
            PlayerListEntry e = nh == null ? null : nh.getPlayerListEntry(mc.player.getUuid());
            line(ctx, tr, "Ping: ", (e == null ? 0 : e.getLatency()) + "ms", x, y); y += 11;
        }

        if (Feature.CPS.enabled()) {
            line(ctx, tr, "CPS: ", InputTracker.left() + " | " + InputTracker.right(), x, y); y += 11;
        }

        if (Feature.COORDS.enabled()) {
            line(ctx, tr, "XYZ: ", String.format("%.1f / %.1f / %.1f",
                    mc.player.getX(), mc.player.getY(), mc.player.getZ()), x, y); y += 11;
        }

        if (Feature.DIRECTION.enabled()) {
            float yaw = MathHelper.wrapDegrees(mc.player.getYaw());
            int idx = Math.round(((yaw + 360f) % 360f) / 45f) % 8;
            line(ctx, tr, "Dir: ", DIRS[idx], x, y); y += 11;
        }

        if (Feature.KEYSTROKES.enabled()) renderKeystrokes(ctx, mc, tr, x, y + 6);
        if (Feature.ARMOR.enabled()) renderArmor(ctx, mc, tr);
    }

    private static void line(DrawContext ctx, TextRenderer tr, String label, String value, int x, int y) {
        ctx.drawTextWithShadow(tr, label, x, y, GRAY);
        ctx.drawTextWithShadow(tr, value, x + tr.getWidth(label), y, WHITE);
    }

    private static void renderKeystrokes(DrawContext ctx, MinecraftClient mc, TextRenderer tr, int x, int y) {
        var o = mc.options;
        key(ctx, tr, "W", x + 22, y, 20, 20, o.forwardKey.isPressed());
        key(ctx, tr, "A", x, y + 22, 20, 20, o.leftKey.isPressed());
        key(ctx, tr, "S", x + 22, y + 22, 20, 20, o.backKey.isPressed());
        key(ctx, tr, "D", x + 44, y + 22, 20, 20, o.rightKey.isPressed());
        key(ctx, tr, "LMB " + InputTracker.left(), x, y + 44, 31, 20, InputTracker.leftDown());
        key(ctx, tr, "RMB " + InputTracker.right(), x + 33, y + 44, 31, 20, InputTracker.rightDown());
        key(ctx, tr, "SPACE", x, y + 66, 64, 14, o.jumpKey.isPressed());
    }

    private static void key(DrawContext ctx, TextRenderer tr, String label, int x, int y, int w, int h, boolean down) {
        ctx.fill(x, y, x + w, y + h, down ? 0xAAFFFFFF : 0x80000000);
        int tw = tr.getWidth(label);
        ctx.drawText(tr, label, x + (w - tw) / 2, y + (h - 8) / 2 + 1, down ? 0xFF000000 : WHITE, false);
    }

    private static void renderArmor(DrawContext ctx, MinecraftClient mc, TextRenderer tr) {
        EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        int x = ctx.getScaledWindowWidth() - 22;
        int y = ctx.getScaledWindowHeight() / 2 - 40;
        for (EquipmentSlot slot : slots) {
            ItemStack stack = mc.player.getEquippedStack(slot);
            if (!stack.isEmpty()) {
                ctx.drawItem(stack, x, y);
                ctx.drawStackOverlay(tr, stack, x, y);
                if (stack.isDamageable()) {
                    String d = String.valueOf(stack.getMaxDamage() - stack.getDamage());
                    ctx.drawTextWithShadow(tr, d, x - 4 - tr.getWidth(d), y + 5, WHITE);
                }
            }
            y += 20;
        }
    }

    public static void renderEffects(DrawContext ctx, RenderTickCounter tc) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!Feature.EFFECTS.enabled() || mc.player == null || mc.options.hudHidden) return;
        TextRenderer tr = mc.textRenderer;
        int y = 5;
        for (StatusEffectInstance e : mc.player.getStatusEffects()) {
            String name = e.getEffectType().value().getName().getString();
            if (e.getAmplifier() > 0) name += " " + (e.getAmplifier() + 1);
            int secs = e.getDuration() / 20;
            String time = e.isInfinite() ? "inf" : String.format("%d:%02d", secs / 60, secs % 60);
            String text = name + "  " + time;
            ctx.drawTextWithShadow(tr, text, ctx.getScaledWindowWidth() - 5 - tr.getWidth(text), y, ACCENT);
            y += 11;
        }
    }
}
