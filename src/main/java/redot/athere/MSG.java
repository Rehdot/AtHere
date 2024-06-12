package redot.athere;

import java.util.stream.Collectors;

public record MSG() {
    public static String
            init = "AtHere initialized.",
            shutDownTasks = "Stopped all running AtHere tasks.",
            clearExclusions = "Cleared AtHere's exclusions.",
            clearInclusions = "Cleared AtHere's inclusions.";

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
        String ea = AtHere.exclusions.stream().map(Object::toString).collect(Collectors.joining(", "));
        return "\nExcluded arguments: " + (ea.isEmpty() ? "None." : ea);
    }

    public static String inclusionStatus() {
        String ia = AtHere.inclusions.stream().map(Object::toString).collect(Collectors.joining(", "));
        return "\nIncluded arguments: " + (ia.isEmpty() ? "None." : ia);
    }
}
