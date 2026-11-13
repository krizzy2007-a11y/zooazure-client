package com.zooazure.client.gui;

import net.minecraft.client.gui.DrawContext;

public final class Branding {
    public static final String NAME = "ZooAzure Client";
    public static final String VERSION = "v1.0.0";
    public static final String DISCORD_URL = "https://discord.gg/your-invite"; // TODO: replace
    public static final String STORE_URL = "https://example.com/store";        // TODO: replace

    private static final String[] FLAME = {
            "......#.....",
            ".....##.....",
            ".....###....",
            "....####..#.",
            "...#####.##.",
            "..###oo####.",
            "..##ooo####.",
            ".###oooo###.",
            ".###ooooo##.",
            ".###ooooo##.",
            "..##ooooo#..",
            "..###ooo##..",
            "...#######..",
            "....#####..."
    };

    private Branding() {}

    /** Draws a small pixel-art flame logo. Replace with a texture if you have one. */
    public static void drawFlame(DrawContext ctx, int x, int y, int scale) {
        for (int row = 0; row < FLAME.length; row++) {
            for (int col = 0; col < FLAME[row].length(); col++) {
                char c = FLAME[row].charAt(col);
                if (c == '.') continue;
                int color = c == 'o' ? 0xFF9FE8FF : 0xFF2E7BFF;
                ctx.fill(x + col * scale, y + row * scale, x + (col + 1) * scale, y + (row + 1) * scale, color);
            }
        }
    }
}
