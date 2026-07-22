package mushroomified.mcci_orange_text.client;

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
                                WordConfig.save();
                        })
                        .build()
        );


        builder.setSavingRunnable(ConfigManager::save);
        return builder.build();

    }

}
