package mushroomified.mcci_orange_text.client.mixin;

import mushroomified.mcci_orange_text.client.InputManager;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void keyPress(
            long handle,
            int action,
            KeyEvent event,
            CallbackInfo ci
    ) {
        if (action == GLFW.GLFW_PRESS) {

            boolean was_used = InputManager.keyPressed(event.key());

            if (was_used) {
                ci.cancel();
            }

        }

        if (action == GLFW.GLFW_RELEASE) {
            InputManager.keyReleased(event.key());
        }
    }
}