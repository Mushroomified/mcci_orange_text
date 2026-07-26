package mushroomified.mcci_orange_text.client;

import mushroomified.mcci_orange_text.client.chat_channels.ChannelManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrangeTextMod implements ClientModInitializer {
	public static final Logger LOGGER  = LoggerFactory.getLogger("orange_mode_text");

	@Override
	public void onInitializeClient() {
		ConfigPaths.init();

		WordConfig.load();
		ConfigManager.load();
		KeybindManager.load();

		ModCommands.register();
		OrangeModeButton.register();
		ChannelManager.register();

	}
}