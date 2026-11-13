package com.zooazure.client.hud;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Deque;

/** Counts mouse clicks per second by polling GLFW every rendered frame (no mixins needed). */
public final class InputTracker {
    private static final Deque<Long> LEFT = new ArrayDeque<>();
    private static final Deque<Long> RIGHT = new ArrayDeque<>();
    private static boolean lastLeft, lastRight;

    private InputTracker() {}

    public static void update(MinecraftClient mc) {
        long handle = mc.getWindow().getHandle();
        boolean l = GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean r = GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        long now = System.currentTimeMillis();
        if (l && !lastLeft) LEFT.addLast(now);
        if (r && !lastRight) RIGHT.addLast(now);
        lastLeft = l;
        lastRight = r;
        prune(LEFT, now);
        prune(RIGHT, now);
    }

    private static void prune(Deque<Long> q, long now) {
        while (!q.isEmpty() && now - q.peekFirst() > 1000) q.pollFirst();
    }

    public static int left() { return LEFT.size(); }
    public static int right() { return RIGHT.size(); }
    public static boolean leftDown() { return lastLeft; }
    public static boolean rightDown() { return lastRight; }
}
