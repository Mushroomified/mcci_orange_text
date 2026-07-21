package mushroomified.mcci_orange_text.client;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WordConfig {
    public static List<String> words = new ArrayList<>();


    public static void load() {
        Path path = FabricLoader.getInstance()
                .getConfigDir()
                .resolve("word_list.txt");

        try{
            if (!Files.exists(path)) {
                Files.writeString(path, "This is the default text, change the config file and restart. One word per line.");
            }

            words = Files.readAllLines(path);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
