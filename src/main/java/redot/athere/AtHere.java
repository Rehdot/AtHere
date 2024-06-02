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
    private static final Logger LOGGER = LoggerFactory.getLogger("athere");
	private static MinecraftClient client = MinecraftClient.getInstance();
	static List<ScheduledExecutorService> schedulers = new ArrayList<>();
	public static List<String> exclusions = new ArrayList<>();
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
				.then(registerLongIntakeExecutor("delay", num -> {
					delay = (int) num;
					MSGManager.sendPlayerMSG(MSG.setDelay());
				}))
				.then(registerPlayerIntakeExecutor("exclude", player -> {
					if (!exclusions.contains(player.toLowerCase())) {
						exclusions.add(player.toLowerCase());
					}
					MSGManager.sendPlayerMSG(MSG.addExclusion(player));
				}))
				.then(registerPlayerIntakeExecutor("include", player -> {
					exclusions.remove(player.toLowerCase());
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

	private static LiteralArgumentBuilder<FabricClientCommandSource> registerPlayerIntakeExecutor(String name, PhraseExecutor executor) {
		return literal(name).then(argument("player", StringArgumentType.greedyString()).executes(context -> {
			String player = StringArgumentType.getString(context, "player");
			executor.execute(player);
			return 1;
		}).suggests(playerSuggestions));
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> registerLongIntakeExecutor(String name, LongExecutor executor) {
		return literal(name).then(argument("milliseconds", LongArgumentType.longArg(0)).executes(context -> {
			long num = LongArgumentType.getLong(context, "milliseconds");
			executor.execute(num);
			return 1;
		}).suggests(delaySuggestions));
	}

	/*switch (args.get(1).toLowerCase()) {
            case "delay":
                if (args.get(2) == null) {
                    MSGManager.sendPlayerMSG(MSG.nullArgs);
                    return;
                }
                try {
                    delay = Math.max(0, Integer.parseInt(args.get(2)));
                    MSGManager.sendPlayerMSG(MSG.setDelay());
                } catch (Exception ignored) {
                    MSGManager.sendPlayerMSG(MSG.invalidArgs);
                }
                break;
            case "stop":
                schedulers.forEach(ExecutorService::shutdownNow);
                schedulers.clear();
                MSGManager.sendPlayerMSG(MSG.shutDownTasks);
                break;
            case "exclude":
                try { playerName = args.get(2); }
                catch (ArrayIndexOutOfBoundsException ignored) {
                    playerName = client.player.getName().getString();
                }

                if (!exclusions.contains(playerName.toLowerCase())) {
                    exclusions.add(playerName.toLowerCase());
                }

                MSGManager.sendPlayerMSG(MSG.addExclusion(playerName));
                break;
            case "include":
                try { playerName = args.get(2); }
                catch (ArrayIndexOutOfBoundsException ignored) {
                    playerName = client.player.getName().getString();
                }

                exclusions.remove(playerName.toLowerCase());
                MSGManager.sendPlayerMSG(MSG.addInclusion(playerName));
                break;
            case "help":
                MSGManager.sendHelpMSG();
                break;
            case "status":
                MSGManager.sendStatusMSG();
                break;
            default:
                MSGManager.sendPlayerMSG(MSG.nullSubCMD);
                break;
        }*/

}