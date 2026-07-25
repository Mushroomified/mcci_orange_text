package mushroomified.mcci_orange_text.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.nio.file.Files;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static ModConfig CONFIG = new ModConfig();

    public static void load() {
        try {
            if (Files.exists(ConfigPaths.CONFIG)) {
                try {
                    CONFIG = GSON.fromJson(Files.readString(ConfigPaths.CONFIG), ModConfig.class);
                } catch (JsonSyntaxException e) {
                    OrangeTextMod.LOGGER.error("[mcci_orange_text] CRITICAL CONFIG ERROR: Your configuration file is corrupted at: {}", ConfigPaths.CONFIG);
                    throw new RuntimeException("Orange Text config file is corrupt! Please delete it at: " + ConfigPaths.CONFIG + " and a new one will be generated for you with defaults", e);
                }
            } else {
                save();
            }
        } catch (IOException e) {
            OrangeTextMod.LOGGER.error("[mcci_orange_text] Failed to load config file due to an unexpected error.", e);
        }
    }

    public static void save() {
        try {
            Files.writeString(ConfigPaths.CONFIG, GSON.toJson(CONFIG));
        } catch (IOException e) {
            OrangeTextMod.LOGGER.error("[mcci_orange_text] Failed to save config file due to an unexpected error.", e);
        }
    }
}