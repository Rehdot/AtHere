package redot.athere;

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

    public static String setExclusion() {
        return "Set self exclusion to " + (CMDProcess.excludeSelf ? "true" : "false") + ".";
    }

    public static String delayStatus() {
        return "\nCommand delay: " + CMDProcess.delay + " milliseconds.";
    }

    public static String exclusionStatus() {
        return "\nSelf exclusion: " + (CMDProcess.excludeSelf ? "true" : "false");
    }
}
