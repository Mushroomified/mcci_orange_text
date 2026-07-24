package mushroomified.mcci_orange_text.client;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class OrangeModeOverlay{


    private static final int X_OFFSET = 4;
    private static final int BOTTOM_Y_OFFSET = 26;
    private static final int PADDING = 2;


    public static void register(){
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath("mcci_orange_text","orange_mode_indicator"),
                (guiGraphicsExtractor, deltaTracker) -> render(guiGraphicsExtractor)
        );
    }

    private static void render(GuiGraphicsExtractor guiGraphicsExtractor){
        Minecraft client = Minecraft.getInstance();

        if(ContextManager.getCurrentContext() != KeyBindContext.CHAT){
            return;
        }

        Component indicatorText;
        int color;

        if(ConfigManager.CONFIG.isOrangeActive){
            indicatorText = Component.literal("★ Orange Mode Active").withStyle(ChatFormatting.GOLD);
            color = 0xFFFFA500;
        } else{
            indicatorText = Component.literal(":( lame mode active").withStyle(ChatFormatting.GRAY);
            color = 0xFF888888;
        }

        int screenHeight = client.getWindow().getGuiScaledHeight();
        int textWidth = client.font.width(indicatorText);

        int textX = X_OFFSET;
        int textY = screenHeight - BOTTOM_Y_OFFSET;

        int x1 = textX - PADDING;
        int y1 = textY - PADDING;
        int x2 = textX + textWidth + PADDING;
        int y2 = textY + client.font.lineHeight + PADDING;

        double opacitySetting = client.options.chatOpacity().get();
        double chatBackgroundOpacity = opacitySetting * 0.5;

        int alpha = (int) (chatBackgroundOpacity * 255.0);
        int backgroundColor = (alpha << 24); // ARGB black with dynamic alpha

        guiGraphicsExtractor.fill(x1, y1, x2, y2, backgroundColor);


        guiGraphicsExtractor.text(
                client.font,
                indicatorText,
                textX,
                textY,
                color,
                true
        );
    }



}
