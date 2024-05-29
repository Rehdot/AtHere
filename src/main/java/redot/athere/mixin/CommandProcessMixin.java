package redot.athere.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import redot.athere.CMDProcess;

import java.util.Arrays;
import java.util.List;

@Mixin(ClientPlayNetworkHandler.class)
public class CommandProcessMixin {

	@Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
	private void onCommand(String content, CallbackInfo ci) {
		String cmd = content.toLowerCase();

		if (cmd.startsWith("athere")) {
			ci.cancel();
			List<String> args = Arrays.stream(cmd.split(" ")).toList();
			CMDProcess.processCommand(args);
			return;
		}

		if (!cmd.contains("@here")) return;
		ci.cancel();
		CMDProcess.processAtHere(cmd);
	}
}
