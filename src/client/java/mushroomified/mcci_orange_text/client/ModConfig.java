package mushroomified.mcci_orange_text.client;

import mushroomified.mcci_orange_text.client.mod_activation.ActivationOption;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class ModConfig {
    public ActivationOption activationOption = ActivationOption.ONLY_IN_MCCI;

    public boolean isOrangeActive = false;

    public int buttonChatOffsetX = 0;

    public int buttonChatOffsetY = 11;

    public KeyCombo insertWordKeybind = new KeyCombo(
            GLFW.GLFW_KEY_SLASH,
            true,
            false,
            false
    );

    public KeyCombo toggleOrangeModeKeybind = new KeyCombo(
            GLFW.GLFW_KEY_G,
            true,
            false,
            false
    );


    public Set<KeyBindContext> insertWordContexts = new HashSet<>(
            Set.of(KeyBindContext.GAMEPLAY, KeyBindContext.CHAT)
    );


    public Set<KeyBindContext> toggleOrangeModeContexts = new HashSet<>(
            Set.of(KeyBindContext.GAMEPLAY, KeyBindContext.CHAT)
    );


}
