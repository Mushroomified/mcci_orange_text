package mushroomified.mcci_orange_text.client;

import org.lwjgl.glfw.GLFW;


import java.util.Set;

public class KeyCombo {
    public int key;
    public boolean ctrl;
    public boolean shift;
    public boolean alt;

    public KeyCombo(
            int key,
            boolean ctrl,
            boolean shift,
            boolean alt
    ) {
        this.key = key;
        this.ctrl = ctrl;
        this.shift = shift;
        this.alt = alt;
    }

    public boolean matches(
            Set<Integer> pressedKeys
    ) {

        boolean ctrlDown = pressedKeys.contains(GLFW.GLFW_KEY_LEFT_CONTROL) || pressedKeys.contains(GLFW.GLFW_KEY_RIGHT_CONTROL);
        boolean shiftDown = pressedKeys.contains(GLFW.GLFW_KEY_LEFT_SHIFT) || pressedKeys.contains(GLFW.GLFW_KEY_RIGHT_SHIFT);
        boolean altDown = pressedKeys.contains(GLFW.GLFW_KEY_LEFT_ALT) || pressedKeys.contains(GLFW.GLFW_KEY_RIGHT_ALT);

        return pressedKeys.contains(key) && (!ctrl || ctrlDown) && (!shift || shiftDown) && (!alt || altDown);
    }


}
