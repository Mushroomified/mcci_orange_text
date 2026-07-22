package mushroomified.mcci_orange_text.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.io.ObjectInputFilter;

public class ModCommands {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            dispatcher.register(ClientCommands.literal("orangetext")
                    .then(ClientCommands.literal("reload")
                            .executes(context -> {
                                ConfigManager.load();
                                WordConfig.load();

                                context.getSource().getPlayer().sendSystemMessage(
                                        Component.literal("Orange Config Reloaded!"));
                                return 1;
                            })
                    )
                    .then(ClientCommands.literal("openconfig")
                            .executes(context ->{
                                Minecraft client = Minecraft.getInstance();

                                client.execute(() -> {

                                    var parent = client.gui.screen();

                                    var configScreen = ConfigScreen.create(parent);

                                    client.gui.setScreen(configScreen);
                                });

                                return 1;
                            })
                    )
            );


        });
    }
}