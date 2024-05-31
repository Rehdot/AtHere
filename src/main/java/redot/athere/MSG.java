package redot.athere;

import java.util.stream.Collectors;

public record MSG() {
    public static String
            init = "AtHere Initialized.",
            nullSubCMD = "Subcommand not recognized. Try '/athere help'.",
            nullArgs = "Please provide an argument.",
            invalidArgs = "The provided arguments are invalid. Try '/athere help'.",
            shutDownTasks = "Stopped all running AtHere tasks.";

    public static String setDelay() {
        return "Set delay to " + CMDProcess.delay + " milliseconds.";
    }

    public static String addExclusion(String playerName) {
        return "Now excluding " + playerName + ".";
    }

    public static String addInclusion(String playerName) {
        return "Now including " + playerName + ".";
    }

    public static String delayStatus() {
        return "\nCommand delay: " + CMDProcess.delay + " milliseconds.";
    }

    public static String exclusionStatus() {
        String ei = CMDProcess.exclusions.stream().map(Object::toString).collect(Collectors.joining(", "));
        return "\nExcluded individuals: " + (ei.isEmpty() ? "None." : ei);
    }
}
