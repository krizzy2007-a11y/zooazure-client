package com.zooazure.client.config;

public enum Feature {
    FPS("FPS Display"),
    KEYSTROKES("Keystrokes"),
    CPS("CPS Counter"),
    PING("Ping Display"),
    ARMOR("Armor HUD"),
    EFFECTS("Potion Effects"),
    COORDS("Coordinates"),
    SCOREBOARD("Scoreboard"),
    DIRECTION("Direction HUD");

    public final String title;

    Feature(String title) { this.title = title; }

    public boolean enabled() {
        return ZooConfig.get().toggles.getOrDefault(name(), true);
    }

    public void toggle() {
        ZooConfig.get().toggles.put(name(), !enabled());
        ZooConfig.save();
    }
}
