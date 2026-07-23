package mushroomified.mcci_orange_text.client;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModConfig {
    public boolean isOrangeActive = true;

    public List<Integer> insertWordKeybind = new ArrayList<>(
            List.of(GLFW.GLFW_KEY_LEFT_CONTROL,
                    GLFW.GLFW_KEY_SLASH)
    );

    public Set<KeyBindContext> insertWordContexts = new HashSet<KeyBindContext>(
            Set.of(KeyBindContext.GAMEPLAY)
    );

    public List<Integer> toggleOrangeModeKeybind = new ArrayList<>(
            List.of(GLFW.GLFW_KEY_H,
                    GLFW.GLFW_KEY_UNKNOWN)
    );

    public Set<KeyBindContext> toggleOrangeModeContexts = new HashSet<KeyBindContext>(
            Set.of(KeyBindContext.GAMEPLAY)
    );


}
