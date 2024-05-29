package redot.athere;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CMDProcess {
    private static MinecraftClient client = MinecraftClient.getInstance();
    public static Boolean excludeSelf = false;
    public static int delay = 100;
    static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void processAtHere(String cmd) {
        List<String> onlinePlayers = client.getNetworkHandler().getPlayerList().stream()
                .map(PlayerListEntry::getProfile)
                .map(GameProfile::getName).toList();
        String senderName = client.player.getName().getString();

        if (onlinePlayers.isEmpty()) {
            return;
        }

        for (int i = 0; i < onlinePlayers.size(); i++) {
            String playerName = onlinePlayers.get(i);

            if (Objects.equals(senderName, playerName) && excludeSelf) continue;

            scheduler.schedule(() -> {
                client.execute(() -> {
                    if (client.player != null && client.player.networkHandler != null) {
                        client.player.networkHandler.sendChatCommand(cmd.replace("@here", playerName));
                    }
                });
            }, (long) delay * i, TimeUnit.MILLISECONDS);
        }
    }

    public static void processCommand(List<String> args) {
        if (args.get(1) == null) {
            MSGManager.sendPlayerMSG(MSG.nullSubCMD);
        }

        switch (args.get(1)) {
            case "delay":
                if (args.get(2) == null) {
                    MSGManager.sendPlayerMSG(MSG.nullArgs);
                    return;
                }
                try {
                    delay = Integer.parseInt(args.get(2));
                    MSGManager.sendPlayerMSG(MSG.setDelay());
                } catch (Exception ignored) {
                    MSGManager.sendPlayerMSG(MSG.invalidArgs);
                }
                break;
            case "exclude":
                excludeSelf = !excludeSelf;
                MSGManager.sendPlayerMSG(MSG.setExclusion());
                break;
            case "help":
                MSGManager.sendHelpMSG();
                break;
            default:
                MSGManager.sendPlayerMSG(MSG.nullSubCMD);
                break;
        }
    }
}
