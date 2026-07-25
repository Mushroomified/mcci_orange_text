package mushroomified.mcci_orange_text.client;



import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class WordConfig {
    public static List<String> DEFAULT_WORD_LIST = List.of(
            "says", "proclaims", "states", "declares", "remarks", "claims",
            "asserts", "maintains", "whispers", "murmurs", "sighs", "cries",
            "believes", "expresses", "observes", "voices", "suggests", "mentions",
            "utters", "announces", "affirms", "adds", "reports", "notes",
            "explains", "comments", "argues", "warns", "confesses", "wonders",
            "screams", "yells", "shouts", "mutters", "admits", "complains"
    );

    public static List<String> words = new ArrayList<>();



    public static void load() {

        try{
            if (!Files.exists(ConfigPaths.WORDS)) {
                Files.write(ConfigPaths.WORDS, DEFAULT_WORD_LIST);
            }

            words = Files.readAllLines(ConfigPaths.WORDS);

        }
        catch (IOException e) {
            OrangeTextMod.LOGGER.error("[mcci_orange_text] Failed to load word list file due to an unexpected error.", e);
        }
    }

    public static void save(){
        try{
            Files.write(ConfigPaths.WORDS,words);
        } catch (IOException e){
            OrangeTextMod.LOGGER.error("[mcci_orange_text] Failed to save word list file due to an unexpected error.", e);
        }
    }
}
