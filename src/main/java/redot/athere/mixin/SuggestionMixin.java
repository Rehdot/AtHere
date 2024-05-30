package redot.athere.mixin;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CommandSuggestionsS2CPacket.class)
public class SuggestionMixin {
    @Inject(method = "getSuggestions", at = @At("RETURN"), cancellable = true)
    private void onCommandSuggestions(CallbackInfoReturnable<Suggestions> cir) {
        List<String> suggestions = cir.getReturnValue().getList().stream().map(Suggestion::getText).toList();
        String name = MinecraftClient.getInstance().player.getName().getString();

        if (suggestions.contains(name)) {
            Suggestion atHere = new Suggestion(cir.getReturnValue().getRange(), "@here");
            cir.getReturnValue().getList().add(atHere);
        }
    }
}