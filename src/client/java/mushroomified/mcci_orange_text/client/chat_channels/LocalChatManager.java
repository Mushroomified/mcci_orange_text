package mushroomified.mcci_orange_text.client.chat_channels;

import mushroomified.mcci_orange_text.client.ConfigManager;

public class LocalChatManager {
    public static boolean isLocal = false;

    public static boolean isLocalAndLocalMatters(){
        if (ConfigManager.CONFIG.onlyActivateInLocalChat) {
            return isLocal;
        } else {
            return true;
        }
    }
}
