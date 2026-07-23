package mushroomified.mcci_orange_text.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeybindManager {

    private record KeybindData(
            Runnable action,
            Set<KeyBindContext> contexts
    ){}

    private static final Map<KeyCombo, KeybindData> binds = new HashMap<>();


    public static void register(KeyCombo combo, Runnable action, Set<KeyBindContext> contexts) {
        binds.put(combo, new KeybindData(action, contexts));
    }


    public static void load(){
        register(
                new KeyCombo(
                        new HashSet<>(
                                ConfigManager.CONFIG.toggleOrangeModeKeybind
                        )
                ),
                () -> OrangeModeManager.toggle(true)
                , ConfigManager.CONFIG.toggleOrangeModeContexts
        );

        register(
                new KeyCombo(
                        new HashSet<>(
                                ConfigManager.CONFIG.insertWordKeybind
                        )
                ),
                WordActions::insertRandomWord
                , ConfigManager.CONFIG.insertWordContexts
        );

    }

    public static void reload(){
        binds.clear();
        load();
    }


    public static boolean checkCombos(Set<Integer> pressedKeys) {

        KeyBindContext currentContext = ContextManager.getCurrentContext();

        for (Map.Entry<KeyCombo, KeybindData> entry : binds.entrySet()) {

            if (entry.getKey().matches(pressedKeys)
                    && entry.getValue().contexts.contains(currentContext)){

                entry.getValue().action.run();
                return true;
            }

        }
        return false;
    }
}