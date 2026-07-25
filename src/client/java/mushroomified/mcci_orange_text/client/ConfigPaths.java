package mushroomified.mcci_orange_text.client;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigPaths {

    public static final Path FOLDER =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("mcci_orange_text");

    public static final Path WORDS =
            FOLDER.resolve("word_list.txt");

    public static final Path CONFIG =
            FOLDER.resolve("config.json");


    public static void init() {
        try {
            Files.createDirectories(FOLDER);
        } catch (IOException e) {
            OrangeTextMod.LOGGER.error("[mcci_orange_text] Failed to create config directory due to an unexpected error.", e);
        }
    }
}