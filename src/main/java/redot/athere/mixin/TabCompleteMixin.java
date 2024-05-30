package redot.athere.mixin;

import net.minecraft.client.network.ClientCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Collection;

@Mixin(ClientCommandSource.class)
public class TabCompleteMixin {
    @Inject(method = "getPlayerNames", at = @At("RETURN"), cancellable = true)
    private void onCommandSuggestions(CallbackInfoReturnable<Collection<String>> cir) {
        cir.getReturnValue().add("@here");
    }
}