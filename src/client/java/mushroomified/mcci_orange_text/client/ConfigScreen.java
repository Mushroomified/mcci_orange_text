package mushroomified.mcci_orange_text.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.terraformersmc.modmenu.util.mod.Mod;
import me.shedaniel.clothconfig2.api.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.List;


public class ConfigScreen {

    public static Screen create(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Orange Mod Config"));

        ConfigCategory general = builder.getOrCreateCategory(
                Component.literal("General")
        );

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

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
                        ).setDefaultValue(List.of(
                                        "says", "proclaims", "states", "declares", "remarks", "claims",
                                        "asserts", "maintains", "whispers", "murmurs", "sighs", "cries",
                                        "believes", "expresses", "observes", "voices", "suggests", "mentions",
                                        "utters", "announces", "affirms", "adds", "reports", "notes",
                                        "explains", "comments", "argues", "warns", "confesses", "wonders",
                                        "screams", "yells", "shouts", "mutters", "admits", "complains"
                                )
                        )
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
                ).setDefaultValue(ModifierKeyCode.of(InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_G), Modifier.of(false,true,false)))
                        .setModifierSaveConsumer(modifierKeyCode -> {
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
                                    Component.literal(context.name()),
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

//        insertWord.addEntry(
//                entryBuilder.startTextDescription(
//                        new Component.literal()
//                )
//        )

        insertWord.addEntry(
                entryBuilder.startModifierKeyCodeField(
                                Component.literal("Insert Word Hotkey"),
                                ModifierKeyCode.of(
                                        InputConstants.Type.KEYSYM.getOrCreate(ConfigManager.CONFIG.insertWordKeybind.key),
                                        Modifier.of(
                                                ConfigManager.CONFIG.insertWordKeybind.alt,
                                                ConfigManager.CONFIG.insertWordKeybind.ctrl,
                                                ConfigManager.CONFIG.insertWordKeybind.shift))
                        )
                        .setDefaultValue(ModifierKeyCode.of(InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_SLASH), Modifier.of(false,true,false)))
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
                                    Component.literal(context.name()),
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
