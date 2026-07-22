package mushroomified.mcci_orange_text.client;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    public boolean isOrangeActive = true;

    public List<Integer> insertWordKeybind = new ArrayList<>(
            List.of(GLFW.GLFW_KEY_LEFT_CONTROL,
                    GLFW.GLFW_KEY_SLASH)
    );

    public List<Integer> toggleOrangeModeKeybind = new ArrayList<>(
            List.of(GLFW.GLFW_KEY_H,
                    GLFW.GLFW_KEY_UNKNOWN)
    );
}
