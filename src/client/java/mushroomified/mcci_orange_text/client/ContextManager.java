package mushroomified.mcci_orange_text.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;

public class ContextManager {

    public static KeyBindContext getCurrentContext(){

        Minecraft client = Minecraft.getInstance();

        if (client.gui.screen() instanceof ChatScreen){
            return KeyBindContext.CHAT;
        }

        if (client.gui.screen() != null){
            return KeyBindContext.GUI;
        }

        return KeyBindContext.GAMEPLAY;

    }

}
