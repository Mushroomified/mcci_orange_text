package mushroomified.mcci_orange_text.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class OrangeModeManager {

    public static void toggle(boolean showMessage){
        ConfigManager.CONFIG.isOrangeActive = !ConfigManager.CONFIG.isOrangeActive;
        ConfigManager.save();

        if(showMessage){
            showStatus();
        }
    }

    public static void setStatus(boolean status, boolean showMessage){
        ConfigManager.CONFIG.isOrangeActive = status;

        if(showMessage){
            showStatus();
        }
    }

    public static void showStatus() {
        Minecraft.getInstance()
                .gui
                .hud
                .setOverlayMessage(
                        ConfigManager.CONFIG.isOrangeActive
                                ? Component.literal("Orange Mode Activated").withStyle(ChatFormatting.GOLD)
                                : Component.literal("Lame Mode Activated").withStyle(ChatFormatting.GRAY)
                        , true
                );
    }
}
