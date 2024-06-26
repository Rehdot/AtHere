package redot.athere.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import redot.athere.AtHere;
import redot.athere.CMDProcess;

@Mixin(ClientPlayNetworkHandler.class)
public class CommandProcessMixin {
	@Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
	private void onCommand(String content, CallbackInfo ci) {
		String cmd = content.toLowerCase();

		if (!cmd.contains("@here") && !CMDProcess.containsNumArg(cmd)) return;

		ci.cancel();
		CMDProcess.processCommand(cmd);
	}
}
