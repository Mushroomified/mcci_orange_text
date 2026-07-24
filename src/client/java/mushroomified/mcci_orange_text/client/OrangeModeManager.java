package mushroomified.mcci_orange_text.client;

public class OrangeModeManager {

    public static void toggle(boolean showMessage){
        ConfigManager.CONFIG.isOrangeActive = !ConfigManager.CONFIG.isOrangeActive;
        ConfigManager.save();

        if(showMessage){
            showStatus();
        }
    }

    public static void setStatus(boolean status, boolean showMessage){
        ConfigManager.CONFIG.isOrangeActive = status;

        if(showMessage){
            showStatus();
        }
    }

    public static void showStatus() {
        OrangeModeButton.flash();

    }
}
