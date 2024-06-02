package redot.athere;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class MSGManager {
    private static MinecraftClient client = MinecraftClient.getInstance();

    public static void sendPlayerMSG(String message) {
        MutableText bracketL = Text.literal("[").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x333333)));
        MutableText bracketR = Text.literal("]").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x333333)));
        MutableText atHere = Text.literal("AtHere").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xE74C3C )));
        MutableText messageComponent = Text.literal(" " + message).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA)));

        MutableText messageText = bracketL.append(atHere).append(bracketR).append(messageComponent);
        client.inGameHud.getChatHud().addMessage(messageText);
    }

    public static void sendHelpMSG() {
        sendPlayerMSG("Hey! I'm AtHere, a command helper for server operators." +
                "\nWhen typing commands, using '@here' in place of a player will execute that command once for each online player!" +
                "\nMy commands include:" +
                "\n/athere delay (milliseconds) <- Sets a custom delay between each command" +
                "\n/athere exclude (player) <- Excludes a player from command execution" +
                "\n/athere include (player) <- Includes a previously-excluded player from command execution" +
                "\n/athere status <- Displays AtHere's statuses" +
                "\n/athere stop <- Stops all running AtHere tasks");
    }

    public static void sendStatusMSG() {
        sendPlayerMSG(MSG.delayStatus() + MSG.exclusionStatus());
    }
}
