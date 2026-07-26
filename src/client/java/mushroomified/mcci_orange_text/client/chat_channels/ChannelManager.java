package mushroomified.mcci_orange_text.client.chat_channels;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class ChannelManager {
    private static ChatChannel currentChannel = ChatChannel.LOCAL;

    public static ChatChannel getCurrentChannel(){
        return currentChannel;
    }


    public static void register(){
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (!overlay) { // ignore action bar messages, only system/chat messages
                ChannelManager.onChatMessage(message.getString());
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ChannelManager.reset();
        });
    }


    public static void onChatMessage(String plainText){
        if (plainText.startsWith("You are now in the ")){
            if(plainText.contains("Local")) {currentChannel = ChatChannel.LOCAL;}
            else if(plainText.contains("Team")) {currentChannel = ChatChannel.TEAM;}
            else if(plainText.contains("Party")) {currentChannel = ChatChannel.PARTY;}
            else if(plainText.contains("Plobby")) {currentChannel = ChatChannel.PLOBBY;}
        }

    }

    public static void reset(){
        currentChannel = ChatChannel.LOCAL;
    }

}
