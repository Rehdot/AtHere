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

    public static void processAtHere(String cmd) {
        if (getOnlinePlayers().isEmpty()) return;

        MSGManager.sendPlayerMSG("Starting task.");
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        AtHere.schedulers.add(scheduler);

        int delayMultiplier = 0;
        for (int i = 0; i < getOnlinePlayers().size(); i++) {
            String playerName = getOnlinePlayers().get(i);

            if (AtHere.exclusions.contains(playerName.toLowerCase())) {
                continue;
            }

            delayMultiplier++;
            scheduler.schedule(() -> {
                client.execute(() -> {
                    if (client.player != null && client.player.networkHandler != null) {
                        client.player.networkHandler.sendChatCommand(cmd.replace("@here", playerName));
                    }
                });
            }, AtHere.delay * delayMultiplier, TimeUnit.MILLISECONDS);
        }
    }

    public static List<String> getOnlinePlayers() {
        return client.getNetworkHandler().getPlayerList().stream()
                .map(PlayerListEntry::getProfile)
                .map(GameProfile::getName).toList();
    }
}
