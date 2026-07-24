package mushroomified.mcci_orange_text.client;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModConfig {
    public boolean isOrangeActive = true;

    public int buttonChatOffsetX = 0;

    public int buttonChatOffsetY = 11;

    public KeyCombo insertWordKeybind = new KeyCombo(
            GLFW.GLFW_KEY_SLASH,
            true,
            false,
            false
    );

    public KeyCombo toggleOrangeModeKeybind = new KeyCombo(
            GLFW.GLFW_KEY_H,
            false,
            false,
            false
    );


    public Set<KeyBindContext> insertWordContexts = new HashSet<KeyBindContext>(
            Set.of(KeyBindContext.GAMEPLAY)
    );


    public Set<KeyBindContext> toggleOrangeModeContexts = new HashSet<KeyBindContext>(
            Set.of(KeyBindContext.GAMEPLAY)
    );


}
