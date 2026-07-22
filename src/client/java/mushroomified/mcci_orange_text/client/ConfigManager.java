package mushroomified.mcci_orange_text.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("mcci_orange_text.json");

    public static ModConfig CONFIG = new ModConfig();

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                CONFIG = GSON.fromJson(Files.readString(PATH), ModConfig.class);
            } else {
                save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Files.writeString(PATH, GSON.toJson(CONFIG));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}