package mushroomified.mcci_orange_text.client;



import mushroomified.mcci_orange_text.client.chat_channels.ChannelManager;
import mushroomified.mcci_orange_text.client.chat_channels.ChatChannel;
import mushroomified.mcci_orange_text.client.compat.ClientCompat;
import mushroomified.mcci_orange_text.client.mod_activation.ModTurnedOnState;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class OrangeModeButton extends AbstractWidget {

    private static final int COLOR_ORANGE = 0xFFFFA500; // ARGB: alpha FF + orange
    private static final int COLOR_DEFAULT = 0xFFFFFFFF; // ARGB: alpha FF + white
    private static final int COLOR_CMD = 0xFFC090F0; // light purple text
    private static final int COLOR_NOT_LOCAL = 0xFFFF5555;

    private static final Component TEXT_ORANGE = Component.literal("ᴏʀᴀɴɢᴇ");
    private static final Component TEXT_LAME = Component.literal("ʟᴀᴍᴇ").withStyle(ChatFormatting.GRAY);
    private static final Component TEXT_CMD = Component.literal("ᴄᴍᴅ");
    private static final Component TEXT_NOT_LOCAL = Component.literal("ɴᴏ ʟᴏᴄ");

    private static final int BACKGROUND_DEFAULT = 0xA0505050;       // lightened from 0x333333
    private static final int BACKGROUND_ORANGE = 0xA0704838;        // default gray with a faint warm/orange tint
    private static final int BACKGROUND_CMD = 0xA0604878;       // muted purple background
    private static final int BACKGROUND_NOT_LOCAL = 0xA0784040;

    private static final int BACKGROUND_HOVER_DEFAULT = 0xA0686868; // lightened from 0x4A4A4A
    private static final int BACKGROUND_HOVER_ORANGE = 0xA0866050;  // faint-orange hover, a bit brighter than BACKGROUND_ORANGE
    private static final int BACKGROUND_HOVER_CMD = 0xA0604878; // same as non-hover — no interactive affordance since clicks are ignored
    private static final int BACKGROUND_HOVER_NOT_LOCAL = 0xA0784040;

    private static final int WIDTH = 52;
    private static final int HEIGHT = 9;

    private static final int BASE_X = 3;
//28
    private static final int BASE_Y = 17;

    private static int configuredX() {return BASE_X + ConfigManager.CONFIG.buttonChatOffsetX;}
    private static int configuredY() {return BASE_Y + ConfigManager.CONFIG.buttonChatOffsetY;}

    private final int bottomOffset;

    private static volatile long visibleUntilMillis = 0L;

    private static volatile long flashStartMillis = 0L;
    private static final long FLASH_DURATION_MILLIS = 1000L;
    private static final long FADE_DURATION_MILLIS = 200L;

    private static boolean isNotLocalChannel() {
        return ChannelManager.getCurrentChannel() != ChatChannel.LOCAL;
    }

    private static boolean isInformationalState() {
        return isCommandMode() || isNotLocalChannel();
    }

    private static final Identifier ICON_TEXTURE_ORANGE =
            Identifier.fromNamespaceAndPath("mcci_orange_text", "textures/gui/orange_icon.png");
    private static final Identifier ICON_TEXTURE_LAME =
            Identifier.fromNamespaceAndPath("mcci_orange_text", "textures/gui/lame_icon.png");
    private static final Identifier ICON_TEXTURE_CMDS =
            Identifier.fromNamespaceAndPath("mcci_orange_text", "textures/gui/cmds_icon.png");
    private static final Identifier ICON_TEXTURE_NOT_LOCAL =
            Identifier.fromNamespaceAndPath("mcci_orange_text", "textures/gui/not_local_icon.png");


    /** Call this to make the button flash on screen for ~1 second, regardless of what screen is open. */
    public static void flash() {
        visibleUntilMillis = System.currentTimeMillis() + 1000L;
    }

    private static boolean isFlashing() {
        return System.currentTimeMillis() < visibleUntilMillis;
    }

    /** Returns 0f–1f: fades in over the first FADE_DURATION_MILLIS, fades out over the last. */
    private static float computeFlashAlpha() {
        long now = System.currentTimeMillis();
        if (now >= visibleUntilMillis) {
            return 0f;
        }

        long elapsedSinceStart = now - flashStartMillis;
        long remainingUntilEnd = visibleUntilMillis - now;

        float fadeIn = Math.min(1f, elapsedSinceStart / (float) FADE_DURATION_MILLIS);
        float fadeOut = Math.min(1f, remainingUntilEnd / (float) FADE_DURATION_MILLIS);

        return Math.min(fadeIn, fadeOut);
    }

    private static int applyAlpha(int argbColor, float alphaMultiplier) {
        int originalAlpha = (argbColor >>> 24) & 0xFF;
        int newAlpha = Math.round(originalAlpha * alphaMultiplier);
        return (newAlpha << 24) | (argbColor & 0x00FFFFFF);
    }


    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
        return null;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (!ModTurnedOnState.isModActive()) {
            return false;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        //must do this before logic to make sure player can still type in chat
        Minecraft.getInstance().execute(() -> {
            Screen screen = Minecraft.getInstance().screen;
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


        if (!ModTurnedOnState.isModActive() || isCommandMode() || isNotLocalChannel()) {
            return;
        }

        OrangeModeManager.toggle(false);


    }




    private static final int BAR_WIDTH = 1;

    public OrangeModeButton() {
        // Total width now includes both side bars; core button is inset by BAR_WIDTH on each side.
        super(configuredX() - BAR_WIDTH, configuredY(), WIDTH + (BAR_WIDTH * 2), HEIGHT, Component.empty());
        this.bottomOffset = configuredY();
    }

    private static final int ICON_WIDTH = 10;
    private static final int ICON_HEIGHT = 7;
    private static final int ICON_PADDING = 4;// gap between icon and text


    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int actualY = screenHeight - bottomOffset - height;
        this.setY(actualY);

        if(ModTurnedOnState.isModActive()){
            drawContents(graphics, getX(), getY(), this.isHovered(), 1f);
        }
    }

    private static void drawStateBlock(GuiGraphicsExtractor graphics, int x, int y, int bgColor,
                                       Component label, int textColor, Identifier iconTexture, float alpha) {
        bgColor = applyAlpha(bgColor, alpha);
        textColor = applyAlpha(textColor, alpha);

        int coreX = x + BAR_WIDTH;
        int coreWidth = WIDTH;
        int height = HEIGHT;
        graphics.fill(coreX, y, coreX + coreWidth, y + height, bgColor);

        int barHeight = height - 2;
        int barY = y + 1;

        int rightBarX = coreX + coreWidth;
        graphics.fill(rightBarX, barY, rightBarX + BAR_WIDTH, barY + barHeight, bgColor);

        int leftBarX = x;
        graphics.fill(leftBarX, barY, leftBarX + BAR_WIDTH, barY + barHeight, bgColor);

        var font = Minecraft.getInstance().font;
        int textWidth = font.width(label);

        int contentWidth = ICON_WIDTH + ICON_PADDING + textWidth;
        int contentX = coreX + (coreWidth - contentWidth) / 2;

        int iconX = contentX;
        int iconY = y + (height - ICON_HEIGHT) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, iconTexture, iconX, iconY, 0, 0,
                ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);

        int textX = contentX + ICON_WIDTH + ICON_PADDING;
        int textY = y + (height - 8) / 2;

        graphics.text(font, label, textX, textY, textColor, false);
    }

    private static int totalBlockWidth() {
        return WIDTH + (BAR_WIDTH * 2);
    }
    private static void drawContents(GuiGraphicsExtractor graphics, int x, int y, boolean hovered, float alpha) {
        boolean commandMode = isCommandMode();
        boolean notLocal = isNotLocalChannel();
        boolean isOrange = ConfigManager.CONFIG.isOrangeActive;

        int bgColor;
        Component label;
        int textColor;
        Identifier iconTexture;

        if (commandMode) {
            bgColor = hovered ? BACKGROUND_HOVER_CMD : BACKGROUND_CMD;
            label = TEXT_CMD;
            textColor = COLOR_CMD;
            iconTexture = ICON_TEXTURE_CMDS;
        } else if (notLocal) {
            bgColor = hovered ? BACKGROUND_HOVER_NOT_LOCAL : BACKGROUND_NOT_LOCAL;
            label = TEXT_NOT_LOCAL;
            textColor = COLOR_NOT_LOCAL;
            iconTexture = ICON_TEXTURE_NOT_LOCAL;
        } else if (isOrange) {
            bgColor = hovered ? BACKGROUND_HOVER_ORANGE : BACKGROUND_ORANGE;
            label = TEXT_ORANGE;
            textColor = COLOR_ORANGE;
            iconTexture = ICON_TEXTURE_ORANGE;
        } else {
            bgColor = hovered ? BACKGROUND_HOVER_DEFAULT : BACKGROUND_DEFAULT;
            label = TEXT_LAME;
            textColor = COLOR_DEFAULT;
            iconTexture = ICON_TEXTURE_LAME;
        }

        drawStateBlock(graphics, x, y, bgColor, label, textColor, iconTexture, alpha);
    }

    private static boolean isCommandMode() {
        if (ContextManager.getCurrentContext() == KeyBindContext.CHAT) {
            for (var child : ClientCompat.getScreen().children()) {
                if (child instanceof EditBox editBox) {
                    return editBox.getValue().startsWith("/");
                }
            }
        }
        return false;
    }


    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
    private static final int OVERLAY_GAP = 3; // px between CMDS button and the flash overlay

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (ContextManager.getCurrentContext() == KeyBindContext.CHAT) {
                Screens.getWidgets(screen).add(new OrangeModeButton());

            }
        });


        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("mushroomified", "orange_mode_flash"),
                (graphics, deltaTracker) -> {
                    if (!ModTurnedOnState.isModActive()){
                        return;
                    }

                    float alpha = computeFlashAlpha();
                    if (alpha <= 0f) {
                        return;
                    }

                    int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                    int x = configuredX() - BAR_WIDTH;
                    int y = screenHeight - configuredY() - HEIGHT;

                    boolean informational = isInformationalState();

                    if (ContextManager.getCurrentContext() != KeyBindContext.CHAT) {
                        // No widget exists to show the primary block — draw it ourselves.
                        drawContents(graphics, x, y, false, alpha);
                    }

                    if (informational) {
                        // Whether chat is open (widget shows CMDS/NO LOC) or closed (we just drew it above),
                        // always add the secondary orange/lame result beside it.
                        boolean isOrange = ConfigManager.CONFIG.isOrangeActive;
                        int bgColor = isOrange ? BACKGROUND_ORANGE : BACKGROUND_DEFAULT;
                        Component label = isOrange ? TEXT_ORANGE : TEXT_LAME;
                        int textColor = isOrange ? COLOR_ORANGE : COLOR_DEFAULT;
                        Identifier iconTexture = isOrange ? ICON_TEXTURE_ORANGE : ICON_TEXTURE_LAME;

                        int overlayX = x + totalBlockWidth() + OVERLAY_GAP;
                        drawStateBlock(graphics, overlayX, y, bgColor, label, textColor, iconTexture, alpha);
                    }
                }
        );

    }
}