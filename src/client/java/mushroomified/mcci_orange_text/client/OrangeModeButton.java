package mushroomified.mcci_orange_text.client;



import mushroomified.mcci_orange_text.client.compat.ClientCompat;
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
    private static final int COLOR_CMDS = 0xFFC090F0; // light purple text

    private static final Component TEXT_ORANGE = Component.literal("ᴏʀᴀɴɢᴇ");
    private static final Component TEXT_LAME = Component.literal("ʟᴀᴍᴇ").withStyle(ChatFormatting.GRAY);
    private static final Component TEXT_CMDS = Component.literal("ᴄᴍᴅ");


    private static final int BACKGROUND_DEFAULT = 0xA0454545;       // lightened from 0x333333
    private static final int BACKGROUND_ORANGE = 0xA04A4038;        // default gray with a faint warm/orange tint
    private static final int BACKGROUND_CMDS = 0xA0604878;       // muted purple background

    private static final int BACKGROUND_HOVER_DEFAULT = 0xA05E5E5E; // lightened from 0x4A4A4A
    private static final int BACKGROUND_HOVER_ORANGE = 0xA0665A48;  // faint-orange hover, a bit brighter than BACKGROUND_ORANGE
    private static final int BACKGROUND_HOVER_CMDS = 0xA0604878; // same as non-hover — no interactive affordance since clicks are ignored

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

    private static final Identifier ICON_TEXTURE_ORANGE =
            Identifier.fromNamespaceAndPath("mcci_orange_text", "textures/gui/orange_icon.png");
    private static final Identifier ICON_TEXTURE_LAME =
            Identifier.fromNamespaceAndPath("mcci_orange_text", "textures/gui/lame_icon.png");
    private static final Identifier ICON_TEXTURE_CMDS =
            Identifier.fromNamespaceAndPath("mcci_orange_text", "textures/gui/cmds_icon.png");



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
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        if (isCommandMode()) {
            return;
        }

        OrangeModeManager.toggle(false);

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

        drawContents(graphics, getX(), getY(), this.isHovered(), 1f);
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
        boolean isOrange = ConfigManager.CONFIG.isOrangeActive;

        int bgColor;
        Component label;
        int textColor;
        Identifier iconTexture;

        if (commandMode) {
            bgColor = hovered ? BACKGROUND_HOVER_CMDS : BACKGROUND_CMDS;
            label = TEXT_CMDS;
            textColor = COLOR_CMDS;
            iconTexture = ICON_TEXTURE_CMDS;
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
                    float alpha = computeFlashAlpha();
                    if (alpha <= 0f) {
                        return;
                    }

                    //boolean chatOpen = Minecraft.getInstance().screen instanceof ChatScreen;
                    int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                    int y = screenHeight - configuredY() - HEIGHT;

                    if (ContextManager.getCurrentContext() == KeyBindContext.CHAT) {
                        if (!isCommandMode()) {
                            return; // widget itself already shows the current orange/lame state, nothing extra needed
                        }

                        // CMDS button is already drawn by the widget — just add the toggle-result indicator beside it
                        boolean isOrange = ConfigManager.CONFIG.isOrangeActive;
                        int bgColor = isOrange ? BACKGROUND_ORANGE : BACKGROUND_DEFAULT;
                        Component label = isOrange ? TEXT_ORANGE : TEXT_LAME;
                        int textColor = isOrange ? COLOR_ORANGE : COLOR_DEFAULT;
                        Identifier iconTexture = isOrange ? ICON_TEXTURE_ORANGE : ICON_TEXTURE_LAME;

                        int buttonX = configuredX() - BAR_WIDTH;
                        int overlayX = buttonX + totalBlockWidth() + OVERLAY_GAP;

                        drawStateBlock(graphics, overlayX, y, bgColor, label, textColor, iconTexture, alpha);
                    } else {
                        // No chat open at all — standalone flash, same as before
                        int x = configuredX() - BAR_WIDTH;
                        drawContents(graphics, x, y, false, alpha);
                    }
                }
        );

    }
}