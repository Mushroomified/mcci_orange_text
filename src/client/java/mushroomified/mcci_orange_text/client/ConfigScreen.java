package mushroomified.mcci_orange_text.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;


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
                entryBuilder.startStrList(
                        Component.literal("Words to start chat with"),
                        WordConfig.words
                )
                        .setSaveConsumer(words ->{
                                WordConfig.words = words;
                        })
                        .build()
        );

        general.addEntry(
                entryBuilder.startKeyCodeField(
                                Component.literal("Insert Word Key 1"),
                                InputConstants.Type.KEYSYM.getOrCreate(ConfigManager.CONFIG.insertWordKeybind.get(0))
                        )
                        .setKeySaveConsumer(key -> {
                            ConfigManager.CONFIG.insertWordKeybind.set(0, key.getValue());

                        })
                        .build()
        );

        general.addEntry(
                entryBuilder.startKeyCodeField(
                                Component.literal("Insert Word Key 2 (optional)"),
                                InputConstants.Type.KEYSYM.getOrCreate(ConfigManager.CONFIG.insertWordKeybind.get(1))
                        )
                        .setKeySaveConsumer(key -> {
                            ConfigManager.CONFIG.insertWordKeybind.set(1, key.getValue());

                        })
                        .build()
        );

        general.addEntry(
                entryBuilder.startKeyCodeField(
                                Component.literal("Orange Toggle Key 1"),
                                InputConstants.Type.KEYSYM.getOrCreate(ConfigManager.CONFIG.toggleOrangeModeKeybind.get(0))
                        )
                        .setKeySaveConsumer(key -> {
                            ConfigManager.CONFIG.toggleOrangeModeKeybind.set(0, key.getValue());

                        })
                        .build()
        );

        general.addEntry(
                entryBuilder.startKeyCodeField(
                                Component.literal("Orange Toggle Key 2 (optional)"),
                                InputConstants.Type.KEYSYM.getOrCreate(ConfigManager.CONFIG.toggleOrangeModeKeybind.get(1))
                        )
                        .setKeySaveConsumer(key -> {
                            ConfigManager.CONFIG.toggleOrangeModeKeybind.set(1, key.getValue());

                        })
                        .build()
        );





        builder.setSavingRunnable(()->{
            ConfigManager.save();
            WordConfig.save();
            KeybindManager.reload();
        });
        return builder.build();

    }

}
