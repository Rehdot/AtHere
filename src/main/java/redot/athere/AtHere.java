package redot.athere;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class AtHere implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("athere");

	@Override
	public void onInitialize() {
		LOGGER.info(MSG.init);
		ClientCommandRegistrationCallback.EVENT.register(this::registerAtHere);
	}

	private void registerAtHere(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
		dispatcher.register(literal("athere")
				.then(literal("delay").then(argument("milliseconds", LongArgumentType.longArg())))
				.then(literal("exclude"))
				.then(literal("help"))
				.then(literal("status"))
				.then(literal("stop"))
		);
	}
}