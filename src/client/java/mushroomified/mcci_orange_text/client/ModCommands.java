package mushroomified.mcci_orange_text.client;

import mushroomified.mcci_orange_text.client.chat_channels.ChannelManager;
import mushroomified.mcci_orange_text.client.compat.ClientCompat;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ModCommands {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            dispatcher.register(ClientCommands.literal("orangetext")
                            .executes(context -> {
                                OrangeModeManager.showStatus();
                                return 1;
                            })
                    .then(ClientCommands.literal("reload")
                            .executes(context -> {
                                ConfigManager.load();
                                WordConfig.load();
                                KeybindManager.reload();

                                context.getSource().getPlayer().sendSystemMessage(
                                        Component.literal("Orange Config Reloaded!"));
                                return 1;
                            })
                    )
                    .then(ClientCommands.literal("openconfig")
                            .executes(context ->{
                                Minecraft client = Minecraft.getInstance();

                                client.execute(() -> {

                                    var parent = ClientCompat.getScreen();

                                    var configScreen = ConfigScreen.create(parent);

                                    ClientCompat.setScreen(configScreen);
                                });

                                return 1;
                            })
                    )
                    .then(ClientCommands.literal("enable")
                            .executes(context ->{
                                OrangeModeManager.setStatus(true,true);
                                return 1;
                            }))
                    .then(ClientCommands.literal("disable")
                            .executes(context -> {
                                OrangeModeManager.setStatus(false,true);
                                return 1;
                            }))
            );


        });
    }
}