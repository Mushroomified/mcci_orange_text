package mushroomified.mcci_orange_text.client;

import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Set;
import java.util.stream.Collectors;

public class KeyCombo {

    private final Set<Integer> keys;

    public KeyCombo(Set<Integer> keys){
        this.keys = keys.stream()
                .filter(key -> key != GLFW.GLFW_KEY_UNKNOWN)
                .collect(Collectors.toSet());
    }

    public boolean matches(Set<Integer> pressedKeys){
        return pressedKeys.containsAll(keys)
                && pressedKeys.size() == keys.size();


    }
}
