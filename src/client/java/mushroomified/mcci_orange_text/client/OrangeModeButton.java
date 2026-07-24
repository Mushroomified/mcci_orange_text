package mushroomified.mcci_orange_text.client;



import mushroomified.mcci_orange_text.client.compat.ClientCompat;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class OrangeModeButton extends AbstractWidget {

    private static final int COLOR_ORANGE = 0xFFFFA500; // ARGB: alpha FF + orange
    private static final int COLOR_DEFAULT = 0xFFFFFFFF; // ARGB: alpha FF + white

    private static final Component TEXT_ORANGE = Component.literal("ᴏʀᴀɴɢᴇ");
    private static final Component TEXT_LAME = Component.literal("ʟᴀᴍᴇ").withStyle(ChatFormatting.GRAY);

    private static final int BACKGROUND_DEFAULT = 0xA0454545;       // lightened from 0x333333
    private static final int BACKGROUND_ORANGE = 0xA04A4038;        // default gray with a faint warm/orange tint

    private static final int BACKGROUND_HOVER_DEFAULT = 0xA05E5E5E; // lightened from 0x4A4A4A
    private static final int BACKGROUND_HOVER_ORANGE = 0xA0665A48;  // faint-orange hover, a bit brighter than BACKGROUND_ORANGE

    private static final int WIDTH = 52;
    private static final int HEIGHT = 9;
    private static final int X = 3;
    //36
    private static final int Y = 28;

    private final int bottomOffset;

    private static final Identifier ICON_TEXTURE_ORANGE =
            Identifier.fromNamespaceAndPath("mcci_orange_text", "textures/gui/orange_icon.png");
    private static final Identifier ICON_TEXTURE_LAME =
            Identifier.fromNamespaceAndPath("mcci_orange_text", "textures/gui/lame_icon.png");



    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        OrangeModeManager.toggle(false);

        Minecraft.getInstance().execute(() -> {
            Screen screen = ClientCompat.getScreen();
            if (screen != null) {
                for (var child : screen.children()) {
                    if (child instanceof EditBox editBox) {
                        screen.setFocused(editBox);
                        editBox.setFocused(true);
                        break;
                    }
                }
            }
        });

    }

    private static final int BAR_WIDTH = 1;

    public OrangeModeButton() {
        // Total width now includes both side bars; core button is inset by BAR_WIDTH on each side.
        super(X - BAR_WIDTH, Y, WIDTH + (BAR_WIDTH * 2), HEIGHT, Component.empty());
        this.bottomOffset = Y;
    }

    private static final int ICON_WIDTH = 10;
    private static final int ICON_HEIGHT = 7;
    private static final int ICON_PADDING = 4; // gap between icon and text

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int actualY = screenHeight - bottomOffset - height;
        this.setY(actualY);

        boolean isOrange = ConfigManager.CONFIG.isOrangeActive;
        boolean hovered = this.isHovered();

        int bgColor;
        if (isOrange) {
            bgColor = hovered ? BACKGROUND_HOVER_ORANGE : BACKGROUND_ORANGE;
        } else {
            bgColor = hovered ? BACKGROUND_HOVER_DEFAULT : BACKGROUND_DEFAULT;
        }

        int coreX = getX() + BAR_WIDTH;
        int coreWidth = width - (BAR_WIDTH * 2);
        graphics.fill(coreX, getY(), coreX + coreWidth, getY() + height, bgColor);

        int barHeight = height - 2;
        int barY = getY() + 1;

        int rightBarX = getX() + width - BAR_WIDTH;
        graphics.fill(rightBarX, barY, rightBarX + BAR_WIDTH, barY + barHeight, bgColor);

        int leftBarX = getX();
        graphics.fill(leftBarX, barY, leftBarX + BAR_WIDTH, barY + barHeight, bgColor);

        Component label = isOrange ? TEXT_ORANGE : TEXT_LAME;
        int textColor = isOrange ? COLOR_ORANGE : COLOR_DEFAULT;
        Identifier iconTexture = isOrange ? ICON_TEXTURE_ORANGE : ICON_TEXTURE_LAME;

        var font = Minecraft.getInstance().font;
        int textWidth = font.width(label);

        // Combined icon + padding + text block, centered together within the core area
        int contentWidth = ICON_WIDTH + ICON_PADDING + textWidth;
        int contentX = coreX + (coreWidth - contentWidth) / 2;

        int iconX = contentX;
        int iconY = getY() + (height - ICON_HEIGHT) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, iconTexture, iconX, iconY, 0, 0,
                ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);

        int textX = contentX + ICON_WIDTH + ICON_PADDING;
        int textY = getY() + (height - 8) / 2;

        graphics.text(font, label, textX, textY, textColor, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (ContextManager.getCurrentContext() == KeyBindContext.CHAT) {
                Screens.getWidgets(screen).add(new OrangeModeButton());

            }
        });
    }
}