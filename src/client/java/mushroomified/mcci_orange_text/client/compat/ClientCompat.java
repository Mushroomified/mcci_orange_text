package mushroomified.mcci_orange_text.client.compat;

import mushroomified.mcci_orange_text.client.OrangeModeButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClientCompat {
    public static Screen getScreen(){
        //return Minecraft.getInstance().gui.screen();
        return Minecraft.getInstance().screen;
    }

    public static void setScreen(Screen screen){
        Minecraft.getInstance().setScreen(screen);
    }

    public static void showOverlayMessage(final Component string, final boolean animate){
        Minecraft.getInstance().gui.setOverlayMessage(string,animate);
    }

}
