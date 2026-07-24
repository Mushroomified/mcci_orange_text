package mushroomified.mcci_orange_text.client;

import net.fabricmc.api.ClientModInitializer;

public class HotKeys implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ConfigPaths.init();

		WordConfig.load();
		ConfigManager.load();
		KeybindManager.load();

		ModCommands.register();
		OrangeModeOverlay.register();

	}

}