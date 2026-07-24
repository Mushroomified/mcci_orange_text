package mushroomified.mcci_orange_text.client;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WordConfig {
    public static List<String> words = new ArrayList<>();


    public static void load() {

        try{
            if (!Files.exists(ConfigPaths.WORDS)) {
                Files.writeString(ConfigPaths.WORDS, "This is the default text, change the config file and restart. One word per line.");
            }

            words = Files.readAllLines(ConfigPaths.WORDS);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(){
        try{
            Files.write(ConfigPaths.WORDS,words);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
