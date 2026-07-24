package mushroomified.mcci_orange_text.client.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class ClientCompat {
    public static Screen getScreen(){
        //return Minecraft.getInstance().gui.screen();
        return Minecraft.getInstance().screen;
    }

    public static void setScreen(Screen screen){
        Minecraft.getInstance().setScreen(screen);
    }

}
