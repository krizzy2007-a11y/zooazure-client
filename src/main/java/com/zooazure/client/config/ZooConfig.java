package com.zooazure.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class ZooConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("zooazure.json");
    private static ZooConfig instance = new ZooConfig();

    public Map<String, Boolean> toggles = new HashMap<>();
    public boolean customTitleScreen = true;

    public static ZooConfig get() { return instance; }

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                try (Reader r = Files.newBufferedReader(FILE)) {
                    ZooConfig c = GSON.fromJson(r, ZooConfig.class);
                    if (c != null) {
                        if (c.toggles == null) c.toggles = new HashMap<>();
                        instance = c;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer w = Files.newBufferedWriter(FILE)) {
                GSON.toJson(instance, w);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
