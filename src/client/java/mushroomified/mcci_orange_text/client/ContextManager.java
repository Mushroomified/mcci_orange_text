package mushroomified.mcci_orange_text.client;

import mushroomified.mcci_orange_text.client.compat.ClientCompat;
import net.minecraft.client.gui.screens.ChatScreen;

public class ContextManager {

    public static KeyBindContext getCurrentContext(){

//        Minecraft client = Minecraft.getInstance();

        if (ClientCompat.getScreen() instanceof ChatScreen){
            return KeyBindContext.CHAT;
        }

        if (ClientCompat.getScreen() != null){
            return KeyBindContext.GUI;
        }

        return KeyBindContext.GAMEPLAY;

    }

}
