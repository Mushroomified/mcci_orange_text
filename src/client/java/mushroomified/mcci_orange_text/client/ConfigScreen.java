package mushroomified.mcci_orange_text.client;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.clothconfig2.api.*;
import mushroomified.mcci_orange_text.client.mod_activation.ActivationOption;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import org.lwjgl.glfw.GLFW;

import static mushroomified.mcci_orange_text.client.WordConfig.DEFAULT_WORD_LIST;


public class ConfigScreen {

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    private static String CapitalizeButNotGUI(String allCapsString){
        if (allCapsString.equals("GUI")){
            return allCapsString;
        } else {
            return capitalize(allCapsString.toLowerCase());
        }
    }

    private static String capitalizeFully(String str) {
        if (str == null || str.isEmpty()) return str;
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }



    public static Screen create(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Orange Mod Config"));

        ConfigCategory general = builder.getOrCreateCategory(
                Component.literal("General")
        );

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(
                entryBuilder.startEnumSelector(
                        Component.literal("Mod turned on"),
                        ActivationOption.class,
                        ConfigManager.CONFIG.activationOption)
                        .setDefaultValue(ActivationOption.ONLY_IN_MCCI)
                        .setEnumNameProvider(state -> {
                            String rawName = state.name();

                            String spacedName = rawName.replace('_',' ').toLowerCase();

                            String titleCaseName = capitalizeFully(spacedName);

                            String capitalizedMCCI = titleCaseName.replace("Mcci", "MCCI");

                            return Component.literal(capitalizedMCCI);
                        }
                        )
                        .setSaveConsumer(value ->
                                ConfigManager.CONFIG.activationOption = value)
                        .build()
        );


        general.addEntry(
                entryBuilder.startBooleanToggle(
                                Component.literal("Only activate in Local chat"),
                                ConfigManager.CONFIG.onlyActivateInLocalChat)
                        .setDefaultValue(true)
                        .setSaveConsumer(value ->
                                ConfigManager.CONFIG.onlyActivateInLocalChat = value)
                        .build()
        );


        general.addEntry(
                entryBuilder.startBooleanToggle(
                        Component.literal("Orange"),
                        ConfigManager.CONFIG.isOrangeActive)
                        .setSaveConsumer(value ->
                                ConfigManager.CONFIG.isOrangeActive = value)
                        .build()


        );

        general.addEntry(
                entryBuilder.startIntSlider(
                        Component.literal("Orange Indicator X-Offset"),
                        ConfigManager.CONFIG.buttonChatOffsetX,
                        0,
                        50
                ).setDefaultValue(0)
                        .setSaveConsumer(value ->
                                ConfigManager.CONFIG.buttonChatOffsetX = value)
                        .build()
        );

        general.addEntry(
                entryBuilder.startIntSlider(
                                Component.literal("Orange Indicator Y-Offset"),
                                ConfigManager.CONFIG.buttonChatOffsetY,
                                0,
                                50
                        ).setDefaultValue(11)
                        .setSaveConsumer(value ->
                                ConfigManager.CONFIG.buttonChatOffsetY = value)
                        .build()
        );



        general.addEntry(
                entryBuilder.startStrList(
                        Component.literal("Words to start chat with"),
                        WordConfig.words
                        ).setDefaultValue(DEFAULT_WORD_LIST)
                        .setSaveConsumer(words ->{
                            WordConfig.words = words;
                        })
                        .build()
        );

        ConfigCategory orangeToggle = builder.getOrCreateCategory(
                Component.literal("Orange Toggle")
        );


        orangeToggle.addEntry(
                entryBuilder.startModifierKeyCodeField(
                        Component.literal("Orange Toggle Hotkey"),
                                ModifierKeyCode.of(
                                        InputConstants.Type.KEYSYM.getOrCreate(ConfigManager.CONFIG.toggleOrangeModeKeybind.key),
                                        Modifier.of(
                                                ConfigManager.CONFIG.toggleOrangeModeKeybind.alt,
                                                ConfigManager.CONFIG.toggleOrangeModeKeybind.ctrl,
                                                ConfigManager.CONFIG.toggleOrangeModeKeybind.shift))
                ).setDefaultValue(ModifierKeyCode.of(
                        InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_G),
                                Modifier.of(
                                        false,
                                        true,
                                        false)
                        )
                )                        .setModifierSaveConsumer(modifierKeyCode -> {
                            ConfigManager.CONFIG.toggleOrangeModeKeybind.key = modifierKeyCode.getKeyCode().getValue();
                            ConfigManager.CONFIG.toggleOrangeModeKeybind.alt = modifierKeyCode.getModifier().hasAlt();
                            ConfigManager.CONFIG.toggleOrangeModeKeybind.ctrl = modifierKeyCode.getModifier().hasControl();
                            ConfigManager.CONFIG.toggleOrangeModeKeybind.shift = modifierKeyCode.getModifier().hasShift();
                        })
                        .build()
        );
        for (KeyBindContext context : KeyBindContext.values()) {
            orangeToggle.addEntry(
                    entryBuilder.startBooleanToggle(
                                    Component.literal(CapitalizeButNotGUI(context.name())),
                                    ConfigManager.CONFIG.toggleOrangeModeContexts.contains(context)
                            )
                            .setSaveConsumer(enabled -> {
                                if (enabled) {
                                    ConfigManager.CONFIG.toggleOrangeModeContexts.add(context);
                                } else {
                                    ConfigManager.CONFIG.toggleOrangeModeContexts.remove(context);
                                }
                            })
                            .build()
            );
        }




