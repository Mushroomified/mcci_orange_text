package mushroomified.mcci_orange_text.client.mixin;


import mushroomified.mcci_orange_text.client.ConfigManager;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(ClientPacketListener.class)
public class ExampleClientMixin {
	@ModifyVariable(at = @At("HEAD"), method = "sendChat", argsOnly = true)
	private String makeOrange(String message) {

		if (ConfigManager.CONFIG.isOrangeActive) {
			return "*" + message + "*";
		}
		return message;
	}
}



