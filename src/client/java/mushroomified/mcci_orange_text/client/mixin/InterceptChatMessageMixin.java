package mushroomified.mcci_orange_text.client.mixin;


import mushroomified.mcci_orange_text.client.ConfigManager;
import mushroomified.mcci_orange_text.client.mod_activation.ModTurnedOnState;
import mushroomified.mcci_orange_text.client.chat_channels.ChannelManager;
import mushroomified.mcci_orange_text.client.chat_channels.ChatChannel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(ClientPacketListener.class)
public class InterceptChatMessageMixin {
	@ModifyVariable(at = @At("HEAD"), method = "sendChat", argsOnly = true)
	private String makeOrange(String message) {

		if (ModTurnedOnState.isModActive() && ConfigManager.CONFIG.isOrangeActive && ChannelManager.getCurrentChannel() == ChatChannel.LOCAL) {
			return "*" + message + "*";
		}
		return message;
	}
}



