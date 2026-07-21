package mushroomified.mcci_orange_text.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ThreadLocalRandom;


public class HotKeys implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("mcci_text","mcci"));

	public static KeyMapping TOGGLE_ORANGE_KEY;
	public static boolean isOrangeActive;

	public static KeyMapping PASTE_SYNONYM;

	@Override
	public void onInitializeClient() {
		WordConfig.load();
		TOGGLE_ORANGE_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.mcci_text.toggle_orange",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				CATEGORY
		));

		PASTE_SYNONYM = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.mcci_text.paste_synonym",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_RIGHT_BRACKET,
				CATEGORY
		));


		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (TOGGLE_ORANGE_KEY.consumeClick()) {
				isOrangeActive = !isOrangeActive;

				if(client.player != null){
					client.gui.toastManager().addToast(
							new SystemToast(
									SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
									Component.literal("Chat"),
									isOrangeActive
											? Component.literal("ORANGE").withStyle(ChatFormatting.GOLD)
											: Component.literal("lame").withStyle(ChatFormatting.GRAY)
							)
					);
				}
			}

			while (PASTE_SYNONYM.consumeClick()){

				if(client.player == null)
					return;

				if (WordConfig.words.isEmpty())
					return;

				String word = WordConfig.words.get(
						ThreadLocalRandom.current().nextInt(WordConfig.words.size())
				);


				client.execute(() ->{
					client.gui.setScreen(new ChatScreen(word,false));
						}
				);



			}


		});

	}


}

