package redot.athere;

import com.ibm.icu.impl.Pair;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CMDProcess {
    private static MinecraftClient client = MinecraftClient.getInstance();
    private static final String numArgsRegex = "@(\\d+)->(\\d+)", stepRegex = "@step->(\\d+)";
    public static void processCommand(String cmd) {
        MSGManager.sendPlayerMSG("Starting task.");

        boolean containsHereArg = cmd.contains("@here");
        boolean containsNumArg = containsNumArg(cmd);

        if (containsHereArg && !containsNumArg) executeAtHere(cmd);
        else if (containsHereArg) executeBoth(cmd);
        else if (containsNumArg) executeNumArgs(cmd);
    }

    public static void executeAtHere(String cmd) {
        ScheduledExecutorService scheduler = getNewScheduler();
        List<String> inclusions = getInclusions();
        int delayMultiplier = 0;

        for (String argument : inclusions) {
            if (AtHere.exclusions.contains(argument.toLowerCase())) continue;

            String command = cmd.replace("@here", argument);
            scheduleCommand(scheduler, delayMultiplier, command);
            delayMultiplier++;
        }
    }

    public static void executeNumArgs(String cmd) {
        ScheduledExecutorService scheduler = getNewScheduler();
        Pair<Integer, Integer> pair = getNumArgPair(cmd);
        int delayMultiplier = 0;

        for (int i = pair.first; i <= pair.second; i+=handleStep(cmd)) {
            String command = replaceNumArg(removeStep(cmd).trim(), i);
            scheduleCommand(scheduler, delayMultiplier, command);
            delayMultiplier++;
        }
    }

    public static void executeBoth(String cmd) {
        ScheduledExecutorService scheduler = getNewScheduler();
        Pair<Integer, Integer> pair = getNumArgPair(cmd);
        List<String> inclusions = getInclusions();
        int delayMultiplier = 0;

        for (String argument : inclusions) {
            if (AtHere.exclusions.contains(argument.toLowerCase())) continue;

            for (int x = pair.first; x <= pair.second; x+=handleStep(cmd)) {
                String command = replaceNumArg(removeStep(cmd).trim(), x).replace("@here", argument);
                scheduleCommand(scheduler, delayMultiplier, command);
                delayMultiplier++;
            }
        }
    }

    private static void scheduleCommand(ScheduledExecutorService scheduler, int delayMultiplier, String cmd) {
        scheduler.schedule(() -> client.execute(() -> {
            if (client.player != null && client.player.networkHandler != null)
                client.player.networkHandler.sendChatCommand(cmd);
        }), AtHere.delay * delayMultiplier, TimeUnit.MILLISECONDS);
    }

    public static int handleStep(String input) {
        Matcher matcher = Pattern.compile(stepRegex).matcher(input);
        if (!matcher.find()) return 1;
        else return Integer.parseInt(matcher.group(1));
    }

    public static String removeStep(String input) {
        Matcher matcher = Pattern.compile(stepRegex).matcher(input);
        return matcher.replaceAll("");
    }

    public static boolean containsNumArg(String input) {
        Matcher matcher = Pattern.compile(numArgsRegex).matcher(input);
        return matcher.find();
    }

    public static String replaceNumArg(String input, int replacement) {
        Matcher matcher = Pattern.compile(numArgsRegex).matcher(input);
        return matcher.replaceAll(String.valueOf(replacement));
    }

    public static Pair<Integer, Integer> getNumArgPair(String input) {
        Matcher matcher = Pattern.compile(numArgsRegex).matcher(input);
        matcher.find();

        int first = Integer.parseInt(matcher.group(1));
        int second = Integer.parseInt(matcher.group(2));

        return Pair.of(first, second);
    }

    public static List<String> getInclusions() {
        List<String> allInclusions = new ArrayList<>(AtHere.inclusions.stream().map(String::toLowerCase).toList());
        getOnlinePlayers().forEach(s -> {
            if (!allInclusions.contains(s.toLowerCase()))
                allInclusions.add(s);
        });
        return allInclusions;
    }

    private static ScheduledExecutorService getNewScheduler() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        AtHere.schedulers.add(scheduler);
        return scheduler;
    }

    public static List<String> getOnlinePlayers() {
        return client.getNetworkHandler().getPlayerList().stream()
                .map(PlayerListEntry::getProfile)
                .map(GameProfile::getName).toList();
    }
}
