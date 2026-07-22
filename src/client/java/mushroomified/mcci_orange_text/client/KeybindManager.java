package mushroomified.mcci_orange_text.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeybindManager {

    private static final Map<KeyCombo, Runnable> binds = new HashMap<>();


    public static void register(KeyCombo combo, Runnable action) {
        binds.put(combo, action);
    }


    public static void load(){
        register(
                new KeyCombo(
                        new HashSet<>(
                                ConfigManager.CONFIG.toggleOrangeModeKeybind
                        )
                ),
                () -> OrangeModeManager.toggle(true)
        );

        register(
                new KeyCombo(
                        new HashSet<>(
                                ConfigManager.CONFIG.insertWordKeybind
                        )
                ),
                WordActions::insertRandomWord
        );

    }

    public static void reload(){
        binds.clear();
        load();
    }


    public static boolean checkCombos(Set<Integer> pressedKeys) {

        for (var entry : binds.entrySet()) {

            if (entry.getKey().matches(pressedKeys)) {
                entry.getValue().run();
                return true;
            }

        }
        return false;
    }
}