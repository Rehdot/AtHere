package redot.athere;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redot.athere.interfaces.LongExecutor;
import redot.athere.interfaces.PhraseExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class AtHere implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("athere");
	private static MinecraftClient client = MinecraftClient.getInstance();
	public static List<ScheduledExecutorService> schedulers = new ArrayList<>();

	// for inclusions make it to where it doesn't need to be a player name so command execution will work anyway,
	// add number arguments to every command, so if I want to do something like "/claim tp Redot 1->40" I can do that
	public static List<String> exclusions = new ArrayList<>(), inclusions = new ArrayList<>();
	public static long delay = 100;
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
				.then(registerLongIntakeExecutor("delay", "milliseconds", num -> {
					delay = num;
					MSGManager.sendPlayerMSG(MSG.setDelay());
				}))
				.then(registerStringIntakeExecutor("exclude", "player", player -> {
					if (!exclusions.contains(player.toLowerCase()))
						exclusions.add(player.toLowerCase());
					inclusions.remove(player.toLowerCase());
					MSGManager.sendPlayerMSG(MSG.addExclusion(player));
				}))
				.then(registerStringIntakeExecutor("include", "player", player -> {
					exclusions.remove(player.toLowerCase());
					inclusions.add(player.toLowerCase());
					MSGManager.sendPlayerMSG(MSG.addInclusion(player));
				}))
				.then(registerExecutor("stop", () -> {
					schedulers.forEach(ExecutorService::shutdownNow);
					schedulers.clear();
					MSGManager.sendPlayerMSG(MSG.shutDownTasks);
				}))
				.then(registerExecutor("clearexclusions", () -> {
					exclusions.clear();
					MSGManager.sendPlayerMSG(MSG.clearExclusions);
				}))
				.then(registerExecutor("clearinclusions", () -> {
					inclusions.clear();
					MSGManager.sendPlayerMSG(MSG.clearInclusions);
				}))
				.then(registerExecutor("help", MSGManager::sendHelpMSG))
				.then(registerExecutor("status", MSGManager::sendStatusMSG))
		);
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> registerExecutor(String name, Runnable executor) {
		return literal(name).executes(context -> {
			executor.run();
			return 1;
		});
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> registerStringIntakeExecutor(String name, String argName, PhraseExecutor executor) {
		return literal(name).then(argument(argName, StringArgumentType.greedyString()).executes(context -> {
			String player = StringArgumentType.getString(context, argName);
			executor.execute(player);
			return 1;
		}).suggests(playerSuggestions));
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> registerLongIntakeExecutor(String name, String argName, LongExecutor executor) {
		return literal(name).then(argument(argName, LongArgumentType.longArg(0)).executes(context -> {
			long num = LongArgumentType.getLong(context, argName);
			executor.execute(num);
			return 1;
		}).suggests(delaySuggestions));
	}

}