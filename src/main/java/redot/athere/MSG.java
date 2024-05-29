package redot.athere;

public record MSG() {
    public static String
            init = "AtHere Initialized.",
            nullSubCMD = "Subcommand not recognized. Try '/athere help'.",
            nullArgs = "Please provide an argument.",
            invalidArgs = "The provided arguments are invalid. Try '/athere help'.";

    public static String setDelay() {
        return "Set delay to " + CMDProcess.delay + " milliseconds.";
    }

    public static String setExclusion() {
        return "Set self exclusion to " + (CMDProcess.excludeSelf ? "true" : "false") + ".";
    }
}
