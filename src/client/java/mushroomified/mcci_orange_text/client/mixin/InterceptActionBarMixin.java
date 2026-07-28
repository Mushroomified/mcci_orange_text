package mushroomified.mcci_orange_text.client.mixin;

import com.mojang.datafixers.util.Unit;
import mushroomified.mcci_orange_text.client.chat_channels.LocalChatManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Gui.class)
public class InterceptActionBarMixin{

    @Unique
    private static final FontDescription MCC_ICON =
            new FontDescription.Resource(Identifier.fromNamespaceAndPath("mcc", "icon"));

    @Inject(method = "setOverlayMessage", at = @At("HEAD"))
    private void checkActionBar(Component string, boolean animate, CallbackInfo ci) {

        if (string == null) {
            return;
        }

        string.visit((style, text) -> {

            // Only check characters using mcc:icon
            if (!MCC_ICON.equals(style.getFont())) {
                return Optional.empty();
            }

            for (int i = 0; i < text.length();) {
                int codepoint = text.codePointAt(i);

                if (codepoint == 0xE023){
                    LocalChatManager.isLocal = true;
                    return Optional.of(Unit.INSTANCE);
                }
                else {
                    LocalChatManager.isLocal = false;
                }

                i += Character.charCount(codepoint);
            }

            return Optional.empty();

        }, Style.EMPTY);
    }
}


//@Mixin(Gui.class)
//public class InterceptActionBarMixin {
//
//    @Inject(method = "setOverlayMessage", at = @At("HEAD"))
//    private void checkActionBar(Component message, boolean tinted, CallbackInfo ci) {
//        if (message == null) return;
//
//        List<Segment> segments = new ArrayList<>();
//        message.visit((style, text) -> {
//            FontDescription font = style.getFont();
//            for (int i = 0; i < text.length(); ) {
//                int codepoint = text.codePointAt(i);
//                segments.add(new Segment(font, codepoint));
//                i += Character.charCount(codepoint);
//            }
//            return Optional.<Unit>empty();
//        }, Style.EMPTY);
//
//        // Diagnostic dump. Records get free toString(), so this will show you
//        // e.g. "[Resource[id=minecraft:default] U+E023]" or
//        // "[AtlasSprite[atlasId=..., spriteId=lobby:icons/local] U+E020]"
//        // -- which immediately tells you which case you're actually in.
//        ModCommands.test = segments.stream()
//                .map(s -> String.format("[%s U+%04X]", s.font(), s.codepoint()))
//                .collect(Collectors.joining(" "));
//
//        for (Segment s : segments) {
//            ChatChannel channel = resolveChannel(s);
//            if (channel != null) {
//                ModCommands.chatChannel = channel;
//                break;
//            }
//        }
//    }
//
//    private record Segment(FontDescription font, int codepoint) {}
//
//    private static ChatChannel resolveChannel(Segment s) {
//        // Case 1: classic bitmap-font selector (font unchanged, codepoint is the key)
//        if (s.font() instanceof FontDescription.Resource) {
//            return switch (s.codepoint()) {
//                case 0xE023 -> ChatChannel.LOCAL;
//                case 0xE024 -> ChatChannel.PARTY;
//                case 0xE027 -> ChatChannel.TEAM;
//                case 0xE025 -> ChatChannel.PLOBBY;
//                default -> null;
//            };
//        }
//        // Case 2: the icon is a direct atlas sprite reference -- match on spriteId
//        // (fill these in once you've seen the real sprite IDs in the diagnostic dump)
//        if (s.font() instanceof FontDescription.AtlasSprite atlasSprite) {
//            String path = atlasSprite.spriteId().getPath();
//            if (path.contains("local")) return ChatChannel.LOCAL;
//            if (path.contains("party")) return ChatChannel.PARTY;
//            if (path.contains("team")) return ChatChannel.TEAM;
//            if (path.contains("plobby")) return ChatChannel.PLOBBY;
//        }
//        return null;
//    }
//}
//@Mixin(Gui.class)
//public class InterceptActionBarMixin {
//
//    @Inject(method = "setOverlayMessage", at = @At("HEAD"))
//    private void checkActionBar(Component message, boolean tinted, CallbackInfo ci) {
//        assert Minecraft.getInstance().player != null;
//        ModCommands.test = message.getString();
//        if (message != null){
//            if (message.getString().contains("\uE023")) {
//                ModCommands.chatChannel = ChatChannel.LOCAL;
//            }
//            else if (message.getString().contains("\uE024")) {
//                ModCommands.chatChannel = ChatChannel.PARTY;
//            }
//            else if (message.getString().contains("\uE027")){
//                ModCommands.chatChannel = ChatChannel.TEAM;
//            }
//            else if (message.getString().contains("\uE025")){
//                ModCommands.chatChannel = ChatChannel.PLOBBY;
//            }
//        }
//
//    }
//}
