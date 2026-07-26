package mushroomified.mcci_orange_text.client.mod_activation;

import mushroomified.mcci_orange_text.client.ConfigManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class ModTurnedOnState {

    private static boolean onMCCIsland = false;

    public static void register(){
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            String brand = client.getConnection().serverBrand();
            onMCCIsland = brand != null && brand.equals("Yacht (Marina)");
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            onMCCIsland = false;
        });
    }

    public static boolean isModActive(){
        return switch (ConfigManager.CONFIG.activationOption){
            case ON -> true;
            case OFF -> false;
            case ONLY_IN_MCCI -> onMCCIsland;
        };
    }
}
