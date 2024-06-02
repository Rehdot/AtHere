package redot.athere;

import java.util.stream.Collectors;

public record MSG() {
    public static String
            init = "AtHere initialized.",
            shutDownTasks = "Stopped all running AtHere tasks.",
            clearExclusions = "Cleared AtHere's exclusions.";

    public static String setDelay() {
        return "Set delay to "+AtHere.delay+" millisecond"+(AtHere.delay==1?"":"s")+".";
    }

    public static String addExclusion(String playerName) {
        return "Now excluding " + playerName + ".";
    }

    public static String addInclusion(String playerName) {
        return "Now including " + playerName + ".";
    }

    public static String delayStatus() {
        return "Statuses:\nCommand delay: "+AtHere.delay+" millisecond"+(AtHere.delay==1?"":"s");
    }

    public static String exclusionStatus() {
        String ei = AtHere.exclusions.stream().map(Object::toString).collect(Collectors.joining(", "));
        return "\nExcluded individuals: " + (ei.isEmpty() ? "None." : ei);
    }
}
