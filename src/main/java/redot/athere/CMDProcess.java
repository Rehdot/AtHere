package redot.athere;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CMDProcess {
    private static MinecraftClient client = MinecraftClient.getInstance();
    public static List<String> exclusions = new ArrayList<>();
    public static int delay = 100;
    static List<ScheduledExecutorService> schedulers = new ArrayList<>();

    public static void processAtHere(String cmd) {
        if (getOnlinePlayers().isEmpty()) return;

        MSGManager.sendPlayerMSG("Starting task.");
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        schedulers.add(scheduler);

        int delayMultiplier = 0;
        for (int i = 0; i < getOnlinePlayers().size(); i++) {
            String playerName = getOnlinePlayers().get(i);

            if (exclusions.contains(playerName.toLowerCase())) {
                continue;
            }

            delayMultiplier++;
            scheduler.schedule(() -> {
                client.execute(() -> {
                    if (client.player != null && client.player.networkHandler != null) {
                        client.player.networkHandler.sendChatCommand(cmd.replace("@here", playerName));
                    }
                });
            }, (long) delay * delayMultiplier, TimeUnit.MILLISECONDS);
        }
    }

    public static void processCommand(List<String> args) {
        String playerName;

        if (args.get(1) == null) {
            MSGManager.sendPlayerMSG(MSG.nullSubCMD);
        }

        switch (args.get(1).toLowerCase()) {
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
        }
    }

    public static List<String> getOnlinePlayers() {
        return client.getNetworkHandler().getPlayerList().stream()
                .map(PlayerListEntry::getProfile)
                .map(GameProfile::getName).toList();
    }
}
