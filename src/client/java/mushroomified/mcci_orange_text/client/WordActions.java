package mushroomified.mcci_orange_text.client;

import mushroomified.mcci_orange_text.client.compat.ClientCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;

import java.util.concurrent.ThreadLocalRandom;

public class WordActions {
    public static void insertRandomWord(){
        Minecraft client = Minecraft.getInstance() ;

        if(client.player == null)
            return;

        if (WordConfig.words.isEmpty())
            return;

        String word = WordConfig.words.get(
                ThreadLocalRandom.current().nextInt(WordConfig.words.size())
        );

        client.execute(() ->{
            ClientCompat.setScreen(new ChatScreen(word, false));
                }
        );
    }
}
