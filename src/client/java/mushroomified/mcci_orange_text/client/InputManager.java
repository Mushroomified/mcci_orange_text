package mushroomified.mcci_orange_text.client;

import java.util.HashSet;
import java.util.Set;

public class InputManager {
    private static final Set<Integer> pressedKeys = new HashSet<>();

    public static boolean keyPressed(int key){
        pressedKeys.add(key);

        return KeybindManager.checkCombos(pressedKeys);
    }

    public static void keyReleased(int key){
        pressedKeys.remove(key);
    }

    public static boolean isPressed(KeyCombo combo){
        return combo.matches(pressedKeys);
    }
}