        ConfigCategory insertWord = builder.getOrCreateCategory(
                Component.literal("Insert Word")
        );

        insertWord.addEntry(
                entryBuilder.startModifierKeyCodeField(
                                Component.literal("Insert Word Hotkey"),
                                ModifierKeyCode.of(
                                        InputConstants.Type.KEYSYM.getOrCreate(ConfigManager.CONFIG.insertWordKeybind.key),
                                        Modifier.of(
                                                ConfigManager.CONFIG.insertWordKeybind.alt,
                                                ConfigManager.CONFIG.insertWordKeybind.ctrl,
                                                ConfigManager.CONFIG.insertWordKeybind.shift)
                                )
                        )
                        .setDefaultValue(ModifierKeyCode.of(
                                InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_SLASH),
                                Modifier.of(
                                        false,
                                        true,
                                        false)
                                )
                        )
                        .setModifierSaveConsumer(modifierKeyCode -> {
                            ConfigManager.CONFIG.insertWordKeybind.key = modifierKeyCode.getKeyCode().getValue();
                            ConfigManager.CONFIG.insertWordKeybind.alt = modifierKeyCode.getModifier().hasAlt();
                            ConfigManager.CONFIG.insertWordKeybind.ctrl = modifierKeyCode.getModifier().hasControl();
                            ConfigManager.CONFIG.insertWordKeybind.shift = modifierKeyCode.getModifier().hasShift();
                        })
                        .build()
        );



        for (KeyBindContext context : KeyBindContext.values()) {
            insertWord.addEntry(
                    entryBuilder.startBooleanToggle(
                                    Component.literal(CapitalizeButNotGUI(context.name())),
                                    ConfigManager.CONFIG.insertWordContexts.contains(context)
                            )
                            .setSaveConsumer(enabled -> {
                                if (enabled) {
                                    ConfigManager.CONFIG.insertWordContexts.add(context);
                                } else {
                                    ConfigManager.CONFIG.insertWordContexts.remove(context);
                                }
                            })
                            .build()
            );
        }






        builder.setSavingRunnable(()->{
            ConfigManager.save();
            WordConfig.save();
            KeybindManager.reload();
        });
        return builder.build();

    }

}
