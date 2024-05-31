package redot.athere;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class AtHere implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("athere");
	private static MinecraftClient client = MinecraftClient.getInstance();
	private static SuggestionProvider<FabricClientCommandSource> playerSuggestions = (context, builder) -> {
		if (client.player != null) {
			for (String entry : CMDProcess.getOnlinePlayers()) {
				builder.suggest(entry);
			}
		}
		return builder.buildFuture();
	};
	private static SuggestionProvider<FabricClientCommandSource> delaySuggestions = (context, builder) -> {
		List<Integer> suggestions = List.of(100, 250, 500, 750, 1000, 1500, 3000, 5000, 7500, 10000);
		for (int suggestion : suggestions) {
			builder.suggest(suggestion);
		}
		return builder.buildFuture();
	};

	@Override
	public void onInitialize() {
		LOGGER.info(MSG.init);
		ClientCommandRegistrationCallback.EVENT.register(this::registerAtHere);
	}

	private void registerAtHere(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
		dispatcher.register(literal("athere")
				.then(literal("delay").then(argument("milliseconds", LongArgumentType.longArg())
						.suggests(delaySuggestions)))
				.then(literal("exclude").then(argument("player", StringArgumentType.word())
						.suggests(playerSuggestions)))
				.then(literal("include").then(argument("player", StringArgumentType.word())
						.suggests(playerSuggestions)))
				.then(literal("help"))
				.then(literal("status"))
				.then(literal("stop")));
	}

}